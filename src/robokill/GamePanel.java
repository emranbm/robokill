package robokill;

import static java.lang.Thread.sleep;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import useful.Animation;
import useful.Direction;
import useful.GlobalKeyListenerFactory;

/**
 * 
 * @author HRM_Shams
 * @author Mr. Coder
 * @version 1.9
 */

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// /////////////////////////////Static///////////////////////
	// //////////////////////////////phase////////////////////////

	private static GamePanel This;
	private static boolean isMaster;
	private static boolean isMultiPlayer;

	/**
	 * Creates an instance of {@link robokill.GamePanel GamePanel}. This method
	 * should just be called once, at the beginning of the game.
	 * 
	 * @param isMultiPlayer
	 *            To check if the game is multi player or not.
	 * @param isMaster
	 * @return The instantiated {@link robokill.GamePanel GamePanel}.
	 */
	public static GamePanel instantiate(GameFrame gameFrame , boolean isMultiPlayer, boolean isMaster) {
		GamePanel.isMaster = isMaster;
		GamePanel.isMultiPlayer = isMultiPlayer;
		System.out.println("GamePanel instantiated.");
		return This = new GamePanel(gameFrame);
	}

	public static GamePanel getGamePanel() {
		if (This == null) {
			System.err
					.println("getGamePanel() method in GamePanel class called before GamePanel being instantiated.");
			System.exit(1);
		}

		return This;
	}

	/**
	 * Determines whether the GamePanel is created (before) or not.
	 * 
	 * @return Returns true if the game panel is created and ready. Otherwise
	 *         false.
	 */
	public static boolean isGamePanelReady() {
		if (This == null)
			return false;
		else
			return true;
	}

	/**
	 * 
	 * @return true if this client is master. false if not.
	 */
	public static boolean isMaster() {
		return isMaster;
	}

	/**
	 * Determines that the game is multi player or single player.
	 * 
	 * @return true if multi player. false if single player.
	 */
	public static boolean isMultiPlayer() {
		return isMultiPlayer;
	}

	// /////////////////////////End of static/////////////////
	// /////////////////////////phase////////////////////////

	private GameFrame gameFrameRef;

	public Player playerRobot1; // the panel of playerRobot!
	public Player playerRobot2; // the second player (used in multiplayer!)
	private GameMap map;
	private Animation gameOver;
	private BufferedImage background;

	private boolean isShooting = false;
	private int curMouseX;
	private int curMouseY;

	private ArrayList<Element> elements = new ArrayList<Element>();
	private ArrayList<Door> doors = new ArrayList<Door>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Prize> prizes = new ArrayList<Prize>();
	private ArrayList<Box> boxes = new ArrayList<Box>();
	private Room currentRoom;

	private final Set<Integer> keys = new HashSet<Integer>(); // related to
																// movement of
																// robot!

	public boolean gameEnded = false;

	public StatusPanel statusPanel = new StatusPanel();

	private GamePanel(GameFrame gameFrameRef) {
		super();

		System.out.println("Start of Game panel constructor.");

		this.gameFrameRef = gameFrameRef;
		
		setBounds(0, 0, 1000, 700);

		setSize(new Dimension(1024, 768));
		setLayout(null);

		/** GameMap **/
		map = new GameMap();
		add(map);


		/** add GameOver Animation panel to GamePanel **/
		gameOver = new Animation(new Point(1, 0),
				new Dimension(1000,700),
				"/images/GameOverAnimation/",
				20,
				40,
				1,
				false);
		add(gameOver);

		/** status bar **/
		add(statusPanel);
		/**************/

		/** adding playerRobot to gamePanel **/

		if (isMaster) {
			playerRobot1 = new Player(100, 320, 60, 60, 6, 0,
					Player.Player_Type_1);
			add(playerRobot1);

			playerRobot2 = new Player(700, 320, 60, 60, 6, 1,
					Player.Player_Type_2);
			add(playerRobot2);
		} else {
			playerRobot1 = new Player(700, 320, 60, 60, 6, 1,
					Player.Player_Type_2);
			add(playerRobot1);

			playerRobot2 = new Player(100, 320, 60, 60, 6, 0,
					Player.Player_Type_1);
			add(playerRobot2);
		}
		
		/** adding mouseListener (for rotating head of robot and shooting) **/
		addMouseListenersForRobot();

		/*** adding keyListener for moving ***/
		addKeyListener(new MultiKeyPressListener());

		/** adding a new Thread for moving robot **/
		Thread robotMovement = new Thread(new RobotMovementHandler());
		robotMovement.start();

		/** adding a new thread for shooting bars **/
		Thread shootingBars = new Thread(new ShootingBarsHandler());
		shootingBars.start();

		/** adding elements to GamePanel **/
		addElements();

		// try {
		// InputStream in = getClass().getResourceAsStream("/data/room 0.dat");
		// ObjectInputStream ois = new ObjectInputStream(in);
		// currentRoom = (Room) ois.readObject();
		// ois.close();
		// rearrange(currentRoom);
		// } catch (IOException | ClassNotFoundException e) {
		// e.printStackTrace();
		// }

		tryOpeningTheDoors();

		System.out.println("End of Game panel constructor.");

	}// end of constructor !!

	/**
	 * Removes all the elements from the game panel and applies the given room
	 * properties.
	 * 
	 * @param room
	 *            The room for applying new properties.
	 */
	public void rearrange(Room room) {
		playerRobot1.setLocation(room.getPlayerLocation());

		background = room.getBackgroundImage();

		/* Remove all elements from gamePanel */
		int a = this.getComponentCount();
		for (int i = a - 1; i >= 0; i--) {
			Component comp = this.getComponent(i);
			if (comp instanceof Element)
				if (!(comp instanceof Player))
					remove(comp);
		}

		doors = room.getDoors();

		enemies = room.getEnemies();

		for (Door door : doors)
			door.revalidateImage();

		for (Element element : room.getElements()) {
			add(element);
			element.revalidateImage();

			if (element instanceof Enemy)
				((Enemy) element).go();
		}

		repaint();

		tryOpeningTheDoors();
	}

	/**
	 * Adds the elements to the game panel.
	 */
	private void addElements() {

		/** set roomId **/
		int roomId = 1;

		/** Add Valleys **/
		add(new Valley(81, 70, 295, 95));
		add(new Valley(81, 163, 330, 55));
		add(new Valley(163, 232, 215, 30));

		add(new Valley(635, 70, 300, 95));
		add(new Valley(585, 163, 336, 55));
		add(new Valley(635, 232, 208, 50));

		add(new Valley(163, 414, 205, 30));
		add(new Valley(82, 469, 330, 55));
		add(new Valley(79, 535, 285, 90));

		add(new Valley(630, 411, 205, 58));
		add(new Valley(580, 475, 336, 50));
		add(new Valley(630, 535, 285, 87));

		/** Add Block **/
		/*
		 * Block block = new Block(450, 300, Block.BLOCK_TYPE_1); add(block);
		 */
		/** Add Boxes **/
		/*
		 * add(new Box(96, 100)); add(new Box(153, 97)); add(new Box(103, 154));
		 * add(new Box(152, 147));
		 */

		/** Add Enemies **/
		Enemy enemy = new Enemy(460, 210, 80, 80, 2, Enemy.ENEMY_TYPE_2, 2);
		add(enemy);
		enemy.go();

		Enemy enemy2 = new Enemy(300, 310, 80, 80, 1, Enemy.ENEMY_TYPE_3, 3);
		add(enemy2);
		enemy2.go();
		/*
		 * Enemy enemy2 = new Enemy(800, 450, 80, 80, 1, Enemy.ENEMY_TYPE_1);
		 * add(enemy2); enemy2.go();
		 */

		/** Add doors **/
		Door door1 = new Door(25, 301, "1", 61);
		add(door1);
		doors.add(door1);

		Door door2 = new Door(451, 20, "2", 32);
		add(door2);
		doors.add(door2);

		Door door3 = new Door(933, 301, "3", 62);
		add(door3);
		doors.add(door3);

		Door door4 = new Door(451, 640, "4", 21);
		add(door4);
		doors.add(door4);

		/** set playerRobot location **/
		// Point playerLocation = new Point(451, 550);
		// playerRobot1.setLocation(playerLocation);

		/** set room background address **/
		String roomBackgroundAddress = "/images/rooms/1.png";

		try {
			background = ImageIO.read(getClass().getResource(
					roomBackgroundAddress));
		} catch (IOException e) {
			System.out.println("error in reading image!");
		}
		repaint();

		/**************************************************/
		/*** Making a new room with desired properties! ***/
		/****** (you don't need to change bottom code) ******/
		/**************************************************/
		Room room = new Room(roomId);

		for (Element element : elements)
			if (!(element instanceof Player))
				room.addElement(element);

		room.setDoors(doors);
		// room.setPlayerLocation(playerLocation);
		room.setBackgroundImagePath(roomBackgroundAddress);

		// try {
		// ObjectOutputStream oos = new ObjectOutputStream(
		// new
		// FileOutputStream("C:/Users/h-noori/Desktop/rooms/room "+roomId+".dat"));
		// oos.writeObject(room);
		// oos.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * when health of robot<=0 this method will be called by statusPanel method!
	 */
	public void gameOver()
	{
		gameOver.start();
		
		try{
		Thread.sleep(3000);
		}catch(Exception e){}
		
		gameFrameRef.remove(this);
		gameFrameRef.add(MainMenu.getMainMenu());
		
		gameFrameRef.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, this);
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);

		repaint();

		if (comp instanceof Element) {
			elements.remove(comp);

			if (comp instanceof Enemy) {
				enemies.remove(comp);
				tryOpeningTheDoors();
			} else if (comp instanceof Prize)
				prizes.remove(comp);
			else if (comp instanceof Box)
				boxes.remove(comp);
		}
	}

	@Override
	public Component add(Component comp) {
		if (comp instanceof Element) {
			if (!(comp instanceof Bar))
				elements.add((Element) comp);

			if (comp instanceof Enemy)
				enemies.add((Enemy) comp);
			else if (comp instanceof Prize)
				prizes.add((Prize) comp);
			else if (comp instanceof Box)
				boxes.add((Box) comp);
		}

		return super.add(comp);
	}

	/**
	 * Checks if the given element is collided with an element or not. If it is
	 * collided with an element, that element is returned. Otherwise null is
	 * returned.
	 * 
	 * @param checkElement
	 *            The element to check if any other element has collided with it
	 *            or not.
	 * @return Returns the element that is collided with the given element.
	 *         Returns null if no collision has occurred.
	 */
	public synchronized Element getCollidedElement(Element checkElement) {
		for (Element e : elements) {
			if (checkElement.isCollided(e) && checkElement != e)
				return e;
		}

		return null;
	}

	/**
	 * Checks if the given element is collided with an element in the next pace
	 * or not. If it is collided with an element in the next pace, that element
	 * is returned. Otherwise null is returned.
	 * 
	 * @param element
	 *            The element to check if any other element has collided with it
	 *            or not.
	 * @param nextLocation
	 *            The new location of the element to check if collision has
	 *            happened.
	 * @return Returns the element that is collided with the given element.
	 *         Returns null if no collision has occurred.
	 */
	public Element getCollidedElement(Element element, Point nextLocation) {

		Dimension size = element.getSize();

		for (Element e : elements) {
			if (e.isCollided(nextLocation, size) && element != e)
				return e;
		}

		return null;
	}

	/**
	 * Determines that whether the element is inside the bounds of the GamePanel
	 * or not. {@link robokill.GamePanel#isElementInside(Point, Dimension)
	 * isElementInside(Point, Dimension)}
	 * 
	 * @param element
	 * @return Returns true if the element is inside the bounds, otherwise
	 *         false.
	 */
	public boolean isElementInside(Element element) {
		return isElementInside(element.getLocation(), element.getSize());
	}

	/**
	 * Determines that whether the element is inside the bounds of the GamePanel
	 * or not.
	 * 
	 * @param element
	 * @return Returns true if the element is inside the bounds, otherwise
	 *         false.
	 */
	public boolean isElementInside(Point elementLoc, Dimension elementSize) {
		if (elementLoc.x >= 0
				&& elementLoc.x + elementSize.getWidth() <= this.getWidth()
				&& elementLoc.y >= 0
				&& elementLoc.y + elementSize.getHeight() <= this.getHeight())
			return true;
		else
			return false;
	}

	/**
	 * Returns the prize that has the given id.
	 * 
	 * @param id
	 *            The id of a prize to search for.
	 * @return The prize that its id is equal to the given id. If such prize is
	 *         not found the return value will be null.
	 */
	public Prize getPrizeById(int id) {
		for (Prize p : prizes)
			if (p.getId() == id)
				return p;

		return null;
	}

	public Box getBoxById(int id) {
		for (Box b : boxes)
			if (b.getId() == id)
				return b;

		return null;
	}

	/**
	 * Returns the enemy that has the given id.
	 * 
	 * @param id
	 *            The id of an enemy to serach for.
	 * @return The enemy that its id is equal to the given id. If such enemy is
	 *         not found the return value will be null.
	 */
	public Enemy getEnemyById(int id) {

		for (Enemy en : enemies)
			if (en.getId() == id)
				return en;

		return null;
	}

	// TODO Check if the key is achieved, then open.
	// Otherwise do nothing.
	/**
	 * Opens all of the doors, if all the enemies are killed. Otherwise does
	 * nothing.
	 */
	public void tryOpeningTheDoors() {
		if (enemies.size() == 0)
			for (Door door : doors)
				door.open();
	}

	/**
	 * Gets the current room id. Id is a unique integer to make rooms
	 * distinguishable. w
	 * 
	 * @return Returns the current room id.
	 */
	public int getRoomId() {
		return currentRoom.getId();
	}

	/**
	 * this method has MouseListener and MouseMotionListener ! this do 2 works :
	 * 1) get the coordinate of mouse when it moved or dragged and call method
	 * setTarget in class Robot 1) get the coordinate of mouse when it pressed
	 * or dragged and isShooting will be true to shoot the bars in a thread!
	 */
	private void addMouseListenersForRobot() {
		/*** add mouse listener ***/
		// 1
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {/* Do Nothing */
			}

			@Override
			public void mousePressed(MouseEvent e) {
				curMouseX = e.getX();
				curMouseY = e.getY();
				playerRobot1.robotHead
						.setTarget(new Point(curMouseX, curMouseY));
				playerRobot1.robotHead.repaint();
				isShooting = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				isShooting = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {/* Do Nothing */
			}

			@Override
			public void mouseExited(MouseEvent e) {/* Do Nothing */
			}
		});

		// 2
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				curMouseX = e.getX();
				curMouseY = e.getY();

				playerRobot1.robotHead
						.setTarget(new Point(curMouseX, curMouseY));
				playerRobot1.robotHead.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int mouseX = e.getX() - 3;
				int mouseY = e.getY() - 25;

				/*** rotating the head of Robot!! ***/
				playerRobot1.robotHead.setTarget(new Point(mouseX, mouseY));
				playerRobot1.robotHead.repaint();
			}
		}); // end of mouse listener
	}

	/*** a new class in GamePanel class for keyListener **/
	class MultiKeyPressListener implements KeyListener {

		public MultiKeyPressListener() {
			GlobalKeyListenerFactory.setKeyListenerGlobal(this);

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_W
					|| keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_D)
				keys.add(keyCode);

			// adding key listener for show and hiding map
			if (keyCode == KeyEvent.VK_M) {
				map.showHideMap();
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_W
					|| keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_D)
				keys.remove(keyCode);
		}

		@Override
		public void keyTyped(KeyEvent e) {/* Not used */
		}
	}

	/*******/

	/*** RobotMovementClass ***/
	class RobotMovementHandler implements Runnable {
		@Override
		public void run() {
			while (!gameEnded) {
				if (keys.contains(KeyEvent.VK_W)
						&& keys.contains(KeyEvent.VK_D)) {
					playerRobot1.move(Direction.North_East, false);
				}

				else if (keys.contains(KeyEvent.VK_W)
						&& keys.contains(KeyEvent.VK_A)) {
					playerRobot1.move(Direction.North_West, false);
				}

				else if (keys.contains(KeyEvent.VK_S)
						&& keys.contains(KeyEvent.VK_D)) {
					playerRobot1.move(Direction.South_East, false);
				}

				else if (keys.contains(KeyEvent.VK_S)
						&& keys.contains(KeyEvent.VK_A)) {
					playerRobot1.move(Direction.South_West, false);
				}

				else if (keys.contains(KeyEvent.VK_W)) {
					playerRobot1.move(Direction.North, false);
				}

				else if (keys.contains(KeyEvent.VK_S)) {
					playerRobot1.move(Direction.South, false);
				}

				else if (keys.contains(KeyEvent.VK_A)) {
					playerRobot1.move(Direction.West, false);
				}

				else if (keys.contains(KeyEvent.VK_D)) {
					playerRobot1.move(Direction.East, false);
				}
				try {
					sleep(7);
				} catch (Exception e) {
				}
			}
		}
	}

	/*******/

	/**
	 * related to mouseListeners!
	 */
	class ShootingBarsHandler implements Runnable {
		public void run() {
			while (!gameEnded) {
				if (isShooting) {
					int mouseX = curMouseX;
					int mouseY = curMouseY;

					// rotating the head of Robot!!
					Point target = new Point(mouseX, mouseY);

					// checking that bar is inside of robot Panel or not!?
					if ((target.x <= playerRobot1.getX()
							+ playerRobot1.getWidth())
							&& (target.x >= playerRobot1.getX()))
						if ((target.y <= playerRobot1.getY()
								+ playerRobot1.getHeight())
								&& (target.y >= playerRobot1.getY()))
							continue;

					playerRobot1.shoot(target, false);

					try {
						Thread.sleep(50);
					} catch (Exception e) {
						System.out.println("ERROR IN SLEEPING!");
					}
				}
				try {
					sleep(80);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/****/

} // End Of Class GamePanel
