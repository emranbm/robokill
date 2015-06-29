package robokill;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import client.ClientCore;
import client.CommunicationConstants;
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
	 * @param isServerCommand
	 *            To take care if it is a server command, so there is no need to
	 *            resend move command to the server.
	 * @return true if move is done (no collision) and false else!
	 */
	public boolean move(Direction dir, boolean isServerCommand) {
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

		if (this instanceof Player)
			changeBody();
		if (collidedElement == null) {

			if (this instanceof Enemy)
				changeBody();
			
			if (gamePanel.isElementInside(new Point(x, y), getSize())) {
				this.setLocation(x, y);

				// send server command
				if (!isServerCommand) {
					if (this instanceof Enemy)
						ClientCore.getClientCore().sendCommand(
								CommunicationConstants.enemyMoveCommand(
										this.getId(), curMoveDirection));
					else
						ClientCore.getClientCore().sendCommand(
								CommunicationConstants.playerMoveCommand(
										this.getId(), curMoveDirection));
				}

				curMoveDirection = null;
			}
			return true;
		} else {

			collidedWith(collidedElement);
			return false;
		}

	}

	/**
	 * for setting first location of bar while it is shooting!
	 * 
	 * @param target
	 * @return
	 */
	public int[] setFirstBarLocation(Point target) {
		Point rCenter = new Point((getX() + getWidth() / 2),
				(getY() + getHeight() / 2));

		int area;
		double tgTeta = ((double) (rCenter.y - target.y) / (double) (target.x - rCenter.x));

		if (tgTeta <= 1 && tgTeta >= -1) {
			if (target.x > rCenter.x)
				area = 1;
			else
				area = 3;
		} else {
			if (target.y > rCenter.y)
				area = 4;
			else
				area = 2;
		}

		int x = 0, y = 0;
		if (area == 1) {
			x = getWidth() / 2;
			y = -1 * ((getWidth() / 2) * (target.y - rCenter.y))
					/ (rCenter.x - target.x);
		} else if (area == 2) {
			x = ((getHeight() / 2) * (target.x - rCenter.x))
					/ (rCenter.y - target.y);
			y = -1 * (getHeight() / 2 + 12); // 12 : height of bar panel+1
		} else if (area == 3) {
			x = -1 * ((getWidth() / 2) + 12);
			y = ((getWidth() / 2) * (target.y - rCenter.y))
					/ (rCenter.x - target.x);
		} else if (area == 4) {
			x = -1 * ((getHeight() / 2) * (target.x - rCenter.x))
					/ (rCenter.y - target.y);
			y = getHeight() / 2; // 12 : height of bar panel+1
		}

		int finalX = rCenter.x + x;
		int finalY = rCenter.y + y;

		int[] firstBarLocation = { finalX, finalY };
		return firstBarLocation;
	}

	public abstract void collidedWith(Element element);

	/**
	 * this method will be implemented in classes Player and Enemy based on
	 * their specifics!
	 * 
	 * @param isServerCommand
	 *            To take care if it is a server command, so there is no need to
	 *            resend shoot command to the server.
	 * @param target
	 */
	public abstract void shoot(Point target, boolean isServerCommand);

}
