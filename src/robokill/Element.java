package robokill;

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
 * @version: 1.4
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

	public boolean isCollided(Element element) {
		int myX = this.getX();
		int myY = this.getY();
		int elementX = element.getX();
		int elementY = element.getY();

		if (myX <= elementX + element.getWidth() || myX >= elementX)
			if (myY <= elementY + element.getHeight() || myY >= elementY)
				return true;

		return false;
	}

	/**
	 * Sets the location of the element. If the location is out of the
	 * {@link robokill.GamePanel GamePanel}, nothing happens.
	 */
	@Override
	public void setLocation(int x, int y) {
		if (GamePanel.getGamePanel().isElementInside(new Point(x, y), getSize()))
			super.setLocation(x, y);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (defaultImage != null)
			g.drawImage(defaultImage, 0, 0, null);
	}
}
