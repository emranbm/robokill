package robokill;

import java.awt.Point;

import useful.Vector;

/**
 * 
 * @author Mr.Coder
 *
 * @version 1.2
 */
public class Bar extends Element implements Runnable, Damagable {

	private static final long serialVersionUID = 1L;

	public static final int SPEED = 15;
	public static final boolean ENEMY_BAR_OWNED = false,
			PLAYER_BAR_OWNED = true;
	public static final int BAR_TYPE_1 = 1, BAR_TYPE_2 = 2;
	public static final int BAR_POWER_LIGHT = 10, BAR_POWER_MEDIUM = 20,
			BAR_POWER_HEAVEY = 30;

	private Vector speedVector;
	private int type, power;
	private boolean barOwner;

	public Bar(Point initilalLocation, Vector vector, int type, int power,
			boolean barOwner) {
		super(initilalLocation.x, initilalLocation.y, 11, 11);

		this.speedVector = vector.getParallelVector(SPEED);
		this.type = type;
		this.power = power;
		this.barOwner = barOwner;

		String imagePath = "images/";

		if (type == BAR_TYPE_2)
			imagePath += "Bar 111.png";
		else
			imagePath += "Bar 1.png";

		setImage(imagePath);
	}

	public Bar(Point initialLocation, Point targetLocation, int type,
			int power, boolean barOwner) {
		this(initialLocation, new Vector(targetLocation.x - initialLocation.x,
				targetLocation.y - initialLocation.y), type, power, barOwner);
	}

	public int getType() {
		return type;
	}

	public int getPower() {
		return power;
	}

	@Override
	public void damage(int amount, boolean isServerCommand) {
		go = false;
		GamePanel.getGamePanel().remove(this);
	}

	/**
	 * Automatically removes and stops when reached out of GamePanel.
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);

		if (!GamePanel.getGamePanel().isElementInside(new Point(x, y),
				this.getSize()))
			damage(0, false);

		Element collidedElement = GamePanel.getGamePanel().getCollidedElement(
				this);
		if (collidedElement != null && collidedElement instanceof Damagable
				&& (!(collidedElement instanceof Player) || !barOwner)) {
			this.damage(0, false);
			int damageAmount = (type == BAR_TYPE_1) ? 10 : 20;
			switch (power) {
			case BAR_POWER_LIGHT:
				damageAmount += 2;
				break;
			case BAR_POWER_MEDIUM:
				damageAmount += 4;
				break;
			case BAR_POWER_HEAVEY:
				damageAmount += 6;
				break;
			}

			// we first check that collided element and bar has not a same
			// owner!
			if (collidedElement instanceof Player
					&& this.barOwner == this.PLAYER_BAR_OWNED) {/* Do Nothing */
			} else if (collidedElement instanceof Enemy
					&& this.barOwner == this.ENEMY_BAR_OWNED) {/* Do Nothing */
			} else {
				// calling method damage that is in collided element
				((Damagable) collidedElement).damage(damageAmount, false);
			}
		}
	}

	/**
	 * Starts the animation as a thread.
	 */
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	// A flag to stop the thread when the bar get damage.
	private boolean go = true;

	@Override
	public void run() {
		while (go) {
			this.setLocation(speedVector.transfer(this.getLocation()));
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
