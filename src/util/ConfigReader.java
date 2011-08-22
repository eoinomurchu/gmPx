package util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

import world.Camera;
import world.World;
import entity.CircleEntity;
import entity.Controller;
import entity.EelEntity;
import entity.FlockEntity;
import entity.FoodEntityLevel1;
import entity.FoodEntityLevel2;
import entity.FoodEntityLevel3;
import entity.FoodEntityLevel4;
import entity.FoodEntityLevel5;
import entity.HostileAIController;
import entity.LevelChangerEntity;
import entity.NonHostileAIController;


public class ConfigReader
{
	private final String EASY_FILE_NAME = "easy.conf";
	private final String HARD_FILE_NAME = "hard.conf";
	private final int NO_OF_LEVELS = 20;
	private final int AREA_WIDTH = 1024;
	private final int AREA_HEIGHT = 768;

	private int level;
	private int eels;
	private int eelSegs;
	private int eelEvolver;
	private int circles;
	private int circleSegs;
	private int circleEvolver;
	private int rays;
	private int raySegs;
	private int rayEvolver;
	private int clouds;
	private int cloudSegs;
	private int cloudEvolver;
	private int level1;
	private int level2;
	private int level3;
	private int level4;
	private int level5;
	private int ups;
	private int downs;
	private int endGame;
	private String[] configLines;

	private Controller controller;

	Random rx;
	Random ry;
	int rangex;
	int rangey;


	{
		configLines = new String[NO_OF_LEVELS];

		rx = new Random();
		ry = new Random();

		rangex = 2*AREA_WIDTH;
		rangey = 2*AREA_HEIGHT;
	}


	public ConfigReader(World[] worlds, Camera camera, boolean isEasy) 
	{
		getConfig(isEasy ? EASY_FILE_NAME : HARD_FILE_NAME);
		populate(worlds, camera);
	}

	private void getConfig(String file) {

		int count = 0;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String input = reader.readLine();
			while (input != null) {
				configLines[count++] = input;
				input = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			System.out.println("Problem reading from Config file");
			e.printStackTrace();
			System.exit(-1);
		}
	}       

	private void setVars(String configLine)
	{
		Scanner scanner = new Scanner(configLine);
		scanner.next();
		level = scanner.nextInt();
		scanner.next();
		eels = scanner.nextInt();
		scanner.next();
		eelSegs = scanner.nextInt();
		scanner.next();
		eelEvolver = scanner.nextInt();
		scanner.next();
		circles = scanner.nextInt();
		scanner.next();
		circleSegs = scanner.nextInt();
		scanner.next();
		circleEvolver = scanner.nextInt();
		scanner.next();
		rays = scanner.nextInt();
		scanner.next();
		raySegs = scanner.nextInt();
		scanner.next();
		rayEvolver = scanner.nextInt ();
		scanner.next();
		clouds = scanner.nextInt();
		scanner.next();
		cloudSegs = scanner.nextInt();
		scanner.next();
		cloudEvolver = scanner.nextInt();
		scanner.next();
		level1 = scanner.nextInt();
		scanner.next();
		level2 = scanner.nextInt();
		scanner.next();
		level3 = scanner.nextInt();
		scanner.next();
		level4 = scanner.nextInt();
		scanner.next();
		level5 = scanner.nextInt();
		scanner.next();
		ups = scanner.nextInt();
		scanner.next();
		downs = scanner.nextInt();
		scanner.next();
		endGame = scanner.nextInt();
	}

	public void populate(World[] worlds, Camera camera)
	{
		for(int x = 0, i = 0; x < NO_OF_LEVELS; x++)
		{
			setVars(configLines[x]);
			for(i = 0; i < eels; i++)
			{
				EelEntity eel = new EelEntity(((rx.nextInt(rangex)-AREA_WIDTH/2)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				eel.setController(new HostileAIController(eel));
				worlds[level].addEntity(eel);
				eel.setWorld(worlds[level]);
				
				for (int j = 0; j < eelSegs; j++)
					eel.addFat(50, true);
				
			}
			for(i = 0; i < circles; i++)
			{
				CircleEntity circle = new CircleEntity( (rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				circle.setController(new HostileAIController(circle));
				worlds[level].addEntity(circle);
				circle.setWorld(worlds[level]);
			}
			for(i = 0; i < circles; i++)
			{
				FlockEntity flock = new FlockEntity( (rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				flock.setController(new HostileAIController(flock));
				worlds[level].addEntity(flock);
				flock.setWorld(worlds[level]);
			}
			for(i = 0; i < level1; i++)
			{
				FoodEntityLevel1 food = new FoodEntityLevel1( (rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				food.setController(new NonHostileAIController(food));
				worlds[level].addEntity(food);
				food.setWorld(worlds[level]);
			}
			for(i = 0; i < level2; i++)
			{
				FoodEntityLevel2 food = new FoodEntityLevel2((rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				food.setController(new NonHostileAIController(food));
				worlds[level].addEntity(food);
				food.setWorld(worlds[level]);
			}
			for(i = 0; i < level3; i++)
			{
				FoodEntityLevel3 food = new FoodEntityLevel3( (rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				food.setController(new NonHostileAIController(food));
				worlds[level].addEntity(food);
				food.setWorld(worlds[level]);
			}
			for(i = 0; i < level4; i++)
			{
				FoodEntityLevel4 food = new FoodEntityLevel4((rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				food.setController(new NonHostileAIController(food));
				worlds[level].addEntity(food);
				food.setWorld(worlds[level]);
			}
			for(i = 0; i < level5; i++)
			{
				FoodEntityLevel5 food = new FoodEntityLevel5( (rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				food.setController(new NonHostileAIController(food));
				worlds[level].addEntity(food);
				food.setWorld(worlds[level]);
			}
			if(ups > 0)
			{
				LevelChangerEntity levelC = new LevelChangerEntity((rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				levelC.setCamera(camera);
				levelC.setController(new NonHostileAIController(levelC));
				levelC.setValue(-1);
				worlds[level].addEntity(levelC);
				worlds[level].addLevelChanger(levelC);
				levelC.setWorld(worlds[level]);
			}
			if(downs > 0)
			{
				LevelChangerEntity levelC = new LevelChangerEntity((rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				levelC.setCamera(camera);
				levelC.setController(new NonHostileAIController(levelC));
				levelC.setValue(1);
				worlds[level].addEntity(levelC);
				worlds[level].addLevelChanger(levelC);
				levelC.setWorld(worlds[level]);
			}
			if(endGame > 0)
			{
				LevelChangerEntity levelC = new LevelChangerEntity((rx.nextInt(rangex)-AREA_WIDTH/2)<<5, (ry.nextInt(rangey)-AREA_HEIGHT/2)<<5);
				levelC.setCamera(camera);
				levelC.setController(new NonHostileAIController(levelC));
				levelC.setValue(-19);
				worlds[level].addEntity(levelC);
				worlds[level].addLevelChanger(levelC);
				levelC.setWorld(worlds[level]);
			} 
		}
	}
}
