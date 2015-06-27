package robokill;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainMenu extends JPanel {

	private static final long serialVersionUID = 1L;

	private GameFrame gameFrameRef;

	private BufferedImage background;

	public Btn singlePlayerGameBtn;
	public Btn multiPlayerBtn;
	public Btn settingBtn;
	public Btn aboutBtn;
	public Btn exitBtn;

	public MainMenu(GameFrame gameFrameRef) {
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

	public void playGame() {
		gameFrameRef.remove(this);
		gameFrameRef.add(GamePanel.instantiate(false), BorderLayout.CENTER);
		gameFrameRef.repaint();
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
						mainMenuRef.playGame();
					} else if (e.getSource() == mainMenuRef.exitBtn) {
						System.exit(0);
					} else if (e.getSource() == mainMenuRef.multiPlayerBtn)
						JOptionPane.showMessageDialog(mainMenuRef, "hello!");
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
