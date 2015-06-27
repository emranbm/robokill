package robokill;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import useful.Animation;

/**
 * 
 * A parent for all elements in the game panel.
 * 
 * @author Mr. Coder
 *
 * @version: 1.8
 */
public abstract class Element extends JPanel {

	private static final long serialVersionUID = 2L;

	// TODO use this boolean! :
	private boolean isMultiPlayer = GamePanel.isMultiPlayer();

	private transient Image defaultImage;
	private Animation animation;
	private String imagePath;
	private int id = -1;

	/**
	 * 
	 * @param x
	 *            X location of the element
	 * @param y
	 *            Y location of the element
	 * @param width
	 *            Element width
	 * @param height
	 *            Element height
	 * @param imagePath
	 *            A relative path to an image. -> Final path will be:
	 *            "[project path]/src/[imagePath]". Note that the imagePath does
	 *            not start with "/"
	 */
	public Element(int x, int y, int width, int height, String imagePath) {
		super(null);
		setBounds(x, y, width, height);
		setImage(imagePath);

		setOpaque(false);
	}

	/**
	 * 
	 * @param x
	 *            X location of the element
	 * @param y
	 *            Y location of the element
	 * @param width
	 *            Element width
	 * @param height
	 *            Element height
	 * @param animation
	 *            The animation of the element to show
	 */
	public Element(int x, int y, int width, int height, Animation animation) {
		super(null);
		setBounds(x, y, width, height);
		setOpaque(false);

		setAnimation(animation);
	}

	/**
	 * 
	 * @param x
	 *            X location of the element
	 * @param y
	 *            Y location of the element
	 * @param width
	 *            Element width
	 * @param height
	 *            Element height
	 */
	public Element(int x, int y, int width, int height) {
		super(null);
		setBounds(x, y, width, height);
		setOpaque(false);

	}

	/**
	 * Determines if the game is multi player or single player.
	 * 
	 * @return true if multi player. false if single player.
	 */
	public boolean isMultiPlayer() {
		return isMultiPlayer;
	}

	/**
	 * Sets the default image of the element.
	 * 
	 * @param imagePath
	 *            The path of the element starting inside the src folder and
	 *            doesn't start with "/". Example: "images/image.png"
	 */
	public void setImage(String imagePath) {
		this.imagePath = imagePath;
		try {
			this.defaultImage = ImageIO.read(getClass().getResource(
					"/" + imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the animation of the element.
	 * 
	 * @param animation
	 *            The animation to be added to the element.
	 */
	public void setAnimation(Animation animation) {
		animation.setLocation(0, 0);
		animation.setSize(getSize());
		this.animation = animation;
		add(animation);
	}

	/**
	 * Sets an id for the element to be distinguishable from other element. Note
	 * that the id should be a unique integer.
	 * 
	 * @param id
	 *            The unique integer to be set as id.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the id of the element, if it has been set. If the id isn't set
	 * yet, then -1 will be returned.
	 * <p>
	 * Id is a unique unchanging integer that makes elements distinguishable.
	 * 
	 * @return Returns the id of the element if it is set. Otherwise returns -1.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Reloads the element image. This method is used for reloading the element
	 * image when it is loaded from a file as a serializable. Since the
	 * BuffuredImage is not serializable, so after loading the element, the
	 * image is null should be reloaded.
	 */
	public void revalidateImage() {
		if (imagePath != null)
			setImage(imagePath);

		if (animation != null)
			animation.revalidateImages();
	}

	/**
	 * Starts the animation of the element if exits. If the element doesn't have
	 * an animation, nothing happens.
	 */
	public void startAnimation() {
		if (animation != null)
			animation.start();
	}

	/**
	 * Determines whether or not an element has collided with the current
	 * element.
	 * 
	 * @param element
	 *            The element to check for collision.
	 * @return Return true if is in collision with the given element. Otherwise
	 *         false.
	 */
	public boolean isCollided(Element element) {
		int myX = this.getX();
		int myY = this.getY();

		int elementX = element.getX();
		int elementY = element.getY();
		int elementWidth = element.getWidth();
		int elementHeight = element.getHeight();

		/*********/
		if (myX <= elementX + elementWidth ^ myX + getWidth() <= elementX)
			if (myY <= elementY + elementHeight ^ myY + getHeight() <= elementY)
				return true;
		/*********/

		return false;
	}

	/**
	 * Determines whether or not an element has collided with the current
	 * element.
	 * 
	 * @param location
	 *            The location to check if the collision is happened on.
	 * @param size
	 *            The size of checking area to check collision.
	 * @return
	 */
	public boolean isCollided(Point location, Dimension size) {
		int myX = this.getX();
		int myY = this.getY();
		int elementX = location.x;
		int elementY = location.y;

		/*********/
		if (myX <= elementX + size.width ^ myX + getWidth() <= elementX)
			if (myY <= elementY + size.height ^ myY + getHeight() <= elementY)
				return true;
		/*********/

		return false;
	}

	// /**
	// * Sets the location of the element. If the location is out of the
	// * {@link robokill.GamePanel GamePanel}, nothing happens.
	// */
	// @Override
	// public void setLocation(int x, int y) {
	// if (GamePanel.getGamePanel()
	// .isElementInside(new Point(x, y), getSize()))
	// super.setLocation(x, y);
	// }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (defaultImage != null)
			g.drawImage(defaultImage, 0, 0, null);
	}
}
