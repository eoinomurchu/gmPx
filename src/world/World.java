package world;

import input.Input;

import java.util.Vector;

import render.Renderer;
import state.FlowState;
import core.GameManager;
import entity.CircleEntity;
import entity.Entity;
import entity.LevelChangerEntity;


public class World {

	/** The Main Area */
	private Area area;

	/** The User Controlled Entity */
	private Entity entity;

	/** The Game Manager */
	private GameManager manager;

	/** The Input Controller */
	private Input input;

	/** The flow state the world is managed by */
	private FlowState flowState;
	
	/** Holds the world's level changers for pinging */
	private Vector<LevelChangerEntity> levelChangers;
	
	/** The level in the flow state this world is */
	private int level;

	public World(GameManager aManager, FlowState flowState, int level, Entity entity) {
		manager = aManager;
		this.flowState = flowState;
		this.entity = entity;
		this.level = level;
		
		levelChangers = new Vector<LevelChangerEntity>();

		area = new Area();
		area.setPos( (manager.getWindowWidth()-Area.getWidth())/2,  (manager.getWindowHeight()-Area.getHeight())/2 );
		if(entity != null)
			area.addEntity(entity);
		area.grow();	
	}

	public void update() {

		Area anArea;
		Entity anEntity;
		Vector<Area> areas = area.getBorderAreas();		
		Vector<Entity> entities = area.getEntities(); 

		for(int i = 0; i < entities.size(); ++i)
		{
			anEntity = entities.get(i);
			if(anEntity.getDead())
			{
				area.removeEntity(anEntity);
				for(Entity e: anEntity.getSpawnedEntities())
					area.addEntity(e);
			}
			else
				anEntity.update();
		}
			
		for(int x = 0; x < areas.size(); ++x)
		{
			anArea = areas.get(x);
			entities = anArea.getEntities();

			for(int i = 0; i < entities.size(); ++i)
			{
				anEntity = entities.get(i);
				if(anEntity.getDead())
				{
					anArea.removeEntity(anEntity);
					for(Entity e: anEntity.getSpawnedEntities())
						anArea.addEntity(e);
				}
				else
					anEntity.update();
			}
		}

		//Remove the entity from its current Area if we're on this level
		if(entity != null) {
			area.removeEntity(entity);

			//Check if entity has left the current Area
			if(entity.getX() < area.getX()) {
				area.left.grow();
				area = area.left;
			}
			else if(entity.getX() > area.getX()+Area.getWidth()) {
				area.right.grow ();
				area = area.right;
			}
			else if(entity.getY() < area.getY()) {
				area.above.grow();
				area = area.above;
			}
			else if(entity.getY() > area.getY()+Area.getHeight()) {
				area.below.grow ();
				area = area.below;
			} 

			//Add the entity to the current area
			area.addEntity(entity);
		}

		for(Area a: area.getBorderAreas()) {
			for(int i = 0; i < a.getEntities().size(); ++i) {
				Entity e = a.getEntities().get(i);
				if(!a.containsPoint(e.getX(), e.getY())) {
					a.removeEntity(e);

					a.grow();
					areas = a.getBorderAreas();
					areas.add(area);

					for(Area borderArea: areas) {
						if(borderArea != null)
							if(borderArea.containsPoint(e.getX(), e.getY())) {
								borderArea.addEntity(e);
								break;
							}
					}
				}
			}
		}
		
		//ping level changers
		if(flowState.getCurrLevel() == level)
			for(int x = 0; x < levelChangers.size(); x++)
				levelChangers.get(x).ping();
	}

	public void render(Renderer theRenderer) {

		area.render(theRenderer);
		for(Area a:area.getBorderAreas())
			if(a != null)
				a.render(theRenderer);

		area.renderEntities(theRenderer);
		for(Area a:area.getBorderAreas())
			if(a != null)
				a.renderEntities(theRenderer);

	}

	public void addEntity(Entity entity)
	{
		area.addEntity(entity);
	}
	
	/**
	 * Adds a level changer to the world so it can be pinged.
	 * 
	 * @param changer The changer to add.
	 */
	public void addLevelChanger(LevelChangerEntity changer)
	{
		levelChangers.add(changer);
	}

