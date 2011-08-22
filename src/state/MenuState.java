package state;

import input.Input;
import world.Camera;

import java.awt.Color;
import java.util.Vector;

import menu.Button;
import menu.FadingMenu;
import menu.Label;
import menu.Menu;
import menu.MenuItem;
import menu.MenuItemListener;
import menu.MovingButton;
import menu.MovingLabel;
import render.JavaSprite;
import render.Renderer;
import render.Sprite;
import render.Window;
import core.GameManager;

/**
 * The MenuState uses the classes from the menu package to build a menu system for the game.
 * 
 * @author Rashid Bhamjee
 *
 */
public class MenuState implements State, MenuItemListener
{
	/** Position to hide logos at */
	private final int LOGO_HIDE = 6500;
	
	/** Position to start logos at */
	private int buttonStartY = 8500;
	
	/** Main menu */
	private Menu mainMenu;
	
	/** Difficulty menu */
	private Menu difficultyMenu;
	
	/** About menu */
	private Menu aboutMenu;
	
	/** Options menu */
	private Menu optionsMenu;
	
	/** Quit menu */
	private Menu quitMenu;
	
	/** Button for starting a new game */
	private Button newGameButton;
	
	/** Button for quitting the game */
	private Button quitButton;

	/** Button for about menu */
	private Button aboutButton;
	
	/** Options button */
	private Button optionsButton;
	
	/** Confirum quit button */
	private MovingButton yesQuitButton;
	
	/** Cancel quit button */
	private MovingButton noQuitButton;
	
	/** Easy difficulty button */
	private MovingButton easyButton;
	
	/** Hard difficulty button */
	private MovingButton hardButton;

	/** Back button for about menu */
	private Button aboutToMainButton;
	
	/** Back button for options menu */
	private MovingButton optionsToMainButton;
	
	/** Back button for difficulty menu */
	private Button difficultyToMainButton;
	
	/** Fullscreen option */
	private Button fullscreenButton;
	
	/** Main logo */
	private MovingLabel logo;
	
	/** Options logo */
	private MovingLabel optionsLogo;
	
	/** Difficulty logo */
	private MovingLabel difficultyLogo;
	
	/** Quit logo */
	private MovingLabel quitLogo;
	
	/** About information */
	private Label aboutLabel;
	
	/** Fullscreen option */
	private MovingButton noFullscreen;
	
	/** Fullscreen option */
	private MovingButton yesFullscreen;
	
	/** True if the res buton has been moved */
	private boolean resMoved;
	
	/** True if the option's back button has been moved */
	private boolean optionsBackMoved;
	
	/** Holds all the game's menus */
	private Vector<Menu> menus;
	
	/** Input to check for key states */
	private Input input;
	
	/** Class managing this state */
	private GameManager manager;

	/** The camera for this state */
	private Camera camera;
	
	/** Table of buttons that can be pressed again */
	private boolean[] canPress;
	
	/** Mouse's x coordinate */
	private int mouseX;
	
	/** Mouse's y coordinate */
	private int mouseY;
	
	/** Background colour */
	private Color backColour;
	
	/**
	 * Constructs a new MenuState.
	 *
	 */
	public MenuState(GameManager manager)
	{
		this.manager = manager;
		input = manager.getInput();

		camera = new Camera(0, 0, manager.getWindowWidth(), manager.getWindowHeight());

		canPress = new boolean[Input.BUTTON_COUNT];
		menus = new Vector<Menu>();
		backColour = Color.getHSBColor(0.56f, 1.0f, 1.0f);
		initButtons();
		initOther();
		initMenus();
		setPositions();
	}
	
