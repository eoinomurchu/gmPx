package menu;

import render.Renderer;
import render.Sprite;

public class MovingLabel extends MenuItem
{

	private Sprite sprite;
	
	private int targetX;
	
	private int targetY;
	
	public MovingLabel(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	@Override
	public void activate()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pressed()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void released()
	{
		// TODO Auto-generated method stub
		
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
	public void update()
	{
		setX((int) (getX() + ((targetX - getX()) * 0.05)));
		setY((int) (getY() + ((targetY - getY()) * 0.05)));
	}
	
	public void setTargetX(int x)
	{
		targetX = x;
	}
	
	public void setTargetY(int y)
	{
		targetY = y;
	}

	public int getTargetX()
	{
		return targetX;
	}
	
	public int getTargetY()
	{
		return targetY;
	}

}
