/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robokill;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

import useful.Animation;
import useful.ImageFactory;

/**
 *
 * @author HRM_Shams
 * @version 1.2
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	

	public GameFrame() {
		setResizable(false);

		setSize(new Dimension(1000, 700));
		setInCenter();
		setUndecorated(true);

		setLayout(new BorderLayout());
		add(new MainMenu(this), BorderLayout.CENTER); //GamePanel.getGamePanel()
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void setInCenter()
    {
        /**set the frame in center of window**/
        Dimension d = new Dimension();
        d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( (d.width/2)-(getWidth()/2) , (d.height/2)-(getHeight()/2)-20  );        
    }
}
