package entity;

import java.awt.Color;
import java.util.Vector;

import render.Renderer;
import world.World;

public class Entity {


	private Entity parent;
	
    /** Constant for an unknown entity type */
    public static final int UNKNOWN = 0;
   
    /** Constant for a mouth entity type */
    public static final int MOUTH = 1;
   
    /** Constant for a fat entity type */
    public static final int FAT = 2;
    
    /** Contant for a food entity type*/
    public static final int FOOD = 4;
    
    /** Contant for a food entity type*/
    public static final int EVOLVER = 5;
    
    /** Contant for a food entity type*/
    public static final int MOUTH_EVOLVER = 6;
    
    /** Constant for a level changing entity type */
    public static final int LEVEL_CHANGER = 3;
    
    /** 90 degrees in Radians */
    protected final double NINETY_DEGREES = Math.PI/2;

    /** 180 degrees in Radians */
    protected final double HUNDRED_EIGHTY_DEGREES = Math.PI;

    /** 180 degrees in Radians */
    protected final double THREE_SIXTY_DEGREES = Math.PI*2;

	protected final int CONVERSION_FACTOR = 5;

	protected final int PIXEL_BUFFER = 1 << CONVERSION_FACTOR;

	private Color color;
	
    /** The World's Entity */
    private World world;
    
    /** A list of body parts */
    private Vector<Entity> bodyParts;
    
    /** The current maximum velocity of an Entity */
    private double maxVelocity;

    /** Entity's x coordinate at the entity's center in game units */
    private int x;

    /** Entity's y coordinate at the entity's center in game units */
    private int y;

    /** Entity's radius in game units */
    private int radius;

    /** Entity's angle of facing in degrees */
    private double angle;
	
    /** Entity's target angle of facing in degrees */
    private double targetAngle;

    /** Entity's rate of turn in degrees per tick */
    private double turningSpeed;

    /** Positive if should rotate clockwise */
    private double clock;

    /** Entity's velocity in game units per tick */
    private double velocity;

    /** Entity's acceleration in game units per tick squared */
    private double acceleration;

    /** Entity's deceleration in game units per tick squared */
    private double deceleration;

    /** The Controller for the entity. Controls behaviour */
    private Controller controller;
    
    /** The energy the entity contains */
    private int value;
    
    private int sizevalue;
    
    private int previousSizeValue;
    
    private int previousValue;
    
    private boolean dead;
 
    private boolean justDied;

	private boolean movingFast;
	
	private int energyLevel;
	
	private int previousEnergyLevel;
    /**
     * Constructor
     * 
     * 
     */
    public Entity() {
        bodyParts = new Vector<Entity>();
    	radius = 15 << 5;
    	angle = 0;
    	targetAngle = 0;
    	turningSpeed = Math.toRadians(2);
    	clock = 0;
    	velocity = 0;
    	acceleration = 1;
    	deceleration = 0.5;
    	sizevalue = 0;
    	value = 0;
    	dead = false;
    	justDied = false;
    	energyLevel = 0;
    }

    public Entity(Entity papa) {
    	parent = papa;
        bodyParts = new Vector<Entity>();
    	radius = 15 << 5;
    	angle = 0;
    	targetAngle = 0;
    	turningSpeed = Math.toRadians(2);
    	clock = 0;
    	velocity = 0;
    	acceleration = 1;
    	deceleration = 0.5;
    	sizevalue = 0;
    	value = 0;
    	dead = false;
    	justDied = false;
    	energyLevel = 0;
    }


    /**
     * Constructor
     * 
     * 
     */   
    public Entity(int aX, int aY) {
        bodyParts = new Vector<Entity>();

    	radius = 20 << 5;
    	angle = 0;
    	targetAngle = 0;
    	turningSpeed = Math.toRadians(2);
    	clock = 0;
    	velocity = 0;
    	acceleration = 1;
    	deceleration = 0.5;
    
    	x = aX;
    	y = aY;
    	dead = false;
    	justDied = false;
    }
    
