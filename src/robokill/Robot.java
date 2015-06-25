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
 * @version : 1.5
 */
public abstract class Robot extends Element {

	private static final long serialVersionUID = 1L;

	private int speed;

	private transient BufferedImage[] bodyImage;
	private Direction curMoveDirection;
	private int bodyLevel = 1;
	private int rotateDegree = 0; // for rotating the bodyimage!

	private int imagesNumber;
	private String imagePath;

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
			String imagePath, int imageNumber) {
		super(x, y, width, height);

		setLayout(null);

		this.speed = speed;

		loadImages(imagePath, imageNumber);
	}

	private void loadImages(String imagePath, int imageNumber) {
		this.imagesNumber = imageNumber;
		this.imagePath = imagePath;
		bodyImage = new BufferedImage[imageNumber];
		try {
			for (int i = 1; i <= imageNumber; i++)
				bodyImage[i - 1] = ImageIO.read(getClass().getResource(
						imagePath + i + ".png"));
		} catch (IOException e) {
			System.err.println("Error in loading image!");
		}
	}

	@Override
	public void revalidateImage() {
		super.revalidateImage();

		loadImages(imagePath, imagesNumber);
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
		g2d.drawImage(bodyImage[bodyLevel % imagesNumber], 0, 0, this);
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
	 * @return true if move is done (no collision) and false else!
	 */
	public boolean move(Direction dir) {
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

		GamePanel gamePanel = GamePanel.getGamePanel();
		Element collidedElement = gamePanel.getCollidedElement(this, new Point(
				x, y));

		changeBody();
		if (collidedElement == null) {
			curMoveDirection = null;

			if (gamePanel.isElementInside(new Point(x, y), getSize()))
				this.setLocation(x, y);
			return true;
		} else {
			collidedWith(collidedElement);
			return false;
		}

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
