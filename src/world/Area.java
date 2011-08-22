package world;

import java.util.Random;
import java.util.Vector;

import render.Renderer;
import entity.Controller;
import entity.Entity;
import entity.NonHostileAIController;
import entity.Speck;

public class Area {

	/** Nunber of specks in each Area */
	private final int NO_OF_SPECKS = 6;
	
	/** This areas X compentent of its ID co-ordinate */    
	private int idX = 0;

	/** This areas Y compentent of its ID co-ordinate */
	private int idY = 0;

	/** The width of each Area in game units */
	private static final int width = 1024 << 5;

	/** The heigh of each Area in game units */
	private static final int height = 768 << 5;

	/** The Area's top left corner X co-ordinate in game units*/
	private int x = 0;

	/** The Area's top left corner Y co-ordinate in game units*/
	private int y = 0;

	/** The Area's top left corner old X co-ordinate in game units*/
	private int oldX = x;

	/** The Area's top left corner old Y co-ordinate in game units*/
	private int oldY = y;

	/** The Main Camera **/
	private Camera camera;

	/** The references to bordering Areas */
	public Area above, below, left, right, aboveLeft, aboveRight, belowLeft, belowRight;

	/** A Vector holding all entities present in this Area */
	private Vector<Entity> entities;

	/** A static Vector holding references to all the Area's created */
	private Vector<Area> areas;
	
	/** A random number generator for generating random coordinates */
	private Random randomX;
	
	/** A random number generator for generating random coordinates */
	private Random randomY;

	/**
	 * Constructor for Area with ID of 0,0
	 * Initialises bordering Area's to null and adds the Area to <var>areas</var>
	 *
	 */
	public Area() {
		entities = new Vector<Entity>();
		areas = new Vector<Area>();
		areas.addElement(this);
		
		above      = null;
		below      = null;
		left       = null;
		right      = null;
		aboveLeft  = null; 
		aboveRight = null; 
		belowLeft  = null; 
		belowRight = null;
		
		randomX = new Random();
		randomY = new Random();
		
		for(int i = 0; i < NO_OF_SPECKS; i++)
		{
			Speck s = new Speck(getX() + randomX.nextInt(width), getY() + randomY.nextInt(height));
			Controller c = new NonHostileAIController(s);
			s.setController(c);
			addEntity(s);
		}
	}

	/**
	 * Constructor for Area
	 * Initialises bordering Area's to null and adds the Area to <var>areas</var>
	 * Takes in one bordering area, along with the side which it borders and assigns it correctly
	 * It also ensures that the new Area is IDed correctly
	 *
	 * @param dir  The side that the neighbouring Area is located
	 * @param anArea The Area bordering this one in the on the <var>dir</var> border
	 */
	public Area(char dir, Area anArea) {
		entities = new Vector<Entity>();
		areas = anArea.getAreas();
		areas.addElement(this);
		
		above      = null;
		below      = null;
		left       = null;
		right      = null;
		aboveLeft  = null; 
		aboveRight = null; 
		belowLeft  = null; 
		belowRight = null;
		
		switch(dir) {
		case 'a': above      = anArea; setId(anArea.idX,   anArea.idY+1); setPos(anArea.getX(), anArea.getY()+height);break;
		case 'b': below      = anArea; setId(anArea.idX,   anArea.idY-1); setPos(anArea.getX(), anArea.getY()-height);break;
		case 'l': left       = anArea; setId(anArea.idX+1, anArea.idY);   setPos(anArea.getX()+width, anArea.getY());break;
		case 'r': right      = anArea; setId(anArea.idX-1, anArea.idY);   setPos(anArea.getX()-width, anArea.getY());break;
		case 'q': aboveLeft  = anArea; setId(anArea.idX+1, anArea.idY+1); setPos(anArea.getX()+width, anArea.getY()+height);break;
		case 'p': aboveRight = anArea; setId(anArea.idX-1, anArea.idY+1); setPos(anArea.getX()-width, anArea.getY()+height);break;
		case 'z': belowLeft  = anArea; setId(anArea.idX+1, anArea.idY-1); setPos(anArea.getX()+width, anArea.getY()-height);break;
		case 'm': belowRight = anArea; setId(anArea.idX-1, anArea.idY-1); setPos(anArea.getX()-width, anArea.getY()-height);break;
		}

		randomX = new Random();
		randomY = new Random();
		
		for(int i = 0; i < NO_OF_SPECKS; i++)
		{
			Speck s = new Speck(getX() + randomX.nextInt(width), getY() + randomY.nextInt(height));
			Controller c = new NonHostileAIController(s);
			s.setController(c);
			addEntity(s);
		}
	}

