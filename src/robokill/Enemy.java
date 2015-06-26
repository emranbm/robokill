package robokill;

import java.awt.Dimension;
import java.awt.Point;

import useful.Animation;
import useful.Direction;

/**
 * 
 * @author HRM_SHAMS
 * @version 1.1
 */
public class Enemy extends Robot implements Runnable, Damagable {

	private static final long serialVersionUID = 1L;

	public static final String ENEMY_TYPE_1 = "1";

	private int health = 100;
	public boolean robotIsDied = false;

	public Enemy(int x, int y, int width, int height, int speed,
			String enemyType,int id) {
		super(x, y, width, height, speed, "/images/enemies/" + enemyType + "/",
				29);
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
		Point playerRobotLocation = GamePanel.getGamePanel().playerRobot
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

		boolean moveResult = this.move(selectedD);
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

			moveResult = this.move(selectedD);
			if (moveResult == true) // if move is done
				return;

			// ///////
			nd = d + i; // nd = nearest direction code
			if (nd > 8)
				nd = nd % 8;

			selectedD = directions[nd - 1];

			moveResult = this.move(selectedD);
			if (moveResult == true) // if move is done
				return;
		}
		// if the direction was occupied we try the farest direction!
		int fd = (d + 4) % 8;
		selectedD = directions[fd];

		moveResult = this.move(selectedD);
		if (moveResult == true) // if move is done
			return;
		else
			System.out.println("it can't be moved!");

	}

	@Override
	public void run() {
		while (!GamePanel.isGamePanelReady())
			Thread.yield();
		
		while (!robotIsDied) {
			selectDirectionAndMove();
			// try{
			// Thread.sleep(100);
			// }catch(Exception e){}
		}
	}

	public void go() {
		Thread enemyRobot = new Thread(this);
		enemyRobot.start();
	}

	@Override
	public void collidedWith(Element element) {
		if (element instanceof Player) {
			this.damage(100);
			((Player) element).damage(20);
		}
	}

	@Override
	public void shoot(Point target) {
		// TODO Auto-generated method stub

	}

	@Override
	public void damage(int amount) {
		health = health - amount;
		if (health <= 0) {
			robotIsDied = true;

			//
			Animation fireExplosion = new Animation(this.getLocation(),
					new Dimension(85, 85), "/images/explosion2/", 7, 30, 1,
					true);
			GamePanel.getGamePanel().add(fireExplosion);
			fireExplosion.start();
			//

			GamePanel.getGamePanel().remove(this);
		}
	}

}
