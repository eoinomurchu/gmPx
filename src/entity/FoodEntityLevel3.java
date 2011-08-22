package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import render.Renderer;

public class FoodEntityLevel3 extends Food {
	
	private SideTails tails;
	private int tailSep = 288;
	private int entitySpecOffset = 250;
	private int entitySpecRadius = 120;
	private int entitywidth = 544;

	/**
	 * Initialises and instance of the entity
	 *
	 */
	public FoodEntityLevel3() {
		setRadius(960);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));

		tails = new SideTails(-1,1,tailSep);
		setValue(150);
	}

	/**
	 * Sets up to position of the entity in the world
	 * @param x the x co-ordinate in game units
	 * @param y the y co-ordinate in game units
	 */
	public FoodEntityLevel3(int x, int y) {
		this();

		setX(x);
		setY(y);
	}

	public void update() {
		super.update();
	}

	public int getType()
	{
		return FOOD;
	}

	public void render(Renderer renderer) {

		renderer.rotate(getX(), getY(), -getAngle());

		Stroke stroke1 = new BasicStroke(2);
		renderer.setStroke(stroke1);
		renderer.drawOval(getX(), getY(), entitywidth, getRadius() , Color.WHITE);
		renderer.fillCircle(getX() , getY()+ entitySpecOffset, entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX(), getY(), entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() , getY() - entitySpecOffset, entitySpecRadius, Color.WHITE);

		tails.render(getX(), getY(), renderer);
		renderer.rotate(getX(), getY(), getAngle());
	}
}

