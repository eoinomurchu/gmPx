package input;

import core.Game;

public abstract class Input
{
	/** Constant for refering to a keyboard device */
	public static final int KEYBOARD = 0;
	
	/** Constant for refering to a mouse device */
	public static final int MOUSE = 1;
	
	
	/** Constant for refering to the up button */
	public static final int UP = 0;
	
	/** Constant for refering to the down button */
	public static final int DOWN = 1;
	
	/** Constant for refering to the left button */
	public static final int LEFT = 2;

	/** Constant for refering to the right button */
	public static final int RIGHT = 3;
	
	/** Constant for refering to the confirm button */
	public static final int CONFIRM = 4;
	
	/** Constant for refering to the cancel button */
	public static final int CANCEL = 5;
	
	/** Constant for refering to the speed up button */
	public static final int MOUSE_ONE = 6;
	
	/** Constant for refering to the zoom in up button */
	public static final int ZOOM_IN = 7;
	
	/** Constant for refering to the zoom out up button */
	public static final int ZOOM_OUT = 8;
	
	/** 
	 * Constant for refering to the pointer's x coordinate.
	 * This value must not clash with any MouseEvent constant.
	 */
	public static final int POINTER_X = 9;
	
	/** 
	 * Constant for refering to the pointer's y coordinate.
	 * This value must not clash with any MouseEvent constant.
	 */
	public static final int POINTER_Y = 10;
	
	/** The number of buttons in the input */
	public static final int BUTTON_COUNT = 11;
	
	/** Constant for refering to a pressed button value */
	public static final int PRESSED = 1;
	
	/** Constant for refering to a released button value */
	public static final int RELEASED = 0;


	/** The scaling of the window to work out pointer coordinates in game units */
	private float scaleX;
	
	/** The scaling of the window to work out pointer coordinates in game units */
	private float scaleY;
	
	/** Represents the current state of the inputs */
	protected int[] inputs;
	
	/** Bindings from keyboard keycode to Input keys */
	protected int[] keyboardBindings;
	
	/** Bindings from mouse keycode to Input keys */
	protected int[] mouseBindings;
	
	public Input()
	{
		inputs = new int[BUTTON_COUNT];
		keyboardBindings = new int[BUTTON_COUNT];
		mouseBindings = new int[BUTTON_COUNT];
		
		//set up these bindings as user cannot change them
		mouseBindings[POINTER_X] = POINTER_X;
		mouseBindings[POINTER_Y] = POINTER_Y;
	}
	
	/** 
	 * Polls the input for changes.
	 *
	 */
	public abstract void poll();
	
	
	/**
	 * Processes an event. The button code should the
	 * the button code from the Java API. If the event was a pointer
	 * event then the button code from the Input class should be
	 * provided.
	 * 
	 * @param device The device the event came from.
	 * @param button The button code of the button causing the event.
	 * @param value The value of the button.
	 */
	protected void processEvent(int device, int button, int value)
	{
		int[] bindings;
		
		//set the bindings to check for
		if(device == MOUSE)
			bindings = mouseBindings;
		else if(device == KEYBOARD)
			bindings = keyboardBindings;
		else
			return;
		
		if(button == bindings[POINTER_X])
			inputs[POINTER_X] = value;
		else if(button == bindings[POINTER_Y])
			inputs[POINTER_Y] = value;
		else if(button == bindings[MOUSE_ONE])
			inputs[MOUSE_ONE] = value;
		else if(button == bindings[UP])
			inputs[UP] = value;
		else if(button == bindings[DOWN])
			inputs[DOWN] = value;
		else if(button == bindings[LEFT])
			inputs[LEFT] = value;
		else if(button == bindings[RIGHT])
			inputs[RIGHT] = value;
		else if(button == bindings[CONFIRM])
			inputs[CONFIRM] = value;
		else if(button == bindings[CANCEL])
			inputs[CANCEL] = value;
		else if(button == bindings[ZOOM_IN])
			inputs[ZOOM_IN] = value;
		else if(button == bindings[ZOOM_OUT])
			inputs[ZOOM_OUT] = value;
	}
	
	/**
	 * Gets the value of a specified input. If the input is a button
	 * the constants PRESSED or RELEASED are returned. If the input 
	 * is a pointer coordinate then the value returned is the position
	 * of the pointer in the window from the top left corner.
	 * 
	 * See class constants.
	 * 
	 * @param input The input to get.
	 * @return The button's value.
	 */
	public int getValue(int input)
	{
		if(input == POINTER_X)
			return (int)((inputs[input] << Game.CONVERSION_FACTOR) / scaleX);
		else if(input == POINTER_Y)
			return (int)((inputs[input] << Game.CONVERSION_FACTOR) / scaleY);
		
		return inputs[input];
	}
	
	public void bindKey(int device, int deviceKey, int gameKey)
	{
		if(device == KEYBOARD)
			keyboardBindings[gameKey] = deviceKey;
		else if(device == MOUSE)
			mouseBindings[gameKey] = deviceKey;
	}
	
	public void setScale(float x, float y)
	{
		scaleX = x;
		scaleY = y;
	}
}