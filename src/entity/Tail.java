package entity;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import render.Renderer;

public class Tail
{
	private Point2D anchor; 
	private Point2D transformedPoint; 
	private AffineTransform transform;
	private int direction;
	private int angle; 
	
	{
		angle = 180;
	}
	
	public Tail()
	{
		;
	}
	/**
	 * A Tail
	 * @param direction The direction the tail moves in the beginning, 1 or -1
	 */
	public Tail(int direction)
	{
		this.direction = direction;
	}
	
	public void render(int ex, int ey, int radius, Renderer renderer)
	{
	    anchor = new Point2D.Double(ex, ey - radius);
	    
		transform = AffineTransform.getRotateInstance(Math.toRadians(angle), anchor.getX() , anchor.getY() - 80);
		
		transformedPoint = transform.transform(anchor, null);
		renderer.drawLine((int)(transformedPoint.getX()),(int)(transformedPoint.getY()), (int)anchor.getX(), (int)anchor.getY(), Color.WHITE);	
		angle += direction;
		if (angle == 150)
			direction *= -1;
		else if (angle == 210)
			direction *= -1;
	}
}
