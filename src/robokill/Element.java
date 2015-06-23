package robokill;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 
 * A parent for all elements in the game panel.
 * 
 * @author Mr. Coder
 *
 * @version: 1.5
 */
public abstract class Element extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image defaultImage;

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
		super();
		setBounds(x, y, width, height);
		setImage(imagePath);

		setOpaque(false);
	}

	public Element(int x, int y, int width, int height) {
		super();
		setBounds(x, y, width, height);
		setOpaque(false);

	}

	public void setImage(String imagePath) {
		try {
			this.defaultImage = ImageIO.read(getClass().getResource(
					"/" + imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	/**
	 * Sets the location of the element. If the location is out of the
	 * {@link robokill.GamePanel GamePanel}, nothing happens.
	 */
	@Override
	public void setLocation(int x, int y) {
		if (GamePanel.getGamePanel()
				.isElementInside(new Point(x, y), getSize()))
			super.setLocation(x, y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (defaultImage != null)
			g.drawImage(defaultImage, 0, 0, null);
	}
}
