package render;

import java.awt.Color;
import java.awt.Stroke;

import world.Camera;

public interface Renderer
{
	public static final int JAVA2D = 0;
	
	public void setCamera(Camera camera);
	
	public float getScaleWidth();
	
	public float getScaleHeight();
	
	public void setStroke(Stroke stroke);
	
	public void setAlphaComposite(float alpha);
	
	public void renderSprite(Sprite sprite, int x, int y);
	
	public void renderSprite(Sprite sprite, int x, int y, float alpha);
	
	public void fillBackground(Color color);
	
	public void drawString(String string, int x, int y, Color color);
	
	public void drawLine(int startX, int startY, int endX, int endY, Color color);
	
	public void drawRect(int x, int y, int width, int height, Color color);
	
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle, Color color);
	
	public void drawOval(int x, int y, int width, int height, Color color);
	
	public void drawCircle(int x, int y, int radius, Color color);
	
	public void drawPentagon(int x, int y, int radius, Color color);
	
	public void drawTriangle(int x, int y, int radiusx, int radiusy, int angle1, int angle2, Color color);
	
	public void drawCross(int x, int y, int radius, Color color);
	
	public void fillRect(int x, int y, int width, int height, Color color);
	
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle, Color color);
	
	public void fillOval(int x, int y, int width, int height, Color color);
	
	public void fillCircle(int x, int y, int radius, Color color);
	
	public void rotate(int x, int y, double angle);
}
