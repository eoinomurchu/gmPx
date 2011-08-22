package entity;

import java.awt.Color;

import render.Renderer;

public class Mouth
{
	/** Max mouth width */
	private int maxWidth;
	
	/** Min mouth width */
	private int minWidth;
	
	/** The height of the mouth */
	private int height;
	
	/** Speed at which to change mouth's width */
	private double CHEW_SPEED = 128.0;
	
	/** Current mouth's width */
	public double currWidth;

	/** True if the mouth should close */
	private boolean close;
	
	/**
	 * Constructs a new mouth.
	 * 
	 * @param maxWidth The mouth's maximum width.
	 * @param minWidth The mouth's minimum width.
	 * @param height The height of the mouth.
	 */
	public Mouth(int maxWidth, int minWidth, int height)
	{
		this.maxWidth = maxWidth;
		this.minWidth = minWidth;
		this.height = height;
		currWidth = maxWidth;
	}
	
	/**
	 * Sets the maximum width of the mouth.
	 * 
	 * @param width Width of the mouth.
	 */
	public void setMaxWidth(int width)
	{
		maxWidth = width;
	}
	
	/**
	 * Sets the minumum width of the mouth.
	 * 
	 * @param width Width of the mouth.
	 */
	public void setMinWidth(int width)
	{
		minWidth = width;
	}

	/**
	 * Sets the height of the mouth.
	 * 
	 * @param height Height of the mouth.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**
	 * Sets the mouth to close or open.
	 * 
	 * @param closed True to close, false to open
	 */
	public void setClosed(boolean closed)
	{
		close = closed;
	}
	
	/**
	 * Updates the mouth's state.
	 *
	 */
	public void update()
	{
		currWidth += close ? -CHEW_SPEED : CHEW_SPEED;
		
		if(currWidth > maxWidth)
			currWidth = maxWidth;
		else if(currWidth < minWidth)
			currWidth = minWidth;
	}
	
	/**
	 * Renders the mouth.
	 * 
	 * @param renderer The renderer to draw the mouth.
	 * @param x the x coordinate of the upper-left corner of the arc to be drawn.
	 * @param y the y coordinate of the upper-left corner of the arc to be drawn.
	 * @param color the colour of the arc to be drawn
	 */
	public void render(Renderer renderer, int x, int y, Color color)
	{
		renderer.drawArc(x, y, (int) currWidth, height, 180, -180, color);
	}
}
