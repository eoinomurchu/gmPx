package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import render.Renderer;

public class FoodEntityLevel5 extends Food {
	
	private Tail taill;
	private Tail tailc;
	private Tail tailr;
	private int tailDist = 0;
	private int tailSepx = 608;
	private int tailSepy = 832;
	
	private int entityArcFrontH = 922;
	private int entityArcFrontW = 1216;
	private int entityArcSideH = 1100;
	private int entityArcSideW = 500;
	private int entityArcBackW = 608;
	private int entityArcBackH = 608;
	private int entityArcSideSeperation = 555;
	private int entityArcSideYoffset = 500;
	private int entityArcBackYoffset = 512;
	private int entitySpecGapx = 224;
	private int entitySpecGapy = 512;
	private int entitySpecRadius = 130;
	
	private int arcAngle1 = 180 ;
	private int arcAngle2 = 90;
	private int arcAngle3 = -180;
	private int arcAngle4 = -90;

	/**
	 * Initialises and instance of the entity
	 *
	 */
	public FoodEntityLevel5() {
		setRadius(640);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));

		setValue(250);
		
		taill = new Tail(-1);
		tailc = new Tail(1);
		tailr = new Tail(1);
	}

	/**
	 * Sets up to position of the entity in the world
	 * @param x the x co-ordinate in game units
	 * @param y the y co-ordinate in game units
	 */
	public FoodEntityLevel5(int x, int y) {
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

	public void render(Renderer renderer) 
	{

		renderer.rotate(getX(), getY(), -getAngle());
		
		Stroke stroke1 = new BasicStroke(2);
		renderer.setStroke(stroke1);

		renderer.fillCircle(getX() + entitySpecGapx, getY(), entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() - entitySpecGapx, getY(), entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() , getY() - entitySpecGapx, entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() , getY() + entitySpecGapx, entitySpecRadius, Color.WHITE);
		renderer.fillCircle(getX() , getY() - entitySpecGapy, entitySpecRadius, Color.WHITE);
		
		renderer.drawArc(getX() , getY(), entityArcFrontW, entityArcFrontH, arcAngle1, arcAngle1, Color.WHITE);
		renderer.drawArc(getX() , getY() - entityArcBackYoffset, entityArcBackW, entityArcBackH, arcAngle1, arcAngle3, Color.WHITE);
		renderer.drawArc(getX()  - entityArcSideSeperation, getY() - entityArcSideYoffset , entityArcSideW, entityArcSideH, arcAngle4,arcAngle2, Color.WHITE);
		renderer.drawArc(getX()  + entityArcSideSeperation, getY() - entityArcSideYoffset , entityArcSideW, entityArcSideH, arcAngle4, arcAngle4, Color.WHITE);

		taill.render(getX() - tailSepx, getY() , tailDist, renderer);
		tailc.render(getX(), getY() - tailSepy , tailDist, renderer);
		tailr.render(getX() + tailSepx, getY() , tailDist, renderer);
		
		renderer.rotate(getX(), getY(), getAngle());
	}
}
