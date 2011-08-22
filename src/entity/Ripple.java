package entity;

import java.awt.Color;

import render.Renderer;
import util.Timer;

/**
 * An entity that does nothing except provide a visual ripple effect 
 * in the game.
 * 
 * @author Team gmPx
 *
 */
public class Ripple extends Entity
{
	/** The percentage of time that must pass before the ripple starts to fade */
	private static final double FADE_THRESHOLD = 0.4;

	/** How long the ripple will live for in milliseconds */
	private int life;
	
	/** How much to add to radius per update (in game units) */
	private int growthRate;
	
	/** Keeps track of how long the ripple has been alive for */
	private Timer timer;
	
	/** The amount to fade by per update */
	private double fade;
	
	/**
	 * Constructs a new <code>Ripple</code> with the specified
	 * properties.
	 * 
	 * @param x x coordinate at center in game units.
	 * @param y y coordinate at center in game units.
	 * @param life How long to live for in milliseconds.
	 * @param growthRate How much to increase radius by in game units per update.
	 * @param colour The colour of the ripple.
	 */
	public Ripple(int x, int y, int life, int growthRate, Color colour)
	{
		super(x, y);
		this.life = life;
		this.growthRate = growthRate;
		setColor(colour);
		setValue(1); //give a positive value so we don't die immediately
		
		/*
		 * precompute fade value.
		 * 255 = max value for a colour component
		 * 60 = game ticks per second
		 */
		fade = (int) (255 / 60 / ((life / 1000) * (1 - FADE_THRESHOLD)));
		
		timer = new Timer();
		timer.start();
	}
	
	public void update()
	{
		long currTime = timer.milliTime();
		
		if(currTime > life)
		{
			setDead(true); //kill it so it's removed
			return;
		}
		
		//fade entity
		if(currTime > life * FADE_THRESHOLD)
		{
			Color c = getColor();
			int alpha = (int)(c.getAlpha() - fade);
			
			/*
			 * Set the new colour. If the alpha value goes below zero, 
			 * set an alpha value of 0 for the new colour. If this was
			 * allowed do drop below zero an exception will be thrown
			 * by Color.
			 */
			setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 
					alpha >= 0 ? alpha : 0));
		}
		
		//expand ripple
		setRadius(getRadius() + growthRate);
	}
	
	public void render(Renderer render)
	{
		render.drawCircle(getX(), getY(), getRadius(), getColor());
	}
}
