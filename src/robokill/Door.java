package robokill;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import useful.Animation;
import useful.Sound;

/**
 * The door element.
 * 
 * @author Mr. Coder
 * @author HRM_SHAMS <<<<<<< HEAD
 * @version 1.4 =======
 * @version 1.5 >>>>>>> 37d5e7640140b8cf99d33b41b189fc1680565be9
 */
public class Door extends Element {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 50, DEPTH = 10;

	// private Animation animation;
	private boolean isOpened = false;

	private int roomId;

	/**
	 * 
	 * @param x
	 *            X location of the door.
	 * @param y
	 *            Y location of the door.
	 * @param type
	 *            can be 1:west door 2:north door 3:east door 4:south door
	 */

	public Door(int x, int y, String type, int roomId) {
		super(x, y, 1, 1);

		this.roomId = roomId;

		/****/
		Dimension size = new Dimension(0, 0);
		if (type.equals("1") || type.equals("3"))
			size = new Dimension(41, 98);
		else
			size = new Dimension(98, 41);
		/****/

		Animation animation = new Animation(new Point(x, y), size,
				"/images/doors/" + type + "/", 5, 200, 1, false); // the size of
																	// a door
																	// image is
																	// 41x98 or
																	// 98x41

		setSize(animation.getSize());
		setAnimation(animation);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	/**
	 * Just plays the animation and opens the door.
	 */
	public void open() {
		isOpened = true;
		startAnimation();
		
		Sound shootSound = new Sound("src/sounds/doorOpened.wav", false);
		shootSound.playSound();
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

		Animation changeRoom = new Animation(new Point(0, 0), new Dimension(
				1000, 700), "/images/changeRoom/", 26, 35, 1, true);

		GamePanel.getGamePanel().add(changeRoom);
		changeRoom.start();

		try {
			Thread.sleep(20);
		} catch (Exception e) {
		}

		GamePanel gamePanel = GamePanel.getGamePanel();

		InputStream newRoomInputStream = getClass().getResourceAsStream(
				"/data/room " + this.roomId + ".dat"); // (currentId + 1)
		try {
			ObjectInputStream ois = new ObjectInputStream(newRoomInputStream);
			Room newRoom = (Room) ois.readObject();
			ois.close();
			gamePanel.rearrange(newRoom);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}
}
