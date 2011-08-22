package menu;

import render.Renderer;
import render.Sprite;

/**
 * A basic button that can be pressed.
 * 
 * @author Rashid Bhamjee
 *
 */
public class Button extends MenuItem
{
	/** Sprite to render when the button is active */
	private Sprite activeSprite;
	
	/** Sprite to render when the button is inactive */
	private Sprite inactiveSprite;
	
	/**
	 * Constructs a new Button.
	 * 
	 * @param active The sprite to display when active.
	 * @param inactive The sprite to display when inactive.
	 */
	public Button(Sprite active, Sprite inactive)
	{
		activeSprite = active;
		inactiveSprite = inactive;
	}
	
	@Override
	public void render(Renderer renderer)
	{
		if(isActive())
			renderer.renderSprite(activeSprite, getX(), getY());
		else
			renderer.renderSprite(inactiveSprite, getX(), getY());
	}
	
	@Override
	public void render(Renderer renderer, float alpha)
	{
		if (isActive())
			renderer.renderSprite(activeSprite, getX(), getY(), alpha);
		else
			renderer.renderSprite(inactiveSprite, getX(), getY(), alpha);
	}

	@Override
	public void update()
	{
		//do nothing
	}
	
	@Override
	public void pressed()
	{
		getListener().itemPressed(this);
	}

	@Override
	public void released()
	{
		//do nothing
	}

}