	/**
	 * Sets the ID to the one provided
	 *
	 * @param anX The x component of the ID
	 * @param aY The y component of the ID
	 */
	public void setId(int anX, int aY) {
		idX = anX;
		idY = aY;
	}

	/**
	 * Sets the Area's relevant position in game units to the Frames top left corner
	 *
	 * @param anX The x component of the co-ordinate
	 * @param aY The y component of the co-ordinate
	 */	
	public void setPos(int anX, int aY) {
		x = anX;
		y = aY;

		oldX = x;
		oldY = y;
	}

	/**
	 * Sets the Area's old coordinates
	 *
	 *
	 */	
	public void setOld() {
		oldX = x;
		oldY = y;
	}

	/**
	 * Sets all the bordering Areas at once
	 * It also ensures that the neighbouring Area's are IDed correctly
	 *
	 * @param anAbove The Area above the current one
	 * @param aBelow The Area below the current one
	 * @param aLeft The Area to the left of the current one
	 * @param aRight The Area to the right of the current one
	 * @param anAboveLeft The Area above and to the left of the current one
	 * @param anAboveRight The Area above and to the right of the current one
	 * @param aBelowLeft The Area below and to the left of the current one
	 * @param aBelowRight The Area below and to the right of the current one
	 */
	public void setBorderAreas(Area anAbove, Area aBelow, Area aLeft, Area aRight, 
			Area anAboveLeft, Area anAboveRight, Area aBelowLeft, 
			Area aBelowRight) {

		above = anAbove;
		above.setId(idX, idY-1);

		below = aBelow;
		below.setId(idX, idY+1);

		left  = aLeft;
		left.setId(idX-1, idY);

		right = aRight;
		right.setId(idX+1, idY);

		aboveLeft = anAboveLeft;
		aboveLeft.setId(idX-1, idY-1);

		aboveRight = anAboveRight;
		aboveRight.setId(idX+1, idY-1);

		belowLeft  = aBelowLeft;
		belowLeft.setId(idX-1, idY+1);

		belowRight = aBelowRight;
		belowRight.setId(idX+1, idY+1);

	}

	/**
	 * Sets a single bordering Area
	 * It also ensures that the neighbouring Area is IDed correctly
	 *
	 * @param dir The side that the neighbouring Area is located
	 * @param anArea The Area above the current one
	 */
	public void setBorderArea(char dir, Area anArea) {
		switch(dir) {
		case 'a': above      = anArea; anArea.setId(idX, idY-1);                 break;
		case 'b': below      = anArea; anArea.setId(idX, idY+1);                 break;
		case 'l': left       = anArea; anArea.setId(idX-1, idY);                 break;
		case 'r': right      = anArea; anArea.setId(idX+1, idY);                 break;
		case 'q': aboveLeft  = anArea; anArea.setId(anArea.idX-1, anArea.idY-1); break;
		case 'p': aboveRight = anArea; anArea.setId(anArea.idX+1, anArea.idY-1); break;
		case 'z': belowLeft  = anArea; anArea.setId(anArea.idX-1, anArea.idY+1); break;
		case 'm': belowRight = anArea; anArea.setId(anArea.idX+1, anArea.idY+1); break;
		}
	}

	/**
	 * Gets the Area's x coordinate in game units
	 * 
	 * @return Area's x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the Area's old x coordinate in game units
	 * 
	 * @return Area's old x coordinate.
	 */
	public int getOldX() {
		return oldX;
	}

	/**
	 * Gets the Area's y coordinate in game units
	 * 
	 * @return Area's y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the Area's old y coordinate in game units
	 * 
	 * @return Area's old y coordinate.
	 */
	public int getOldY() {
		return oldY;
	}

	/**
	 * Returns the width of an Area
	 *
	 * @return The width of an Area
	 */
	public static int getWidth() {
		return width;
	}

	/**
	 * Returns the height of an Area
	 *
	 * @return The height of an Area
	 */
	public static int getHeight() {
		return height;
	}

