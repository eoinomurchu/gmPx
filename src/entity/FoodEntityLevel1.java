package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

import render.Renderer;

public class FoodEntityLevel1 extends Food {

	private Tail tail;
	
	/**
	 * Initialises an instance of the entity
	 *
	 */
	public FoodEntityLevel1() {
		setRadius(160);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));
		
		setValue(50);
		
		tail = new Tail();
	}
	
	/**
	 * Sets up to position of the entity in the world
	 * @param x the x co-ordinate in game units
	 * @param y the y co-ordinate in game units
	 */
	public FoodEntityLevel1(int x, int y) {
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
		renderer.drawCircle(getX(), getY() , (int)(160), Color.WHITE);
	    renderer.fillCircle(getX(),getY() , (int)(96), Color.WHITE);

	    tail.render(getX(), getY(), getRadius(), renderer);
	    renderer.rotate(getX(), getY(), getAngle());		
	}
}
