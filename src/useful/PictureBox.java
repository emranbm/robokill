package useful;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * An object to hold a picture or simply a color in it. C# concept.
 * 
 * @author Mr. Coder
 *
 */

public class PictureBox extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image img;
	private Color color;
	Dimension size;

	public PictureBox(BufferedImage img) {
		this.img = img;
		setSize(img.getWidth(), img.getHeight());
	}

	public PictureBox(Color color, Dimension size) {
		this.color = color;
		this.size = size;
		setSize(size);
	}

	public void setImage(BufferedImage image) {
		this.img = image;
		setSize(image.getWidth(), image.getHeight());
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		// super.paint(g);

		if (img != null)
			g.drawImage(img, 0, 0, this);
		else {
			g.setColor(color);
			g.fillRect(0, 0, size.width, size.height);
		}

	}
}
