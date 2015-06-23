package robokill;

import java.util.Random;

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

	/**
	 * Returns the prize type and removes itself from the GamePanel. See
	 * {@link robokill.PrizeType PrizeType}
	 * 
	 * @return The type of prize.
	 */
	public PrizeType achievePrize() {
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
