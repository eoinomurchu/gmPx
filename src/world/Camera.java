package world;

import entity.Entity;

public class Camera extends Entity {

	/** The width of the camera */
	private final int DEFAULT_WIDTH = 800 << 5;

	/** The width of the camera */
	private final int DEFAULT_HEIGHT = 600 << 5;

	/** The width of the camera in game units */
	private int width;

	/** The height of the camera in game units */
	private int height;

	/** The Entity that the Camera should follow */
	private Entity entity;

	public Camera() {
		super(0,0);
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
	}

	public Camera(Entity anEntity) {
		super(0,0);
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;

		entity = anEntity;
	}

	public Camera(int aX, int aY, int aWidth, int aHeight) {
		super(aX, aY);

		width = aWidth;
		height = aHeight;
	}

	public Camera(int aX, int aY, int aWidth, int aHeight, Entity anEntity) {
		super(aX, aY);

		entity = anEntity;

		width = aWidth;
		height = aHeight;
	}

	/**
	 * Sets the Entity for the Camera to follow
	 *
	 * @param anEntity The Entity for the Camera to follow
	 */
	public void setEntity(Entity anEntity) {
		entity = anEntity;
	}

	public void setWidth(int aWidth) {
		width = aWidth;
	}

	public void setHeight(int aHeight) {
		height = aHeight;
	}

	public void setScale(float scaleW, float scaleH) {
		width = (int)(width / scaleW);
		height = (int)(height / scaleH);
	}

	/**
	 * Gets the Camera's x coordinate in game units
	 * 
	 * @return Camera's x coordinate.
	 */
	public int getX()
	{
		return super.getX();
	}

	/**
	 * Gets the Camera's y coordinate in game units
	 * 
	 * @return Camera's y coordinate.
	 */
	public int getY()
	{
		return super.getY();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void update() {
		setX(entity.getX()-width/2);
		setY(entity.getY()-height/2);

		super.update();
	}

	public String toString() {
		return ""+getX()+","+getY();
	}
}
