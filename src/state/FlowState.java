package state;

import java.awt.Color;

import render.Renderer;
import util.ConfigReader;
import world.Camera;
import world.World;
import core.GameManager;
import entity.EelEntity;
import entity.Entity;
import entity.HostileAIController;
import entity.NonHostileAIController;
import entity.PlayerController;

public class FlowState implements State
{
	/** The number of levels in the game */
	private final int WORLDS = 20;
	
	/** The alpha composite that the level below the current will be rendered at */
	private final float LOWER_LEVEL_ALPHA_COMPOSITE = .2f;

	/** The game manager */
	private GameManager manager;

	/** The game's config file reader */
	private ConfigReader reader;
	
	/** This states camera */
	private Camera camera;

	/** The players entity what ever it maybe */
	private Entity entity;

	/** The players controller */
	private PlayerController playerController;

	/** A non hostile AI controller */
	private NonHostileAIController nonHostileAIController;

	/** A hostile AI controller */
	private HostileAIController hostileAIController;

	/** Array to contain game's levels */
	private World[] worlds;

	/** The current level the player is in */
	private int currWorld;

	/** ? */
	private int changeLevel;

	/** The current color to render in */
	private Color colour;

	/**
	 * The Contructor.
	 * Sets up the camera, worlds and entities. 
	 * @param manager The game manager
	 */
	public FlowState(GameManager manager)
	{
		this.manager = manager;
		int width  = manager.getWindowWidth();
		int height = manager.getWindowHeight();

		entity = new EelEntity(width/2, height/2);

		playerController = new PlayerController(manager.getInput(), entity);
		entity.setController(playerController);

		camera = new Camera(0, 0, width, height, entity);

		worlds = new World[WORLDS];
		currWorld = 0;

		colour = Color.getHSBColor(0.56f, 1.0f, (1.0f - 0.05f*currWorld));

		for(int x = 0; x < WORLDS; x++)
			worlds[x] = new World(manager, this, x, null);

		worlds[currWorld].setPlayerEntity(entity);
		entity.setWorld(worlds[currWorld]);

		//TODO
		//Need to sort out how we set the boolean for easy
		reader = new ConfigReader(worlds, camera, true);
		
//
//
//		for (int i = 0; i < 100; i++)
//		{
//			entity = new EvolverEntity(i*32, 0);
//			worlds[currWorld].addEntity(entity);
//			entity.setController(new NonHostileAIController(entity));
//			entity.setValue(EvolverEntity.MOUTH_VAL);
//			entity.setWorld(worlds[currWorld]);
//		}
	}

	/** 
	 * This method is called when this state becomes active.
	 * It makes sure that the renderer knows what camera to use.
	 * And that the camera knows what size it should be in case 
	 * the window size has been altered.
	 */
	public void enter() {
		//set the camera if it needs to be set
		manager.getRenderer().setCamera(camera);
		camera.setWidth(manager.getWindowWidth());
		camera.setHeight(manager.getWindowHeight());
		camera.setScale(manager.getRenderer().getScaleWidth(), manager.getRenderer().getScaleHeight());
	}

	/**
	 * This method is called when another state becomes active.
	 */
	public void exit() {

	}

	/**
	 * Updates the state, making sure that the player in on the
	 * right level and updating the current world and the world below it.
	 */
	public void update()
	{
		if(changeLevel != 0)
		{
			worlds[currWorld].removePlayerEntity();

			currWorld += changeLevel;

			if(currWorld > WORLDS - 1)
				currWorld = WORLDS - 1;
			else if(currWorld < 0)
				currWorld = 0;

			entity.setWorld(worlds[currWorld]);
			worlds[currWorld].setPlayerEntity(entity);

			changeLevel = 0;

			colour = Color.getHSBColor(0.56f, 1.0f, (1.0f - 0.05f*currWorld));
		}

		//Update the camera to its new position
		camera.update();

		//Update entity position
		playerController.update(camera.getX(), camera.getY());

		worlds[currWorld].update();
		
		//FIXME uncomment. also do level below
		if(currWorld + 1 < WORLDS)
			worlds[currWorld + 1].update();
	}

	/**
	 * Render the state, the current world and the one below it
	 * as well as the entities at both.
	 * The world below the current one, if it exists is rendered at
	 * <var>LOWER_LEVEL_ALPHA_COMPOSITE</var> the level of alpha composite
	 * than the one above it
	 * 
	 *  @param renderer The renderer to draw with
	 */
	public void render(Renderer renderer)
	{

		renderer.fillBackground(colour);


		worlds[currWorld].render(renderer);

		renderer.setAlphaComposite(LOWER_LEVEL_ALPHA_COMPOSITE);
//		FIXME uncomment. also do level below
		if(currWorld + 1 < WORLDS)
			worlds[currWorld + 1].render(renderer);
	}

	/**
	 * Changes the currently active world. Positive values
	 * go down, negative values go up.
	 * 
	 * @param value The value to change worlds by.
	 */
	public void changeLevel(int value)
	{
		changeLevel = value;
	}

	/**
	 * The currently active world.
	 * 
	 * @return The currently active world.
	 */
	public int getCurrLevel()
	{
		return currWorld;
	}

	/**
	 * Gets the state's camera.
	 *
	 * @return The camera.
	 */
	public Camera getCamera()
	{
		return camera;
	}

}
