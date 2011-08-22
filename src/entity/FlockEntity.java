package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;
import render.Renderer;
import world.World;

public class FlockEntity extends Entity
{	
	private int test = 1000;

	private int test2 = 520;

	private int increment2 = 5;

	private int incdist = 5;

	private int entityLevel = 2;
	
	private int maxentityLevel = 16;

	Random length, angle;

	private int[][] random;

	public int[][] RandomGenerator()
	{
		length = new Random();
		angle = new Random();
		random = new int[maxentityLevel][3];
		int z;
		for(int x = 0; x < maxentityLevel; x++)
		{
			z = length.nextInt(1000);
			random[x][0]= z + 1800 - (z%10);
			random[x][1] = angle.nextInt(60)+ 30;
			random[x][2] = incdist;
		}
		return random;
	}


	public FlockEntity() {
	}

	public FlockEntity(int x, int y)
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


	public FlockEntity(int x, int y, World world)
	{
		this(x, y);
		setWorld(world);
	}

	public void update()
	{
		super.update();
		for(int x = 0; x < entityLevel; x++)
		{
			if((random[x][0] == 2800)||(random[x][0] == 1800))
			{
				random[x][2] = random[x][2]*-1;
				random[x][0] += random[x][2];
			}
			else
			{
				random[x][0] += random[x][2];
			}
		}
		if((test2 ==640)||(test2==400))
			increment2 = increment2*-1;
		test2 +=increment2;
		getWorld().checkCollisions(this);
	}

	public void render(Renderer renderer)
	{
		AffineTransform at;
		Point2D rotated;
		Point2D original; 
		Point2D ref;
		renderer.rotate(getX(), getY(), -getAngle());
		Stroke stroke = new BasicStroke(1,
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
				new float[] { 4, 4 }, 16);

		renderer.setStroke(stroke);
		
		renderer.drawCircle(getX(), getY(), test, Color.WHITE);
		renderer.drawCircle(getX(), getY() + 420, test2, Color.WHITE);
		for(int i = 1; i <= entityLevel ; i++)
		{
			original = new Point2D.Double(getX(),getY() - random[i-1][0]);
			ref = new Point2D.Double(getX(), getY());
			Stroke stroke1 = new BasicStroke(1);
			renderer.setStroke(stroke1);
			at = AffineTransform.getRotateInstance(Math.toRadians(random[i-1][1]), ref.getX(), ref.getY());
			rotated = at.transform(original, null);
			renderer.drawCircle((int)rotated.getX(), (int)rotated.getY(), getRadius()/3, Color.WHITE);
		}
		
		renderer.rotate(getX(), getY(), getAngle());

	}

	@Override   
	public boolean checkCollision(Entity entity)
	{
		if(super.checkCollision(entity))
			return true;

		for(int x = 0; x < getBodyParts().size(); x++)
			if(getBodyParts().get(x).checkCollision(entity))
				return true;
		return false;
	}

	public int getType()
	{
		return MOUTH;
	}
}
