package robokill;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import useful.Animation;

/**
 * 
 * @author HRM_SHAMS
 * @version 1.4
 */

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private int health = 100;
	private int shield = 100;
	private int money = 0;
	private int keys = 0; // this field saves number of keys!

	public KeyPanel keyPanel = new KeyPanel();
	private MoneyPanel moneyPanel = new MoneyPanel();

	private Gooshe gooshe = new Gooshe();
	private HealthBar healthBar = new HealthBar();
	private BarBackground healthBackground = new BarBackground(new Point(26, 0));
	private ShieldBar shieldBar = new ShieldBar();
	private BarBackground shieldBackground = new BarBackground(
			new Point(126, 0));

	public StatusPanel() {
		setSize(1000, 27);
		setLocation(0, 673);
		setOpaque(false);
		setLayout(null);

		add(keyPanel);
		keyPanel.setVisible(false);

		add(moneyPanel);
		add(new GoosheRight());

		add(gooshe);
		add(new HealthText());
		add(healthBar);
		add(healthBackground);
		add(new ShieldText());
		add(shieldBar);
		add(shieldBackground);
	}

	public void addMoney(int amount) {
		money += amount;
		moneyPanel.setMoneyLabel(money);
	}

	/**
	 * when Robot damage method (in class Player) then this method will be called!
	 * this method try to reduce from shield and health together!
	 */
	public synchronized void doReducing(int reducePercent)
	{
		int healthReducePercent ;
		int shieldReducePercent ;

		shieldReducePercent = (int) (reducePercent * 0.6) ;
		healthReducePercent = (int) (reducePercent * 0.4) ;
		
		if (shield >= shieldReducePercent)
		{
			reduceShield(shieldReducePercent);
			reduceHealth(healthReducePercent);
		}
		else
		{
			shieldReducePercent = shield;
			healthReducePercent = reducePercent - shield;

			reduceShield(shieldReducePercent);
			reduceHealth(healthReducePercent);
		}
	}
	
	/**
	 * Reduces the health of the player.
	 * 
	 * @param reducePercent
	 *            An integer between 1 to 100
	 */
	public synchronized void reduceHealth(int reducePercent) {
		new Thread() {
			public void run() {
				health -= reducePercent;
				int reduceAmount = (int) ((double) (reducePercent / 100.0) * 117);

				int curX = healthBar.getX();
				int curY = healthBar.getY();

				for (int i = 1; i <= reduceAmount; i++) {
					curX--;
					healthBar.setLocation(curX, curY);

					try {
						Thread.sleep(10);
					} catch (Exception e) {
						System.out.println("error in sleeping");
					}
				}
				if (health <= 0)
				{
					//TODO die
					GamePanel.getGamePanel().gameOver();
				}
			}
		}.start();
	}

	/**
	 * Increases the health of the player.
	 * 
	 * @param increasePercent
	 *            An integer between 1 to 100
	 */
	public synchronized void increaseHealth(int increasePercent) {
		new Thread() {
			public void run() {
				health += increasePercent;
				if (health > 100)
					health = 100;

				int increaseAmount = (int) ((double) (increasePercent / 100.0) * 117);

				int curX = healthBar.getX();
				int curY = healthBar.getY();

				for (int i = 1; i <= increaseAmount; i++) {

					if (healthBar.getLocation().x == 26)
						break;

					curX++;
					healthBar.setLocation(curX, curY);

					try {
						Thread.sleep(10);
					} catch (Exception e) {
						System.out.println("error in sleeping");
					}
				}
			}
		}.start();
	}

	/**
	 * Reduces the shield of the player.
	 * 
	 * @param reducePercent
	 *            An integer between 1 to 100
	 */
	public synchronized void reduceShield(int reducePercent) {
		new Thread() {
			public void run() {

				shield -= reducePercent;
				if (shield < 0)
					shield = 0;

				int reduceAmount = (int) ((double) (reducePercent / 100.0) * 117);

				int curX = shieldBar.getX();
				int curY = shieldBar.getY();

				for (int i = 1; i <= reduceAmount; i++) {
					curX--;
					shieldBar.setLocation(curX, curY);

					try {
						Thread.sleep(10);
					} catch (Exception e) {
						System.out.println("error in sleeping");
					}
				}
			}
		}.start();
	}

	/**
	 * Increases the shield of the player.
	 * 
	 * @param increasePercent
	 *            An integer between 1 to 100
	 */
	public synchronized void increaseShield(int increasePercent) {
		new Thread() {
			public void run() {
				shield += increasePercent;
				if (shield > 100)
					shield = 100;

				int increaseAmount = (int) ((double) (increasePercent / 100) * 117);

				int curX = shieldBar.getX();
				int curY = shieldBar.getY();

				for (int i = 1; i <= increaseAmount; i++) {
					if (shieldBar.getLocation().x == 126)
						break;

					curX++;
					shieldBar.setLocation(curX, curY);

					try {
						Thread.sleep(10);
					} catch (Exception e) {
						System.out.println("error in sleeping");
					}
				}
			}
		}.start();
	}

	class GoosheRight extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public GoosheRight() {
			setSize(159, 27);
			setLocation(841, 0);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/goosheRight.png"));
			} catch (IOException e) {
				System.out.println("error in reading image gooshe");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

	class KeyPanel extends JPanel {
		private BufferedImage image;

		public KeyPanel() {
			setLocation(865, 8);
			setSize(19, 18);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/key.png"));
			} catch (IOException e) {
				System.err.println("error in reading key image!");
			}

		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

		public void addKey() {
			this.setVisible(true);
		}

		public void getKey() {
			this.setVisible(false);
		}

		public boolean hasKey() {
			return this.isVisible();
		}
	}

	class MoneyPanel extends JPanel {

		public JLabel moneyLabel;

		public MoneyPanel() {
			setLocation(903, 6);
			setSize(58, 20);
			setOpaque(false);
			setLayout(new GridLayout(1, 1));

			moneyLabel = new JLabel("0.0 $");
			moneyLabel.setForeground(Color.white);
			add(moneyLabel);
		}

		public void setMoneyLabel(int amount) {
			String money = Integer.toString(amount);
			moneyLabel.setText(money + ".0 $");
		}
	}

	class Gooshe extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public Gooshe() {
			setSize(40, 27);
			setLocation(0, 0);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/gooshe.png"));
			} catch (IOException e) {
				System.out.println("error in reading image gooshe");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

	class BarBackground extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public BarBackground(Point location) {
			setSize(121, 27);
			setLocation(location);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/BarBackground.png"));
			} catch (IOException e) {
				System.out.println("error in reading image barBAckground");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

	class HealthBar extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public HealthBar() {
			setSize(121, 27);
			setLocation(26, 0);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/HealthBar.png"));
			} catch (IOException e) {
				System.out.println("error in reading image HealthBar");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

	class ShieldBar extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public ShieldBar() {
			setSize(121, 27);
			setLocation(126, 0);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/ShieldBar.png"));
			} catch (IOException e) {
				System.out.println("error in reading image SheildBar");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

	class HealthText extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public HealthText() {
			setSize(121, 27);
			setLocation(26, 0);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/HealthText.png"));
			} catch (IOException e) {
				System.out.println("error in reading image SheildBar");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

	class ShieldText extends JPanel {

		private static final long serialVersionUID = 1L;

		BufferedImage image;

		public ShieldText() {
			setSize(121, 27);
			setLocation(126, 0);
			setOpaque(false);

			try {
				image = ImageIO.read(getClass().getResource(
						"/images/panel/ShieldText.png"));
			} catch (IOException e) {
				System.out.println("error in reading image SheildBar");
			}
		}

		public void paintComponent(Graphics g) {
			g.drawImage(image, 0, 0, null);
		}

	}

}
