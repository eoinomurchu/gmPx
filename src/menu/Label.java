package menu;

import render.Renderer;
import render.Sprite;

public class Label extends MenuItem
{
	private Sprite sprite;
	
	public Label(Sprite sprite)
	{
		this.sprite = sprite;
	}


	@Override
	public void update()
	{
		
	}
	
	@Override
	public void render(Renderer renderer)
	{
		renderer.renderSprite(sprite, getX(), getY());
	}

	@Override
	public void render(Renderer renderer, float alpha)
	{
		renderer.renderSprite(sprite, getX(), getY(), alpha);
	}
	
	@Override
	public void activate()
	{
		
	}

	@Override
	public void deactivate()
	{
		
	}

	@Override
	public void pressed()
	{
		
	}

	@Override
	public void released()
	{
		
	}
}
