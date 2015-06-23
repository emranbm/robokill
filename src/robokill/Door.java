package robokill;

import useful.Animation;
import useful.Direction;

/**
 * The door element.
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public class Door extends Element {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 50, DEPTH = 10;

	private Animation animation;

	/**
	 * 
	 * @param x
	 *            X location of the door.
	 * @param y
	 *            Y location of the door.
	 * @param animation
	 *            The animation for opening the door.
	 */
	public Door(int x, int y, Animation animation) {
		super(x, y, 1, 1);

		this.animation = animation;

		setSize(animation.getSize());
		animation.setLocation(0, 0);
		add(animation);
	}

	/**
	 * Just plays the animation and opens the door.
	 */
	public void open() {
		animation.start();
	}
}
