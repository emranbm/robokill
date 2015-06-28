package robokill;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * @author HRM_SHAMS
 * @version 1.1
 */
public class GameMap extends JPanel{

	BufferedImage mapImage;
	boolean isShowed = false; //when map showed this boolean will be true and when it hided this will be false!
	boolean isMoving = false; //while moving the panel this will be true and after it will be false!
	
	public GameMap(){
		setLocation(188,-421);
		setSize(624, 421);
		setOpaque(false);
		
		try{
		mapImage = ImageIO.read(getClass().getResource("/images/map.png"));
		}catch(Exception e){System.out.println("Error in reading file!");}
		
	}
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(mapImage, 0, 0, null);
	}
	
	/**
	 * this method used in GamePanel class!
	 * when user press M then GamePanel call this method!
	 */
	public void showHideMap()
	{
		if (!isMoving)
		{
			if (isShowed)
				hideMap();
			else
				showMap();
		}
	}
	/**
	 * this method with a thread setLocation the panel of map! and down it!
	 */
	private void showMap()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				isMoving = true;
				while (getY() != 140)
				{
					setLocation(188	, getY() + 1);
					
					try{
					Thread.sleep(1);
					}catch(Exception e){System.out.println("Erron in sleeping!");}
				}
				isMoving = false;
				isShowed = true;
			}
		}.start();
	}
	
	/**
	 * this method with a thread setLocation the panel of map until entirely hide!
	 */
	private void hideMap()
	{
		new Thread()
		{
			public void run()
			{
				isMoving = true;
				while (getY() != -421)
				{
					setLocation(188	, getY() - 1);

					try{
					Thread.sleep(1);
					}catch(Exception e){System.out.println("Erron in sleeping!");}
				}
				isMoving = false;
				isShowed = false;
			}
		}.start();		
	}
	
}