	/**
	 * Initialises the all the menu state's buttons.
	 *
	 */
	private void initButtons()
	{
		Sprite startA = null;
		Sprite startI = null;
		
		Sprite aboutA = null;
		Sprite aboutI = null;
		
		Sprite backA = null;
		Sprite backI = null;
		
		Sprite optionsA = null;
		Sprite optionsI = null;
		
		Sprite quitA = null;
		Sprite quitI = null;
		
		Sprite yesA = null;
		Sprite yesI = null;

		Sprite noA = null;
		Sprite noI = null;
		
		Sprite resolutionA = null;
		Sprite resolutionI = null;
		
		Sprite fullscreenA = null;
		Sprite fullscreenI = null;
		
		Sprite easyA = null;
		Sprite easyI = null;
		
		Sprite hardA = null;
		Sprite hardI = null;
		
		Sprite wideA = null;
		Sprite wideI = null;

		Sprite boxA = null;
		Sprite boxI = null;
		
		if(manager.getRenderingDevice() == Window.JAVA_WINDOW)
		{
			startA = new JavaSprite("images/starton.png");
			startI = new JavaSprite("images/startoff.png");
			
			aboutA = new JavaSprite("images/abouton.png");
			aboutI = new JavaSprite("images/aboutoff.png");
			
			backA = new JavaSprite("images/backon.png");
			backI = new JavaSprite("images/backoff.png");
			
			optionsA = new JavaSprite("images/optionson.png");
			optionsI = new JavaSprite("images/optionsoff.png");
			
			quitA = new JavaSprite("images/quiton.png");
			quitI = new JavaSprite("images/quitoff.png");
			
			yesA = new JavaSprite("images/yeson.png");
			yesI = new JavaSprite("images/yesoff.png");
			
			noA = new JavaSprite("images/noon.png");
			noI = new JavaSprite("images/nooff.png");
			
			resolutionA = new JavaSprite("images/screensizeon.png");
			resolutionI = new JavaSprite("images/screensizeoff.png");
			
			fullscreenA = new JavaSprite("images/fullscreenon.png");
			fullscreenI = new JavaSprite("images/fullscreenoff.png");
			
			easyA = new JavaSprite("images/easyon.png");
			easyI = new JavaSprite("images/easyoff.png");
			
			hardA = new JavaSprite("images/hardon.png");
			hardI = new JavaSprite("images/hardoff.png");
			
			wideA = new JavaSprite("images/1610on.png");
			wideI = new JavaSprite("images/1610off.png");
			
			boxA = new JavaSprite("images/43on.png");
			boxI = new JavaSprite("images/43off.png");
		}
		else if(manager.getRenderingDevice() == Window.GL_WINDOW)
		{
			//TODO
			assert false;
		}
		
		
		//make buttons
		
		//main menu
		newGameButton = new Button(startA, startI);
		newGameButton.setListener(this);
		newGameButton.setVisible(true);
		newGameButton.setWidth(startA.getWidth());
		newGameButton.setHeight(startA.getHeight());
		
		aboutButton = new Button(aboutA, aboutI);
		aboutButton.setListener(this);
		aboutButton.setVisible(true);
		aboutButton.setWidth(aboutA.getWidth());
		aboutButton.setHeight(aboutA.getHeight());

		optionsButton = new Button(optionsA, optionsI);
		optionsButton.setListener(this);
		optionsButton.setVisible(true);
		optionsButton.setWidth(optionsA.getWidth());
		optionsButton.setHeight(optionsA.getHeight());

		quitButton = new Button(quitA, quitI);
		quitButton.setListener(this);
		quitButton.setVisible(true);
		quitButton.setWidth(quitA.getWidth());
		quitButton.setHeight(quitA.getHeight());
		
		
		//difficulty menu
		easyButton = new MovingButton(easyA, easyI);
		easyButton.setListener(this);
		easyButton.setVisible(true);
		easyButton.setWidth(easyA.getWidth());
		easyButton.setHeight(easyA.getHeight());
		
		hardButton = new MovingButton(hardA, hardI);
		hardButton.setListener(this);
		hardButton.setVisible(true);
		hardButton.setWidth(hardA.getWidth());
		hardButton.setHeight(hardA.getHeight());
		
		difficultyToMainButton = new Button(backA, backI);
		difficultyToMainButton.setListener(this);
		difficultyToMainButton.setVisible(true);
		difficultyToMainButton.setWidth(backA.getWidth());
		difficultyToMainButton.setHeight(backA.getHeight());
		
		//about menu
		aboutToMainButton = new Button(backA, backI);
		aboutToMainButton.setListener(this);
		aboutToMainButton.setVisible(true);
		aboutToMainButton.setWidth(backA.getWidth());
		aboutToMainButton.setHeight(backA.getHeight());
		
		//quit menu
		yesQuitButton = new MovingButton(yesA, yesI);
		yesQuitButton.setListener(this);
		yesQuitButton.setVisible(true);
		yesQuitButton.setWidth(yesA.getWidth());
		yesQuitButton.setHeight(backA.getHeight());
		
		noQuitButton = new MovingButton(noA, noI);
		noQuitButton.setListener(this);
		noQuitButton.setVisible(true);
		noQuitButton.setWidth(noA.getWidth());
		noQuitButton.setHeight(noA.getHeight());

		
		//options menu
		fullscreenButton = new Button(fullscreenA, fullscreenI);
		fullscreenButton.setListener(this);
		fullscreenButton.setVisible(true);
		fullscreenButton.setWidth(fullscreenA.getWidth());
		fullscreenButton.setHeight(fullscreenA.getHeight());
		
		yesFullscreen = new MovingButton(yesA, yesI);
		yesFullscreen.setListener(this);
		yesFullscreen.setVisible(true);
		yesFullscreen.setWidth(yesA.getWidth());
		yesFullscreen.setHeight(yesA.getHeight());

		noFullscreen = new MovingButton(noA, noI);
		noFullscreen.setListener(this);
		noFullscreen.setVisible(true);
		noFullscreen.setWidth(noA.getWidth());
		noFullscreen.setHeight(noA.getHeight());
		
		optionsToMainButton = new MovingButton(backA, backI);
		optionsToMainButton.setListener(this);
		optionsToMainButton.setVisible(true);
		optionsToMainButton.setWidth(backA.getWidth());
		optionsToMainButton.setHeight(backA.getHeight());
		
		//set next and previous items
//		newGameButton.setNextItem(aboutButton);
//		newGameButton.setPrevItem(aboutButton);
//		
//		aboutButton.setNextItem(newGameButton);
//		aboutButton.setPrevItem(newGameButton);
	}
	
