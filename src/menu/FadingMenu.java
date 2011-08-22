package menu;

import render.Renderer;

/**
 * Fading menus handle the visibility of the menu themselves, showing the menu when
 * activated and hiding the menu when fadeout is complete.
 * 
 * @author Rashid Bhamjee
 *
 */
public class FadingMenu extends Menu
{
	/** Constant for fading in state */
	private final int FADE_IN = 0;
	
	/** Constant for fading out state */
	private final int FADE_OUT = 1;
	
	/** Constant for fading solid state */
	private final int NO_FADE = 2;
	
	/** The rate to change the alpha value by each update */
	private final float ALPHA_CHANGE = 0.02f;
	
	/** The current state */
	private int state;
	
	/** The current aplha value */
	private float currAlpha;
	
	/**
	 * Constructs a new fading 
	 *
	 */
	public FadingMenu()
	{
		state = NO_FADE;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		switch (state)
		{
		case FADE_IN:
			currAlpha += ALPHA_CHANGE;
			if (currAlpha > 1.0f)
			{
				currAlpha = 1.0f;
				state = NO_FADE;
			}
			break;
		case FADE_OUT:
			currAlpha -= ALPHA_CHANGE;
			if (currAlpha < 0.0f)
			{
				currAlpha = 0.0f;
				state = NO_FADE;
				setVisible(false);
			}
			break;
		}
	}
	
	@Override
	public void render(Renderer renderer)
	{
		switch (state)
		{
		case FADE_IN:
			for(int x = 0, n = items.size(); x < n; x++)
				if(items.get(x).isVisible())
					items.get(x).render(renderer, currAlpha);
			break;
		case FADE_OUT:
			for(int x = 0, n = items.size(); x < n; x++)
				if(items.get(x).isVisible())
					items.get(x).render(renderer, currAlpha);
			break;
		case NO_FADE:
			super.render(renderer);
			break;
		}
	}
	
	/**
	 * Activates the menu and sets it visible using a fade in effect.
	 */
	@Override
	public void activate()
	{
		super.activate();
		setVisible(true);
		state = FADE_IN;
		currAlpha = 0.0f;
	}
	
	/**
	 * Deactivates the menu and hides it using a fade out effect.
	 */
	@Override
	public void deactivate()
	{
		super.deactivate();
		state = FADE_OUT;
		currAlpha = 1.0f;
	}

}
