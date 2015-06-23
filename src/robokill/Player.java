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
 * @version : 1.3
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
		
		int[] firstBarLoction = setFirstBarLocation(target);
		
		Bar bar = new Bar(new Point(firstBarLoction[0],firstBarLoction[1]),
				target,
				Bar.BAR_TYPE_1,
				Bar.BAR_POWER_LIGHT);
		
		GamePanel.getGamePanel().add(bar);
		Thread barMovement = new Thread(bar);
		barMovement.start();
	}
	
	/**
	 * for setting first location of bar while it is shooting!
	 * @param target
	 * @return
	 */
	private int[] setFirstBarLocation(Point target)
	{
		Point rCenter = new Point( (getX() + getWidth()/2) , (getY() + getHeight()/2) );
		
		int area ;
		double tgTeta = ( (double)(rCenter.y - target.y) / (double)(target.x - rCenter.x) );
		
		if (tgTeta<=1 && tgTeta>=-1)
		{
			if (target.x > rCenter.x)
				area = 1;
			else
				area = 3;
		}
		else
		{
			if (target.y > rCenter.y)
				area = 4;
			else
				area = 2;
		}
		
		int x = 0 , y = 0 ;
		if (area == 1)
		{
			x = getWidth()/2;
			y = -1 * ( (getWidth()/2) * (target.y-rCenter.y) ) / ( rCenter.x - target.x );
		}
		else if (area == 2)
		{
			x = ( (getHeight()/2) * (target.x - rCenter.x) ) / (rCenter.y - target.y) ;
			y = -1 * ( getHeight()/2 + 12); // 12 : height of bar panel+1
		}
		else if (area == 3)
		{
			x = -1 * ( (getWidth()/2) + 12 );
			y = ( (getWidth()/2) * (target.y-rCenter.y) ) / ( rCenter.x - target.x );
		}
		else if (area == 4)
		{
			x = -1 * ( (getHeight()/2) * (target.x - rCenter.x) ) / (rCenter.y - target.y) ;
			y = getHeight()/2; // 12 : height of bar panel+1
		}
		
		int finalX = rCenter.x + x;
		int finalY = rCenter.y + y;		
	
		int[] firstBarLocation = {finalX , finalY};
		return firstBarLocation;
	}
}
