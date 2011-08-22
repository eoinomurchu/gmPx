package render;

public interface Sprite
{
	public static final int JAVA_SPRITE = 0;
	
	public static final int GL_SPRITE = 1;
	
	int getWidth();
	
	int getHeight();
	
	int getType();
}
