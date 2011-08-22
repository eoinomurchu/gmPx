package entity;

import java.awt.Color;

import render.Renderer;

public class EvolverEntity extends Food {

	public static int EVOLVER_VAL = 1;
	
	public static int MOUTH_VAL = 2;
	
	public EvolverEntity() {
		setRadius(640);
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));

		setValue(50);
	}

	public EvolverEntity(int x, int y) {
		this();

		setX(x);
		setY(y);
	}

	public void update() {
		super.update();
	}

	public int getType()
	{
		return EVOLVER;
	}

	public void render(Renderer renderer) {

		renderer.rotate(getX(), getY(), -getAngle());

		if(getValue() == 1)
		{
			renderer.rotate(getX(), getY(), -getAngle());

			if(getValue() == 1){
				renderer.drawPentagon(getX(),getY(),320, Color.WHITE);
				renderer.drawCross(getX(), getY(), 160, Color.WHITE);
			}
			renderer.rotate(getX(), getY(), getAngle());
		}
		else
		{
			renderer.drawPentagon(getX(),getY(),320, Color.WHITE);
			renderer.drawArc(getX(), getY(), 320, 320, 0, -180, Color.WHITE);		
		}
		renderer.rotate(getX(), getY(), getAngle());

	}
	
	public void collision(Entity entity)
	{
		setDead(true);
	}
}

