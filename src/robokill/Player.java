/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robokill;

import java.awt.Point;

/**
 *
 * @author HRM_Shams 
 * @version : 1.1
 */
public class Player extends Robot implements Damagable {

	private static final long serialVersionUID = 1L;

	public Player(int x, int y, int width, int height, int speed) {
		super(x, y, width, height, speed, "images/RobotH.png");
	}

	@Override
	public void damage(int amount) {
		;
	}

	/**
	 * this method gives the primary location of bar and target ! then we give
	 * the object of Bar to a Thread and call start method of thread!
	 * 
	 * @param target
	 */
	@Override
	public void shoot(Point target) {
		Bar bar = new Bar(new Point(this.getX(), this.getY()), target,
				Bar.BAR_TYPE_1, Bar.BAR_POWER_LIGHT);
		GamePanel.getGamePanel().add(bar);
		Thread barMovement = new Thread(bar);
		barMovement.start();
	}
}
