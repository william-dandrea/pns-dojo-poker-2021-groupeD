package cards;

/**
 * This class is a blueprint for cards that only registered a value (for simplification, the value is an integer
 * between two and fourteen where a value greater than ten is a figure)
 *
 * @author Gabriel Cogne
 * @author Yann CLODONG
 *
 * @version 1.0
 */

public class Card implements Comparable<Card> {
    // -- final statics variables --
    private final static String[] names = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "V", "D", "R", "A"};

    // -- Variables --
    private final Value value;
    private Color color;

    // -- Constructors --
    public Card (Value value) {
        this.value = value;
    }

    public Card (Value value, Color color) {
        this (value);
        this.color = color;
    }

    // -- Getters --
    public Value getValue () {
        return value;
    }

    public Color getColor () {
        return color;
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

        return getValue().compareTo(o.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  Card) {
            return ((Card) obj).getValue() == getValue() && ((Card) obj).getColor().equals(getColor());
        } else return false;
    }

    @Override
    public String toString () {
        return getDisplayableValue() + getColor();
    }

    // -- Other methods --
    /**
     * This method will give a displayable version of the value
     * for example you will have R for King instead of 13.
     * @return 2, 3, 4, 5, 6, 7, 8, 9, 10, V, D, R or A following the card value
     */
    public String getDisplayableValue () {
        return getValue().name();
    }
}
