package robokill;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A class to save the properties of a room.
 * 
 * @author Mr. Coder
 * @version 1.3
 */
public class Room implements Serializable {

	private static final long serialVersionUID = 4L;

	private int id;
	private ArrayList<Element> elements = new ArrayList<Element>();
	private ArrayList<Door> doors = new ArrayList<Door>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Prize> prizes = new ArrayList<Prize>();
	private ArrayList<Box> boxes = new ArrayList<Box>();
	private Point masterPlayerLocation, normalPlayerLocation;
	private String backgroundImagePath;
	private int masterPlayerId, normalPlayerId;

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

	public Point getMasterPlayerLocation() {
		return masterPlayerLocation;
	}

	public Point getNormalPlayerLocation() {
		return normalPlayerLocation;
	}

	public int getMasterPlayerId() {
		return masterPlayerId;
	}

	public int getNormalPlayerId() {
		return normalPlayerId;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public BufferedImage getBackgroundImage() {
		if (backgroundImagePath == null)
			return null;

		try {
			return ImageIO.read(getClass().getResource(backgroundImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void addElement(Element element) {
		elements.add(element);
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

	public void addPrize(Prize prize) {
		prizes.add(prize);
	}

	public void setPrizes(ArrayList<Prize> prizes) {
		this.prizes = prizes;
	}

	public void addBox(Box box) {
		boxes.add(box);
	}

	public void setBoxes(ArrayList<Box> boxes) {
		this.boxes = boxes;
	}

	public void setMasterPlayerLocation(int x, int y) {
		masterPlayerLocation = new Point(x, y);
	}

	public void setMasterPlayerLocation(Point playerLocation) {
		masterPlayerLocation = playerLocation;
	}

	public void setNormalPlayerLocation(int x, int y) {
		normalPlayerLocation = new Point(x, y);
	}

	public void setNormalPlayerLocation(Point playerLocation) {
		normalPlayerLocation = playerLocation;
	}

	public void setMasterPlayerId(int masterPlayerId) {
		this.masterPlayerId = masterPlayerId;
	}

	public void setNormalPlayerId(int normalPlayerId) {
		this.normalPlayerId = normalPlayerId;
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
