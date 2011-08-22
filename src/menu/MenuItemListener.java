package menu;

public interface MenuItemListener
{
	/**
	 * Invoked when an item has been pressed.
	 * 
	 * @param item The item that was pressed.
	 */
	void itemPressed(MenuItem item);
	
	/**
	 * Invoked when an item has been released.
	 * 
	 * @param item The item that was released.
	 */
	void itemReleased(MenuItem items);
}
