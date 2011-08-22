package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import render.Renderer;

public class FoodEntityLevel2 extends Food {
	
	private Tail tail1;
	private Tail tail2;
	private int tailDist = 256;
	private int tailSep = 150;
	private int entityWidth = 480;
	private int entitySpecGap = 160;
	private int entitySpecRadius = 120;

	/**
	 * Initialises and instance of the entity
	 *
	 */
	public FoodEntityLevel2() {
		setRadius(960);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));

		setValue(100);
		tail1 = new Tail(-1);
		tail2 = new Tail(1);
	}

	/**
	 * Sets up to position of the entity in the world
	 * @param x the x co-ordinate in game units
	 * @param y the y co-ordinate in game units
	 */
	public FoodEntityLevel2(int x, int y) {
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
		renderer.drawOval(getX(), getY(), getRadius() , entityWidth, Color.WHITE);
		renderer.fillCircle(getX() + entitySpecGap, getY(), entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() - entitySpecGap, getY(), entitySpecRadius, Color.WHITE);
		tail1.render(getX() - tailSep, getY() , tailDist, renderer);
		tail2.render(getX() + tailSep, getY() , tailDist, renderer);

		renderer.rotate(getX(), getY(), getAngle());		
	}
}
