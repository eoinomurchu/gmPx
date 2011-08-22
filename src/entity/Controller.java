package entity;

public interface Controller
{
	public static final int PLAYER = 0;
	
	public static final int HOSTILE = 1;
	
	public static final int NON_HOSTILE = 2;
	
	void update();
	
	int getType();
}

