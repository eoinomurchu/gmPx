package render;

import input.Input;
import input.JavaInput;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.Vector;

import javax.swing.JFrame;

import world.Camera;


public class JavaWindow implements Window, Renderer, ComponentListener, MouseWheelListener
{
	/** The maximum zoom value */
	private final int MAX_ZOOM = 3;

	/** The minimum zoom value */
	private final int MIN_ZOOM = 1;

	private final int DEFAULT_WIDTH = 800;

	private final int DEFAULT_HEIGHT = 600;

	private final int DEFAULT_WIDE_WIDTH = 960;

	private boolean widescreen;

	private int currWorkingWidth;

	private int currWorkingHeight;

	/** The window for the game */
	private JFrame frame;

	/** The strategy that allows us to use Java's inbuild buffer flipping */
	private BufferStrategy strategy;

	/** Graphics component for the java window */
	private Graphics2D graphics;

	/** The default display mode before changing anything */
	private DisplayMode defaultMode;

	private float currZoom;

	private float scaleW;

	private float scaleH;

	private int conversionFactor;

	private boolean resizeOnRender;

	/** The input class for this window */
	private JavaInput input;

	/** The Camera for this window */
	private Camera camera;

	private boolean blurred = false;

	/**
	 * Creates a new Java window of specified width and height.
	 * If fullscreen is set to true, it will try to enter
	 * fullscreen mode.
	 * 
	 * @param width The desired width of the window.
	 * @param height The desired height of the window.
	 * @param fullscreen True to set fullscreen mode if possible.
	 */
	//@ requires width > 0;
	//@ requires height > 0;
	//@ ensures false;
	public JavaWindow(int width, int height, int conversionFactor)
	{
		this.conversionFactor = conversionFactor;
		frame = new JFrame();
		
		DisplayMode curr = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		
		currWorkingWidth = ((double)curr.getWidth() / (double)curr.getHeight() != 4.0 / 3.0) ? DEFAULT_WIDE_WIDTH : DEFAULT_WIDTH;
		currWorkingHeight = DEFAULT_HEIGHT;
		init(currWorkingWidth, currWorkingHeight);

		input = new JavaInput();
		//FIXME don't bind here
		input.bindKey(Input.KEYBOARD, KeyEvent.VK_UP, Input.UP);
		input.bindKey(Input.KEYBOARD, KeyEvent.VK_DOWN, Input.DOWN);
		input.bindKey(Input.KEYBOARD, KeyEvent.VK_LEFT, Input.LEFT);
		input.bindKey(Input.KEYBOARD, KeyEvent.VK_RIGHT, Input.RIGHT);
		input.bindKey(Input.KEYBOARD, KeyEvent.VK_ENTER, Input.CONFIRM);
		input.bindKey(Input.KEYBOARD, KeyEvent.VK_ESCAPE, Input.CANCEL);
		input.bindKey(Input.MOUSE, MouseEvent.BUTTON1, Input.MOUSE_ONE);

		frame.addKeyListener(input);
		frame.addMouseListener(input);
		frame.addMouseMotionListener(input);

		frame.addComponentListener(this);
		frame.addMouseWheelListener(this);

		currZoom = MIN_ZOOM;
	}

	/**
	 * Initializes the window.
	 * 
	 * @param width Window's width.
	 * @param height Window's height.
	 */
	private void init(int width, int height)
	{
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("gmPx");
		frame.setVisible(true);
		frame.setIgnoreRepaint(true);

		/*
		 * check if fullscreen mode is available
		 * note: this must be done before the strategy is created
		 * as if the display mode is change to fullscreen first the
		 * strategy can use page flipping which is vsynced so as to
		 * avoid screen tearing.
		 */
		GraphicsDevice graphicsDevice = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice();

		defaultMode = graphicsDevice.getDisplayMode();

		//create the buffer strategy 
		frame.createBufferStrategy(2);
		strategy = frame.getBufferStrategy();

		//don't forget to initialize g after we've made the strategy
		graphics = (Graphics2D) strategy.getDrawGraphics();
	}

