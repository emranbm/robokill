package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server of the RoboKill game. It manages the clients of the game.
 * <p>
 * Protocol: One of the two clients is master and another is normal. The master
 * client handles the smartness of the game such as movement of enemies by some
 * algorithms. But the normal client just receives the movement of enemies from
 * the server. So for example, in the normal client, the enemies move according
 * to the commands received from server; At the other hand, in master client
 * enemies move according to the algorithms and then their movement is sent to
 * server to be received by the normal client.
 * 
 * @author Mr. Coder
 * 
 * @version 1.0
 *
 */
public class Run {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5050);

			/* First client connects and sends its type: master OR normal */
			Socket client1 = serverSocket.accept();
			BufferedReader client1input = new BufferedReader(
					new InputStreamReader(client1.getInputStream()));
			String client1Type = client1input.readLine();

			/* Second client connects and sends its type: master OR normal */
			Socket client2 = serverSocket.accept();
			BufferedReader client2input = new BufferedReader(
					new InputStreamReader(client2.getInputStream()));
			String client2Type = client2input.readLine();

			serverSocket.close();

			/* A thread to handle the master client */
			MasterHandler masterHandler;

			/* A thread to handle the normal client */
			NormalHandler normalHandler;

			/* Checks if the clients are not both the same type */
			if (!client1Type.equals(client2Type)) {
				if (client1Type.equalsIgnoreCase("master")) {
					masterHandler = new MasterHandler(client1input,
							client2.getOutputStream());
					normalHandler = new NormalHandler(client1input,
							client2.getOutputStream());
				} else {
					masterHandler = new MasterHandler(client2input,
							client1.getOutputStream());
					normalHandler = new NormalHandler(client2input,
							client1.getOutputStream());
				}

				masterHandler.start();
				normalHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
