package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * A handler that listens to the normal client and sends all received data from
 * it to the master client. It's exactly the opposite of
 * {@link server.MasterHandler MasterHandler}
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public class NormalHandler extends Thread {
	private PrintWriter masterOutput;
	private BufferedReader normalInput;

	/**
	 * Actually, connects the normalClientReader to the
	 * masterClientOutputStream, so all data in normal client stream is passed
	 * to master client.
	 * 
	 * @param normalClientReader
	 * @param masterClientOutputStream
	 */
	public NormalHandler(BufferedReader normalClientReader,
			OutputStream masterClientOutputStream) {
		normalInput = normalClientReader;
		masterOutput = new PrintWriter(masterClientOutputStream);
	}

	@Override
	public void run() {
		while (true) {
			try {
				masterOutput.println(normalInput.readLine());
				masterOutput.flush();
				System.out.println("normal handler command.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
