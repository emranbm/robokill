package robokill;

import java.util.Random;

/**
 * An element representing a non-passable box which is damagable and may contain
 * a prize that can be reached when the box is destroyed.
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */

public class Box extends Element implements Damagable {

	private static final long serialVersionUID = 1L;

	public static final int BOX_WIDTH = 20, BOX_HEIGHT = 60;

	private int health = 100;

	public Box(int x, int y) {
		super(x, y, BOX_WIDTH, BOX_HEIGHT);
	}

	@Override
	public void damage(int amount) {
		health -= amount;
		if (health <= 0) {
			// Adds a random prize in its location.
			GamePanel.getGamePanel().add(
					new Prize(getX(), getY(), PrizeType.values()[new Random()
							.nextInt(PrizeType.values().length)]));
			GamePanel.getGamePanel().remove(this);
		}
	}
}
