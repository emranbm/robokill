package robokill;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import useful.Animation;

/**
 * The door element.
 * 
 * @author Mr. Coder
 * @author HRM_SHAMS
 * @version 1.4
 */
public class Door extends Element {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 50, DEPTH = 10;

	// private Animation animation;
	private boolean isOpened = false;

	/**
	 * 
	 * @param x
	 *            X location of the door.
	 * @param y
	 *            Y location of the door.
	 * @param type
	 *            can be 1:west door 2:north door 3:east door 4:south door
	 */
	public Door(int x, int y, String type) {
		super(x, y, 1, 1);

		Animation animation = new Animation(new Point(x, y), new Dimension(41,
				98), "/images/doors/" + type + "/", 5, 200, 1, false);// the
																		// size
																		// of a
																		// door
																		// image
																		// is
																		// 41x98
		setSize(animation.getSize());
		setAnimation(animation);
	}

	/**
	 * Just plays the animation and opens the door.
	 */
	public void open() {
		isOpened = true;
		startAnimation();
	}

	/**
	 * Determines whether the door is open or closed.
	 * 
	 * @return True if the door is open. Otherwise false.
	 */
	public boolean isOpen() {
		return isOpened;
	}

	/**
	 * Passes to the next room, if the door is open. Otherwise nothing happens.
	 * 
	 * @return Returns true if passed to the next room successfully. Otherwise
	 *         false.
	 */
	public boolean passToNextRoom() {
		if (!isOpened)
			return false;

		// TODO Go to next room.

		Animation changeRoom = new Animation(new Point(0, 0), new Dimension(
				1000, 700), "/images/changeRoom/", 24, 35, 1, true);
		GamePanel.getGamePanel().add(changeRoom);
		changeRoom.start();

		try {
			Thread.sleep(20);
		} catch (Exception e) {
		}

		GamePanel gamePanel = GamePanel.getGamePanel();
		int currentId = gamePanel.getRoomId();
		System.out.println("currentId = " + currentId);
		InputStream newRoomInputStream = getClass().getResourceAsStream(
				"/data/room " + (currentId + 1) + ".dat"); // ()
		try {
			ObjectInputStream ois = new ObjectInputStream(newRoomInputStream);
			Room newRoom = (Room) ois.readObject();
			ois.close();
			gamePanel.rearrange(newRoom);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			// TODO handle end of the Game!
		}

		return true;
	}
}
