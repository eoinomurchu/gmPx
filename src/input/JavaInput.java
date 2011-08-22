package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

public class JavaInput extends Input implements MouseMotionListener, 
	MouseListener, KeyListener
{
	/** Index of device information in an event array */
	private final int DEVICE_INDEX = 0;

	/** Index of key information in an event array */
	private final int KEY_INDEX = 1;
	
	/** Index of value information in an event array */
	private final int VALUE_INDEX = 2;
	
	private Vector<int[]> events;
	
	public JavaInput()
	{
		events = new Vector<int[]>();
	}
	
	public void poll()
	{
		while(events.size() > 0)
		{
			processEvent(events.get(0)[DEVICE_INDEX], events.get(0)[KEY_INDEX],
					events.get(0)[VALUE_INDEX]);
			events.remove(0);
		}
	}
	
	/**
	 * Adds an event to the event queue. The button code should the
	 * the button code from the Java API. If the event was a pointer
	 * event then the button code from the Input class should be
	 * provided.
	 * 
	 * @param device The device the event came from.
	 * @param button The code of the button causing the event.
	 * @param value The value of the button.
	 */
	private void addEvent(int device, int button, int value)
	{
		events.add(new int[]{device, button, value});
	}
	
	public void mouseDragged(MouseEvent e)
	{
		addEvent(MOUSE, Input.POINTER_X, e.getX());
		addEvent(MOUSE, Input.POINTER_Y, e.getY());
	}

	public void mouseMoved(MouseEvent e)
	{
		addEvent(MOUSE, Input.POINTER_X, e.getX());
		addEvent(MOUSE, Input.POINTER_Y, e.getY());
	}

	public void mouseClicked(MouseEvent e)
	{
		
	}

	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e)
	{
		addEvent(MOUSE, e.getButton(), PRESSED);
	}

	public void mouseReleased(MouseEvent e)
	{
		addEvent(MOUSE, e.getButton(), RELEASED);
	}

	public void keyPressed(KeyEvent e)
	{
		addEvent(KEYBOARD, e.getKeyCode(), PRESSED);
	}

	public void keyReleased(KeyEvent e)
	{
		addEvent(KEYBOARD, e.getKeyCode(), RELEASED);
	}

	public void keyTyped(KeyEvent e){}
}
