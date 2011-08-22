package menu;

import render.Renderer;
import entity.Entity;

/**
 * Base class for active menu items in the game menu. Items are rectangular and so
 * they have an associated width and height. Their x and y coordinates are referenced
 * from the top left of the item. This information can be used for selection items
 * based on curson position.
 * 
 * @author Rashid Bhamjee
 *
 */
public abstract class MenuItem extends Entity
{
	/** Item after this one in a menu */
	private MenuItem nextItem;
	
	/** Item before this one in a menu */
	private MenuItem prevItem;
	
	/** The width of the item in game units */
	private int width;
	
	/** The height of the item in game units */
	private int height;
	
	/** Whether the menu is active */
	private boolean active;
	
	/** Whether the menu is visible */
	private boolean visible;
	
	/** Listener object to sent events to */
	private MenuItemListener listener;
	
	/** 
	 * Invoked when an item is selected by the user.
	 *
	 */
	public void activate()
	{
		active = true;
	}
	
	/**
	 * Invoked when a button is deselected by the user.
	 *
	 */
	public void deactivate()
	{
		active = false;
	}
	
	/**
	 * Gets whether the menu item is active or not.
	 *
	 * @return True if the menu item is active.
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Sets whether the item should be visible.
	 * 
	 * @param visible True to set to visible.
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	/**
	 * Gets whether or not the item should be visible.
	 *  
	 * @return True if item should be visible.
	 */
	public boolean isVisible()
	{
		return visible;
	}
	
	/**
	 * Sets the listener to send events to from the menu item.
	 * 
	 * @param listener The listener to send events to.
	 */
	public void setListener(MenuItemListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * Gets the listener this item is sending events to.
	 * 
	 * @return Item's listener.
	 */
	protected MenuItemListener getListener()
	{
		return listener;
	}
	
	/**
	 * Invoked when an item is pressed by the user.
	 *
	 */
	public abstract void pressed();
	
	/**
	 * Invoked when an item is released by the user.
	 *
	 */
	public abstract void released();
	
	/**
	 * Updates the menu item's visual logic.
	 *
	 */
	public abstract void update();
	
	/**
	 * Renders the menu item using the specified renderer.
	 * 
	 * @param renderer The rendering device.
	 */
	public abstract void render(Renderer renderer);
	
	/**
	 * Renders the menu item using the specified renderer
	 * with the specified alpha value (if available).
	 * 
	 * @param renderer The rendering device.
	 * @param alpha The alpha value to render with.
	 */
	public abstract void render(Renderer renderer, float alpha);
	
//	/**
//	 * Sets the x coordinate of the menu item in pixels.
//	 * 
//	 * @param x The desired x coordinate in pixels.
//	 */
//	public void setPixelX(int x)
//	{
//		setX(x << CONVERSION_FACTOR);
//	}
//	
//	/**
//	 * Sets the x coordinate of the menu item in pixels.
//	 * 
//	 * @param x The desired x coordinate in pixels.
//	 */
//	public void setPixelY(int y)
//	{
//		setY(y << CONVERSION_FACTOR);
//	}
//	
//	/**
//	 * Sets the width of the menu item in pixels.
//	 * 
//	 * @param width The width in pixels.
//	 */
//	public void setPixelWidth(int width)
//	{
//		setWidth(width << CONVERSION_FACTOR);
//	}
//	
//	/**
//	 * Sets the height of the menu item in pixels.
//	 * 
//	 * @param height The height in pixels.
//	 */
//	public void setPixelHeight(int height)
//	{
//		setHeight(height << CONVERSION_FACTOR);
//	}
//	
//	public int getPixelWidth()
//	{
//		return width >> CONVERSION_FACTOR;
//	}
//	
//	public int getPixelHeight()
//	{
//		return height >> CONVERSION_FACTOR;
//	}
	
	/**
	 * Sets the menu item that should come after this one
	 * in a menu. Null if there is no next item.
	 * 
	 * @param item The next menu item.
	 */
	public void setNextItem(MenuItem item)
	{
		nextItem = item;
	}
	
	/**
	 * Sets the menu item that should come before this one
	 * in a menu. Null if there is no previous item.
	 * 
	 * @param item The previous menu item.
	 */
	public void setPrevItem(MenuItem item)
	{
		prevItem = item;
	}
	
	/**
	 * Gets the menu item that comes after this one in a menu.
	 * 
	 * @return The next menu item.
	 */
	public MenuItem getNextItem()
	{
		return nextItem;
	}
	
	/**
	 * Gets the menu item that comes before this one in a menu.
	 * 
	 * @return The previous menu item.
	 */
	public MenuItem getPrevItem()
	{
		return prevItem;
	}

	/**
	 * Sets the height of the item in game units.
	 * 
	 * @param height The desired height in game units.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
//	/**
//	 * Sets the height of the item in pixels.
//	 * 
//	 * @param height The desired height in pixels.
//	 */
//	public void setHeightPixels(int height)
//	{
//		this.height = height << CONVERSION_FACTOR;
//	}


	/**
	 * Gets the height of the item in game units.
	 * 
	 * @return Item's height in game units.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Sets the width of the item in game units.
	 * 
	 * @param width The desired width in game units.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}
	
//	/**
//	 * Sets the width of the item in pixels.
//	 * 
//	 * @param width The desired width in pixels.
//	 */
//	public void setWidthPixels(int width)
//	{
//		this.width = width << CONVERSION_FACTOR;
//	}

	/**
	 * Gets the width of the item in game units.
	 * 
	 * @return Item's width in game units.
	 */
	public int getWidth()
	{
		return width;
	}
	
//	/**
//	 * Gets the x coordinate of the item in pixels.
//	 * 
//	 * @return Item's x coordinate in pixels.
//	 */
//	public int getPixelX()
//	{
//		return getX() >> CONVERSION_FACTOR;
//	}
//	
//	/**
//	 * Gets the y coordinate of the item in pixels.
//	 * 
//	 * @return Item's y coordinate in pixels.
//	 */
//	public int getPixelY()
//	{
//		return getY() >> CONVERSION_FACTOR;
//	}
	
	/**
	 * Checks if the specified point made by two game unit coordinates is contained
	 * in the menu item.
	 * 
	 * @param x The x coordinate in game units.
	 * @param y The y coordinate in game units.
	 * @return True if point is contained within the item.
	 */
	public boolean containsPoint(int x, int y)
	{
		if(x < getX() || x > getX() + getWidth() || y < getY() || y > getY() + getHeight())
			return false;
		return true;
	}
	
}