    public Entity(int x, int y, Controller controller)
    {
    	this(x, y);
    	this.controller = controller;
    }

    /**
     * Sets the entity's energy value
     * 
     * @param value The new value
     */
    public void setValue(int value) {
    	this.value = value;
    }
    
    public void setSizeValue(int sizevalue)
    {
    	previousSizeValue = this.sizevalue;
    	this.sizevalue = sizevalue;
    }
    
    public int getSizeValue()
    {
    	return sizevalue;
    }
    
    public void setEnergyLevel(int energyAdded){
    	previousEnergyLevel = this.energyLevel;
    	this.energyLevel += energyAdded;
    }
    
    public int getEnergyLevel(){
    	return this.energyLevel;
    }
    
    /**
     * Gets the entity's energy value
     * 
     * @return The entitys energy value
     */
    public int getValue() {
    	return value;
    }
    
    public int getpreviousValue(){
    	return previousValue;
    }
    
    public int getpreviousSizeValue()
    {
    	return previousSizeValue;
    }
    /**
     * Gets the entity's x coordinate in game units
     * 
     * @return Entity's x coordinate.
     */
    public int getX() {
	return x;
    }
	

    /**
     * Sets the entity's x coordinate to the specified value
     * in game units.
     * 
     * @param x The x coordinate to set.
     */
    public void setX(int x) {
	this.x = x;
    }


    /**
     * Gets the entity's y coordinate in game units
     * 
     * @return Entity's y coordinate.
     */
    public int getY() {
	return y;
    }


    /**
     * Sets the entity's y coordinate to the specified value
     * in game units.
     * 
     * @param y The y coordinate to set.
     */
    public void setY(int y) {
	this.y = y;
    }

    /**
     * Sets the entity's front x coordinate to the specified value
     * in game units.
     * 
     * @param frontX The front x coordinate to set.
     */
    public void setFrontX(int frontX) {
	    x = (int)(frontX - Math.sin(angle)*radius);
    }
    
    /**
     * Sets the entity's front y coordinate to the specified value
     * in game units.
     * 
     * @param frontX The front y coordinate to set.
     */
    public void setFrontY(int frontY) {
	    y = (int)(frontY - Math.cos(angle)*radius);
    }
    
    /**
     * Gets the entity's Front x coordinate in game units
     * 
     * @return Entity's Front x coordinate.
     */
    public int getFrontX() {
	    return (int)(x + Math.sin(angle)*radius);
    }

    /**
     * Gets a x coordinate in game units a certain distance away
     * from the entity's center at the correct angle in the forwards direction
     * 
     * @return The x coord  
     */
    public int getXFromCenterToFront(int distance) {
	    return (int)(x + Math.sin(angle)*distance);
    }
    
    /**
     * Gets a x coordinate in game units a certain distance away
     * from the entity's center at the correct angle in the forwards direction
     * 
     * @return The x coord  
     */
    public int getXAwayFromFront(int distance) {
	    return (int)(x + Math.sin(angle)*(radius+distance));
    }
    
    /**
     * Gets the entity's Front y coordinate in game units
     * 
     * @return Entity's Front y coordinate.
     */
    public int getFrontY() {
	    return (int)(y + Math.cos(angle)*radius);
    }

    /**
     * Gets a y coordinate in game units a certain distance away
     * from the entity's center at the correct angle in the forwards direction
     * 
     * @return The y coord  
     */
    public int getYFromCenterToFront(int distance) {
	    return (int)(y + Math.cos(angle)*distance);
    }
    
    /**
     * Gets a y coordinate in game units a certain distance away
     * from the entity's circumference at the correct angle in the forwards direction
     * 
     * @return The y coord  
     */
    public int getYAwayFromFront(int distance) {
	    return (int)(y + Math.cos(angle)*(radius+distance));
    }
    
