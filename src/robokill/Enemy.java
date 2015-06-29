package robokill;

import java.awt.Dimension;
import java.awt.Point;

import client.ClientCore;
import client.CommunicationConstants;
import useful.Animation;
import useful.Direction;
import useful.Sound;

/**
 * 
 * @author HRM_SHAMS
 * @version 1.3
 */
public class Enemy extends Robot implements Runnable, Damagable {

	private static final long serialVersionUID = 1L;

	public static final String ENEMY_TYPE_1 = "1";
	public static final String ENEMY_TYPE_2 = "2";
	public static final String ENEMY_TYPE_3 = "3";

	private String enemyType;
	private static final int[] imageNumbers = { 29, 20, 29 }; // this variable
																// needed for
																// the
																// constructor
																// of this
																// class!

	private int health = 100;
	public boolean robotIsDied = false;

	public Enemy(int x, int y, int width, int height, int speed,

	String enemyType, int id) {
		super(x, y, width, height, speed, "/images/enemies/" + enemyType + "/",
				imageNumbers[Integer.valueOf(enemyType) - 1]); // some strange
																// works!

		this.enemyType = enemyType;
		setId(id);
	}

	/**
	 * this method is the main method of this class! robot first discover the
	 * best direction and then call method move with that direction!
	 * 
	 * @return a Direction (enum)
	 */
	public void selectDirectionAndMove() {
		/** where is the robots ?!! **/
		Point playerRobotLocation = GamePanel.getGamePanel().playerRobot1
				.getLocation();
		Point PC = new Point(playerRobotLocation.x + 30,
				playerRobotLocation.y + 30); // width / 2 = 30 & PC =
												// PlayerCenter

		Point EC = new Point(getX() + getWidth() / 2, getY() + getHeight() / 2); // EC
																					// =
																					// EnemyCenter

		/** finding the best direction **/
		byte d = 0;

		if ((Math.abs(PC.x - EC.x)) < 10 && PC.y < EC.y) {
			d = 1;
		} // 1
		else if ((Math.abs(PC.x - EC.x)) < 10 && PC.y > EC.y) {
			d = 5;
		} // 5
		else if ((Math.abs(PC.y - EC.y)) < 10 && PC.x < EC.x) {
			d = 7;
		} // 7
		else if ((Math.abs(PC.y - EC.y)) < 10 && PC.x > EC.x) {
			d = 3;
		} // 3
		else if (PC.x > EC.x && PC.y < EC.y) {
			d = 2;
		} // 2
		else if (PC.x > EC.x && PC.y > EC.y) {
			d = 4;
		} // 4
		else if (PC.x < EC.x && PC.y < EC.y) {
			d = 8;
		} // 8
		else if (PC.x < EC.x && PC.y > EC.y) {
			d = 6;
		} // 6
		else {
			System.err.println("syntax error in finding direction");
		}

		/** try to move (check the direction is occupied or not!) **/
		Direction[] directions = Direction.values();
		Direction selectedD = directions[d - 1];

		boolean moveResult = this.move(selectedD, false);
		if (moveResult == true) // if move is done
			return;

		/**
		 * if direction is occupied! (moveResult == false) -> trying nearest
		 * directions!
		 **/
		for (int i = 1; i <= 3; i++) {
			int nd = d - i; // nd = nearest direction code
			if (nd < 1)
				nd = nd + 8;

			selectedD = directions[nd - 1];

			moveResult = this.move(selectedD, false);
			if (moveResult == true) // if move is done
				return;

			// ///////
			nd = d + i; // nd = nearest direction code
			if (nd > 8)
				nd = nd % 8;

			selectedD = directions[nd - 1];

			moveResult = this.move(selectedD, false);
			if (moveResult == true) // if move is done
				return;
		}
		// if the direction was occupied we try the farest direction!
		int fd = (d + 4) % 8;
		selectedD = directions[fd];

		moveResult = this.move(selectedD, false);
		if (moveResult == true) // if move is done
			return;
			
	}

	@Override
	public void run() {
		while (!GamePanel.isGamePanelReady())
			Thread.yield();

		int counter = 1;

		while (!robotIsDied) {
			selectDirectionAndMove();

			if (this.enemyType.equals(ENEMY_TYPE_3)) // if enemy type == 3
														// -> do shooting!
			{
				counter++;

				if (counter == 20) {
					shoot(null, false);
					counter = 0;
				}

			}

			try {
				Thread.sleep(20);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Starts to manner smartly. (If it is master client. If not, nothing
	 * happens)
	 */
	public void go() {
		if (isMaster() || !GamePanel.isMultiPlayer()) {
			Thread enemyRobot = new Thread(this);
			enemyRobot.setPriority(Thread.MIN_PRIORITY);
			enemyRobot.start();
		}
	}

	@Override
	public void collidedWith(Element element) {
		if (element instanceof Player) {
//			this.damage(100, false);
//			((Player) element).damage(20, false);
		}
	}

	@Override
	public void shoot(Point target, boolean isServerCommand) {

		// discovering best direction for shooting!
		Point rLocation = GamePanel.getGamePanel().playerRobot1.getLocation();
		Point PC = new Point(rLocation.x + 30, rLocation.y + 30);

		// send server command
		if (!isServerCommand)
			ClientCore.getClientCore().sendCommand(
					CommunicationConstants.enemyShootCommand(this.getId(), PC));

		int[] firstBarLoction = super.setFirstBarLocation(PC);

		// TODO Implement bar power.
		Bar bar = new Bar(new Point(firstBarLoction[0], firstBarLoction[1]),
				PC, Bar.BAR_TYPE_1, Bar.BAR_POWER_LIGHT, Bar.ENEMY_BAR_OWNED);

		GamePanel.getGamePanel().add(bar);
		
		Sound shootSound = new Sound("src/sounds/shoot2.wav", false);
		shootSound.playSound();
		
		bar.start();

	}

	@Override
	public void damage(int amount, boolean isServerCommand) {
		health = health - amount;
		if (health <= 0) {

			if (isMaster()) {
				if (!isServerCommand) {

					ClientCore.getClientCore().sendCommand(
							CommunicationConstants.enemyDestroyCommand(this
									.getId()));

					robotIsDied = true;

					Animation fireExplosion = new Animation(this.getLocation(),
							new Dimension(85, 85), "/images/explosion2/", 7,
							30, 1, true);
					GamePanel.getGamePanel().add(fireExplosion);
					fireExplosion.start();

					Sound shootSound = new Sound("src/sounds/explosion.wav", false);
					shootSound.playSound();					
					
					GamePanel.getGamePanel().remove(this);
				}
			} else {
				if (isServerCommand) {
					robotIsDied = true;

					Animation fireExplosion = new Animation(this.getLocation(),
							new Dimension(85, 85), "/images/explosion2/", 7,
							30, 1, true);
					GamePanel.getGamePanel().add(fireExplosion);
					fireExplosion.start();

					Sound shootSound = new Sound("src/sounds/explosion.wav", false);
					shootSound.playSound();					
					
					GamePanel.getGamePanel().remove(this);
				}
			}
		}
	}

}
