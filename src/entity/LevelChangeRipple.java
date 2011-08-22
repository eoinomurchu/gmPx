package entity;

import java.awt.Color;

import render.Renderer;
import world.Camera;
import world.World;

/**
 * A level changer ripple is the same as a normal ripple except it is always rendered
 * at the same on-screen coordinates rather than where it actually exists. This will not
 * affect the rest of the game as ripples are not involved in collisions and do not effect
 * the game in any way other than aesthetically.
 *
 */
public class LevelChangeRipple extends Ripple
{
	/** Camera to always render on screen */
	private Camera camera;
	
	/**
	 * Constructs a new <code>LevelChangeRipple</code> with the specified
	 * properties.
	 * 
	 * @param x x coordinate at center in game units.
	 * @param y y coordinate at center in game units.
	 * @param life How long to live for in milliseconds.
	 * @param growthRate How much to increase radius by in game units per update.
	 * @param colour The colour of the ripple.
	 * @param world The world the ripple is to exist in.
	 */
	public LevelChangeRipple(int x, int y, int life, int growthRate, Color colour, World world)
	{
		super(x, y, life, growthRate, colour);
		setWorld(world);
		camera = world.getCamera();
		setX(x - camera.getX());
		setY(y - camera.getY());
	}
	
	public void render(Renderer render)
	{
		render.drawCircle(getX() + camera.getX(), getY() + camera.getY(), getRadius(), getColor());
	}

}
