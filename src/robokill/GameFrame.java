/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robokill;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 *
 * @author HRM_Shams
 * @version 1.2
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static GameFrame This;

	public static GameFrame getGameFrame() {
		if (This == null)
			This = new GameFrame();

		return This;
	}

	private GameFrame() {
		setResizable(false);

		setSize(new Dimension(1000, 700));
		setInCenter();
		setUndecorated(true);

		setLayout(new BorderLayout());
		add(MainMenu.instantiate(this), BorderLayout.CENTER); // GamePanel.getGamePanel()

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void setInCenter() {
		/** set the frame in center of window **/
		Dimension d = new Dimension();
		d = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width / 2) - (getWidth() / 2), (d.height / 2)
				- (getHeight() / 2) - 20);
	}
}