    /**
     * Gets the entity's rear x coordinate in game units
     * 
     * @return Entity's rear x coordinate.
     */
    public int getRearX() {
	    return (int)(x - Math.sin(angle)*radius);
    }

    /**
     * Gets a x coordinate in game units a certain distance away
     * from the entity's circumference at the correct angle in the backwards direction
     * 
     * @return The x coord  
     */
    public int getXAwayFromRear(int distance) {
	    return (int)(x - Math.sin(angle)*(radius+distance));
    }

    /**
     * Gets a x coordinate in game units a certain distance away
     * from the entity's center at the correct angle in the forwards direction
     * 
     * @return The x coord  
     */
    public int getXFromCenterToRear(int distance) {
	    return (int)(x - Math.sin(angle)*distance);
    }
    
    /**
     * Gets the entity's rear y coordinate in game units
     * 
     * @return Entity's rear y coordinate.
     */
    public int getRearY() {
	    return (int)(y - Math.cos(angle)*radius);
    }
    
    /**
     * Gets a y coordinate in game units a certain distance away
     * from the entity's center at the correct angle in the forwards direction
     * 
     * @return The y coord  
     */
    public int getYFromCenterToRear(int distance) {
	    return (int)(y - Math.cos(angle)*distance);
    }
    /**
     * Gets a y coordinate in game units a certain distance away
     * from the entity's center at the correct angle in the backwards direction
     * 
     * @return The y coord  
     */
    public int getYAwayFromRear(int distance) {
	    return (int)(y - Math.cos(angle)*(radius+distance));
    }


    /**
     * Gets the entity's radius in game units.
     * 
     * @return Entity's radius.
     */
    public int getRadius() {
	    return radius;
    }


    /**
     * Sets the entity's radius to the specified value
     * in game units.
     * 
     * @param radius The radius coordinate to set.
     */
    public void setRadius(int radius) {
	this.radius = radius;
    }


    /**
     * Gets the entity's angle of orientation
     * 
     * @return Entity's angle of orientation
     */
    public double getAngle() {
	    return angle;
    }
    
    /**
     * Sets the entity's angle of orientation
     * 
     * @param angle Entity's angle of orientation
     */
    public void setAngle(double angle) {
	    this.angle = angle;
    }
    
    /**
     * Gets the entity's target angle of orientation
     * 
     * @return Entity's target angle of orientation
     */
    public double getTargetAngle() {
	    return targetAngle;
    }
    
    /**
     * Gets the entity's turning speed
     * 
     * @return Entity's turning speed
     */
    public double getTurningSpeed() {
	    return turningSpeed;
    }

    /**
     * Sets the entity's turning speed
     * 
     * @param turningSpeed The Entity's new turning speed
     */
    public void setTurningSpeed(double turningSpeed) {
	    this.turningSpeed = turningSpeed;
    }
    
    /**
     * Gets the clockwise idicator
     * 
     * @return the clockwise idicator
     */
    public double getClock() {
	    return clock;
    }


    /**
     * Gets the entity's velocity
     * 
     * @return Entity's velocity
     */
    public double getVelocity() {
	return velocity;
    }


    /**
     * Gets the entity's velocity
     * 
     * @return Entity's velocity
     */
    public void setVelocity(double velocity) {
	this.velocity = velocity;
    }


    /**
     * Gets the entity's velocity
     * 
     * @return Entity's velocity
     */
    public double getMaxVelocity() {
	return maxVelocity;
    }


    /**
     * Gets the entity's velocity
     * 
     * @return Entity's velocity
     */
    public void setMaxVelocity(double maxVelocity) {
	this.maxVelocity = maxVelocity;
    }


    /**
     * Gets the entity's acceleration
     * 
     * @return Entity's acceleration
     */
    public double getAcceleration() {
	return acceleration;
    }


