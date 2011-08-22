package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Vector;

import render.Renderer;
import util.Timer;
import world.World;

public class CircleEntity extends Entity
{	
	private final int MAX_FAT_RADIUS = 2800;
	
	private final int MIN_FAT_RADIUS = 1800;
	
	private final int MAX_MOVING_FAST_FAT_RADIUS = 1800;
	
	private final int MIN_MOVING_FAST_FAT_RADIUS = 1100;
		
	private int outerRadius = 1000;
	
	private int minFatRadius;
	
	private int maxFatRadius;

	private int mouthRadius = 520;

	private int increment2 = 5;

	private int entityLevel = 2;

	private int incdist = 5;

	private int entityAngle = 360/entityLevel;

	Random length, angle;

	protected final int FAT_DISTANCE = 640;

	protected final int NECK_RADIUS = 3 << CONVERSION_FACTOR;

	protected final int MOUTH_WIDTH = 30 << CONVERSION_FACTOR;

	protected final int MOUTH_HEIGHT = 24 << CONVERSION_FACTOR;

	protected final int TAIL_DISTANCE = 7 << CONVERSION_FACTOR;

	protected final int TAIL_RADIUS = 1 << CONVERSION_FACTOR;

	protected final int MAX_FATS = 16;

	protected final int TIME_TO_DIGEST = 5000;

	protected boolean digesting;

	protected long accumulator;

	protected long currTime;

	protected long prevTime;

	protected Timer timer;

	protected Random random;

	private int[][] randomArray;

	{
		random = new Random();
		timer = new Timer();
		accumulator = 0;
		digesting = false;
		prevTime = 0;
		currTime = 0;

		minFatRadius = MIN_FAT_RADIUS;
		maxFatRadius = MAX_FAT_RADIUS;
		length = new Random();
		randomArray = new int[MAX_FATS][2];
	}
	
	public CircleEntity() 
	{
		setRadius(mouthRadius);
		addBodyPart(new Fat(50, true));
		addBodyPart(new Fat(0));
		entityAngle = 360/getBodyParts().size();
	}

	public CircleEntity(int x, int y)
	{
		this();
		RandomGenerator();
		setX(x);
		setY(y);      

		setTurningSpeed(getTurningSpeed()*1.5);
		setAcceleration(2.0);
		setDeceleration(0.5);
		setValue(100);
	}

	public CircleEntity(int x, int y, World world)
	{
		this(x, y);
		setWorld(world);
	}

	public int[][] RandomGenerator()
	{
		int z;
		for(int x = 0; x < MAX_FATS; x++)
		{
			z = length.nextInt(1000);
			randomArray[x][0] = z + 1800 - (z%10);
			randomArray[x][1] = incdist;
		}
		return randomArray;
	}
	
	public void update()
	{
		super.update();
		Vector<Entity> fats = getBodyParts();
		entityAngle = 360/fats.size();
		for(int x = 0; x < fats.size(); x++)
		{
			if((randomArray[x][0] >= maxFatRadius)||(randomArray[x][0] <= minFatRadius))
			{
				randomArray[x][1] = randomArray[x][1]*-1;
				randomArray[x][0] += randomArray[x][1];
			}
			else
			{
				randomArray[x][0] += randomArray[x][1];
			}
		}
		
		AffineTransform at;
		Point2D original;
		Point2D rotated;
		Point2D ref;
		
		Entity aFat;
		for(int i = 1; i <= fats.size(); i++) 
		{
			original = new Point2D.Double(getX(),(getY() - 420 - randomArray[i-1][0]));
			ref = new Point2D.Double(getX(), getY() - 420);
			at = AffineTransform.getRotateInstance(Math.toRadians(entityAngle*(i)), ref.getX(), ref.getY());
			rotated = at.transform(original, null);
			aFat = fats.get(i-1);
			aFat.setTarget(aFat.getX()*length.nextInt(), aFat.getY()*length.nextInt());
			aFat.setX((int)rotated.getX());
			aFat.setY((int)rotated.getY());
			aFat.update();
		}
		
		getWorld().checkCollisions(this);
	}

	public void render(Renderer renderer)
	{
		renderer.rotate(getX(), getY(), -getAngle());
		Stroke stroke = new BasicStroke(1,
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
				new float[] { 4, 4 }, 16);

		renderer.setStroke(stroke);

		renderer.drawCircle(getX(), getY() - 420, outerRadius, getColor());
		renderer.drawCircle(getX(), getY(), mouthRadius, getColor());

		renderer.setStroke(new BasicStroke(1));
		Vector<Entity> fats = getBodyParts();
		for(int i = 0; i < fats.size(); i++)
			fats.get(i).render(renderer);

		renderer.rotate(getX(), getY(), getAngle());
	}
	
	public boolean evolve() 
	{
		//Need to evolve
		//Go to the last fat that has a value and evolve
		//If this fat is fully evolved then evolve the first fat that 
		//is not fully evolved, starting with the one behind the mouth.
		//Health of fat is disregarded.
		Vector<Entity> fats = getBodyParts();
		Fat aFat;
		boolean evolved;
		for(int i = fats.size()-1; i >= 0; --i)
		{
			aFat = (Fat)(fats.get(i));
			if(aFat.getValue() > 0 && !aFat.getSmallShoulders());
			{
				if(aFat.evolve())
					return true;
				else
				{
					for(int x = 0; x < fats.size(); ++x)
					{
						aFat = (Fat)(fats.get(x));
						if(aFat.evolve())
							return true;
					}
				}
			}
		}
		return false;
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
	
	@Override
	public int getSizeValue()
	{
		int temp = 0;
		for (Entity e : getBodyParts())
			if (((Fat) e).getBig() && ((Fat) e).getEdible())
				temp += e.getSizeValue();
		return temp;
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

		}

		for (int x = 0; x < getBodyParts().size(); x++)
			if (entity == getBodyParts().get(x))
				return;

		if (entity.getType() == FAT) {
			addValue(entity.removeValue());
			setValue(getValue());
			digesting = true;
			timer.start();
			prevTime = timer.milliTime();
		} else if (entity.getType() == FOOD) {
			addValue(entity.removeValue());
			setValue(getValue());
			digesting = true;
			timer.start();
			prevTime = timer.milliTime();
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
			fat.setFrontX(last.getX());
			fat.setY(last.getY());
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
