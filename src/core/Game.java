package core;

import input.Input;

import java.awt.Color;

import render.JavaWindow;
import render.Renderer;
import render.Window;
import state.FlowState;
import state.MenuState;
import state.State;
import util.Timer;

public class Game implements GameManager
{
	/** The number of times to update the game per second */
	private final int TICKS = 60;
	
	/** The amount to shift by to convert from game units to pixels. */
	public static final int CONVERSION_FACTOR = 5;
	
	/** The length of each timestep in the game */
	private final int TIMESTEP_MILLIS = 1000 / TICKS;
	
	/** The width of the window in pixels */
	private final int WINDOW_WIDTH = 800;
	
	/** The width of the window in pixels */
	private final int WINDOW_HEIGHT = 600;
	
	/** The game window */
	private Window window;
	
	/** Array of states. Only one instance of a state can be held in the game */
	private State[] states;
	
	/** The current state the game is in. The game can only be in one state at a time */
	private State currState;
	
	public Game()
	{
		init();
		initStates();
	}
	
	private void init()
	{
		states = new State[State.TOTAL_STATES];
		
		window = new JavaWindow(WINDOW_WIDTH, WINDOW_HEIGHT, CONVERSION_FACTOR);
		window.setWidescreen(false);
	}
	
	private void initStates()
	{
		states[State.MENU_STATE] = new MenuState(this);
		states[State.GAME_STATE] = new FlowState(this);	
		currState = states[State.MENU_STATE];		
		currState.enter();
	}
	
	public void loop()
	{
		Timer timer = new Timer();
    	long currTime = timer.milliTime();
    	long prevTime = timer.milliTime();
    	long accumulator = 0;
    	
    	//fps counter variables
    	Timer fpsTimer = new Timer();
    	int frame = 0;
    	int fps = 0;
    	
		while(true)
		{
            currTime = timer.milliTime(); //get the time we are at
            accumulator += currTime - prevTime; // add time taken since last loop to accumulator
            
            prevTime = currTime; //set new previous time
            
            while(accumulator >= TIMESTEP_MILLIS) //while there is an update rate in the accumulator
            {
            	//poll input first!
            	window.getInput().poll();
            	
    			currState.update();
                
            	accumulator -= TIMESTEP_MILLIS; //take one update value away from the accumulator
            }

			currState.render(window);
			window.drawString(fps+"", 300, 1500, Color.RED);
			window.displayRenderer();
			
			//calculate frame per second
			if(fpsTimer.secondTime() > 1)
			{
				fps = frame;
				frame = 0;
				fpsTimer.reset();
			}
			else 
				frame++;
			
		}
	}
	
	public void changeState(int stateID)
	{
		currState.exit();
		currState = states[stateID];
		currState.enter();
	}
	
	public Renderer getRenderer()
	{
		return window;
	}
	
	public void newGame(int difficulty)
	{
		window.setResizable(true);
		changeState(State.GAME_STATE);
	}

	public void quit()
	{
		System.exit(0);
	}
	
	public void setFullscreen(boolean fullscreen)
	{
		if(fullscreen)
		{
			window.setResizable(false);
			window.setDecorated(false);
		}
		
		System.out.println("fullscreen setting to " + fullscreen);
		
		boolean prevFull = window.isFullscreen();
		window.setFullscreen(fullscreen);
		
		//if change success reposition menu
		if(prevFull != window.isFullscreen())
			((MenuState)states[State.MENU_STATE]).reinit();
		
		if(!window.isFullscreen())
		{
			window.setResizable(true);
			window.setDecorated(true);
		}
	}	
	
	public Input getInput()
	{
		return window.getInput();
	}
	
	public int getWindowWidth()
	{
		return window.getWidth() << CONVERSION_FACTOR;
	}
	
	public int getWindowHeight()
	{
		return window.getHeight() << CONVERSION_FACTOR;
	}
	
	public int getRenderingDevice()
	{
		return window.getType();
	}
	
	public static void main(String[] args)
	{
		Game game = new Game();
		game.loop();
	}

	public void setResolution(int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
}