	/** 
	 * Sets the position of all the menu's items in relation to the window size.
	 *
	 */
	public void setPositions()
	{
		buttonStartY= (int) ((double)manager.getWindowHeight() / 2.5);
		
		//main menu
		newGameButton.setX((manager.getWindowWidth() / 2) - (newGameButton.getWidth() / 2));
		newGameButton.setY(buttonStartY);
		
		aboutButton.setX((manager.getWindowWidth() / 2) - (aboutButton.getWidth() / 2));
		aboutButton.setY(newGameButton.getY() + newGameButton.getHeight());

		optionsButton.setX((manager.getWindowWidth() / 2) - (optionsButton.getWidth() / 2));
		optionsButton.setY(aboutButton.getY() + aboutButton.getHeight());

		quitButton.setX((manager.getWindowWidth() / 2) - (quitButton.getWidth() / 2));
		quitButton.setY(optionsButton.getY() + optionsButton.getHeight());
		
		
		//difficulty menu
		easyButton.setX(-easyButton.getWidth());
		easyButton.setY(buttonStartY);
		easyButton.setTargetX(-easyButton.getWidth());
		easyButton.setTargetY(buttonStartY);

		hardButton.setX(manager.getWindowWidth() + hardButton.getWidth());
		hardButton.setY(buttonStartY);
		hardButton.setTargetX(manager.getWindowWidth() + hardButton.getWidth());
		hardButton.setTargetY(buttonStartY);

		difficultyToMainButton.setX((manager.getWindowWidth() / 2) - (difficultyToMainButton.getWidth() / 2));
		difficultyToMainButton.setY(easyButton.getY() + easyButton.getHeight());
		
		
		//about menu
		aboutLabel.setX((manager.getWindowWidth() / 2) - (aboutLabel.getWidth() / 2));
		aboutLabel.setY((manager.getWindowHeight() / 2) - (aboutLabel.getHeight() / 2));

		aboutToMainButton.setX((manager.getWindowWidth() / 2) - (aboutToMainButton.getWidth() / 2));
		aboutToMainButton.setY(aboutLabel.getY() + aboutLabel.getHeight());

		
		//quit menu
		yesQuitButton.setX(-yesQuitButton.getWidth());
		yesQuitButton.setY(buttonStartY);
		yesQuitButton.setTargetX(-yesQuitButton.getWidth());
		yesQuitButton.setTargetY(buttonStartY);
		
		noQuitButton.setX(manager.getWindowWidth() + noQuitButton.getWidth());
		noQuitButton.setY(buttonStartY);
		noQuitButton.setTargetX(manager.getWindowWidth() + noQuitButton.getWidth());
		noQuitButton.setTargetY(buttonStartY);

		
		//options menu
		fullscreenButton.setX((manager.getWindowWidth() / 2) - (fullscreenButton.getWidth() / 2));
		fullscreenButton.setY(buttonStartY);
		
		yesFullscreen.setX(-yesFullscreen.getWidth());
		yesFullscreen.setY(fullscreenButton.getY() + fullscreenButton.getHeight());
		yesFullscreen.setTargetX(-yesFullscreen.getWidth());
		yesFullscreen.setTargetY(fullscreenButton.getY() + fullscreenButton.getHeight());

		noFullscreen.setX(manager.getWindowWidth() + noFullscreen.getWidth());
		noFullscreen.setY(fullscreenButton.getY() + fullscreenButton.getHeight());
		noFullscreen.setTargetX(manager.getWindowWidth() + noFullscreen.getWidth());
		noFullscreen.setTargetY(fullscreenButton.getY() + fullscreenButton.getHeight());
		
		optionsToMainButton.setX((manager.getWindowWidth() / 2) - (optionsToMainButton.getWidth() / 2));
		optionsToMainButton.setY(fullscreenButton.getY() + fullscreenButton.getHeight());
		optionsToMainButton.setTargetX((manager.getWindowWidth() / 2) - (optionsToMainButton.getWidth() / 2));
		optionsToMainButton.setTargetY(fullscreenButton.getY() + fullscreenButton.getHeight());
		
		
		//logos
		logo.setY(-logo.getHeight() - LOGO_HIDE);
		logo.setX((manager.getWindowWidth() / 2) - (logo.getWidth() / 2));
		logo.setTargetY(0);
		logo.setTargetX((manager.getWindowWidth() / 2) - (logo.getWidth() / 2));
		
		optionsLogo.setY(-optionsLogo.getHeight() - LOGO_HIDE);
		optionsLogo.setX((manager.getWindowWidth() / 2) - (logo.getWidth() / 2));
		optionsLogo.setTargetY(-optionsLogo.getHeight() - LOGO_HIDE);
		optionsLogo.setTargetX((manager.getWindowWidth() / 2) - (logo.getWidth() / 2));

		difficultyLogo.setY(-difficultyLogo.getHeight() - LOGO_HIDE);
		difficultyLogo.setX((manager.getWindowWidth() / 2) - (difficultyLogo.getWidth() / 2));
		difficultyLogo.setTargetY(-difficultyLogo.getHeight() - LOGO_HIDE);
		difficultyLogo.setTargetX((manager.getWindowWidth() / 2) - (difficultyLogo.getWidth() / 2));

		quitLogo.setY(-quitLogo.getHeight() - LOGO_HIDE);
		quitLogo.setX((manager.getWindowWidth() / 2) - (quitLogo.getWidth() / 2));
		quitLogo.setTargetY(-quitLogo.getHeight() - LOGO_HIDE);
		quitLogo.setTargetX((manager.getWindowWidth() / 2) - (quitLogo.getWidth() / 2));
		
		resMoved = false;
		optionsBackMoved = false;
	}
	