    /**
     * Sets the entity's acceleration
     * 
     * @param acceleration The Entity's new acceleration
     */
    public void setAcceleration(double acceleration) {
    	this.acceleration = acceleration;
    }
    
    /**
     * Gets the entity's deceleration
     * 
     * @return Entity's deceleration
     */
    public double getDeceleration() {
	return acceleration;
    }


    /**
     * Sets the entity's deceleration
     * 
     * @param deceleration The Entity's new deceleration
     */
    public void setDeceleration(double deceleration) {
    	this.deceleration = deceleration;
    }
    
    /**
     * Gets the distance between this entity and another
     * 
     * @param entity The other entity
     * @return the distance between this entity and <var>entity</var>
     */
    public int getDistance(Entity entity) {
        int xDiff = Math.abs(getX() - entity.getX());
        int yDiff = Math.abs(getY() - entity.getY());
        
        return (int)Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
    }

    /**
     * Gets the distance between the rear of this entity and the front of another
     * 
     * @param entity The other entity
     * @return the distance between this entity and <var>entity</var>
     */
    public int getDistanceFromRear(Entity entity) {
        int xDiff = Math.abs(getRearX() - entity.getFrontX());
        int yDiff = Math.abs(getRearY() - entity.getFrontY());
        
        return (int)Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
    }

    

    /**
     * Figures out the angle between this entity and another set of coords
     *
     * @param x0 The target x cooridinate
     * @param y0 The target y cooridinate
     */
    public void setTarget(int aX, int aY) {
    	double targetX = aX - getX();
    	double targetY = aY - getY();
    	
    	if (targetY == 0) {
	    if (targetX < 0)
		targetAngle = -NINETY_DEGREES;
	    else
		targetAngle = NINETY_DEGREES;
	}
    	else {
	    targetAngle = Math.atan(targetX / targetY);
			
	    if (targetY < 0)
		targetAngle += HUNDRED_EIGHTY_DEGREES;
	}

	if (targetAngle < 0)
	    targetAngle += THREE_SIXTY_DEGREES;
	
        clock = (targetX * Math.cos(angle)) - (targetY * Math.sin(angle));
    }

    public void update() {
    	if(checkDead())
    		return;
    	
    	if(controller != null)
    		controller.update();
    	
    	double leftThreshold = targetAngle - turningSpeed;
//    	if(leftThreshold < 0)
//    	    leftThreshold += THREE_SIXTY_DEGREES;
    	
    	double rightThreshold = targetAngle + turningSpeed;
//    	if(rightThreshold > THREE_SIXTY_DEGREES)
//    	    rightThreshold -= THREE_SIXTY_DEGREES;
    	
    	//if we're close enough to the target so don't move to stop jerking
       	if (!(angle > leftThreshold && angle < rightThreshold)) {
	
    	    if (clock > 0) {
        		angle += turningSpeed;
        		angle %= THREE_SIXTY_DEGREES;
    	    } 
    	    else {
        		angle -= turningSpeed;
    
        		if (angle < 0)
        		    angle += THREE_SIXTY_DEGREES;
    	    }
    	}

    	velocity -= deceleration;

    	if(velocity < maxVelocity) {
    	    velocity += acceleration;

    	    if(velocity > maxVelocity)
        		velocity = maxVelocity;
    	}
	
    	if(velocity < 0)
    	    velocity = 0;

    	x += velocity*Math.sin(angle);
    	y += velocity*Math.cos(angle);
    }

    /**
     * Renders that entity to the screen
     *
     * @param theRenderer The renderer
     */
    public void render(Renderer theRenderer) {
 
    }

    public String toString() {
	return x+", "+x;
    }
    
    public void setWorld(World world)
    {
        this.world = world;
    }
   
    public World getWorld()
    {
        return world;
    }
    
    /**
     * Sets the controller to control the entity.
     * 
     * @param c The controller to set.
     */
    public void setController(Controller c)
    {
    	controller = c;
    }
    