	/**
	 * Returns a vector containing all bordering Areas
	 *
	 */
	public Vector<Area> getBorderAreas() {
		Vector<Area> v = new Vector<Area>();
		v.add(above);
		v.add(below);
		v.add(left);
		v.add(right);
		v.add(aboveLeft);
		v.add(aboveRight);
		v.add(belowLeft);
		v.add(belowRight);
		return v;
	}
	
	public Vector<Area> getAreas() {
		return areas;
	}

	/**
	 * Add an Entity to the Area.
	 *
	 * @param e The Entity to be added
	 */
	public void addEntity(Entity anEntity) {
		entities.add(anEntity);
//		((PlayerEntity)anEntity).setArea(this);
	}

	/**
	 * Get an Enitity from the Area
	 *
	 * @param e The Entity to be gotten
	 * @return The Entity that has been gotten
	 */
	public Entity getEntity(Entity e) {
		Entity temp = null;
		for(Entity anEntity:entities)
			if(anEntity.equals(e)) {
				temp = anEntity;
				break;
			}
		return temp;
	}

	/**
	 * Remove an Enitity from the Area
	 *
	 * @param e The Entity to be removed
	 * @return The Entity that has been removed
	 */
	public Entity removeEntity(Entity e) {
		Entity temp = getEntity(e);
		entities.remove(temp);
		return temp;
	}

