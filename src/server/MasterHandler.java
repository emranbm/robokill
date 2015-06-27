package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * A handler that listens to the master client and sends all received data from
 * it to the normal client. It's exactly the opposite of
 * {@link server.NormalHandler NormalHandler}
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public class MasterHandler extends Thread {

	private BufferedReader masterInput;
	private PrintWriter normalOutput;

	/**
	 * Actually, connects the masterClientReader to the
	 * normalClientOutputStream, so all data in master client stream is passed
	 * to normal client.
	 * 
	 * @param masterClientReader
	 * @param normalClientOutputStream
	 */
	public MasterHandler(BufferedReader masterClientReader,
			OutputStream normalClientOutputStream) {
		masterInput = masterClientReader;
		normalOutput = new PrintWriter(normalClientOutputStream);
	}

	@Override
	public void run() {
		while (true) {
			try {
				normalOutput.println(masterInput.readLine());
				normalOutput.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
