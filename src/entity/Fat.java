package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import render.Renderer;


public class Fat extends Entity
{
	private final int INNER_MOST_RADIUS = 3 << CONVERSION_FACTOR;
	private final int MID_RADIUS = 5 << CONVERSION_FACTOR;
	private final int OUTER_MOST_RADIUS = 7 << CONVERSION_FACTOR;

	private final int SMALL_SHOULDER_RADIUS = 2 << CONVERSION_FACTOR;
	private final int BIG_SHOULDER_RADIUS = 3 << CONVERSION_FACTOR;
	
	private final int ARM_LENGTH = 30 << CONVERSION_FACTOR;

	private final int TAIL_DISTANCE = 7 << CONVERSION_FACTOR;
	private final int TAIL_RADIUS = 2 << CONVERSION_FACTOR;

	private final int MAX_SMALL_VALUE = 50;
	private final int MAX_BIG_VALUE = 150;
	private final int MAX_SMALL_SHOULDER_VALUE = 200;
	private final int MAX_BIG_SHOULDER_VALUE = 250;
	
	private final double ARM_ANGLE_UPPER_THRESHOLD = -30;
	private final double ARM_ANGLE_LOWER_THRESHOLD = -75;

	private boolean big;
	private boolean edible;
	private boolean smallShoulders;
	private boolean bigShoulders;

	private Point2D anchor; 
	private Point2D transformedPoint;
	AffineTransform transform;
	private double angle;
	private double angleIncrement;
	
	private double armUpper;
	private double armLower;
	
	public Fat()
	{
		setRadius(OUTER_MOST_RADIUS);
		setVelocity(0);
		setAcceleration(0);
		setDeceleration(0);
		setColor(Color.WHITE);

		big = false;
		edible = true;
		smallShoulders = false;
		bigShoulders = false;

		anchor = null; 
		transformedPoint = null;
		transform = null;
		angle = ARM_ANGLE_UPPER_THRESHOLD-1;
		angleIncrement = 1;

		armUpper = ARM_ANGLE_UPPER_THRESHOLD;
		armLower = ARM_ANGLE_LOWER_THRESHOLD;
	}

	public Fat(int value) 
	{
		this();

		setValue(value);
		setTurningSpeed(Math.toRadians(1));
	}

	public Fat(int value, boolean big) 
	{
		this(value);
		this.big = big;
	}


	public void update() 
	{
		super.update();
	}

	public void render(Renderer renderer)
	{
		Stroke stroke = new BasicStroke(1);
		Stroke stroke1 = new BasicStroke(2);
		if(edible)
			setColor(new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 
					255));

		renderer.setStroke(stroke);

