package robokill;

/**
 * An enumerator for the type of the prize; Like: Money, Energy, Key, ... <br>
 * Note that the "Key" enumerator always should be the last item, because of
 * some implementation based on it. See the constructors of
 * {@link robokill.Prize Prize } class.
 * 
 * @author Mr. Coder
 * 
 * @version 1.1
 *
 */
public enum PrizeType {
	Money, Energy, Sheild, Weapon, Key;
}
