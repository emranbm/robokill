package robokill;

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

import static java.lang.Thread.sleep;

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
 * @version 1.7
 */

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static GamePanel This;

	private Player playerRobot; // the panel of playerRobot!
	// private Thread playerRobotThread ; // a thread that related to player
	// robot
	private BufferedImage background;

	private boolean isShooting = false;
	private int curMouseX;
	private int curMouseY;

	private ArrayList<Element> elements = new ArrayList<Element>();

	private final Set<Integer> keys = new HashSet<Integer>(); // related to
																// movement of
																// robot!

	public static GamePanel getGamePanel() {
		if (This == null)
			This = new GamePanel();

		return This;
	}

	private GamePanel() {
		super();

		setBounds(0, 0, 1000, 700);

		setSize(new Dimension(1024, 768));
		setLayout(null);

		/** initializing background **/
		try {
			background = ImageIO.read(getClass().getResource(
					"/images/GamePanelBackground.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		/** adding playerRobot to gamePanel **/
		playerRobot = new Player(100, 100, 60, 60, 4);
		add(playerRobot);

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
	}// end of constructor !!

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, this);
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp); // To change body of generated methods, choose Tools
							// | Templates.
		repaint();
	}

	@Override
	public Component add(Component comp) {
		if (comp instanceof Element) {
			if (!(comp instanceof Bar))
				elements.add((Element) comp);
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
	public Element getCollidedElement(Element checkElement) {

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

	private void addElements() {
		Block block = new Block(400, 300, Block.BLOCK_TYPE_1);
		add(block);

		/** TESTING ANIMATION CLASS (temporary) **/
		Animation robotbody = new Animation(new Point(200, 350), new Dimension(
				80, 80), "/images/enemy1/", 29, 30, 0);

		add(robotbody);
		robotbody.start();
		/***************************/

		/*** (temporary) ***/
		Animation explosion = new Animation(new Point(400, 100), new Dimension(
				130, 130), "/images/explosion/", 9, 30, 10);

		Thread animation2 = new Thread(explosion);
		super.add(explosion);
		animation2.start();
		/*****************/

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
				playerRobot.robotHead
						.setTarget(new Point(curMouseX, curMouseY));
				playerRobot.robotHead.repaint();
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

				playerRobot.robotHead
						.setTarget(new Point(curMouseX, curMouseY));
				playerRobot.robotHead.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				int mouseX = e.getX() - 3;
				int mouseY = e.getY() - 25;

				/*** rotating the head of Robot!! ***/
				playerRobot.robotHead.setTarget(new Point(mouseX, mouseY));
				playerRobot.robotHead.repaint();
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
			while (true) {
				if (keys.contains(KeyEvent.VK_W)
						&& keys.contains(KeyEvent.VK_D)) {
					playerRobot.move(Direction.North_East);
				}

				else if (keys.contains(KeyEvent.VK_W)
						&& keys.contains(KeyEvent.VK_A)) {
					playerRobot.move(Direction.North_West);
				}

				else if (keys.contains(KeyEvent.VK_S)
						&& keys.contains(KeyEvent.VK_D)) {
					playerRobot.move(Direction.South_East);
				}

				else if (keys.contains(KeyEvent.VK_S)
						&& keys.contains(KeyEvent.VK_A)) {
					playerRobot.move(Direction.South_West);
				}

				else if (keys.contains(KeyEvent.VK_W)) {
					playerRobot.move(Direction.North);
				}

				else if (keys.contains(KeyEvent.VK_S)) {
					playerRobot.move(Direction.South);
				}

				else if (keys.contains(KeyEvent.VK_A)) {
					playerRobot.move(Direction.West);
				}

				else if (keys.contains(KeyEvent.VK_D)) {
					playerRobot.move(Direction.East);
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
			while (true) {
				if (isShooting) {
					int mouseX = curMouseX;
					int mouseY = curMouseY;

					// rotating the head of Robot!!
					playerRobot.shoot(new Point(mouseX, mouseY));

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
