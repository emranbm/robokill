package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
		while (true) {
			try {
				String command = input.readLine();
				// TODO switch on command.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
