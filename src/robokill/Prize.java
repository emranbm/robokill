package robokill;

<<<<<<< HEAD
/**
 * An element representing the prize concept to be added into
 * {@link robokill.Box Box}
 * 
 * @author Mr. Coder
 * 
 * @version 1.2
 */
=======
import java.util.Random;

>>>>>>> 37d5e7640140b8cf99d33b41b189fc1680565be9
public class Prize extends Element {

	private static final long serialVersionUID = 1L;

	public static final int PRIZE_WIDTH = 30, PRIZE_HEIGHT = 30;

	private PrizeType type;

	/**
	 * 
	 * @param x
	 *            X location of the prize.
	 * @param y
	 *            Y location of the prize.
	 * @param prizeType
	 *            The type of the prize.
	 * 
	 */
<<<<<<< HEAD
	public Prize(PrizeType prizeType, int id) {
		/*
		 * Initial location is set to (0,0) because the location of the prize
		 * will be modified in the Box constructor.
		 */
		super(0, 0, PRIZE_WIDTH, PRIZE_HEIGHT);
		setType(prizeType);
		setId(id);
	}

	// /**
	// *
	// * A prize with a random {@link robokill.PrizeType PrizeType}. The prize
	// may
	// * be Energy,Money,... but it definitely is not Key.
	// *
	// * @param x
	// * X location of the prize.
	// * @param y
	// * Y location of the prize.
	// */
	// public Prize() {
	// /*
	// * Initial location is set to (0,0) because the location of the prize
	// * will be modified in the Box constructor.
	// */
	// super(0, 0, PRIZE_WIDTH, PRIZE_HEIGHT);
	// PrizeType[] types = new PrizeType[PrizeType.values().length - 1];
	//
	// for (int i = 0; i < types.length; i++) {
	// types[i] = PrizeType.values()[i];
	// }
	//
	// setType(types[new Random().nextInt(types.length)]);
	// }
=======
	public Prize(int x, int y, PrizeType prizeType) {
		super(x, y, PRIZE_WIDTH, PRIZE_HEIGHT);
		setType(prizeType);
	}

	/**
	 * 
	 * A prize with a random {@link robokill.PrizeType PrizeType}. The prize may
	 * be Energy,Money,... but it definitely is not Key.
	 * 
	 * @param x
	 *            X location of the prize.
	 * @param y
	 *            Y location of the prize.
	 */
	public Prize(int x, int y) {
		super(x, y, PRIZE_WIDTH, PRIZE_HEIGHT);
		PrizeType[] types = new PrizeType[PrizeType.values().length - 1];

		for (int i = 0; i < types.length; i++) {
			types[i] = PrizeType.values()[i];
		}

		setType(types[new Random().nextInt(types.length)]);
	}
>>>>>>> 37d5e7640140b8cf99d33b41b189fc1680565be9

	/**
	 * Returns the prize type and removes itself from the GamePanel. See
	 * {@link robokill.PrizeType PrizeType}
	 * 
	 * @return The type of prize.
	 */
	public synchronized PrizeType achievePrize() {
		GamePanel.getGamePanel().remove(this);
		return type;
	}

	/**
	 * This method is private because the type of the prize should be set just
	 * in constructors.
	 * 
	 * @param type
	 *            The type of the prize.
	 */
	private void setType(PrizeType type) {
		this.type = type;
		System.out.println(type);
		switch (type) {
		case Energy:
			setImage("images/Prize energy.png");
			break;
		case Key:
			setImage("images/Prize key.png");
			break;
		case Money:
			setImage("images/Prize money.png");
			break;
		case Sheild:
			setImage("images/Prize shield.png");
			break;
		case Weapon:
			setImage("images/Prize weapon 1.png");
			break;
		}
	}
}
