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
import robokill.Prize;
import useful.Direction;

/**
 * A class to handle the communication with the server.
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public class ClientCore extends Thread {

	private String serverName;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	/**
	 * Creates an instance of {@link client.ClientCore ClientCore} with the
	 * given server name.
	 * 
	 * @param serverName
	 *            The name of the server that is shown in the network.
	 */
	public ClientCore(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Connects to the server and receives the client type: master/normal.
	 * 
	 * @return Client type. Returns null if connecting fails.
	 */
	public String connect() {

		try {
			socket = new Socket(InetAddress.getByName(serverName), 5050);
			input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream());
			return input.readLine();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Sends a command to the server.
	 * 
	 * @param command
	 *            The command to be sent.
	 */
	public void sendCommand(String command) {
		output.println(command);
	}

	/**
	 * Starts the client core thread to listen to the server commands. Starts
	 * only if the client is connected to the server. Otherwise does nothing.
	 */
	@Override
	public synchronized void start() {
		if (socket != null && socket.isConnected() && !socket.isClosed())
			super.start();
	}

	@Override
	public void run() {
		GamePanel gamePanel = GamePanel.getGamePanel();
		while (true) {
			try {
				String command = input.readLine();
				// TODO switch on command.
				String[] attr = command.trim().split(" ");

				int id = Integer.parseInt(attr[1]);

				switch (attr[0]) {
				case "prize":
					// prize remove.
					Prize prize = gamePanel.getPrizeById(id);
					gamePanel.remove(prize);
					break;
				case "player":
					// TODO player command: shoot/move
					break;
				case "enemy":
					// TODO enemy command: shoot/move
					Enemy enemy = gamePanel.getEnemyById(id);
					if (attr[2].equals("shoot")) {
						int x = Integer.parseInt(attr[3]);
						int y = Integer.parseInt(attr[4]);
						enemy.shoot(new Point(x, y));
					} else {
						enemy.move(Direction.valueOf(attr[3]));
					}
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
