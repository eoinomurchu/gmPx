package entity;

import java.awt.Color;

import render.Renderer;


public class Speck extends Entity 
{
	/** Specks radius in game units */ 
	private final int RADIUS = 32;

	/** Specks maximum velocity */
	private final int VELOCITY = 3; 

	/** Specks turning speed */
	private final double TURNING_SPEED = Math.toRadians(0.5); 

	
	public Speck(int x, int y)
	{
		setX(x);
		setY(y);
		setAcceleration(0);
		setDeceleration(0);
		setVelocity(VELOCITY);
		setTurningSpeed(TURNING_SPEED);

		setRadius(RADIUS);
		setColor(new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), 
				255/3));
	}

	public void update()
	{
		super.update();
	}

	public void render(Renderer renderer)
	{
		renderer.fillCircle(getX(), getY(), getRadius()+64/2, getColor());
		renderer.fillCircle(getX(), getY(), getRadius()+32/2, getColor());
		renderer.fillCircle(getX(), getY(), getRadius(), getColor());
	}

	public boolean checkDead()
	{
		return false;
	}
}