	/**
	 * Generates all neighbouring Areas around an Area that has not got all of them
	 *
	 * @param anArea The Area that may need neighbouring Areas generated
	 */
	public void grow() {

		above = find(idX, idY-1);
		if(above == null) {
			above            = new Area('b', this);
			above.above      = find(above.idX, above.idY-1);
			above.left       = find(above.idX-1, above.idY);
			above.right      = find(above.idX+1, above.idY);
			above.aboveLeft  = find(above.idX-1, above.idY-1);
			above.aboveRight = find(above.idX+1, above.idY-1);
			above.belowLeft  = find(above.idX-1, above.idY+1);
			above.belowRight = find(above.idX+1, above.idY+1);

			if(above.above != null)
				above.above.below = above;
			if(above.left  != null)
				above.left.right = above;
			if(above.right != null)
				above.right.left = above;
			if(above.aboveLeft != null)
				above.aboveLeft.belowRight = above;
			if(above.aboveRight != null)
				above.aboveRight.belowLeft = above;
			if(above.belowLeft != null)
				above.belowLeft.aboveRight = above;
			if(above.belowRight != null)
				above.belowRight.aboveLeft = above;
		}

		below = find(idX, idY+1);
		if(below == null) {
			below            = new Area('a', this);
			below.below      = find(below.idX, below.idY+1);
			below.left       = find(below.idX-1, below.idY);
			below.right      = find(below.idX+1, below.idY);
			below.aboveLeft  = find(below.idX-1, below.idY-1);
			below.aboveRight = find(below.idX+1, below.idY-1);
			below.belowLeft  = find(below.idX-1, below.idY+1);
			below.belowRight = find(below.idX+1, below.idY+1);

			if(below.below != null)
				below.below.above = below;
			if(below.left  != null)
				below.left.right = below;
			if(below.right != null)
				below.right.left = below;
			if(below.aboveLeft != null)
				below.aboveLeft.belowRight = below;
			if(below.aboveRight != null)
				below.aboveRight.belowLeft = below;
			if(below.belowLeft != null)
				below.belowLeft.aboveRight = below;
			if(below.belowRight != null)
				below.belowRight.aboveLeft = below;
		}

		left  = find(idX-1, idY);
		if(left  == null) {
			left            = new Area('r', this);
			left.above      = find(left.idX,  left.idY-1 );
			left.below      = find(left.idX,  left.idY+1 );
			left.left       = find(left.idX-1,  left.idY );
			left.aboveLeft  = find(left.idX-1, left.idY-1);
			left.aboveRight = find(left.idX+1, left.idY-1);
			left.belowLeft  = find(left.idX-1, left.idY+1);
			left.belowRight = find(left.idX+1, left.idY+1);

			if(left.above  != null)
				left.above.below  = left;
			if(left.below  != null)
				left.below.above  = left;
			if(left.left   != null)
				left.left.right   = left;
			if(left.aboveLeft != null)
				left.aboveLeft.belowRight = left;
			if(left.aboveRight != null)
				left.aboveRight.belowLeft = left;
			if(left.belowLeft != null)
				left.belowLeft.aboveRight = left;
			if(left.belowRight != null)
				left.belowRight.aboveLeft = left;
		}

		right = find(idX+1, idY);
		if(right == null) {
			right            = new Area('l', this);
			right.above      = find(right.idX, right.idY-1);
			right.below      = find(right.idX, right.idY+1);
			right.right      = find(right.idX+1, right.idY);
			right.aboveLeft  = find(right.idX-1, right.idY-1);
			right.aboveRight = find(right.idX+1, right.idY-1);
			right.belowLeft  = find(right.idX-1, right.idY+1);
			right.belowRight = find(right.idX+1, right.idY+1);

			if(right.above != null)
				right.above.below = right;
			if(right.below != null)
				right.below.above = right;
			if(right.right != null)
				right.right.left  = right;
			if(right.aboveLeft != null)
				right.aboveLeft.belowRight = right;
			if(right.aboveRight != null)
				right.aboveRight.belowLeft = right;
			if(right.belowLeft != null)
				right.belowLeft.aboveRight = right;
			if(right.belowRight != null)
				right.belowRight.aboveLeft = right;
		}

		aboveLeft = find(idX-1, idY-1);
		if(aboveLeft == null) {
			aboveLeft            = new Area('m', this);
			aboveLeft.above      = find(aboveLeft.idX, aboveLeft.idY-1);
			aboveLeft.below      = find(aboveLeft.idX, aboveLeft.idY+1);
			aboveLeft.left       = find(aboveLeft.idX-1, aboveLeft.idY);
			aboveLeft.right      = find(aboveLeft.idX+1, aboveLeft.idY);
			aboveLeft.aboveLeft  = find(aboveLeft.idX-1, aboveLeft.idY-1);
			aboveLeft.aboveRight = find(aboveLeft.idX+1, aboveLeft.idY-1);
			aboveLeft.belowLeft  = find(aboveLeft.idX-1, aboveLeft.idY+1);

			if(aboveLeft.above != null)
				aboveLeft.above.below = aboveLeft;
			if(aboveLeft.below != null)
				aboveLeft.below.above = aboveLeft;
			if(aboveLeft.left  != null)
				aboveLeft.left.right = aboveLeft;
			if(aboveLeft.right != null)
				aboveLeft.right.left = aboveLeft;
			if(aboveLeft.aboveLeft != null)
				aboveLeft.aboveLeft.belowRight = aboveLeft;
			if(aboveLeft.aboveRight != null)
				aboveLeft.aboveRight.belowLeft = aboveLeft;
			if(aboveLeft.belowLeft != null)
				aboveLeft.belowLeft.aboveRight = aboveLeft;
		}

		aboveRight = find(idX+1, idY-1);
		if(aboveRight == null) {
			aboveRight            = new Area('z', this);
			aboveRight.above      = find(aboveRight.idX, aboveRight.idY-1);
			aboveRight.below      = find(aboveRight.idX, aboveRight.idY+1);
			aboveRight.left       = find(aboveRight.idX-1, aboveRight.idY);
			aboveRight.right      = find(aboveRight.idX+1, aboveRight.idY);
			aboveRight.aboveLeft  = find(aboveRight.idX-1, aboveRight.idY-1);
			aboveRight.aboveRight = find(aboveRight.idX+1, aboveRight.idY-1);
			aboveRight.belowRight = find(aboveRight.idX+1, aboveRight.idY+1);

			if(aboveRight.above != null)
				aboveRight.above.below = aboveRight;
			if(aboveRight.below != null)
				aboveRight.below.above = aboveRight;
			if(aboveRight.left  != null)
				aboveRight.left.right = aboveRight;
			if(aboveRight.right != null)
				aboveRight.right.left = aboveRight;
			if(aboveRight.aboveLeft != null)
				aboveRight.aboveLeft.belowRight = aboveRight;
			if(aboveRight.aboveRight != null)
				aboveRight.aboveRight.belowLeft = aboveRight;
			if(aboveRight.belowRight != null)
				aboveRight.belowRight.aboveLeft = aboveRight;
		}

		belowLeft = find(idX-1, idY+1);
		if(belowLeft == null) {
			belowLeft            = new Area('p', this);
			belowLeft.above      = find(belowLeft.idX, belowLeft.idY-1);
			belowLeft.below      = find(belowLeft.idX, belowLeft.idY+1);
			belowLeft.left       = find(belowLeft.idX-1, belowLeft.idY);
			belowLeft.right      = find(belowLeft.idX+1, belowLeft.idY);
			belowLeft.aboveLeft  = find(belowLeft.idX-1, belowLeft.idY-1);
			belowLeft.belowLeft  = find(belowLeft.idX-1, belowLeft.idY+1);
			belowLeft.belowRight = find(belowLeft.idX+1, belowLeft.idY+1);

			if(belowLeft.above != null)
				belowLeft.above.below = belowLeft;
			if(belowLeft.below != null)
				belowLeft.below.above = belowLeft;
			if(belowLeft.left  != null)
				belowLeft.left.right = belowLeft;
			if(belowLeft.right != null)
				belowLeft.right.left = belowLeft;
			if(belowLeft.aboveLeft != null)
				belowLeft.aboveLeft.belowRight = belowLeft;
			if(belowLeft.belowLeft != null)
				belowLeft.belowLeft.aboveRight = belowLeft;
			if(belowLeft.belowRight != null)
				belowLeft.belowRight.aboveLeft = belowLeft;
		}

		belowRight = find(idX+1, idY+1);
		if(belowRight == null) {
			belowRight            = new Area('q', this);
			belowRight.above      = find(belowRight.idX, belowRight.idY-1);
			belowRight.below      = find(belowRight.idX, belowRight.idY+1);
			belowRight.left       = find(belowRight.idX-1, belowRight.idY);
			belowRight.right      = find(belowRight.idX+1, belowRight.idY);
			belowRight.aboveRight = find(belowRight.idX+1, belowRight.idY-1);
			belowRight.belowLeft  = find(belowRight.idX-1, belowRight.idY+1);
			belowRight.belowRight = find(belowRight.idX+1, belowRight.idY+1);

			if(belowRight.above != null)
				belowRight.above.below = belowRight;
			if(belowRight.below != null)
				belowRight.below.above = belowRight;
			if(belowRight.left  != null)
				belowRight.left.right = belowRight;
			if(belowRight.right != null)
				belowRight.right.left = belowRight;
			if(belowRight.aboveRight != null)
				belowRight.aboveRight.belowLeft = belowRight;
			if(belowRight.belowLeft != null)
				belowRight.belowLeft.aboveRight = belowRight;
			if(belowRight.belowRight != null)
				belowRight.belowRight.aboveLeft = belowRight;
		}
	}

