package client;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import robokill.Enemy;
import robokill.GamePanel;
import robokill.MainMenu;
import robokill.Prize;
import robokill.Valley;
import useful.Direction;

/**
 * A class to handle the communication with the server.
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public class ClientCore extends Thread {

	private static ClientCore This;

	/**
	 * Returns the client core, if exists. Otherwise, an instance of ClientCore
	 * will be created and returned.
	 * 
	 * @return The client core instance.
	 */
	public static ClientCore getClientCore() {
		if (This == null)
			This = new ClientCore();

		return This;
	}

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	private ClientCore() {
	}

	/**
	 * Connects to the server and receives the client type: master/normal.
	 * 
	 * @param serverName
	 *            The name of the server that is shown in the network.
	 * @return Client type. Returns null if connecting fails.
	 */
	public String connect(String serverName) {
		try {
			socket = new Socket(InetAddress.getByName(serverName), 5050);
			output = new PrintWriter(socket.getOutputStream());
			input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			return input.readLine();
		} catch (UnknownHostException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}

		return null;
	}

	int c = 1;

	/**
	 * Sends a command to the server, if possible. If not, nothing happens!
	 * 
	 * @param command
	 *            The command to be sent.
	 */
	public void sendCommand(String command) {
		try {
			output.println(command);
			output.flush();
		} catch (Exception e) {
			System.out.println(c++ + ": " + command);
		}
	}

	/**
	 * Starts the client core thread to listen to the server commands. Starts
	 * only if the client is connected to the server. Otherwise does nothing.
	 */
	@Override
	public synchronized void start() {
		// TODO uncomment below line!
		// if (socket != null && socket.isConnected() && !socket.isClosed())
		super.start();
	}

	@Override
	public void run() {
		GamePanel gamePanel = GamePanel.getGamePanel();
		System.out.println("Client core started!");
		while (true) {
			try {
				String command = input.readLine();

				System.out.println("Client: Command received: " + command);

				String[] attr = command.trim().split(" ");

				int id = -2;
				try {
					id = Integer.parseInt(attr[1]);
				} catch (ArrayIndexOutOfBoundsException e) {
				}

				switch (attr[0]) {
				case "prize":
					// prize achieved.
					Prize prize = gamePanel.getPrizeById(id);
					gamePanel.remove(prize);
					break;
				case "box":
					// box destroy.
					gamePanel.getBoxById(id).damage(100, true);
					break;
				case "player":
					// player command: shoot/move
					switch (attr[2]) {
					case "shoot":
						int x = Integer.parseInt(attr[3]);
						int y = Integer.parseInt(attr[4]);
						gamePanel.playerRobot2.shoot(new Point(x, y), true);
						break;
					case "move":
						gamePanel.playerRobot2.move(Direction.valueOf(attr[3]),
								true);
						break;
					case "destroy":
						// TODO better logic!
						gamePanel.playerRobot2.collidedWith(new Valley(0, 0, 0,
								0));
						break;
					}
					break;
				case "enemy":
					// enemy command: shoot/move
					Enemy enemy = gamePanel.getEnemyById(id);
					if (enemy == null)
						continue;
					switch (attr[2]) {
					case "shoot":
						int x = Integer.parseInt(attr[3]);
						int y = Integer.parseInt(attr[4]);
						enemy.shoot(new Point(x, y), true);
						break;
					case "move":
						enemy.move(Direction.valueOf(attr[3]), true);
						break;
					case "destroy":
						enemy.damage(100, true);
						break;
					}
					break;
				case "start":
					MainMenu.getMainMenu().playGame();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