	/**
	 * This sets the Camera for the renderer, 
	 * So that when we paint to the screen we can translate the coordinates
	 * from world coordinates to frame coordinates.
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void setStroke(Stroke stroke)
	{
		graphics.setStroke(stroke);
	}

	public void setFullscreen(boolean fullscreen)
	{        
		GraphicsDevice graphicsDevice = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice();

		//already in desired mode!
		if(fullscreen && isFullscreen())
			return;
		else if(!fullscreen && !isFullscreen())
			return;


		if(fullscreen && graphicsDevice.isFullScreenSupported())
			setFullscreen(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());
		else if(!fullscreen && isFullscreen())
			setWindowed();
		else
			return;//nothing works

	
		//create the buffer strategy 
		frame.createBufferStrategy(2);
		strategy = frame.getBufferStrategy();


		//don't forget to initialize graphics after we've made the strategy
		graphics = (Graphics2D) strategy.getDrawGraphics();
	}

	private void setFullscreen(int width, int height)
	{
		GraphicsDevice graphicsDevice = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		DisplayMode[] modes = graphicsDevice.getDisplayModes();
		Vector<DisplayMode> suitable = new Vector<DisplayMode>();

		//get a list of suitable modes
		for (int x = 0; x < modes.length; x++)
		{
			if (modes[x].getWidth() == width
					&& modes[x].getHeight() == height)
				suitable.add(modes[x]);
		}

		if(suitable.size() == 0) //no suitable modes
			return;

		graphicsDevice.setFullScreenWindow(frame);

		boolean success = false;
		while(!success && suitable.size() > 0)
		{
			//get the next suitable mode from the end of the list(probably the one with highest specs)
			DisplayMode m = suitable.remove(suitable.size() - 1);

			try
			{
				graphicsDevice.setDisplayMode(m);
				currWorkingWidth = frame.getWidth();
				currWorkingHeight = frame.getHeight();
				success = true;
			}
			catch (Exception e)
			{
				System.out.println("Couldn't change into display mode. " + e.getMessage());
			}
		}
		
		setScale(widescreen);
	}

	private void setWindowed()
	{		
		GraphicsDevice graphicsDevice = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		graphicsDevice.setFullScreenWindow(null);
		currWorkingWidth = DEFAULT_WIDTH;
		currWorkingHeight = DEFAULT_HEIGHT;
		init(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		currZoom = MIN_ZOOM;
		
		setScale(false);
		
//		graphicsDevice.setDisplayMode(defaultMode);
	}

	public boolean isFullscreen()
	{
		GraphicsDevice graphicsDevice = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		return graphicsDevice.getFullScreenWindow() != null;
	}

	public void setWidescreen(boolean widescreen)
	{
		this.widescreen = widescreen;
		setScale(widescreen);
	}

	private void setScale(boolean widescreen)
	{
		if(widescreen)
			scaleW = currZoom * (frame.getWidth() / (float)DEFAULT_WIDE_WIDTH);
		else
			scaleW = currZoom * (frame.getWidth() / (float)currWorkingWidth);

		scaleH = currZoom * (frame.getHeight() / (float)currWorkingHeight);

		//create the buffer strategy 
		frame.createBufferStrategy(2);
		strategy = frame.getBufferStrategy();

		graphics = (Graphics2D) strategy.getDrawGraphics();

		//set pointer scaling
		input.setScale(scaleW, scaleH);

		if(camera != null)
		{
			camera.setWidth(frame.getWidth() << conversionFactor);
			camera.setHeight(frame.getHeight() << conversionFactor);
			camera.setScale(scaleW, scaleH);
		}
	}

	public float getScaleWidth()
	{
		return scaleW;
	}

	public float getScaleHeight()
	{
		return scaleH;
	}

	public void setResizable(boolean resizable)
	{
		frame.setResizable(resizable);
	}

	public void setDecorated(boolean value)
	{
		int w = frame.getWidth();
		int h = frame.getHeight();
		frame.setVisible(false);
		frame.dispose();
		frame = new JFrame();

		frame.addKeyListener(input);
		frame.addMouseListener(input);
		frame.addMouseMotionListener(input);

		frame.addComponentListener(this);
		frame.addMouseWheelListener(this);

		if(value = false)
			frame.setUndecorated(true);

		init(w, h);
	}

	public void displayRenderer()
	{
		graphics.dispose();
		graphics = (Graphics2D) strategy.getDrawGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setStroke(new BasicStroke(2));

		if(resizeOnRender)
		{
			setScale(widescreen);
			resizeOnRender = false;
		}
		graphics.scale(scaleW, scaleH);

		strategy.show();
	}

	public int getRenderingType()
	{
		return Renderer.JAVA2D;
	}

	public Graphics2D getGraphics()
	{
		return graphics;
	}

	public Input getInput()
	{
		return input;
	}

	public int getType()
	{
		return JAVA_WINDOW;
	}

	public int getWidth()
	{
//		return frame.getWidth();
		return currWorkingWidth;
	}

	public int getHeight()
	{
//		return frame.getHeight();
		return currWorkingHeight;
	}

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		currZoom += (float)e.getWheelRotation() / 10;

		if(currZoom > MAX_ZOOM)
			currZoom = MAX_ZOOM;
		else if(currZoom < MIN_ZOOM)
			currZoom = MIN_ZOOM;
		else
			resizeOnRender = true;
	}

	public void componentHidden(ComponentEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void componentMoved(ComponentEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void componentResized(ComponentEvent e)
	{
		resizeOnRender = true;
	}

	public void componentShown(ComponentEvent e)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * 
	 */
	public void renderSprite(Sprite sprite, int x, int y)
	{
		if (sprite.getType() != Sprite.JAVA_SPRITE)
			return;
		graphics.drawImage(((JavaSprite) sprite).getImage(),
				(x-camera.getX()) >> conversionFactor, (y-camera.getY()) >> conversionFactor, null);
	}

