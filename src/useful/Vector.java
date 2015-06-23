package useful;

import java.awt.Point;

/**
 * A class representing the physical vector concept.
 * 
 * @author Mr. Coder
 * 
 * @version 1.0
 *
 */
public class Vector {

	private double width, height;

	public Vector(double width, double height) {
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	/**
	 * Makes a unit vector (with the size 1) from current vector.
	 * 
	 * @return Returns a new parallel vector with size 1.
	 */
	public Vector getUnitVector() {
		double size = Math.sqrt(width * width + height * height);

		return new Vector(width / size, height / size);
	}

	/**
	 * Makes a parallel vector with the given size.
	 * 
	 * @param newSize
	 *            The size of the new parallel vector.
	 * @return Returns the parallel vector.
	 */
	public Vector getParallelVector(double newSize) {
		Vector v = this.getUnitVector();
		return new Vector(v.width * newSize, v.height * newSize);
	}

	/**
	 * Transfers a point with the current vector.
	 * 
	 * @param initialPoint
	 *            The point to be transfered.
	 * @return The transfered point.
	 */
	public Point transfer(Point initialPoint) {
		Point p = new Point(initialPoint.x + (int) this.width, initialPoint.y
				+ (int) this.height);

		return p;
	}
}
