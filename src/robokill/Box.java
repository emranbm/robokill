package robokill;

import client.ClientCore;
import client.CommunicationConstants;

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

	public static final int BOX_WIDTH = 52, BOX_HEIGHT = 52;

	private int health = 100;

	private Prize prize;

	/**
	 * Creates an instance of {@link robokill.Box Box} that includes the given
	 * prize inside it.
	 * 
	 * @param x
	 * @param y
	 * @param prize
	 *            The prize that should be shown when the box is destroyed. It
	 *            can be null.
	 */
	public Box(int x, int y, Prize prize, int id) {
		super(x, y, BOX_WIDTH, BOX_HEIGHT);
		setImage("images/Box.png");
		if (prize != null) {
			prize.setLocation(getX() + getWidth() / 2 - 15, getY()
					+ getHeight() / 2 - 15);
			this.prize = prize;
		}
		setId(id);
	}

	@Override
	public void revalidateImage() {
		super.revalidateImage();
		if (prize != null)
			prize.revalidateImage();
	}

	@Override
	public synchronized void damage(int amount, boolean isServerCommand) {
		health -= amount;
		if (health <= 0) {
			// Adds a random prize in its location.

			if (isMaster()) {
				if (!isServerCommand)
					ClientCore.getClientCore().sendCommand(
							CommunicationConstants.boxDestroyCommand(this
									.getId()));

				GamePanel.getGamePanel().remove(this);

				if (prize != null)
					GamePanel.getGamePanel().add(prize);
			} else {
				if (isServerCommand) {
					if (prize != null)
						GamePanel.getGamePanel().add(prize);

					GamePanel.getGamePanel().remove(this);
				}
			}
		}
	}
}
