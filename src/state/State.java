package state;

import render.Renderer;

public interface State 
{
	/** Constant ID for the game state */
	public static final int GAME_STATE = 0;
	
	/** Constant ID for the menu state */
	public static final int MENU_STATE = 1;
	
	/** The total number of states */
	public static final int TOTAL_STATES = 2;
	
	/**
	 * Updates the game logic of the state.
	 *
	 */
	void update();
	
	/**
	 * This method is called to render the state to
	 * the renderer provided.
	 * 
	 * @param renderer The rendering device.
	 */
	void render(Renderer render);
	
	/**
	 * Called when a state is entered.
	 *
	 */
	void enter();
	
	/**
	 * Called when a state is exited.
	 *
	 */
	void exit();
}