	/**
	 * Initialises all the menu state's menus.
	 *
	 */
	private void initMenus()
	{
		//main menus
		menus = new Vector<Menu>();
		mainMenu = new FadingMenu();
		menus.add(mainMenu);
		mainMenu.addItem(newGameButton);
		mainMenu.addItem(aboutButton);
		mainMenu.addItem(optionsButton);
		mainMenu.addItem(quitButton);
				
		//difficulty menu
		difficultyMenu = new FadingMenu();
		menus.add(difficultyMenu);
		difficultyMenu.addItem(difficultyToMainButton);
		difficultyMenu.addItem(easyButton);
		difficultyMenu.addItem(hardButton);
		
		//about menu
		aboutMenu = new FadingMenu();
		menus.add(aboutMenu);
		aboutMenu.addItem(aboutLabel);
		aboutMenu.addItem(aboutToMainButton);
		
		//options menu
		optionsMenu = new FadingMenu();
		menus.add(optionsMenu);
		optionsMenu.addItem(fullscreenButton);
		optionsMenu.addItem(optionsToMainButton);
		optionsMenu.addItem(yesFullscreen);
		optionsMenu.addItem(noFullscreen);
		
		//quit menu
		quitMenu = new FadingMenu();
		menus.add(quitMenu);
		quitMenu.addItem(yesQuitButton);
		quitMenu.addItem(noQuitButton);
		
		//activate the first menu the user should see
		mainMenu.activate();
		mainMenu.setVisible(true);
	}
	
