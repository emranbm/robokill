/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robokill;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import client.ClientCore;
import client.CommunicationConstants;
import useful.Animation;

/**
 *
 * @author HRM_Shams
 * @author Mr. Coder
 * @version 1.5
 */
public class Player extends Robot implements Damagable {

	private static final long serialVersionUID = 1L;

	public static String Player_Type_1 = "1";
	public static String Player_Type_2 = "2";

	public RobotHead robotHead;

	private int barType = Bar.BAR_TYPE_1;

	public Player(int x, int y, int width, int height, int speed, int id,
			String type) {
		super(x, y, width, height, speed, "/images/playerRobot/" + type + "/",
				12);

		setId(id);

		robotHead = new RobotHead("images/playerRobot/" + type + "/RobotH.png",
				this);
		robotHead.setBounds(0, 0, 60, 60);
		add(robotHead);
	}

	@Override
	public void damage(int amount, boolean isServerCommand) {
		GamePanel.getGamePanel().statusPanel.doReducing(amount);
	}

	/**
	 * this method gives the primary location of bar and target ! then we give
	 * the object of Bar to a Thread and call start method of thread!
	 * 
	 * @param target
	 */

	@Override
	public void shoot(Point target, boolean isServerCommand) {

		// send server command
		if (!isServerCommand)
			ClientCore.getClientCore().sendCommand(
					CommunicationConstants.playerShootCommand(this.getId(),
							target));

		int[] firstBarLoction = super.setFirstBarLocation(target);

		// TODO Implement bar power.
		Bar bar = new Bar(new Point(firstBarLoction[0], firstBarLoction[1]),
				target, barType, Bar.BAR_POWER_LIGHT, Bar.PLAYER_BAR_OWNED);

		GamePanel.getGamePanel().add(bar);
		bar.start();
	}

	private void fallRobot() {
		GamePanel.getGamePanel().remove(this);

		Animation fallingRobot = new Animation(new Point(this.getX(),
				this.getY()), new Dimension(130, 130), "/images/fallingrobot/",
				7, 50, 1, true);

		GamePanel.getGamePanel().add(fallingRobot);
		fallingRobot.start();

	}

	@Override
	public void collidedWith(Element element) {

		if (element instanceof Valley) {
			GamePanel.getGamePanel().gameEnded = true;
			this.fallRobot();
			GamePanel.getGamePanel().statusPanel.reduceHealth(100);

		} else if (element instanceof Prize) {
			Prize prize = (Prize) element;
			PrizeType prizeType = prize.achievePrize();

			GamePanel gamePanel = GamePanel.getGamePanel();

			switch (prizeType) {
			case Energy:
				gamePanel.statusPanel.increaseHealth(10);
				break;
			case Key:
				gamePanel.statusPanel.keyPanel.addKey();
				break;
			case Money:
				GamePanel.getGamePanel().statusPanel.addMoney(100);
				break;
			case Sheild:
				gamePanel.statusPanel.increaseShield(10);
				break;
			case Weapon:
				barType = Bar.BAR_TYPE_2;
				break;
			}

		} else if (element instanceof Door) {
			((Door) element).passToNextRoom();

		} else if (element instanceof Enemy) {
			((Enemy) element).damage(100, false);
			this.damage(20, false);
		}
	}

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
