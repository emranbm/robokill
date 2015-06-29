package robokill;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;

/**
 * 
 * @author HRM_SHAMS
 * @version 1.3
 */
public class GameMap extends JPanel{

	private static final long serialVersionUID = 1L;
	
	BufferedImage mapImage;
	
	boolean isShowed = false; //when map showed this boolean will be true and when it hided this will be false!
	boolean isMoving = false; //while moving the panel this will be true and after it will be false!
	
	/*Every where of this class we only declare an object of class CurrentHome
	 *and setLocation it in method showCurrentRoom! 
	 **/
	CurrentHome currentHome = new CurrentHome(new Point(-500,-500)); 

	/**
	 * we save location of homes in map with static Point objects!
	 */
	public static Point Room_Location_0 = new Point(100,217);
	public static Point Room_Location_5 = new Point(174,217);
	public static Point Room_Location_21 = new Point(248,217);
	public static Point Room_Location_31 = new Point(248,269);
	public static Point Room_Location_61 = new Point(248,321);
	public static Point Room_Location_1 = new Point(248,164);
	public static Point Room_Location_62 = new Point(174,164);
	public static Point Room_Location_32 = new Point(247,113);
	public static Point Room_Location_71 = new Point(247,62);
	public static Point Room_Location_63 = new Point(322,164);
	public static Point Room_Location_8 = new Point(397,164);
	public static Point Room_Location_72 = new Point(397,217);
	public static Point Room_Location_22 = new Point(470,164);
	public static Point Room_Location_23 = new Point(396,113);
	
	public GameMap(){
		setLocation(188,-421);
		setSize(624, 421);
		setOpaque(false);
		
		add(currentHome);
		
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
					setLocation(188	, getY() + 3);
					
					try{
					Thread.sleep(4);
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
					setLocation(188	, getY() - 3);

					try{
					Thread.sleep(4);
					}catch(Exception e){System.out.println("Erron in sleeping!");}
				}
				isMoving = false;
				isShowed = false;
			}
		}.start();		
	}

	/**
	 * 
	 */
	private Point convertIdToLocation(int homeID)
	{
		Point homeLocation;
		switch(homeID)
		{
		case 0 :
			homeLocation = this.Room_Location_0;
		break;

		case 5 :
			homeLocation = this.Room_Location_5;
		break;
		
		case 21 :
			homeLocation = this.Room_Location_21;
		break;
		
		case 31 :
			homeLocation = this.Room_Location_31;
		break;

		case 61 :
			homeLocation = this.Room_Location_61;
		break;
		
		case 1 :
			homeLocation = this.Room_Location_1;
		break;
		
		case 62 :
			homeLocation = this.Room_Location_62;
		break;
		
		case 32 :
			homeLocation = this.Room_Location_32;
		break;
		
		case 71 :
			homeLocation = this.Room_Location_71;
		break;
		
		case 63 :
			homeLocation = this.Room_Location_63;
		break;
		
		case 8 :
			homeLocation = this.Room_Location_8;
		break;
		
		case 72 :
			homeLocation = this.Room_Location_72;
		break;
		
		case 23 :
			homeLocation = this.Room_Location_23;
		break;
		
		case 22 :
			homeLocation = this.Room_Location_22;
		break;

		default:
			homeLocation = new Point();
			System.out.println("invalid home id!");
		}
		return homeLocation;
	}	

	/**
	 * in the map must be shown the current room that player is there!
	 * @param homeLocation
	 */
	public void setCurrentRoom(int homeID)
	{
		Point location = convertIdToLocation(homeID);
		currentHome.setLocation(location);
	}
	
	/**
	 * when player entered a room , that room must showed as occupied in map.
	 * this method do this work!!
	 * @param homeLocation
	 */
	public void setHomeOccupied(int homeID)
	{
		Point location = convertIdToLocation(homeID);
		System.out.println(location);
		HomeOccupied a = new HomeOccupied(location);
		add (a);
	}
	
	
	/**
	 * when player entered a room , that room must showed as occupied in map.
	 * this panel shows occupied home!
	 * @author HRM_SHAMS
	 */
	class HomeOccupied extends JPanel{
		
		BufferedImage homeOccupied;
		
		public HomeOccupied(Point location)
		{
			setLocation(location);
			setSize(61, 41);
			
			try{
				homeOccupied = ImageIO.read(getClass().getResource("/images/mapHomeOccupied.png"));
			}catch(Exception e){System.out.println("Error in reading file!");}
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			g.drawImage(homeOccupied,0,0,null);
		}
	}
	
	/**
	 * used for showing current home in map!
	 * @author HRM_SHAMS
	 */
	class CurrentHome extends JPanel{

		BufferedImage currentHome;
		
		public CurrentHome(Point location)
		{
			setLocation(location);
			setSize(61, 41);
			
			try{
				currentHome = ImageIO.read(getClass().getResource("/images/currentHome.png"));
			}catch(Exception e){System.out.println("Error in reading file!");}
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			g.drawImage(currentHome,0,0,null);
		}
		
	}
}
