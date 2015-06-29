package robokill;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import useful.Sound;
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
	
	private Sound backgroundSound;

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
		
		backgroundSound = new Sound("src/sounds/backgroundSound.wav" , true);
		backgroundSound.playSound();
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

	/**
	 * 
	 */
	class AboutPanel extends JPanel{
		
		BufferedImage image;
		
		public AboutPanel()
		{
			
			setSize(611 , 504);
			setLocation(200, 50);
			setLayout(null);
			setOpaque(false);
			
			try{
			image = ImageIO.read(getClass().getResource("/images/about.png"));
			}catch(Exception e){System.out.println("error in reading image!!");}
			
			add (new ToolBar(this));
		}
		@Override
		public void paintComponent(Graphics g)
		{
			g.drawImage(image, 0, 0, null);
		}
		
		/**
		 * this class used for closing panel and moving it!
		 */
		class ToolBar extends JPanel{

			private Point initialClick;
			private AboutPanel aboutRef;
			private JPanel closeBtn;
			
			public ToolBar(AboutPanel aboutRef){
				this.aboutRef = aboutRef;
				
				setOpaque(false);
				setLocation(1,1);
				setSize(602,446);
				setLayout(null);
				
				/**close btn**/
				closeBtn = new JPanel();
				closeBtn.setOpaque(false);
				closeBtn.setSize(15,15);
				closeBtn.setLocation(581, 19);
				closeBtn.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {}
					
					@Override
					public void mousePressed(MouseEvent e) {}
					
					@Override
					public void mouseExited(MouseEvent e) {}
					
					@Override
					public void mouseEntered(MouseEvent e) {}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						MainMenu.getMainMenu().remove(aboutRef);
						MainMenu.getMainMenu().repaint();
					}
				});
				add(closeBtn);
				/**end of adding close BTN**/
				
		        /***add mouse listeners***/
		        this.addMouseListener(new MouseAdapter() {
		        	
		        public void mousePressed(MouseEvent e) {
		            initialClick = e.getPoint();
		            getComponentAt(initialClick);
		        }
		        });

		        addMouseMotionListener(new MouseMotionAdapter() {

	        	@Override
		        public void mouseDragged(MouseEvent e) {

	        		// get location of Window
		            int thisX = aboutRef.getLocation().x;
		            int thisY = aboutRef.getLocation().y;

		            // Determine how much the mouse moved since the initial click
		            int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
		            int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

		            // Move window to this position
		            int X = thisX + xMoved;
		            int Y = thisY + yMoved;
		            aboutRef.setLocation(X, Y);
		            aboutRef.repaint();
		        }
		    });        
				
			}
			
		}

		
	}
	
	/**
	 * this class used in Btn class!
	 */
	/** class multiplayer Panel **/
	class MultiPlayerPanel extends JPanel{
		BufferedImage image ;
		JTextField serverAddress;
		JLabel state;
		MultiPlayerPanel mppRef = this;
		
		MultiPlayerPanel()
		{
			setSize(549,230);
			setLocation(225,235);
			setOpaque(false);
			setLayout(null);
			
			/** adding JLabel state!**/
			state = new JLabel("");
			state.setForeground(Color.black);
			state.setLocation(17,208);
			state.setSize(200, 10);
			add(state);
			
			/** adding JTextField**/
			serverAddress = new JTextField("");
			serverAddress.setBounds(28,106,496,23);
			serverAddress.setOpaque(false);
			serverAddress.setBorder(null);
			serverAddress.setForeground(Color.white);
			add(serverAddress);
			
			/** adding toolbar **/
			ToolBar toolbar = new ToolBar(this);
			add(toolbar);
			
			/** adding connectBtn **/
			add (new ConnectBtn());
			
			/**close btn**/
			JPanel cancelBtn;
			cancelBtn = new JPanel();
			cancelBtn.setOpaque(false);
			cancelBtn.setSize(173,34);
			cancelBtn.setLocation(287, 160);
			cancelBtn.addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					MainMenu.getMainMenu().remove(mppRef);
					MainMenu.getMainMenu().repaint();
				}
			});
			add(cancelBtn);
			/**end of adding close BTN**/
			
			try{
			image = ImageIO.read(getClass().getResource("/images/mainMenu/multiplayerPanel.png"));
			}catch(Exception e){System.out.println("error in reading file!");}
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			g.drawImage(image , 0 , 0 , null);
		}
		
		/**
		 * this class is a panel that by clicking try to connect to server
		 */
		class ConnectBtn extends JPanel{
			public ConnectBtn()
			{
				setSize(173,34);
				setLocation(96,160);
				setOpaque(false);
				
				addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {}
				
				@Override
				public void mousePressed(MouseEvent e) {}
				
				@Override
				public void mouseExited(MouseEvent e) {}
				
				@Override
				public void mouseEntered(MouseEvent e) {}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					String hostAddress = serverAddress.getText();
					
					state.setText("Trying to connect to server...");
					System.out.println("Trying to connect to server...");
					
					String result = ClientCore.getClientCore().connect(
					hostAddress);
					
					System.out.println("connected as: " + result);
					
					if (result == null)
						state.setText("Connection failed!");

					else if (result.equals("normal")) 
					{
					ClientCore.getClientCore().sendCommand("start");
					GamePanel.instantiate(gameFrameRef,true, false);
					ClientCore.getClientCore().start();
					playGame();
					} 
					
					else if (result.equals("master")) 
					{
					GamePanel.instantiate(gameFrameRef,true, true);
					ClientCore.getClientCore().start();
				// Waits and listens until get start command.
				// TODO nothing. Just a graphical waiting!
					}
					
				}
			});	
			}
			
		}
		
		/**
		 * this class used for closing panel and moving it!
		 */
		class ToolBar extends JPanel{

			private Point initialClick;
			private MultiPlayerPanel mppRef;
			private JPanel closeBtn;
			
			public ToolBar(MultiPlayerPanel mppRef){
				this.mppRef = mppRef;
				
				setOpaque(false);
				setLocation(0, 0);
				setSize(547,44);
				setLayout(null);
				
				/**close btn**/
				closeBtn = new JPanel();
				closeBtn.setOpaque(false);
				closeBtn.setSize(15,15);
				closeBtn.setLocation(520, 14);
				closeBtn.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {}
					
					@Override
					public void mousePressed(MouseEvent e) {}
					
					@Override
					public void mouseExited(MouseEvent e) {}
					
					@Override
					public void mouseEntered(MouseEvent e) {}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						MainMenu.getMainMenu().remove(mppRef);
						MainMenu.getMainMenu().repaint();
					}
				});
				add(closeBtn);
				/**end of adding close BTN**/
				
		        /***add mouse listeners***/
		        this.addMouseListener(new MouseAdapter() {
		        	
		        public void mousePressed(MouseEvent e) {
		            initialClick = e.getPoint();
		            getComponentAt(initialClick);
		        }
		        });

		        addMouseMotionListener(new MouseMotionAdapter() {

	        	@Override
		        public void mouseDragged(MouseEvent e) {

	        		// get location of Window
		            int thisX = mppRef.getLocation().x;
		            int thisY = mppRef.getLocation().y;

		            // Determine how much the mouse moved since the initial click
		            int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
		            int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

		            // Move window to this position
		            int X = thisX + xMoved;
		            int Y = thisY + yMoved;
		            mppRef.setLocation(X, Y);
		            mppRef.repaint();
		        }
		    });        
				
			}
			
		}
		
	}
	/**end of class MultiPlayerPanel**/
	
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
						mainMenuRef.backgroundSound.stopSound();
						
						GamePanel.instantiate(gameFrameRef,false, true);
						mainMenuRef.playGame();
						
					} else if (e.getSource() == mainMenuRef.exitBtn) {
						System.exit(0);
					
					} else if (e.getSource() == mainMenuRef.multiPlayerBtn) {
						// TODO toooooooooooooo weak graphics!
						mainMenuRef.add (new MultiPlayerPanel());
						mainMenuRef.repaint();
//						String hostAddress = JOptionPane
//								.showInputDialog(mainMenuRef,
//										"Enter the server computer address! (The name diplayed in network)");
//						System.out.println("Trying to connect to server...");
//						String result = ClientCore.getClientCore().connect(
//								hostAddress);
//						System.out.println("connected as: " + result);
//						if (result == null)
//							JOptionPane.showMessageDialog(mainMenuRef,
//									"Connection failed!");
//						else if (result.equals("normal")) {
//							ClientCore.getClientCore().sendCommand("start");
//							GamePanel.instantiate(gameFrameRef,true, false);
//							ClientCore.getClientCore().start();
//							playGame();
//						} else if (result.equals("master")) {
//							GamePanel.instantiate(gameFrameRef,true, true);
//							ClientCore.getClientCore().start();
//							// Waits and listens until get start command.
//							// TODO nothing. Just a graphical waiting!
//						}
					}
					
					else if (e.getSource() == mainMenuRef.aboutBtn)
					{
						mainMenuRef.add(new AboutPanel());
						mainMenuRef.repaint();
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