	/**
	 * Checks for a collision between an entity and other entities in its area(s).
	 * If a collision is found the collision() methods of both entities are called.
	 * Mouths can only collide with fat and fat can only collide with mouths.
	 *
	 * @param entity The entity to check collisions for.
	 */
	public void checkCollisions(Entity entity)
	{    
		Vector<Entity> entities = getCollidingEntities(entity);

		for(int x = 0, n = entities.size(); x < n; x++)
		{
			entity.collision(entities.get(x));
			entities.get(x).collision(entity);
		}
	}

	/**
	 * Gets a vector of all entities colliding with a specified entity.
	 * 
	 * @param entity The entity to check against.
	 * @return Other entities colliding with specified entity.
	 */
	private Vector<Entity> getCollidingEntities(Entity entity)
	{
		Vector<Entity> entities = new Vector<Entity>();
		Vector<Entity> colliding = new Vector<Entity>();
		Vector<Entity> areaEntities;

		Vector<Area> areas = area.getBorderAreas();
		areas.add(area);

		/*
		 * Fill possible colliding entities with area's entities.
		 */
		for (int x = 0; x < areas.size(); x++)
		{
			areaEntities = areas.get(x).getEntities();

			for(int y = 0, i = areaEntities.size(); y < i; y++)
				if(areaEntities.get(y) != null)
					entities.add(areaEntities.get(y));
		}

		/*
		 * Don't add the entity we're checking against as it will be in
		 * the entities vector also.
		 */
		for(int x = 0, n = entities.size(); x < n; x++)
			if(entities.get(x).checkCollision(entity) && entities.get(x) != entity)
			{
				Entity ent = entities.get(x);

				if(ent.getType() == Entity.MOUTH)
				{
					
					Entity f = ent.getCollidingFat(entity);
					if(f != null)
						colliding.add(ent.getCollidingFat(entity));
				}
				else if(ent.getType() == Entity.FAT || ent.getType() == Entity.FOOD
						|| ent.getType() == Entity.LEVEL_CHANGER)
					colliding.add(ent);
				else if(ent.getType() == Entity.EVOLVER)
					colliding.add(ent);
			}

		return colliding;
	}

	/**
	 * Gets the nearest piece of food that collides with
	 * the specified entity.
	 * 
	 * @param radar Entity The entity to check against.
	 * @param entity The entity to exclude in the check.
	 * @return Nearest food or null of no food.
	 */
	public Entity getNearbyFood(Entity radarEntity, Entity entity)
	{
		Vector<Entity> entities = new Vector<Entity>();
		Vector<Entity> areaEntities;

		Vector<Area> areas = area.getBorderAreas();
		areas.add(area);

		/*
		 * Fill possible colliding entities with area's entities.
		 */
		for (int x = 0; x < areas.size(); x++)
		{
			areaEntities = areas.get(x).getEntities();

			for(int y = 0, i = areaEntities.size(); y < i; y++)
				if(areaEntities.get(y) != null)
					entities.add(areaEntities.get(y));
		}

		/*
		 * Don't addd the entity we're checking against as it will be in
		 * the entities vector also.
		 */
		for(int x = 0, n = entities.size(); x < n; x++)
			if(entities.get(x).checkCollision(radarEntity) && entities.get(x) != entity)
			{
				Entity ent = entities.get(x);

				if(ent.getType() == Entity.MOUTH)	
				{
					Entity f = ent.getCollidingFat(radarEntity);
					if(f != null && f.getValue() > 0)
						return f;
				}
				else if(ent.getType() == Entity.FOOD && ent.getValue() > 0)
					return ent;
			}

		return null;
	}

	public void changeLevel(int value)
	{
		flowState.changeLevel(value);
	}

	public void setPlayerEntity(Entity entity)
	{
		this.entity = entity;
	}

	/** 
	 * Removes the player entity from the worlds
	 * areas and sets the player entity in this 
	 * world to be null.
	 */
	public void removePlayerEntity()
	{
		area.removeEntity(entity);
		entity = null;
	}
	
	/**
	 * Gets the camera the world is being viewed through.
	 * 
	 * @return World's camera.
	 */
	public Camera getCamera()
	{
		return flowState.getCamera();
	}

}


