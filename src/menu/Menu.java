package menu;

import java.util.Vector;

import render.Renderer;

/**
 * A class that is an active menu in the game. Menus hold and manage menu items.
 * 
 * @author Rashid Bhamjee
 *
 */
public class Menu
{
	/**
	 * Vector to hold all the items in the menu.
	 */
	protected Vector<MenuItem> items;
	
	/** If the menu is active or not. */
	private boolean active;
	
	/** If the menu is to be rendered or not */
	private boolean visible;
	
	/** The currently selected menu item */
	private MenuItem currSelected;
	
	/**
	 * Constructs a new Menu object. A menu starts deactive
	 * by default.
	 *
	 */
	public Menu()
	{
		items = new Vector<MenuItem>();
		active = false;
	}
	
	/**
	 * Updates the menu's logic.
	 *
	 */
	public void update()
	{
		for(int x = 0, n = items.size(); x < n; x++)
				items.get(x).update();
	}
	
	/**
	 * Renders the menu using the specified renderer.
	 * 
	 * @param renderer The rendering device.
	 */
	public void render(Renderer renderer)
	{
		for(int x = 0, n = items.size(); x < n; x++)
			if(items.get(x).isVisible())
				items.get(x).render(renderer);
	}
	
	/**
	 * Defines what a menu should do when it is activated by the user.
	 *
	 */
	public void activate()
	{
		active = true;
	}
	
	/**
	 * Defines what a menu should do when it is deactivated by the user.
	 *
	 */
	public void deactivate()
	{
		active = false;
	}
	
	/**
	 * Gets whether or not the menu is active.
	 * 
	 * @return True if the menu is active.
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Gets whether or not the menu should be rendered.
	 * 
	 * @return True if the menu is to be rendered.
	 */
	public boolean isVisible()
	{
		return visible;
	}
	
	/**
	 * Sets whether the menu should be rendered or not.
	 * 
	 * @param visible True if the menu should be rendered.
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	
	/**
	 * Adds an item to the menu.
	 * 
	 * @param item The item to add.
	 */
	public void addItem(MenuItem item)
	{
		items.add(item);
		
		if(items.size() == 1)
		{
			currSelected = item;
			currSelected.activate();
		}
	}
	
	/**
	 * Removes an item from the menu.
	 * 
	 * @param item The item to remove.
	 */
	public void removeItem(MenuItem item)
	{
		items.remove(item);
		
		if(item == currSelected)
			currSelected = null;
	}
	
	/**
	 * Sets the next item as being selected in the menu. If no next
	 * item exists, nothing is changed.
	 *
	 */
	public void nextItem()
	{
		//only change the currently selected item if there's a next item.
		//if current item is null then make the first item selected
		if(currSelected == null && items.size() != 0)
		{
			currSelected = items.get(0);
			currSelected.activate();
		}
		else if(currSelected != null && currSelected.getNextItem() != null)
		{
			currSelected.deactivate();
			currSelected = currSelected.getNextItem();
			currSelected.activate();
		}
	}
	
	/**
	 * Sets the previous item as being selected in the menu. If no previous
	 * item exists, nothing is changed.
	 *
	 */
	public void prevItem()
	{
		//only change the currently selected item if there's a previous item.
		//if current item is null then make the last item selected
		if(currSelected == null && items.size() != 0)
		{
			currSelected = items.get(items.size() - 1);
			currSelected.activate();
		}
		else if(currSelected != null && currSelected.getPrevItem() != null)
		{
			currSelected.deactivate();
			currSelected = currSelected.getPrevItem();
			currSelected.activate();
		}
	}
	
	/**
	 * Selects a menu item at the specified point. If there is no
	 * item at the point then no item is selected.
	 * 
	 * @param x The x coordinate of the point in game units.
	 * @param y The y coordinate of the point in game units.
	 */
	public void selectItemAt(int x, int y)
	{
		MenuItem intersectingItem = null;
		
		for(int i = 0, n = items.size(); i < n; i++)
			if(items.get(i).containsPoint(x, y))
				intersectingItem = items.get(i);
		
		if(currSelected != null && currSelected != intersectingItem)
		{
			currSelected.deactivate();
			
			currSelected = intersectingItem;
			
			if(currSelected != null)
				currSelected.activate();
		}
		else if(intersectingItem != null && currSelected != intersectingItem)
		{
			currSelected = intersectingItem;
			
			currSelected.activate();
		}
	}
	
	/**
	 * Presses the currently selected menu item and send events
	 * if required. If there is no currently selected item this 
	 * method does nothing.
	 *
	 */
	public void pressItem()
	{
		if(currSelected != null)
			currSelected.pressed();
	}
	
	/**
	 * Releases the currently selected menu item and send events
	 * if required. If there is no currently selected item this 
	 * method does nothing.
	 *
	 */
	public void releaseItem()
	{
		if(currSelected != null)
			currSelected.released();
	}
	
}
