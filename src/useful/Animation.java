package useful;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import robokill.GamePanel;

/**
 * For using this class you must place all your images(png type) in a folder and
 * rename them from 1.png !
 * 
 * @author HRM_SHAMS
 * @version 1.3
 */
public class Animation extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private BufferedImage[] images;
	private int imageNumber;
	private int delay;
	private int counter = 0;
	private int loopNumber;

	/**
	 * @param location
	 * @param size
	 * @param address
	 *            is the address of images that starts from src folder ! Example
	 *            : "/images/explosion/"
	 * @param imagesNumber
	 * @param delay
	 * @param loopNumber if loopNumber be 0 the loop is infinite !
	 */
	public Animation(Point location, Dimension size, String address,
			int imagesNumber, int delay, int loopNumber) {
		setSize(size);
		setLocation(location);
		setOpaque(false);

		this.delay = delay;
		this.imageNumber = imagesNumber;
		this.loopNumber = loopNumber;

		images = new BufferedImage[imagesNumber];

		for (int i = 0; i < imagesNumber; i++) {
			try {
				images[i] = ImageIO.read(getClass().getResource(
						address + (i + 1) + ".png"));
			} catch (IOException e) {
				System.err.println("Error in reading image file");
			}
		}

	}

	/**
	 * 
	 * @param address
	 * 				is the address of images that starts from src folder ! Example
	 *            	: "/images/explosion/"
	 * @param imagesNumber
	 * @param delay
	 * @param loopNumber if loopNumber be 0 the loop is infinite !
	 */
	public Animation(String address,int imagesNumber, int delay, int loopNumber) {
		setOpaque(false);

		this.delay = delay;
		this.imageNumber = imagesNumber;
		this.loopNumber = loopNumber;

		images = new BufferedImage[imagesNumber];

		for (int i = 0; i < imagesNumber; i++) {
			try {
				images[i] = ImageIO.read(getClass().getResource(
						address + (i + 1) + ".png"));
			} catch (IOException e) {
				System.err.println("Error in reading image file");
			}
		}

	}

	
	/**
	 * Starts the animation as a thread.
	 */
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(images[counter], 0, 0, null);
	}

	@Override
	public void run() {
		int i = 0;
		
		if (loopNumber == 0) //this is for fixing a bug!
			i = -1;
		
		while (i < loopNumber) {

			while (counter < imageNumber) {

				repaint();

				try {
					Thread.sleep(delay);
				} catch (Exception e) {
					System.err.println("Error in sleep!");
				}

				counter++;

			}
			counter = 0;

			if (loopNumber != 0)
				i++;
		}
		GamePanel.getGamePanel().remove(this);
	}
}