	/** 
	 * Initializes all the other items in the menu.
	 *
	 */
	private void initOther()
	{
		Sprite logoSprite = null;
		Sprite optionsSprite = null;
		Sprite aboutSprite = null;
		Sprite diffSprite = null;
		Sprite quitSprite = null;
		
		if(manager.getRenderingDevice() == Window.JAVA_WINDOW)
		{
			logoSprite = new JavaSprite("images/gmPxalt.png");
			optionsSprite = new JavaSprite("images/optionsbig.png");
			aboutSprite = new JavaSprite("images/aboutus.png");
			diffSprite = new JavaSprite("images/difficultybig.png");
			quitSprite = new JavaSprite("images/areyousure.png");
		}
		else
			assert false; //TODO

		logo = new MovingLabel(logoSprite);
		logo.setWidth(logoSprite.getWidth());
		logo.setHeight(logoSprite.getHeight());
		
		optionsLogo = new MovingLabel(optionsSprite);
		optionsLogo.setWidth(optionsSprite.getWidth());
		optionsLogo.setHeight(optionsSprite.getHeight());
		
		difficultyLogo = new MovingLabel(diffSprite);
		difficultyLogo.setWidth(optionsSprite.getWidth());
		difficultyLogo.setHeight(optionsSprite.getHeight());
		
		quitLogo = new MovingLabel(quitSprite);
		quitLogo.setWidth(quitSprite.getWidth());
		quitLogo.setHeight(quitSprite.getHeight());
		
		aboutLabel = new Label(aboutSprite);
		aboutLabel.setVisible(true);
		aboutLabel.setWidth(aboutSprite.getWidth());
		aboutLabel.setHeight(aboutSprite.getHeight());
	}
	
	/**
	 * Reinitializes the menu.
	 *
	 */
	public void reinit()
	{
		initMenus();
		setPositions();
	}

	public void update()
	{		
		logo.update();
		optionsLogo.update();
		difficultyLogo.update();
		quitLogo.update();
		
		for(int x = 0, n = menus.size(); x < n; x++)
			menus.get(x).update();
		
		checkInput();
	}	
	
	public void render(Renderer renderer)
	{
		renderer.fillBackground(backColour);
		logo.render(renderer);
		optionsLogo.render(renderer);
		difficultyLogo.render(renderer);
		quitLogo.render(renderer);
		
		for(int x = 0, n = menus.size(); x < n; x++)
			if(menus.get(x).isVisible())
				menus.get(x).render(renderer);
	}
	
