package cards;

/**
 * This class is a blueprint for cards that only registered a value (for simplification, the value is an integer
 * between two and fourteen where a value greater than ten is a figure)
 *
 * @author
 * @author Gabriel Cogne
 * @author
 * @author
 *
 * @version 0.1
 */

public class Card implements Comparable<Card> {
    private int value;

    // -- Constructors --
    public Card (int value) {
        this.value = value;
    }

    // -- Getters --
    public int getValue () {
        return value;
    }

    // -- Override method --
    /**
     * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive integer
     * as this object is less than, equal to, or greater than the specified object.
     *
     * The comparator for this particular class is the value
     *
     * @param o - the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *          the specified object.
     *
     * @throws NullPointerException - if the specified object is null
     */
    @Override
    public int compareTo(Card o) throws NullPointerException {
        if (o == null) {
            throw new NullPointerException("compare can be done with a null object");
        }

        if (getValue() < o.getValue())
            return -1;
        else if (getValue() > o.getValue())
            return 1;
        else
            return 0;
    }
}