		if(!big)
		{
			renderer.drawCircle(getX(), getY(), INNER_MOST_RADIUS, getColor());

			if(getValue() >= 50)
				renderer.fillCircle(getX(), getY(), INNER_MOST_RADIUS, getColor());
		}
		else
		{
			if(!edible)
				setColor(new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), 
						100));

			renderer.drawCircle(getX(), getY(), INNER_MOST_RADIUS, getColor());

			renderer.drawCircle(getX(), getY(), OUTER_MOST_RADIUS, getColor());

			if(getValue() >= 100)
				renderer.fillCircle(getX(), getY(), INNER_MOST_RADIUS, getColor());

			if(getValue() >= 150)
				renderer.drawCircle(getX(), getY(), MID_RADIUS, getColor());

			if(smallShoulders && !bigShoulders)
			{

				renderer.rotate(
						getX(), 
						getY(), 
						-getAngle()
				);
				
				if(getValue() >= MAX_SMALL_SHOULDER_VALUE)
				{
					renderer.fillCircle(getX()+getRadius(), getY()-getRadius(), SMALL_SHOULDER_RADIUS, getColor());
					renderer.fillCircle(getX()-getRadius(), getY()-getRadius(), SMALL_SHOULDER_RADIUS, getColor());
				}
				else
				{
					renderer.drawCircle(getX()+getRadius(), getY()-getRadius(), SMALL_SHOULDER_RADIUS, getColor());
					renderer.drawCircle(getX()-getRadius(), getY()-getRadius(), SMALL_SHOULDER_RADIUS, getColor());
				}
				
				renderer.rotate(
						getX(), 
						getY(), 
						getAngle()
				);

			}

			if(bigShoulders)
			{

				renderer.rotate(
						getX(), 
						getY(), 
						-getAngle()
				);

				renderer.drawCircle(getX()+getRadius(), getY()-getRadius(), BIG_SHOULDER_RADIUS, getColor());
				renderer.drawCircle(getX()-getRadius(), getY()-getRadius(), BIG_SHOULDER_RADIUS, getColor());
				

				
				anchor = new Point2D.Double(getX()+getRadius()+BIG_SHOULDER_RADIUS, getY()-getRadius());

				transform = AffineTransform.getRotateInstance(Math.toRadians(angle), anchor.getX() , anchor.getY());
				transformedPoint = transform.transform(new Point2D.Double(getX()+getRadius()+BIG_SHOULDER_RADIUS+ARM_LENGTH, getY()-getRadius()), null);

				renderer.drawLine((int)anchor.getX(), (int)anchor.getY(), (int)transformedPoint.getX(), (int)transformedPoint.getY(), getColor());	
				renderer.fillRect((int)transformedPoint.getX(), (int)transformedPoint.getY(), TAIL_RADIUS, TAIL_RADIUS, getColor()); 
				
				transform = AffineTransform.getTranslateInstance(-2*(getRadius()+BIG_SHOULDER_RADIUS), 0);
				anchor = transform.transform(anchor, null);
				
				transform = AffineTransform.getRotateInstance(Math.toRadians(-angle), anchor.getX() , anchor.getY());
				transformedPoint = transform.transform(new Point2D.Double(getX()-getRadius()-BIG_SHOULDER_RADIUS-ARM_LENGTH, getY()-getRadius()), null);

				renderer.drawLine((int)anchor.getX(), (int)anchor.getY(), (int)transformedPoint.getX(), (int)transformedPoint.getY(), getColor());	
				renderer.fillRect((int)transformedPoint.getX(), (int)transformedPoint.getY(), TAIL_RADIUS, TAIL_RADIUS, getColor()); 
				
				angle += angleIncrement;				
				if(getMovingFast())
				{
					armLower = -90;
					armUpper = -75;
				}
				else
				{
					armLower = ARM_ANGLE_LOWER_THRESHOLD;
					armUpper = ARM_ANGLE_UPPER_THRESHOLD;
				}
				if (angle >= armUpper)
					angleIncrement = -1;
				else if (angle <= armLower)
					angleIncrement = 1;

				if(getValue() >= MAX_BIG_SHOULDER_VALUE)
				{
					renderer.fillCircle(getX()+getRadius(), getY()-getRadius(), BIG_SHOULDER_RADIUS-SMALL_SHOULDER_RADIUS/2, getColor());
					renderer.fillCircle(getX()-getRadius(), getY()-getRadius(), BIG_SHOULDER_RADIUS-SMALL_SHOULDER_RADIUS/2, getColor());
				}
				else if(getValue() >= MAX_BIG_SHOULDER_VALUE-50)
				{
					renderer.fillCircle(getX()+getRadius(), getY()-getRadius(), BIG_SHOULDER_RADIUS-SMALL_SHOULDER_RADIUS/2, getColor());	
				}
				
				renderer.rotate(
						getX(), 
						getY(), 
						getAngle()
				);
			}

		}

		renderer.rotate(
				getXFromCenterToRear(OUTER_MOST_RADIUS+TAIL_DISTANCE), 
				getYFromCenterToRear(OUTER_MOST_RADIUS+TAIL_DISTANCE), 
				-getAngle()
		);

		renderer.fillRect(
				getXFromCenterToRear(OUTER_MOST_RADIUS+TAIL_DISTANCE), 
				getYFromCenterToRear(OUTER_MOST_RADIUS+TAIL_DISTANCE), 
				(big ? TAIL_RADIUS*2 : TAIL_RADIUS), 
				(big ? TAIL_RADIUS*2 : TAIL_RADIUS),
				getColor()
		);

		renderer.rotate(
				getXFromCenterToRear(OUTER_MOST_RADIUS+TAIL_DISTANCE), 
				getYFromCenterToRear(OUTER_MOST_RADIUS+TAIL_DISTANCE), 
				getAngle()
		);
		renderer.setStroke(stroke1);
	}

	@Override
	public int getType()
	{
		return FAT;
	}

	@Override
	public void collision(Entity entity)
	{

	}

	@Override
	public int addValue(int value) 
	{
		if(value <= 0)
			return 0;

		if(edible)
		{
			if(big)
			{
				if(bigShoulders && getValue() <= MAX_BIG_SHOULDER_VALUE)
				{
					int valueStillNeeded = MAX_BIG_SHOULDER_VALUE - getValue();
					super.addValue(valueStillNeeded > value ? value : valueStillNeeded);

					return 0 > value - valueStillNeeded ? 0 : value - valueStillNeeded;
				}
				
				else if(smallShoulders && getValue() <= MAX_SMALL_SHOULDER_VALUE)
				{
					int valueStillNeeded = MAX_SMALL_SHOULDER_VALUE - getValue();
					super.addValue(valueStillNeeded > value ? value : valueStillNeeded);

					return 0 > value - valueStillNeeded ? 0 : value - valueStillNeeded;
				}

				else if(getValue() <= MAX_BIG_VALUE)
				{
					int valueStillNeeded = MAX_BIG_VALUE - getValue();
					super.addValue(valueStillNeeded > value ? value : valueStillNeeded);

					return 0 > value - valueStillNeeded ? 0 : value - valueStillNeeded;
				}

				else
				{
					return value;
				}
			}

			else
			{
				if(getValue() <= MAX_SMALL_VALUE)
				{
					int valueStillNeeded = MAX_SMALL_VALUE - getValue();
					super.addValue(valueStillNeeded > value? value : valueStillNeeded);

					return 0 > value - valueStillNeeded ? 0 : value - valueStillNeeded;
				}
				else
				{
					return value;
				}
			}
		}
		else
		{
			edible = true;
			value -= 50;
			if(value > 0)
				addValue(value);
		}
		return 0;
	}

	@Override
	public int removeValue() 
	{
		if(big && edible)
		{
			edible = false;
			return getValue();
		}
		else
			return 0;
	}

	public void setBig(boolean big)
	{
		this.big = big;
	}

	public boolean getBig()
	{
		return big;
	}

	public void setEdible(boolean edible)
	{
		this.edible = edible;
	}

	public boolean getEdible()
	{
		return edible;
	}
	
	public void setSmallShoulders(boolean smallShoulders)
	{
		this.smallShoulders = smallShoulders;
	}

	public boolean getSmallShoulders()
	{
		return smallShoulders;
	}

	public void setBigShoulders(boolean bigShoulders)
	{
		this.bigShoulders = bigShoulders;
	}

	public boolean getBigShoulders()
	{
		return bigShoulders;
	}
	
	public boolean evolve()
	{
		System.out.println("Fat Evolving");
		if(!big && getValue() > 0)
		{
			big = true;
		}
		else if(!smallShoulders && !bigShoulders)
		{
			smallShoulders = true;
		}
		else if(!bigShoulders)
		{
			bigShoulders = true;
		}
		else
		{
			return false;	
		}
		return true;
	}
}
