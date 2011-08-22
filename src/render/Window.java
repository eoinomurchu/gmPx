package render;

import input.Input;

/**
 * An ADT for a class to be a game window. 
 */
public interface Window extends Renderer
{
	public static final int JAVA_WINDOW = 0;
	
	public static final int GL_WINDOW = 1;
	
	
	/**
	 * Sets the window to fullscreen mode if available
	 * or windowed mode.
	 * 
	 * @param fullscreen True to set fullscreen
	 */
	void setFullscreen(boolean fullscreen);
	
	/**
	 * Draws the current render to the screen.
	 *
	 */
	void displayRenderer();
	
	/**
	 * Gets the input from the window.
	 * 
	 * @return Input.
	 */
	Input getInput();
	
	/**
	 * Gets the type of rendering device this is.
	 * See constants.
	 * 
	 * @return Rendering device type.
	 */
	int getType();
	
	/**
	 * Sets whether or not to use a widescreen aspect ratio.
	 * 
	 * @param widescreen True to set aspect to widescreen.
	 */
	void setWidescreen(boolean widescreen);
	
	/**
	 * Sets whether the window is resizable.
	 * 
	 * @param resizable True for the window to be resizable.
	 */
	void setResizable(boolean resizable);
	
	/**
	 * Gets the width of the window in pixels.
	 * 
	 * @return Window's width.
	 */
	int getWidth();
	
	/**
	 * Gets the height of the window in pixels.
	 * 
	 * @return Window's height.
	 */
	int getHeight();
	
	/**
	 * Returns true if the window is in fullscreen mode.
	 * 
	 * @return True if window is fullscreen.
	 */
	boolean isFullscreen();
	
	/**
	 * Turns the window decoration (menu bars) on or off.
	 * 
	 * @param value True to turn decoration on.
	 *
	 */
	void setDecorated(boolean value);
}
