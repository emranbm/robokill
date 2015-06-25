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
 * @version 1.4
 */
public class Animation extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private transient BufferedImage[] images;
	private int imageNumber;
	private int delay;
	private int counter = 0;
	private int loopNumber;
	private boolean deleteIt;
	private String address;

	/**
	 * @param location
	 * @param size
	 * @param address
	 *            is the address of images that starts from src folder ! Example
	 *            : "/images/explosion/"
	 * @param imagesNumber
	 * @param delay
	 * @param loopNumber
	 *            if loopNumber be 0 the loop is infinite !
	 */
	public Animation(Point location, Dimension size, String address,
			int imagesNumber, int delay, int loopNumber, boolean deleteIt) {
		setSize(size);
		setLocation(location);
		setOpaque(false);

		this.delay = delay;
		this.imageNumber = imagesNumber;
		this.loopNumber = loopNumber;
		this.deleteIt = deleteIt;

		loadImages(address, imagesNumber);

	}

	/**
	 * 
	 * @param address
	 *            is the address of images that starts from src folder ! Example
	 *            : "/images/explosion/"
	 * @param imagesNumber
	 * @param delay
	 * @param loopNumber
	 *            if loopNumber be 0 the loop is infinite !
	 */
	public Animation(String address, int imagesNumber, int delay,
			int loopNumber, boolean deleteIt) {
		setOpaque(false);

		this.delay = delay;
		this.imageNumber = imagesNumber;
		this.loopNumber = loopNumber;
		this.deleteIt = deleteIt;

		loadImages(address, imagesNumber);

	}

	/**
	 * Loads the images to the images array.
	 * 
	 * @param address
	 *            is the address of images that starts from src folder ! Example
	 *            : "/images/explosion/"
	 * @param imagesNumber
	 */
	private void loadImages(String address, int imagesNumber) {
		this.address = address;
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
	 * Reloads the animation images. This method is used for reloading the
	 * animation images when it is loaded from a file as a serializable. Since
	 * the BuffuredImage is not serializable, so after loading the element, the
	 * images are null should be reloaded.
	 */
	public void revalidateImages() {
		loadImages(address, imageNumber);
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

		if (loopNumber == 0) // this is for fixing a bug!
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

		if (deleteIt == true)
			GamePanel.getGamePanel().remove(this);
		else if (loopNumber == 1)
			counter = imageNumber - 1;
	}
}
