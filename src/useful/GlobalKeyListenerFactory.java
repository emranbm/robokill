package useful;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A Class that can make a {@link java.awt.event.KeyListener KeyListener}
 * globally responsible. So that any keyboard event occurred in any part of the
 * application will be dispatched to the KeyListener.
 * 
 * @author Mr. Coder (Emran BatmanGhelich)
 * 
 * @version 1.0
 *
 */

public class GlobalKeyListenerFactory {

	/**
	 * Makes a {@link java.awt.event.KeyListener KeyListener} globally
	 * responsible.
	 * 
	 * @param keyListener
	 *            The keyListener to get globally responsible.
	 * 
	 */
	public static void setKeyListenerGlobal(KeyListener keyListener) {

		KeyboardFocusManager keyBoardFocusManager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		keyBoardFocusManager.addKeyEventDispatcher(new GlobalDispatcher(
				keyListener));

	}

	// Custom dispather
	private static class GlobalDispatcher implements KeyEventDispatcher {

		private static KeyListener listener;

		public GlobalDispatcher(KeyListener listener) {

			GlobalDispatcher.listener = listener;
		}

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {

			// Switch to decide which event should be called.
			switch (e.getID()) {
			case KeyEvent.KEY_PRESSED:
				listener.keyPressed(e);
				break;
			case KeyEvent.KEY_RELEASED:
				listener.keyReleased(e);
				break;
			case KeyEvent.KEY_TYPED:
				listener.keyTyped(e);
				break;
			}

			// Allow the event to be redispatched
			return false;
		}
	}
}
