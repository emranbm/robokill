package useful;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

/**
 * A class to deal with images. Implemented methods for scaling.
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public abstract class ImageFactory {

	/**
	 * Scales an image into a custom width and height.
	 * 
	 * @param img
	 *            The image to be scaled.
	 * @param width
	 *            The desired width.
	 * @param height
	 *            The desired height.
	 * @return
	 */
	public static ImageIcon getScaledImageIcon(ImageIcon img, int width,
			int height) {
		return new ImageIcon(img.getImage().getScaledInstance(width, height,
				Image.SCALE_DEFAULT));
	}

	/**
	 * Scales an image into a custom width and height.
	 * 
	 * @param img
	 *            The image to be scaled.
	 * @param width
	 *            The desired width.
	 * @param height
	 *            The desired height.
	 * @return
	 */
	public static BufferedImage getScaledBufferedImage(BufferedImage img,
			int width, int height) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage after = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale((double) width / img.getWidth(),
				(double) height / img.getHeight());
		AffineTransformOp scaleOp = new AffineTransformOp(at,
				AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(img, after);

		return after;
	}
}