	/**
	 * 
	 * 
	 */
	public void renderSprite(Sprite sprite, int x, int y, float alpha)
	{
		if (sprite.getType() != Sprite.JAVA_SPRITE)
			return;


		Composite c = graphics.getComposite();
		graphics.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, alpha));
		graphics.drawImage(((JavaSprite) sprite).getImage(),
				(x-camera.getX()) >> conversionFactor, (y-camera.getY()) >> conversionFactor, null);
		graphics.setComposite(c);
	}

	/**
	 * 
	 * 
	 */
	public void fillBackground(Color color) {
		graphics.setColor(color);
		graphics.fillRect(0, 0, currWorkingWidth, currWorkingHeight);
	}

	/**
	 * 
	 * 
	 */
	public void drawString(String string, int x, int y, Color color)
	{  
		graphics.setColor(color);

		graphics.setFont(new Font("Century Schoolbook", Font.PLAIN, 12));

		graphics.drawString(
				string, 
				(x-camera.getX()) >> conversionFactor,
				(y-camera.getY()) >> conversionFactor);
	}

	/**
	 * 
	 * 
	 */
	public void drawLine(int startX, int startY, int endX, int endY, Color color) 
	{
		graphics.setColor(color);

		graphics.drawLine(
				startX-camera.getX() >> conversionFactor, 
				startY-camera.getY() >> conversionFactor, 
				endX-camera.getX() >> conversionFactor, 
				endY-camera.getY() >> conversionFactor);
	}

	/**
	 * 
	 * 
	 */
	public void drawRect(int x, int y, int width, int height, Color color)
	{
		graphics.setColor(color);

		graphics.drawRect(
				(x-(width/2)-camera.getX()) >> conversionFactor, 
				(y-(height/2)-camera.getY()) >> conversionFactor, 
				width >> conversionFactor, 
				height >> conversionFactor);
	}

	/**
	 * 
	 * 
	 */
	public void drawArc(int x, int y, int width, int height,
			int startAngle, int arcAngle, Color color)
	{
		graphics.setColor(color);

		graphics.drawArc(
				(x-(width/2)-camera.getX()) >> conversionFactor, 
				(y-(height/2)-camera.getY()) >> conversionFactor, 
				width >> conversionFactor, 
				height >> conversionFactor,
				startAngle,
				arcAngle);
	}

	/**
	 * 
	 * 
	 */
	public void drawOval(int x, int y, int width, int height, Color color)
	{
		graphics.setColor(color);

		graphics.drawOval(
				(x-(width)/2-camera.getX()) >> conversionFactor, 
				(y-(height)/2-camera.getY()) >> conversionFactor, 
				width >> conversionFactor, 
				height >> conversionFactor);

		if(blurred) {
			Composite composite = graphics.getComposite();

			for(int i = -160; i <= 160; i+=16) {

				AlphaComposite fadeComposite =
					AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .05f);

				graphics.setComposite(fadeComposite);

				graphics.drawOval(
						(x-i-width/2-camera.getX()) >> conversionFactor, 
						(y-i-height/2-camera.getY()) >> conversionFactor, 
						width+i >> conversionFactor, 
						height+i >> conversionFactor);

			}
			graphics.setComposite(composite);
		}
	}

	/**
	 * 
	 * 
	 */
	public void drawCircle(int x, int y, int radius, Color color)
	{
		graphics.setColor(color);

		graphics.drawOval(
				(x-radius-camera.getX()) >> conversionFactor, 
				(y-radius-camera.getY()) >> conversionFactor, 
				radius*2 >> conversionFactor, 
				radius*2 >> conversionFactor);

		if(blurred) {
			Composite composite = graphics.getComposite();

			for(int i = -160; i <= 160; i+=16) {

				AlphaComposite fadeComposite =
					AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .05f);

				graphics.setComposite(fadeComposite);

				graphics.drawOval(
						(x-radius-i-camera.getX()) >> conversionFactor, 
						(y-radius-i-camera.getY()) >> conversionFactor, 
						(radius+i)*2 >> conversionFactor, 
						(radius+i)*2 >> conversionFactor);

			}
			graphics.setComposite(composite);
		}
	}

	public void drawPentagon(int x, int y, int radius, Color color)
	{
		AffineTransform at;
		Point2D rotated; 

		graphics.setColor(color);
		Polygon polygon = new Polygon();
		Point2D original = new Point2D.Double((x - camera.getX() + radius)>>conversionFactor, (y - camera.getY()) >> conversionFactor);

		polygon.addPoint((int)original.getX() ,(int)original.getY());
		for(int i = 1; i < 5; ++i)
		{
			at = AffineTransform.getRotateInstance(Math.toRadians(72*i), (x - camera.getX()) >> conversionFactor, (y- camera.getY()) >> conversionFactor);
			rotated = at.transform(original, null);
			polygon.addPoint((int)rotated.getX() , (int)rotated.getY());
		}
		graphics.drawPolygon(polygon);
	}

	public void drawTriangle(int x, int y, int radiusx, int radiusy, int angle1, int angle2, Color color)
	{
		AffineTransform at1 , at2;
		Point2D rotated1 , rotated2; 

		graphics.setColor(color);
		Polygon polygon = new Polygon();
		Point2D original = new Point2D.Double((x - camera.getX() + radiusx)>>conversionFactor, (y - camera.getY() + radiusy) >> conversionFactor);
		at1 = AffineTransform.getRotateInstance(Math.toRadians(angle1), (x - camera.getX()) >> conversionFactor, (y- camera.getY()) >> conversionFactor);
		at2 = AffineTransform.getRotateInstance(Math.toRadians(angle2), (x - camera.getX()) >> conversionFactor, (y- camera.getY()) >> conversionFactor);
		rotated1 = at1.transform(original, null);
		rotated2 = at2.transform(original, null);
		polygon.addPoint( (x - camera.getX()) >> conversionFactor, (y- camera.getY()) >> conversionFactor);
		polygon.addPoint((int)rotated1.getX() , (int)rotated1.getY());
		polygon.addPoint((int)rotated2.getX() , (int)rotated2.getY());
		graphics.drawPolygon(polygon);
	}


	public void drawCross(int x, int y, int radius, Color color)
	{
		graphics.setColor(color);
		graphics.drawLine((x - camera.getX() + radius)>>conversionFactor,(y - camera.getY())>>conversionFactor,
				(x - camera.getX() - radius)>>conversionFactor,(y - camera.getY())>>conversionFactor);
		graphics.drawLine((x - camera.getX())>>conversionFactor,(y - camera.getY() + radius)>>conversionFactor,
				(x - camera.getX())>>conversionFactor,(y - camera.getY() - radius)>>conversionFactor);
	}

	/**
	 * 
	 * 
	 */
	public void fillRect(int x, int y, int width, int height, Color color)
	{
		graphics.setColor(color);

		graphics.fillRect(
				(x-(width/2)-camera.getX()) >> conversionFactor, 
				(y-(height/2)-camera.getY()) >> conversionFactor, 
				width >> conversionFactor, 
				height >> conversionFactor);
	}

	/**
	 * 
	 * 
	 */
	public void fillArc(int x, int y, int width, int height,
			int startAngle, int arcAngle, Color color)
	{
		graphics.setColor(color);

		graphics.fillArc(
				(x-(width/2)-camera.getX()) >> conversionFactor, 
				(y-(height/2)-camera.getY()) >> conversionFactor, 
				width >> conversionFactor, 
				height >> conversionFactor,
				startAngle,
				arcAngle);
	}

	/**
	 * 
	 * 
	 */
	public void fillOval(int x, int y, int width, int height, Color color)
	{
		graphics.setColor(color);

		graphics.fillOval(
				(x-(width/2)-camera.getX()) >> conversionFactor, 
				(y-(height/2)-camera.getY()) >> conversionFactor, 
				width >> conversionFactor, 
				height >> conversionFactor);
	}

	/**
	 * 
	 * 
	 */
	public void fillCircle(int x, int y, int radius, Color color)
	{
		graphics.setColor(color);

		graphics.fillOval(
				(x-radius-camera.getX()) >> conversionFactor, 
				(y-radius-camera.getY()) >> conversionFactor, 
				radius*2 >> conversionFactor, 
				radius*2 >> conversionFactor);
	}

	public void rotate(int x, int y, double angle) {
		graphics.rotate(
				angle, 
				x-camera.getX() >> conversionFactor, 
				y-camera.getY() >> conversionFactor
		);
	}
	
	public void setAlphaComposite(float alpha) 
	{
		AlphaComposite fadeComposite =
			AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

		graphics.setComposite(fadeComposite);
	}	
}
