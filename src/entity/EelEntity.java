package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Random;
import java.util.Vector;

import render.Renderer;
import util.Timer;
import world.World;

public class EelEntity extends Entity {
	private final int FAT_DISTANCE = 640;

	private final int NECK_RADIUS = 3 << CONVERSION_FACTOR;

	private final int MOUTH_WIDTH = 30 << CONVERSION_FACTOR;

	private final int MIN_MOUTH_WIDTH = 10 << CONVERSION_FACTOR;

	private final int MOUTH_HEIGHT = 24 << CONVERSION_FACTOR;

	private final int TAIL_DISTANCE = 7 << CONVERSION_FACTOR;

	private final int TAIL_RADIUS = 1 << CONVERSION_FACTOR;

	private final int MAX_FATS = 16;

	private final int TIME_TO_DIGEST = 750;
	
	private final int MOUTH_TIME = 1000;

	private boolean digesting;
	
	private boolean enlargedMouth;

	private long accumulator;

	private long currTime;

	private long prevTime;

	private Timer timer;

	private Timer mouthTimer;
	
	private Random random;
	
	private Mouth mouth;

	{
		random = new Random();
		timer = new Timer();
		mouthTimer = new Timer();
		accumulator = 0;
		digesting = false;
		prevTime = 0;
		currTime = 0;
		mouth = new Mouth(MOUTH_WIDTH, MIN_MOUTH_WIDTH, MOUTH_HEIGHT);
	}

	public EelEntity() {
		setRadius(MOUTH_HEIGHT / 2);

		addBodyPart(new Fat(50, true));
		addBodyPart(new Fat(0));
	}

	public EelEntity(int x, int y) {
		this();

		setX(x);
		setY(y);

		Entity inFront = this;
		Entity temp;
		for (int i = 0; i < getBodyParts().size(); i++) {
			temp = getBodyParts().get(i);
			temp.setAngle(inFront.getAngle());
			temp.setFrontX(inFront.getRearX());
			temp.setY(inFront.getYFromCenterToRear(FAT_DISTANCE+ NECK_RADIUS));
			temp.setMaxVelocity(0);
			inFront = temp;
		}

		setTurningSpeed(getTurningSpeed() * 1.5);
		setValue(getValue());
	}

	public EelEntity(int x, int y, World world) {
		this(x, y);
		setWorld(world);
	}

	public void update() {
		if (getJustDied()) {
			//for loop to give out lots of food and to take away a body part
			FoodEntityLevel1 entity = new FoodEntityLevel1(
					getXFromCenterToRear(TAIL_DISTANCE * 5),
					getYFromCenterToRear(TAIL_DISTANCE * 5));
			NonHostileAIController controller = new NonHostileAIController(
					entity);
			entity.setController(controller);
			entity.setWorld(getWorld());
			getWorld().addEntity(entity);
			setJustDied(false);
		}

		if (!checkDead()) {
			super.update();

			Entity inFront = this;
			Entity temp;
			for (int i = 0; i < getBodyParts().size(); i++) {
				temp = getBodyParts().get(i);

				temp.setTarget(inFront.getRearX(), inFront.getRearY());
				temp.setAngle(temp.getTargetAngle());
				temp.setMaxVelocity(inFront.getMaxVelocity());
				temp.setVelocity(inFront.getVelocity());

				if (inFront.getDistance(temp) >= FAT_DISTANCE)
					temp.update();

				inFront = temp;
			}
		} else if (getController().getType() == Controller.PLAYER) {
			setJustDied(true);
			setDead(false);
			addValue(50);

			LevelChangerEntity entity = new LevelChangerEntity(getX(), getY());
			entity.setValue(-1);
			entity.setDead(true);
			entity.setWorld(getWorld());
			getWorld().addEntity(entity);
		}
		if(digesting) {
			currTime = timer.milliTime();
			accumulator += currTime - prevTime;
			prevTime = currTime;
			if(digesting && (accumulator > TIME_TO_DIGEST)) {
				digesting = false;
				mouth.setClosed(false);
				timer.reset();
				accumulator = 0;
				getWorld().checkCollisions(this);
			}
		}
		else
			getWorld().checkCollisions(this);
		
		if(enlargedMouth)
		{
			if(mouthTimer.milliTime() > MOUTH_TIME)
				shrinkMouth();
		}
		
		mouth.update();
	}

