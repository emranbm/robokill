package client;

import java.awt.Point;

import useful.Direction;

/**
 * A class for keeping server-client communications standard.
 * 
 * @author Mr. Coder
 * @version 1.0
 */
public abstract class CommunicationConstants {

	public static final String prizeAchievedCommand(int prizeId) {
		return "prize " + prizeId;
	}

	public static final String playerShootCommand(int playerId,
			Point targetPoint) {
		return "player " + playerId + " shoot " + targetPoint.x + " "
				+ targetPoint.y;
	}

	public static final String playerMoveCommand(int playerId,
			Direction moveDirection) {
		return "player " + playerId + " move " + moveDirection.toString();
	}

	public static final String enemyShootCommand(int enemyId, Point targetPoint) {
		return "player " + enemyId + " shoot " + targetPoint.x + " "
				+ targetPoint.y;
	}

	public static final String enemyMoveCommand(int enemyId,
			Direction moveDirection) {
		return "player " + enemyId + " move " + moveDirection.toString();
	}
}
