package robokill;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import useful.Direction;

/**
 * 
 * @author HRM_Shams
 * @version : 1.4
 */
public abstract class Robot extends Element {

	private static final long serialVersionUID = 1L;

	private int speed;

	private BufferedImage[] bodyImage = new BufferedImage[12];
	private Direction curMoveDirection;
	private int bodyLevel = 1;
	private int rotateDegree = 0; // for rotating the bodyimage!

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param speed
	 * @param imagePath
	 *            A relative path to an image. -> Final path will be:
	 *            "[project path]/src/[imagePath]". Note that the imagePath does
	 */
	public Robot(int x, int y, int width, int height, int speed,
			String imagePath) {
		super(x, y, width, height);

		setLayout(null);

		this.speed = speed;

		try {
			for (int i = 1; i <= 12; i++)
				bodyImage[i - 1] = ImageIO.read(getClass().getResource(
						"/images/robotbodyimages/" + i + ".png"));
		} catch (IOException e) {
			System.err.println("Error in loading image!");
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		rotate(g2d);
	}

	private void rotate(Graphics2D g2d) {
		// ///////////////////
		double rotateRadian = Math.toRadians(rotateDegree);

		AffineTransform record = g2d.getTransform();
		AffineTransform tx = new AffineTransform();
		tx.rotate(rotateRadian, this.getWidth() / 2, this.getHeight() / 2);
		g2d.transform(tx);
		g2d.drawImage(bodyImage[bodyLevel % 12], 0, 0, this);
		g2d.setTransform(record);
	}

	public void changeBody() {
		if (curMoveDirection == Direction.North) {
			rotateDegree = 0;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.South) {
			rotateDegree = 180;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.West) {
			rotateDegree = -90;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.East) {
			rotateDegree = 90;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.North_West) {
			rotateDegree = -45;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.North_East) {
			rotateDegree = 45;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.South_West) {
			rotateDegree = -135;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (curMoveDirection == Direction.South_East) {
			rotateDegree = 135;
			repaint();
			bodyLevel++;

			try {
				Thread.sleep(30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Moves in the given direction by the robot speed. Doesn't move if the next
	 * location (according to the given direction) is passable.
	 * 
	 * @param dir
	 */
	public void move(Direction dir) {
		curMoveDirection = dir;

		int x = this.getX();
		int y = this.getY();

		switch (dir) {
		case North:
			y -= speed;
			break;
		case North_East:
			y -= speed;
			x += speed;
			break;
		case East:
			x += speed;
			break;
		case South_East:
			y += speed;
			x += speed;
			break;
		case South:
			y += speed;
			break;
		case South_West:
			y += speed;
			x -= speed;
			break;
		case West:
			x -= speed;
			break;
		case North_West:
			y -= speed;
			x -= speed;
			break;
		}

		changeBody();

		Element collidedElement = GamePanel.getGamePanel().getCollidedElement(
				this, new Point(x, y));
		if (collidedElement == null)
			this.setLocation(x, y);
		else
			collidedWith(collidedElement);
			
		curMoveDirection = null;

	}
	
	public abstract void collidedWith(Element element);

	/**
	 * this method will be implements in classes Player and Enemy based on their
	 * specifics!
	 * 
	 * @param target
	 */
	public abstract void shoot(Point target);

}
