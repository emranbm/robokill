package robokill;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

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

	public RobotHead robotHead;

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

		robotHead = new RobotHead(imagePath, this);
		robotHead.setBounds(0, 0, 60, 60);
		add(robotHead);
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
		if (collidedElement == null) {
			this.setLocation(x, y);
		}
		curMoveDirection = null;

	}

	/**
	 * this method will be implements in classes Player and Enemy based on their
	 * specifics!
	 * 
	 * @param target
	 */
	public abstract void shoot(Point target);

	/**
	 *
	 * @author HRM_Shams
	 * @version 1.1
	 */
	public class RobotHead extends JPanel {

		private static final long serialVersionUID = 1L;

		private Image robotImage;
		private Point targetPoint = new Point(0, 0);

		/**
		 * because RobotHead is added to Robot Panel so the location of it
		 * always is (0,0) and we need to access the coordinate of parent panel!
		 */
		private Robot parent;

		public RobotHead(String imagePath, Robot parent) {

			try {
				this.robotImage = ImageIO.read(getClass().getResource(
						"/" + imagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.parent = parent;

			setOpaque(false);
		}

		/**
		 * this method will be used for initializing robotImage!
		 * 
		 * @param address
		 *            that contains only address from src folder! Example :
		 *            /images/robot.png
		 */
		public void setImage(String address) {
			try {
				robotImage = ImageIO.read(getClass().getResource(address));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Set the target where the robot is looking to.
		 * 
		 * @param point
		 */
		public void setTarget(Point point) {
			targetPoint = point;
			repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;

			rotate(g2d);
		}

		private void rotate(Graphics2D g2d) {
			int area = 0;
			double finalRadian = 0;

			double X = Math.abs((parent.getX() + this.getWidth() / 2)
					- targetPoint.getX());
			double Y = Math.abs((parent.getY() + this.getHeight() / 2)
					- (targetPoint.getY() - 25));

			if (targetPoint.getX() - (parent.getX() + this.getWidth() / 2) > 0
					&& targetPoint.getY()
							- (parent.getY() + this.getHeight() / 2) <= 0)
				area = 1;
			if (targetPoint.getX() - (parent.getX() + this.getWidth() / 2) >= 0
					&& targetPoint.getY()
							- (parent.getY() + this.getHeight() / 2) > 0)
				area = 4;
			if (targetPoint.getX() - (parent.getX() + this.getWidth() / 2) < 0
					&& targetPoint.getY()
							- (parent.getY() + this.getHeight() / 2) >= 0)
				area = 3;
			if (targetPoint.getX() - (parent.getX() + this.getWidth() / 2) <= 0
					&& targetPoint.getY()
							- (parent.getY() + this.getHeight() / 2) < 0)
				area = 2;

			double tanTeta = (double) (Y / X);

			double radian = Math.atan(tanTeta);

			if (area == 1)
				finalRadian = (Math.PI / 2) - radian;
			else if (area == 2)
				finalRadian = -1 * ((Math.PI / 2) - radian);
			else if (area == 3)
				finalRadian = -1 * ((Math.PI / 2) + radian);
			else if (area == 4)
				finalRadian = (Math.PI / 2) + radian;

			// ///////////////////
			AffineTransform record = g2d.getTransform();
			AffineTransform tx = new AffineTransform();
			tx.rotate(finalRadian, this.getWidth() / 2, this.getHeight() / 2);
			g2d.transform(tx);
			g2d.drawImage(robotImage, 0, 0, this);
			g2d.setTransform(record);
		}
	}

}
