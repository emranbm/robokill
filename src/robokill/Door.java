package robokill;

import java.awt.Dimension;
import java.awt.Point;

import useful.Animation;
import useful.Direction;

/**
 * The door element.
 * 
 * @author Mr. Coder
 * @author HRM_SHAMS
 * @version 1.1
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
	 * @param type
	 *            can be 1:west door 2:north door 3:east door 4:south door
	 */
	public Door(int x, int y, String type) {
		super(x, y, 1, 1);

		animation = new Animation(new Point(x,y) , 
				new Dimension(41,98) ,
				"/images/doors/" + type + "/" ,
				5 ,
				200 ,
				1 ,
				false) ; // the size of a door image is 41 x 98
		
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
