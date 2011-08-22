package menu;

import render.Sprite;

public class MovingButton extends Button
{
	private int targetX;
	
	private int targetY;
	
	public MovingButton(Sprite active, Sprite inactive)
	{
		super(active, inactive);
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
