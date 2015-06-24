package robokill;

import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A class to save the properties of a room.
 * 
 * @author Mr. Coder
 * @version 1.2
 */
public class Room implements Serializable {

	private static final long serialVersionUID = 3L;

	private int id;
	private ArrayList<Element> elements = new ArrayList<Element>();
	private ArrayList<Door> doors = new ArrayList<Door>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private Point playerLocation;
	private String backgroundImagePath;

	/**
	 * 
	 * @param id
	 *            A unique id to make rooms distinguishable.
	 */
	public Room(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public ArrayList<Door> getDoors() {
		return doors;
	}

	public Point getPlayerLocation() {
		return playerLocation;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void addElement(Element element) {
		elements.add(element);
	}

	public Image getBackgroundImage() {
		if (backgroundImagePath == null)
			return null;

		try {
			return ImageIO.read(getClass().getResource(
					"/" + backgroundImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
	}

	public void addDoor(Door door) {
		doors.add(door);
	}

	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}

	public void setPlayerLocation(int x, int y) {
		playerLocation = new Point(x, y);
	}

	public void setPlayerLocation(Point playerLocation) {
		this.playerLocation = playerLocation;
	}

	/**
	 * 
	 * @param backgroundImagePath
	 *            A relative path to an image. It is inside the src folder and
	 *            doesn't start with "/".
	 *            <p>
	 *            Example: "images/background.png"
	 */
	public void setBackgroundImagePath(String backgroundImagePath) {
		this.backgroundImagePath = backgroundImagePath;
	}

	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
}
