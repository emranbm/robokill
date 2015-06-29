package robokill;


/**
 * An element representing a non-passable block.
 * 
 * @author Mr. Coder
 *
 * @version 1.0
 */
public class Block extends Element implements Damagable{

	private static final long serialVersionUID = 1L;

	public static final int BLOCK_WIDTH = 100, BLOCK_HEIGHT = 100;

	public static final int BLOCK_TYPE_1 = 1, BLOCK_TYPE_2 = 2,
			BLOCK_TYPE_3 = 3;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param type
	 *            The shape of the block.
	 */
	public Block(int x, int y, int type) {
		super(x, y, BLOCK_WIDTH, BLOCK_HEIGHT, "images/Block " + type + ".png");
	}

	@Override
	public void damage(int amount, boolean isServerCommand) {
		// Nothing happens!
	}

}
