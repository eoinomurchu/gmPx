package entity;


public class Food extends Entity {
	
	public Food() {
		setAcceleration(0.0);
		setDeceleration(0.0);
		setTurningSpeed(Math.toRadians(0.5));
		setValue(0);
	}
	
	public Food(int x, int y) {
		this();
		
		setX(x);
		setY(y);
	}
	
	public void update() {
		super.update();
	}
	
    public int getType()
    {
        return FOOD;
    }
}