	public void render(Renderer renderer) {
		Stroke stroke = new BasicStroke(2);
		renderer.setStroke(stroke);
		renderer.rotate(getX(), getY(), -getAngle());

		renderer.drawCircle(getX(), getY(), NECK_RADIUS, getColor());

		mouth.render(renderer, getX(), getY() + PIXEL_BUFFER + NECK_RADIUS
				+ MOUTH_HEIGHT / 2, getColor());
//		renderer.drawArc(getX(), getY() + PIXEL_BUFFER + NECK_RADIUS
//				+ MOUTH_HEIGHT / 2, MOUTH_WIDTH, MOUTH_HEIGHT, 0, 90,
//				getColor());
//
//		renderer.drawArc(getX(), getY() + PIXEL_BUFFER + NECK_RADIUS
//				+ MOUTH_HEIGHT / 2, MOUTH_WIDTH, MOUTH_HEIGHT, 180, -90,
//				getColor());

		renderer.rotate(getX(), getY(), getAngle());

		renderer.rotate(getXFromCenterToRear(NECK_RADIUS + TAIL_DISTANCE),
				getYFromCenterToRear(NECK_RADIUS + TAIL_DISTANCE), -getAngle());

		renderer.fillRect(getXFromCenterToRear(NECK_RADIUS + TAIL_DISTANCE),
				getYFromCenterToRear(NECK_RADIUS + TAIL_DISTANCE),
				TAIL_RADIUS * 2, TAIL_RADIUS * 2, getColor());

		renderer.rotate(getXFromCenterToRear(NECK_RADIUS + TAIL_DISTANCE),
				getYFromCenterToRear(NECK_RADIUS + TAIL_DISTANCE), getAngle());

		for (int x = 0; x < getBodyParts().size(); x++)
			getBodyParts().get(x).render(renderer);

	}

	@Override
	public boolean checkCollision(Entity entity) {
		if (super.checkCollision(entity))
			return true;

		for (int x = 0; x < getBodyParts().size(); x++)
			if (getBodyParts().get(x).checkCollision(entity))
				return true;
		return false;
	}

	public int getType() {
		return MOUTH;
	}

	public void collision(Entity entity) {
			if (entity.getType() == LEVEL_CHANGER
					&& getController() instanceof PlayerController) {
				getWorld().changeLevel(entity.getValue());
				digesting = true;
				mouth.setClosed(true);
				timer.start();
				prevTime = timer.milliTime();

			}

			for (int x = 0; x < getBodyParts().size(); x++)
				if (entity == getBodyParts().get(x))
					return;

			if (entity.getType() == FAT) {
				addValue(entity.removeValue());
				setValue(getValue());
				digesting = true;
				mouth.setClosed(true);
				timer.start();
				prevTime = timer.milliTime();
			} else if (entity.getType() == FOOD) {
				addValue(entity.removeValue());
				setValue(getValue());
				digesting = true;
				mouth.setClosed(true);
				timer.start();
				prevTime = timer.milliTime();
			}
			else if (entity.getType() == EVOLVER)
			{
				if(entity.getValue() == EvolverEntity.EVOLVER_VAL)
					evolve();
				else if(entity.getValue() == EvolverEntity.MOUTH_VAL)
					enlargeMouth();
			}
	}

	@Override
	public int getValue() {
		int temp = 0;
		for (Entity e : getBodyParts())
			if (((Fat) e).getBig() && ((Fat) e).getEdible())
				temp += e.getValue();
		return temp;
	}

	@Override
	public int addValue(int value) {
		for (Entity e : getBodyParts())
			value = e.addValue(value);

		if (value > 0) {
			value -= 50;
			Vector<Entity> fats = getBodyParts();
			Entity last = fats.get(fats.size() - 1);
			if (fats.size() < MAX_FATS) {
				Fat fat = new Fat();
				fat.setAngle(last.getAngle());
				fat.setFrontX(last.getRearX());
				fat.setY(last.getYFromCenterToRear(FAT_DISTANCE
						+ NECK_RADIUS));
				fat.setMaxVelocity(0);
				addBodyPart(fat);
			} else
				for (int i = 0; i < fats.size(); ++i)
					if (!((Fat) fats.get(i)).getBig()) {
						((Fat) fats.get(i)).setBig(true);
						break;
					}

			for (int i = 0; i < fats.size(); ++i)
				fats.get(i).setValue(((Fat) fats.get(i)).getBig() ? 50 : 0);
		}
		if (value > 0)
			spawnExcrement(value);

		return 0;
	}

