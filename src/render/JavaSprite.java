package render;

import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.Game;

public class JavaSprite implements Sprite
{
	/** The sprite's image */
	private BufferedImage image;
	
	public JavaSprite(File file)
	{
		loadSprite(file);
	}
	
	public JavaSprite(String fileName)
	{
		loadSprite(new File(fileName));
	}
	
	private void loadSprite(File file)
	{
		try
		{
			BufferedImage tempImg = ImageIO.read(file);

			/*
			 * Create an image that is compatible with the current graphics
			 * environment. This saves it being done on the fly during rendering.
			 */
			image = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration().createCompatibleImage(
							tempImg.getWidth(), tempImg.getHeight(),
							Transparency.TRANSLUCENT);
			
			//draw our original image to our compatible image
			image.getGraphics().drawImage(tempImg, 0, 0, null);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public int getHeight()
	{
		return image.getHeight() << Game.CONVERSION_FACTOR;
	}

	public int getWidth()
	{
		return image.getWidth() << Game.CONVERSION_FACTOR;
	}

	public int getType()
	{
		return JAVA_SPRITE;
	}

}