	public void enter()
	{
        manager.getRenderer().setCamera(camera);
		camera.setWidth(manager.getWindowWidth());
		camera.setHeight(manager.getWindowHeight());
		camera.setScale(manager.getRenderer().getScaleWidth(), manager.getRenderer().getScaleHeight());
	}
	
	public void exit()
	{
		
	}
	
	/**
	 * Checks for user input and selects/invokes appropiate items.
	 *
	 */
	private void checkInput()
	{
		mouseX = input.getValue(Input.POINTER_X);
		mouseY = input.getValue(Input.POINTER_Y);
		selectItemAt(mouseX, mouseY);
		
		if(input.getValue(Input.MOUSE_ONE) == Input.RELEASED)
			canPress[Input.MOUSE_ONE] = true;
		else if(canPress[Input.MOUSE_ONE])
		{
			mouseX = input.getValue(Input.POINTER_X);
			mouseY = input.getValue(Input.POINTER_Y);
			selectItemAt(mouseX, mouseY);
			
			confirmPressed();
			canPress[Input.MOUSE_ONE] = false;
		}
		
		//TODO keyboard input too!
		//TODO if user presses a key only take keyboard input until they move the mouse
		//TODO this is so moving items will only be selected while they are under the mouse
		
		/*
		if(input.getValue(Input.DOWN) == Input.RELEASED)
			canPress[Input.DOWN] = true;
		else if(canPress[Input.DOWN])
		{
			nextPressed();
			canPress[Input.DOWN] = false;
		}
		
		if(input.getValue(Input.UP) == Input.RELEASED)
			canPress[Input.UP] = true;
		else if(canPress[Input.UP])
		{
			previousPressed();
			canPress[Input.UP] = false;
		}
		
		
		if(input.getValue(Input.CONFIRM) == Input.RELEASED)
		{
			canPress[Input.CONFIRM] = true;
		}
		else if(canPress[Input.CONFIRM])
		{
			confirmPressed();
			canPress[Input.CONFIRM] = false;
		}
		

		*/
	}
	
	/**
	 * Selects the menu item at the specified coordinates.
	 * 
	 * @param x x coordinate to select the item at.
	 * @param y y coordinate to select the item at.
	 */
	private void selectItemAt(int x, int y)
	{
		for(int i = 0, n = menus.size(); i < n; i++)
			if(menus.get(i).isActive())
				menus.get(i).selectItemAt(x, y);
	}

	/**
	 * Tells all active menus to set their next item active.
	 * Generally there should only be one active menu.
	 */
	private void nextPressed()
	{
		for(int x = 0, n = menus.size(); x < n; x++)
			if(menus.get(x).isActive())
				menus.get(x).nextItem();
	}
	
	/**
	 * Tells all active menus to set their previous item active.
	 * Generally there should only be one active menu.
	 */
	private void previousPressed()
	{
		for(int x = 0, n = menus.size(); x < n; x++)
			if(menus.get(x).isActive())
				menus.get(x).prevItem();
	}
	
	/**
	 * Presses the selected item.
	 *
	 */
	private void confirmPressed()
	{
//		for(int x = 0, n = menus.size(); x < n; x++)
//			if(menus.get(x).isActive())
//				menus.get(x).pressItem();
		Vector<Menu> active = new Vector<Menu>();
		for(int x = 0, n = menus.size(); x < n; x++)
			if(menus.get(x).isActive())
				active.add(menus.get(x));
		
		for(int x = 0, n = active.size(); x < n; x++)
			active.get(x).pressItem();
	}
	
	/**
	 * Releases the selected item.
	 *
	 */
	private void cancelPressed()
	{
		for(int x = 0, n = menus.size(); x < n; x++)
			if(menus.get(x).isActive())
				menus.get(x).releaseItem();
	}
	