	@Override
	public Vector<Entity> getSpawnedEntities() {
		Vector<Entity> entities = super.getSpawnedEntities();

		Entity entity = new FoodEntityLevel1(getX(), getY()); 
		entity.setController(new NonHostileAIController(entity));
		entities.add(entity);

		entity = new EvolverEntity(getX(), getY()); 
		entity.setController(new NonHostileAIController(entity));
		entities.add(entity);

		entity = new EvolverEntity(getX(), getY()); 
		entity.setController(new NonHostileAIController(entity));
		entity.setValue(-1);
		entities.add(entity);

		return entities;
	}

	@Override
	public boolean checkDead() {
		if (getValue() <= 0) {
			setDead(true);
			return true;
		}
		return false;
	}

	public void setColor(Color color) {
		super.setColor(color);
		for (Entity e : getBodyParts())
			e.setColor(color);
	}

	public void spawnExcrement(int value) {
		Entity entity = null;

		Vector<Entity> fats = getBodyParts();
		Entity last = fats.get(fats.size() - 1);

		switch (value) {
		case 50:
			entity = new FoodEntityLevel1();
			break;
		case 100:
			entity = new FoodEntityLevel2();
			break;
		case 150:
			entity = new FoodEntityLevel3();
			break;
		case 200:
			entity = new FoodEntityLevel4();
			break;
		case 250:
			entity = new FoodEntityLevel5();
			break;
		}

		if (entity != null) {
			entity.setX(last.getRearX());
			entity.setY(last.getRearY());
			NonHostileAIController controller = new NonHostileAIController(
					entity);
			entity.setController(controller);
			entity.setWorld(getWorld());
			getWorld().addEntity(entity);
		}
	}

	public void setMovingFast(boolean movingFast) {
		for (Entity e : getBodyParts())
			((Fat) e).setMovingFast(movingFast);
	}

	public boolean evolve() 
	{
		//Need to evolve
		//Go to the last fat that has a value and evolve
		//If this fat is fully evolved then evolve the first fat that 
		//is not fully evolved, starting with the one behind the mouth.
		//Health of fat is disregarded.
		Vector<Entity> fats = getBodyParts();
		Entity aFat;
		boolean evolved;
		for(int i = fats.size()-1; i >= 0; --i)
		{
			aFat = fats.get(i);
			if(aFat.getValue() > 0)
			{
				if(aFat.evolve())
					return true;
				else
				{
					for(int x = 0; x < fats.size(); ++x)
					{
						aFat = fats.get(x);
						if(aFat.evolve())
							return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int getSizeValue()
	{
		int temp = 0;
		for (Entity e : getBodyParts())
			if (((Fat) e).getBig() && ((Fat) e).getEdible())
				temp += e.getSizeValue();
		return temp;
	}
	
	/**
	 * Enlarges the Eel's mouth.
	 *
	 */
	public void enlargeMouth()
	{
		if(enlargedMouth)
			return;
		
		mouthTimer.reset();
		mouthTimer.start();
		mouth.setMaxWidth(MOUTH_WIDTH * 2);
		mouth.setHeight(MOUTH_HEIGHT * 2);
		mouth.setClosed(false);
		this.setRadius(getRadius() * 2);
		enlargedMouth = true;
	}
	
	/**
	 * Shrinks the Eel's mouth.
	 *
	 */
	public void shrinkMouth()
	{
		mouth.setMaxWidth(MOUTH_WIDTH);
		mouth.setHeight(MOUTH_HEIGHT);
		this.setRadius(getRadius() / 2);
		enlargedMouth = false;
	}
	
	/**
	 * adds a fat.
	 * 
	 * @param value fat's value.
	 * @param big fat's size.
	 */
	public void addFat(int value, boolean big)
	{
		Entity inFront = getBodyParts().get(getBodyParts().size() - 1);
		Fat temp = new Fat(value, big);
		temp.setFrontX(inFront.getRearX());
		temp.setY(inFront.getYFromCenterToRear(FAT_DISTANCE+ NECK_RADIUS));
		addBodyPart(temp);
	}
}
