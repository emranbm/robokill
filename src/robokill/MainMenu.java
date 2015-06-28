package robokill;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.ClientCore;

/**
 * 
 * @author HRM_SHAMS
 * @author Mr. Coder
 * @version 1.2
 */

public class MainMenu extends JPanel {

	private static final long serialVersionUID = 1L;

	// /////////////static phase/////////////////////
	private static MainMenu This;

	/**
	 * Creates an instance of {@link robokill.MainMenu MainMenu}. This method
	 * should just be called once, at the beginning of the game.
	 * 
	 * @param gameFrame
	 *            The main frame.
	 * @return The instantiated {@link robokill.MainMenu MainMenu}.
	 */
	public static MainMenu instantiate(GameFrame gameFrame) {
		return This = new MainMenu(gameFrame);
	}

	public static MainMenu getMainMenu() {
		if (This == null) {
			System.err
					.println("getMainMenu() method in MainMenu class called before MainMenu being instantiated.");
			System.exit(1);
		}

		return This;
	}

	// ///////////end of static phase////////////////

	private GameFrame gameFrameRef;

	private BufferedImage background;

	public Btn singlePlayerGameBtn;
	public Btn multiPlayerBtn;
	public Btn settingBtn;
	public Btn aboutBtn;
	public Btn exitBtn;

	private MainMenu(GameFrame gameFrameRef) {
		this.gameFrameRef = gameFrameRef;

		setSize(1000, 700);
		setLocation(0, 0);
		setLayout(null);

		setButtons();

		try {
			background = ImageIO.read(getClass().getResource(
					"/images/mainMenu/mainBackground.png"));
		} catch (Exception e) {
			System.out.println("error in reading file!");
		}
	}

	/**
	 * add buttons to panel!
	 */
	private void setButtons() {
		singlePlayerGameBtn = new Btn(0, 325, "playgame", this);
		multiPlayerBtn = new Btn(0, 384, "multiplayer", this);
		settingBtn = new Btn(0, 445, "setting", this);
		aboutBtn = new Btn(0, 508, "about", this);
		exitBtn = new Btn(0, 567, "exit", this);

		add(singlePlayerGameBtn);
		add(multiPlayerBtn);
		add(settingBtn);
		add(aboutBtn);
		add(exitBtn);
	}

	/**
	 * this method remove this panel and add GamePanel to Frame!
	 * 
	 * @param isMultiPlayer
	 */
	public void playGame() {
		gameFrameRef.remove(this);
		gameFrameRef.add(GamePanel.getGamePanel(), BorderLayout.CENTER);
		gameFrameRef.repaint();
		System.out.println("GamePanel added to frame.");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
	}

	/** class Btn **/
	class Btn extends JPanel {

		private static final long serialVersionUID = 1L;

		MainMenu mainMenuRef;
		BtnEffect btnE;
		BtnText btnT;

		public Btn(int x, int y, String identifer, MainMenu mainMenuRef) {
			setSize(362, 40);
			setLocation(x, y);
			setOpaque(false);
			setLayout(null);

			btnT = new BtnText(identifer);
			add(btnT);
			btnE = new BtnEffect();
			add(btnE);
			btnE.setVisible(false);

			this.mainMenuRef = mainMenuRef;

			addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
				}

				@Override
				public void mouseExited(MouseEvent e) {
					btnE.setVisible(false);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					btnE.setVisible(true);
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getSource() == mainMenuRef.singlePlayerGameBtn) {
						GamePanel.instantiate(false, true);
						mainMenuRef.playGame();
					} else if (e.getSource() == mainMenuRef.exitBtn) {
						System.exit(0);
					} else if (e.getSource() == mainMenuRef.multiPlayerBtn) {
						// TODO toooooooooooooo weak graphics!
						String hostAddress = JOptionPane
								.showInputDialog(mainMenuRef,
										"Enter the server computer address! (The name diplayed in network)");
						System.out.println("Trying to connect to server...");
						String result = ClientCore.getClientCore().connect(
								hostAddress);
						System.out.println("connected as: " + result);
						if (result == null)
							JOptionPane.showMessageDialog(mainMenuRef,
									"Connection failed!");
						else if (result.equals("normal")) {
							ClientCore.getClientCore().sendCommand("start");
							GamePanel.instantiate(true, false);
							ClientCore.getClientCore().start();
							playGame();
						} else if (result.equals("master")) {
							GamePanel.instantiate(true, true);
							ClientCore.getClientCore().start();
							// Waits and listens until get start command.
							// TODO nothing. Just a graphical waiting!
						}
					}
				}
			});
		}

	}

	/** end of class Btn **/

	/** class BtnEffect **/
	class BtnEffect extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public BtnEffect() {

			setLocation(0, 0);
			setSize(362, 40);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/mainMenu/buttonEffect.png"));
			} catch (Exception ex) {
				System.out.println("error in reading file!");
			}

		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}

	/** end of class BtnEffect **/

	class BtnText extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public BtnText(String identifier) {
			setSize(362, 40);
			setLocation(0, 0);
			try {
				image = ImageIO.read(getClass().getResource(
						"/images/mainMenu/" + identifier + ".png"));
			} catch (Exception e) {
				System.out.println("error in reading image!");
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}
	}

}
