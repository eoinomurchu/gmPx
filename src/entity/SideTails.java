package entity;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import render.Renderer;

public class SideTails
{
	private Point2D anchorleft, anchorright; 
	private Point2D transformedPointl;
	private Point2D transformedPointr; 
	private AffineTransform transformleft, transformright;
	private int directionleft, directionright;
	private int tailseperation;
	private int taillength = 80;
	private int angleleft, angleright; 
	
	{
		angleleft = -110;
		angleright = 110;
	}
	
	public SideTails()
	{
		;
	}
	
	/**
	 * A set of side Tails
	 * @param directionleft the direction of movement of the left tail
	 * @param directionright the direction of movement of the right tail
	 * @param tailseperation the distance between the 2 tails
	 */
	public SideTails(int directionleft, int directionright, int tailseperation)
	{
		this.directionleft = directionleft;
		this.directionright = directionright;
		this.tailseperation = tailseperation;
	}
	
	public void render(int ex, int ey, Renderer renderer)
	{
		anchorleft = new Point2D.Double(ex - tailseperation, ey);
		anchorright = new Point2D.Double(ex + tailseperation, ey);
	    
		transformleft = AffineTransform.getRotateInstance(Math.toRadians(angleleft), anchorleft.getX()  - taillength, anchorleft.getY());
		transformright = AffineTransform.getRotateInstance(Math.toRadians(angleright), anchorright.getX()  + taillength , anchorright.getY());
		transformedPointl = transformleft.transform(anchorleft, null);
		transformedPointr = transformright.transform(anchorright, null);
		renderer.drawLine((int)(transformedPointl.getX()),(int)(transformedPointl.getY()), (int)anchorleft.getX(), (int)anchorleft.getY(), Color.WHITE);
		renderer.drawLine((int)(transformedPointr.getX()),(int)(transformedPointr.getY()), (int)anchorright.getX(), (int)anchorright.getY(), Color.WHITE);
		angleleft += directionleft;
		angleright += directionright; 

		if (angleleft == -105)
			directionleft *= -1;
		else if (angleleft == -140)
			directionleft *= -1;
		
		if (angleright == 105)
			directionright *= -1;
		else if (angleright == 140)
			directionright *= -1;
	}
}
