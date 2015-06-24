package robokill;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class to save the properties of a room.
 * 
 * @author Mr. Coder
 * @version 1.0
 */
public class Room implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private ArrayList<Element> elements = new ArrayList<Element>();
	private ArrayList<Door> doors = new ArrayList<Door>();
	private Point playerLocation;

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

	public void setPlayerLocation(int x, int y) {
		playerLocation = new Point(x, y);
	}

	public void setPlayerLocation(Point playerLocation) {
		this.playerLocation = playerLocation;
	}
}