    /**
     * Gets the controller that is currently controlling
     * the entity.
     * 
     * @return Entity's controller.
     */
    public Controller getController()
    {
    	return controller;
    }
   
    /**
     * Gets a constant value representing the type of entity this is.
     * This method should be overridden by subclasses if they are a
     * known entity type.
     *
     * @return Entity's type.
     */
    public int getType()
    {
        return UNKNOWN;
    }
   
    /**
     * Logic to handle collisions with other entities.
     * This method can be overridden by subclasses should
     * they require collision response.
     *
     * @param entity The colliding entity.
     */
    public void collision(Entity entity)
    {
       
    }
   
    /**
     * Checks and entity intersects with this entity.
     *
     * @param entity True if entities intersect.
     */
    public boolean checkCollision(Entity entity)
    {
        long vx = x - entity.getX();
        long vy = y - entity.getY();

        return (vx*vx + vy*vy < (radius * radius) + (entity.getRadius () * entity.getRadius()));
    }
    
    public Vector<Entity> getBodyParts() {
        return bodyParts;
    }

    public void addBodyPart(Entity entity) {
        bodyParts.add(entity);
    }
    
    public Fat getCollidingFat(Entity entity)
    {
        for(int x = 0, n = bodyParts.size(); x < n; x++)
            if(bodyParts.get(x).getType() == Entity.FAT 
            		&& entity.checkCollision(bodyParts.get(x)))
                return (Fat) bodyParts.get(x);
        return null;
    }
    
    public int removeValue() {
    	int temp = value;
    	value = 0;
    	return temp;
    }
    
    public int addValue(int value) {
    	this.value += value;
    	sizevalue += value;
    	return 0;
    }
    
    public void setColor(Color color) {
    	this.color = color;
    }
    
    public Color getColor() {
    	return color;
    }
    
    public boolean getDead() {
    	return dead;
    }
    
    public void setDead(boolean dead)
    {
    	this.dead = dead;
    }
    
    public Vector<Entity> getSpawnedEntities() 
    {
    	Vector<Entity> entities = new Vector<Entity>();
		Vector<Entity> fats = getBodyParts();
		Entity entity;
		
		int value;
		for(int i = 0; i < fats.size(); ++i)
		{
			entity = fats.get(i);
			value = entity.getValue();
			if(value <= 0)
				;
			else if(value == 50)
			{
				entity = new FoodEntityLevel1(entity.getX(), entity.getY());
				entity.setController(new NonHostileAIController(entity));
				entities.add(entity);
			}
			else if(value == 100)
			{
				entity = new FoodEntityLevel2(entity.getX(), entity.getY());
				entity.setController(new NonHostileAIController(entity));
				entities.add(entity);
			}
			else if(value == 150)
			{
				entity = new FoodEntityLevel3(entity.getX(), entity.getY());
				entity.setController(new NonHostileAIController(entity));
				entities.add(entity);
			}
			else if(value == 200)
			{
				entity = new FoodEntityLevel4(entity.getX(), entity.getY());
				entity.setController(new NonHostileAIController(entity));
				entities.add(entity);
			}
			else if(value == 250)
			{
				entity = new FoodEntityLevel5(entity.getX(), entity.getY());
				entity.setController(new NonHostileAIController(entity));
				entities.add(entity);
			}
		}
		return entities;
    }
    
    public boolean checkDead()
    {
    	if(getValue() <= 0 && getType() != FAT)
    	{
    		dead = true;
    		return true;
    	}
    	return false;
    }
    
    public void setJustDied(boolean justDied)
    {
    	this.justDied = justDied;
    }
    
    public boolean getJustDied()
    {
    	return justDied;
    }
    
	public void setMovingFast(boolean movingFast)
	{
		this.movingFast = movingFast;
	}
	
	public boolean getMovingFast()
	{
		return movingFast;
	}

	public boolean evolve()
	{
		return false;
	}
}
