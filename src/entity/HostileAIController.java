package entity;

import java.awt.Color;
import java.util.Random;

import util.Timer;

public class HostileAIController implements Controller{

	/** The maximum velocity of an Entity */
	private final double MAX_VELOCITY = 25;

	/** The maximum velocity of an Entity */
	private final double MAX_ACCELERATION = 4;

	/** The maximum attack velocity of an Entity */
	private final double MAX_ATTACK_VELOCITY = 125;

	/** The maximum attack velocity of an Entity */
	private final double MAX_ATTACK_ACCELERATION = 8;

	/** The range of randomness */
	private final int RANGE = 50 << 5;

	/** The range of running away because i'm a scared little entity */
	private final int FLEE_RANGE = 500 << 5;

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

	/** The Entities radar entity */
	private Entity radarEntity;

	/** The previous value of the entity */
	private int prevEntityValue;

	/** The boolean to say weather to runaway or not */
	private boolean runAway;

	/** The time to spend running away */
	private int runningTime;

	Random rx, ry, rbooleanx, rbooleany, timerrandom;

/**
 * Initialises a new controller
 * @param entity the entity to control
 */
	public HostileAIController(Entity entity) {
		this.entity = entity;
		radarEntity = new Entity();
		radarEntity.setRadius(100<<5);
		timer = new Timer();
		currTime = timer.milliTime();
		prevTime = timer.milliTime();
		timerrandom = new Random();
		TIMESTEP_MILLIS = (1000 * (timerrandom.nextInt(19)+1));
		accumulator = TIMESTEP_MILLIS;
		rx = new Random();
		ry = new Random();
		rbooleanx = new Random();
		rbooleany = new Random();
		prevEntityValue = entity.getSizeValue();
	}



	/**
	 * Updates the enitities position depending on nearest food and attacking
	 *
	 *
	 */
	public void update() 
	{
		radarEntity.setX(entity.getX());
		radarEntity.setY(entity.getY());
		Entity target = entity.getWorld().getNearbyFood(radarEntity, entity);

		if(entity.getSizeValue()*entity.getBodyParts().size() < prevEntityValue*entity.getBodyParts().size())
			runAway = true;

		if(!runAway)
		{
			if (target != null)
			{

				int xTarget = target.getX();
				int yTarget = target.getY();
				if(target.getType() < 3)
				{
					entity.setColor(Color.orange);
					entity.setVelocity(MAX_ATTACK_VELOCITY);
					entity.setAcceleration(MAX_ATTACK_ACCELERATION);
					entity.setDeceleration(MAX_ATTACK_ACCELERATION/2);
					entity.setTarget(xTarget, yTarget);
				}
				else
				{
					entity.setColor(Color.white);
					entity.setVelocity(MAX_VELOCITY);
					entity.setAcceleration(MAX_ACCELERATION);
					entity.setDeceleration(MAX_ACCELERATION/2);	
					entity.setTarget(xTarget, yTarget);
				}
			}
			else 
			{
				entity.setColor(Color.white);
				entity.setVelocity(MAX_VELOCITY);
				entity.setAcceleration(MAX_ACCELERATION);
				entity.setDeceleration(MAX_ACCELERATION/2);
				currTime = timer.milliTime(); //get the time we are at
				accumulator += currTime - prevTime; // add time taken since last loop to accumulator

				prevTime = currTime; //set new previous time

				if(accumulator >= TIMESTEP_MILLIS) 
				{ //while there is an update rate in the accumulator

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
			prevEntityValue = entity.getSizeValue();
		}
		else //we're running away
		{
			entity.setColor(Color.blue);
			entity.setVelocity(MAX_ATTACK_VELOCITY);
			entity.setAcceleration(MAX_ATTACK_ACCELERATION);
			currTime = timer.milliTime(); //get the time we are at
			accumulator += currTime - prevTime; // add time taken since last loop to accumulator
			runningTime += currTime - prevTime;
			prevTime = currTime; //set new previous time

			if(accumulator >= TIMESTEP_MILLIS) { //while there is an update rate in the accumulator

				int xTarget = entity.getX();
				int yTarget = entity.getY();

				if(xTarget <  0)
					xTarget -= FLEE_RANGE ;

				else
					xTarget += FLEE_RANGE;

				if(yTarget < 0)
					yTarget -= FLEE_RANGE;

				else
					yTarget += FLEE_RANGE;

				entity.setTarget(xTarget, yTarget);
				accumulator = 0;
			}


			if(runningTime > 10000)
			{
				runAway = false;
				runningTime = 0;
				prevEntityValue = entity.getSizeValue();
				accumulator = TIMESTEP_MILLIS;
			}
		}
	}

	public int getType()
	{
		return HOSTILE;
	}
}


