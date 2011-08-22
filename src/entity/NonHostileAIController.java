package entity;

import java.util.Random;

import util.Timer;

public class NonHostileAIController implements Controller{

	/** The maximum velocity of an Entity */
	private final double MAX_VELOCITY = 5;

	/** The range of randomness */
	private final int RANGE = 50 << 5;

	/** The length of each timestep in the game */
	private int TIMESTEP_MILLIS;

	/** The entity being controlled */
	private Entity entity;

	/** The timer */
	private Timer timer;

	/** The timer's current time */
	private long currTime;

	/** The timer's previous time */
	private long prevTime;

	/** The timer's accumulator */
	private long accumulator;

	/** The random token initialised */
	Random rx, ry, rbooleanx, rbooleany, timerrandom;

/** 
 * Initialises a new controller
 * @param entity the entity to control
 */
	public NonHostileAIController(Entity entity) {
		this.entity = entity;

		timer = new Timer();
		currTime = timer.milliTime();
		prevTime = 0;
		timerrandom = new Random();
		TIMESTEP_MILLIS = (1000 * (timerrandom.nextInt(9)+1));
		accumulator = TIMESTEP_MILLIS;
		
		entity.setMaxVelocity(MAX_VELOCITY);
		entity.setVelocity(MAX_VELOCITY);

		rx = new Random();
		ry = new Random();
		rbooleanx = new Random();
		rbooleany = new Random();
	}



	/**
	 * Updates the enitities position, moving it towards the mouse pointer
	 *
	 *
	 */
	public void update() {

		currTime = timer.milliTime(); //get the time we are at
		accumulator += currTime - prevTime; // add time taken since last loop to accumulator

		prevTime = currTime; //set new previous time

		if(accumulator >= TIMESTEP_MILLIS) { //while there is an update rate in the accumulator

			boolean bx =  rbooleanx.nextBoolean();
			boolean by = rbooleany.nextBoolean();

			int xTarget = entity.getX();
			int yTarget = entity.getY();

			if(bx == true)
				xTarget += rx.nextInt(RANGE);
			else
				xTarget += (rx.nextInt(RANGE))*-1;

			if(by == true)
				yTarget += ry.nextInt(RANGE);
			else
				yTarget += (ry.nextInt(RANGE))*-1;

			entity.setTarget(xTarget, yTarget);
			
			accumulator = 0;
		}
	}		
	
	public int getType()
	{
		return NON_HOSTILE;
	}
}
