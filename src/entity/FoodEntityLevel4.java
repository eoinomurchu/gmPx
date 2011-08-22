package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import render.Renderer;

public class FoodEntityLevel4 extends Food {
	
	private Tail tail1;
	private Tail tail2;
	private int tailDist = 720;
	private int tailSep = 192;
	private int entityHeigth = 480;
	private int entityWidth = 960;
	private int entityArcSeperation = 170;
	private int entityArcHeightDrop = 200;
	private int entitySpecGapx = 192;
	private int entitySpecGapy = 416;
	private int entitySpecRadius = 130;
	private int angle1a = 45 ;
	private int angle1b = 270;
	private int angle2a = -135;
	private int angle2b = 270;
	
	/**
	 * Initialises and instance of the entity
	 *
	 */
	public FoodEntityLevel4() {
		setRadius(960);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));

		setValue(200);
		
		tail1 = new Tail(-1);
		tail2 = new Tail(1);
	}

	/**
	 * Sets up to position of the entity in the world
	 * @param x the x co-ordinate in game units
	 * @param y the y co-ordinate in game units
	 */
	public FoodEntityLevel4(int x, int y) {
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
		renderer.drawArc(getX() -entityArcSeperation, getY() - entityArcHeightDrop, entityHeigth, entityWidth, angle1a, angle1b, Color.WHITE);
		renderer.drawArc(getX() +entityArcSeperation, getY() - entityArcHeightDrop, entityHeigth, entityWidth, angle2a, angle2b, Color.WHITE);
		renderer.fillCircle(getX() + entitySpecGapx, getY(), entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() - entitySpecGapx, getY(), entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() + entitySpecGapx, getY() - entitySpecGapy, entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() - entitySpecGapx, getY() - entitySpecGapy, entitySpecRadius, Color.WHITE);
		
		tail1.render(getX() - tailSep, getY() , tailDist, renderer);
		tail2.render(getX() + tailSep, getY() , tailDist, renderer);
		
		renderer.rotate(getX(), getY(), getAngle());
	}
}
