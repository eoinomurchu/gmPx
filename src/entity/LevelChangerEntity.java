package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import render.Renderer;
import util.Timer;
import world.Camera;

public class LevelChangerEntity extends Entity {

	/** How long to wait between pings in milliseconds */
	private static int PING_TIME = 7000;
	
	/** How long a ping ripple will live for */
	private static int RIPPLE_LIFE = 1000;
	
	/** How fast a ping ripple will grow */
	private static int RIPPLE_GROWTH = 35;
	
	/** used for pinging */
	private Camera camera;
	
	/** Keeps track of when we need to ping again */
	private Timer timer;
	
	/** Keeps track of the moving triangle position */
	private int adjust = 0;
	
	/** Adjustment increment of triangle positions */
	private int adjustinc =5;
	
	/**
	 * Initialises an instance of the entity
	 *
	 */
	public LevelChangerEntity() {
		setRadius(320);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));		
		
		setDead(false);
		
		timer = new Timer();
		timer.start();
	}

	/**
	 * Sets up to position of the entity in the world
	 * @param x the x co-ordinate in game units
	 * @param y the y co-ordinate in game units
	 */
	public LevelChangerEntity(int x, int y) {
		this();

		setX(x);
		setY(y);
	}

	public void update() {
		//adjust has been moved to the update to ensure consistant rendering
		adjust = adjust + adjustinc;
		if ((adjust == 200)||(adjust == 0))
			adjustinc = adjustinc*-1;
		super.update();
	}
	
	public boolean checkDead()
	{
		return getDead();
	}
	
	public void ping()
	{
		if(camera != null && timer.milliTime() > PING_TIME)
		{
			int x = 0;
			int y = 0;
			
			if(getX() > camera.getX() + camera.getWidth())
				x = camera.getX() + camera.getWidth();
			else if(getX() < camera.getX())
				x = camera.getX();
			else
				x = getX();
			
			if(getY() > camera.getY() + camera.getHeight())
				y = camera.getY() + camera.getHeight();
			else if(getY() < camera.getY())
				y = camera.getY();
			else
				y = getY();
			
			if(getX() == x && getY() == y)//is on screen so don't spawn
				return;
			
			Ripple r = new LevelChangeRipple(x, y, RIPPLE_LIFE, RIPPLE_GROWTH, getColor(), getWorld());
			getWorld().addEntity(r);
			timer.reset();
			
		}
	}

	public int getType()
	{
		return LEVEL_CHANGER;
	}
	
	public void setValue(int value)
	{
		super.setValue(value);
		

		if(value < -10)
			setColor(Color.YELLOW);
		else if(value < 0)
			setColor(Color.GREEN);
		else
			setColor(Color.RED);
	}
	
	/**
	 * Sets the camera used for pining the changer's location.
	 * 
	 * @param camera The camera.
	 */
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}

	public void render(Renderer renderer) 
	{
		renderer.rotate(getX(), getY(), -getAngle());	
		Stroke stroke = new BasicStroke(1);
		Stroke stroke1 = new BasicStroke(2);

		if(getValue() == -19)
		{
			renderer.drawCircle(getX(), getY() , (int)(160), Color.WHITE);
		    renderer.fillCircle(getX(),getY() , (int)(96), getColor());
		}
		else if(getValue() < 0)
		{

			renderer.setStroke(stroke);
			renderer.fillCircle(getX(), getY(), 96, Color.GREEN);
			renderer.drawCircle(getX(), getY(), getRadius(), Color.WHITE);
		
			renderer.drawTriangle(getX() - getRadius() -adjust, getY() , 160, 0, -30, 30, Color.WHITE);
			renderer.drawTriangle(getX() + getRadius() + adjust, getY() ,  - 160, 0, -30, 30, Color.WHITE);
			renderer.drawTriangle(getX(), getY() - getRadius() - adjust, 0, 160, -30, 30, Color.WHITE);
			renderer.drawTriangle(getX(), getY() +getRadius() + adjust, 0,  -160, -30, 30, Color.WHITE);
			renderer.setStroke(stroke1);
			
		}
		else 
		{
			renderer.setStroke(stroke);
			renderer.fillCircle(getX(), getY(), 96, Color.RED);
			renderer.drawCircle(getX(), getY(), getRadius(), Color.WHITE);
			renderer.drawTriangle(getX() - 192 - adjust, getY() , -160, 0, -30, 30, Color.WHITE);
			renderer.drawTriangle(getX() + 192 + adjust, getY() , 160, 0, -30, 30, Color.WHITE);
			renderer.drawTriangle(getX(), getY() - 192 - adjust, 0, -160, -30, 30, Color.WHITE);
			renderer.drawTriangle(getX(), getY() +192 + adjust, 0, 160, -30, 30, Color.WHITE);
			renderer.setStroke(stroke1);
		}
		
		renderer.rotate(getX(), getY(), getAngle());
	}
}

