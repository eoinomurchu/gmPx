package core;

import input.Input;
import render.Renderer;

public interface GameManager 
{
	/**
	 * Changes the current game state to the state
	 * with the specified. If an invalud state ID
	 * is given no state is changed.
	 * 
	 * @param stateID The ID of the state to change to.
	 */
	void changeState(int stateID);
	
	/**
	 * Gets input to ask for key states.
	 * 
	 * @return Input device.
	 */
	Input getInput();
	
	/**
	 * Gets a constant refering to the rendering device being used.
	 * See window constants.
	 * 
	 * @return rendering device.
	 */
	int getRenderingDevice();
	
	/**
	 * Starts a new game.
	 * 
	 * @param difficulty The difficulty to set.
	 */
	void newGame(int difficulty);
	
	
	/**
	 * Exits the game.
	 *
	 */
	void quit();
	
	/**
	 * Gets the width of the window in game units.
	 * 
	 * @return Window's width.
	 */
	int getWindowWidth();
	
	/**
	 * Gets the height of the window in game units.
	 * 
	 * @return Window's height.
	 */
	int getWindowHeight();
	
	/**
	 * If the value is true, sets the game into fullscreen mode if supported by
	 * the operating system.
	 * 
	 * @param fullscreen True to try to set to fullscreen mode.
	 */
	void setFullscreen(boolean fullscreen);
	
	/**
	 * Sets the resolution to the specified resolution if supported by
	 * the hardware.
	 * 
	 * @param width Width to set in pixels.
	 * @param height Height to set in pixels.
	 */
	void setResolution(int width, int height);
	
	/**
	 * Gets the game's renderer.
	 * 
	 * @return Game's renderer.
	 */
	Renderer getRenderer();
}