	public void itemPressed(MenuItem item)
	{
		if(item == newGameButton)
		{
			mainMenu.deactivate();
			difficultyMenu.activate();
			easyButton.setTargetX((manager.getWindowWidth() / 2) - easyButton.getWidth());
			hardButton.setTargetX((manager.getWindowWidth() / 2));
			
			logo.setTargetY(-logo.getHeight() - LOGO_HIDE);
			difficultyLogo.setTargetY(0);
		}
		else if(item == aboutButton)
		{
			logo.setTargetY(-logo.getHeight());
			mainMenu.deactivate();
			aboutMenu.activate();
		}
		else if(item == aboutToMainButton)
		{
			logo.setTargetY(0);
			aboutMenu.deactivate();
			mainMenu.activate();
		}
		else if(item == optionsToMainButton)
		{
			optionsMenu.deactivate();
			mainMenu.activate();
			logo.setTargetY(0);
			optionsLogo.setTargetY(-optionsLogo.getHeight() - LOGO_HIDE);
			
			if(resMoved)
			{
				optionsToMainButton.setTargetY(optionsToMainButton.getTargetY() - fullscreenButton.getHeight());
				yesFullscreen.setTargetX(-yesFullscreen.getWidth());
				noFullscreen.setTargetX(manager.getWindowWidth() + noFullscreen.getWidth());
				resMoved = false;
			}
			
			if(optionsBackMoved)
			{
				optionsToMainButton.setTargetY(fullscreenButton.getY() + fullscreenButton.getHeight());
				optionsBackMoved = false;
			}
			
		}
		else if(item == difficultyToMainButton)
		{
			difficultyMenu.deactivate();
			mainMenu.activate();
			easyButton.setTargetX(-easyButton.getWidth());
			hardButton.setTargetX(manager.getWindowWidth() + hardButton.getWidth());
		
			logo.setTargetY(0);
			difficultyLogo.setTargetY(-difficultyLogo.getHeight() - LOGO_HIDE);
		}
		else if(item == optionsButton)
		{
			optionsMenu.activate();
			mainMenu.deactivate();
			logo.setTargetY(-logo.getHeight() - LOGO_HIDE);
			optionsLogo.setTargetY(0);
		}
		else if(item == quitButton)
		{
			quitMenu.activate();
			mainMenu.deactivate();
			
			quitLogo.setTargetY(0);
			logo.setTargetY(-logo.getHeight() - LOGO_HIDE);
			
			yesQuitButton.setTargetX((manager.getWindowWidth() / 2) - yesQuitButton.getWidth());
			noQuitButton.setTargetX((manager.getWindowWidth() / 2));
		}
		else if(item == yesQuitButton)
		{
			manager.quit();
		}
		else if(item == noQuitButton)
		{
			quitMenu.deactivate();
			mainMenu.activate();

			quitLogo.setTargetY(-quitLogo.getHeight() - LOGO_HIDE);
			logo.setTargetY(0);
			
			yesQuitButton.setTargetX(-yesQuitButton.getWidth());
			noQuitButton.setTargetX(manager.getWindowWidth() + noQuitButton.getWidth());
		}
		else if(item == fullscreenButton)
		{
			if(!resMoved)
			{
				optionsToMainButton.setTargetY(optionsToMainButton.getTargetY() + optionsToMainButton.getHeight());
				yesFullscreen.setTargetX((manager.getWindowWidth() / 2) - yesFullscreen.getWidth());
				noFullscreen.setTargetX(manager.getWindowWidth() / 2);
				
				resMoved = true;
				optionsBackMoved = true;
			}
		}
		else if(item == yesFullscreen)
		{
			manager.setFullscreen(true);
		}
		else if(item == noFullscreen)
		{
			manager.setFullscreen(false);
		}
		else if(item == easyButton)
		{
			//FIXME
			manager.newGame(0);
		}
		else if(item == hardButton)
		{
			//FIXME
			manager.newGame(0);
		}
	}

	public void itemReleased(MenuItem items)
	{
		
	}
}
