package robokill;

public interface Damagable {

	/**
	 * A method called when the element is damaged by shoot or something else.
	 * 
	 * @param amount
	 *            The amount of energy/health to be reduced.
	 */
	void damage(int amount, boolean isServerCommand);
}