	/**
	 * Searches through the Vector <var>areas</var>.
	 * Checks if there are any Areas with the provided ID
	 *
	 * @param anX The x component of the ID
	 * @param aY The y component of the ID
	 * @return The Area that was found, or null
	 */
	public Area find(int anX, int aY) {
		for(Area a: areas)
			if(a.idX == anX && a.idY == aY)
				return a;
		return null;
	}

	/**
	 * Paints the Area to the screen along with any entities may be present
	 *
	 * @param g The frame's graphic object
	 */
	public void render(Renderer theRenderer) {

	}

	public void renderEntities(Renderer theRenderer) {
		for(Entity e: entities)
			if(e != null)
				e.render(theRenderer);
	}

	/**
	 * Checks if two Areas are equal
	 *
	 * @return true if both IDs are the same
	 */
	public boolean equals(Object o) {
		return this.idX == ((Area)o).idX && this.idY == ((Area)o).idY;
	}

	/**
	 * Returns a String representation of the Area in the form
	 * (<var>idX</var>, <var>idY</var>)
	 *
	 * @return A string reprensentation of the Area
	 */
	public String toString() {
		return "("+idX+", "+idY+")";
	}

	/**
	 * Gets a vector of entities in the area.
	 *
	 * @return Entities in the area.
	 */
	public Vector<Entity> getEntities()
	{
		return entities;
	}

	public boolean containsPoint(int x, int y)
	{
		if(x < getX() || x > getX() + getWidth() || y < getY() || y > getY() + getHeight())
			return false;
		return true;
	}
	
	
}
