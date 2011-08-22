package entity;

import input.Input;

import java.awt.Color;

public class PlayerController implements Controller
{

	/** The maximum velocity of an Entity when not clicking */
	private final double MAX_VELOCITY = 50;

	/** The maximum uber velocity of an Entity when clicking */
	private final double MAX_CLICKED_VELOCITY = 150;

	/** Mouse Pointer's old x coordinate in game units */
	private int oldMouseX;

	/** Mouse Pointer's old y coordinate in game units */
	private int oldMouseY;

	/** Input controller for the entity */
	private Input input;

	/** The entity being controlled */
	private Entity entity;

	/**
	 * Constructor
	 * 
	 * 
	 */
	public PlayerController(Input anInput, Entity entity) {
		input = anInput;
		this.entity = entity;
	}

	/**
	 * Updates the enitities position, moving it towards the mouse pointer
	 *
	 *
	 */
	public void update(int cameraX, int cameraY) {
		entity.setColor(Color.WHITE);
		int mouseX = input.getValue(input.POINTER_X);
		int mouseY = input.getValue(input.POINTER_Y);
		boolean clicked = input.getValue(Input.MOUSE_ONE) == Input.PRESSED;

		entity.setMaxVelocity(clicked ? MAX_CLICKED_VELOCITY : MAX_VELOCITY);
		entity.setMovingFast(clicked);
		entity.setTarget(mouseX + cameraX, mouseY + cameraY);

		if(oldMouseX !=  mouseX || oldMouseY != mouseY)
		{
			entity.setAcceleration(2);

			oldMouseX = mouseX;
			oldMouseY = mouseY;
		}
		else if(clicked)
			entity.setAcceleration(2);
		else
			entity.setAcceleration(0);
	}

	public void update()
	{

	}

	public int getType()
	{
		return PLAYER;
	}
}
