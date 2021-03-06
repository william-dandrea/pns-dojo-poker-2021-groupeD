package hands;

import cards.Card;
import cards.Value;
import interaction.*;

import java.util.AbstractMap;

/**
 * This class is used to compare several hands. She will determine which is the better and why.
 *
 * @author Gabriel Cogne
 * @author Amine CHOUHABI
 * @author Yann CLODONG
 */

public class HandComparator {

    /**
     * This will determine who win following the known poker rules
     * @param h1 the first hand in
     * @param h2 the second hand in
     * @return a {@link Victory victory object} that describe who win and why
     */
    public Victory compare (Hand h1, Hand h2) throws Exception {
        if ((h1 == null) || (h2 == null))
            throw new NullPointerException("All hands must be initialized !!");

        Card valueHand1;
        Card valueHand2 = null;
        Value[] value_Hand1;
        Value[] value_Hand2 = null;

        // Poker rules
        if (((valueHand1 = h1.isStraightFlush()) != null) || ((valueHand2 = h2.isStraightFlush()) != null))
            // We have at least one straightFlush
            return refereeOnStraightFlush(h2, valueHand1, valueHand2);

        else if (((valueHand1 = h1.isSquare()) != null) || ((valueHand2 = h2.isSquare()) != null))
            // We have at least one quad
            return refereeOnQuad(h2, valueHand1, valueHand2);

        else if (((value_Hand1 = h1.getFull()) != null) || ((value_Hand2 = h2.getFull()) != null))
            // We have Full
            return refereeOnFull(h2, value_Hand1, value_Hand2);


        else if (((valueHand1 = h1.isFlush()) != null) || ((valueHand2 = h2.isFlush()) != null))
            // We have at least one flush
            return refereeOnFlush(h1, h2, valueHand1, valueHand2);

        else if (((valueHand1 = h1.isStraight()) != null) || ((valueHand2 = h2.isStraight()) != null))
            // We have at least one straight
            return refereeOnStraight(h2, valueHand1, valueHand2);

        else if (((valueHand1 = h1.getBrelan()) != null) || ((valueHand2 = h2.getBrelan()) != null))
            // We have at least one trip
            return refereeOnTrip(h2, valueHand1, valueHand2);

        else if ((h1.getDoublePairCards() != null) || (h2.getDoublePairCards() != null))
            // We have at least one two pair
            return refereeOnTwoPairs(h1, h2);

        else if (((valueHand1 = h1.getPairCards()) != null) || ((valueHand2 = h2.getPairCards()) != null))
            // We have at least one pair in a hand
            return refereeOnPair(h1, h2, valueHand1, valueHand2);

        else
            // We have nothing and we use the default referee rule
            return compareOnHighestCard(h1, h2);
    }

    /**
     * This will return the winner hand comparing them by the highest value
     * @param hand1 the first hand in
     * @param hand2 the second hand in
     * @return a {@link Victory victory object} that describe who win and why
     */
    private Victory compareOnHighestCard (Hand hand1, Hand hand2) {
        if (hand1.isEmpty() || hand2.isEmpty())
            return new Victory(Victorieu.egalite, ResultType.higherCard, null);

        Card highest1 = hand1.getHighestCard();
        Card highest2 = hand2.getHighestCard();

        if (highest1.compareTo(highest2) > 0) {
            return new Victory(Victorieu.main1, ResultType.higherCard, highest1.getValue());
        } else if (highest1.compareTo(highest2) < 0) {
            return new Victory(Victorieu.main2, ResultType.higherCard, highest2.getValue());
        }
        hand1.remove(highest1);
        hand2.remove(highest2);
        if (hand1.isEmpty() || hand2.isEmpty())
            return new Victory(Victorieu.egalite, ResultType.higherCard, highest1.getValue());
        else
            return compareOnHighestCard(hand1, hand2);
    }

    /**
     * This will return the winner hand comparing them by pair value
     * @param hand1 the first hand in
     * @param hand2 the second hand in
     * @return a {@link Victory victory object} that describe who win and why
     */
    private Victory refereeOnPair(Hand hand1, Hand hand2, Card valueHand1, Card valueHand2) {
        if (valueHand2 == null)
            valueHand2 = hand2.getPairCards();

        if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1, valueHand2);

            if (winner.equals(Victorieu.egalite)) {
                hand1.removeCardsOfValue(valueHand1);
                hand2.removeCardsOfValue(valueHand1);
                return compareOnHighestCard(hand1, hand2);
            }
            return new Victory(winner, ResultType.pair,
                    (winner.equals(Victorieu.main1) ? valueHand1.getValue() : valueHand2.getValue()));
        } else if (valueHand1 != null)
            return new Victory(Victorieu.main1, ResultType.pair, valueHand1.getValue());
        else
            return new Victory(Victorieu.main2, ResultType.pair, valueHand2.getValue());
    }

    /**
     * This is a method to referee two hand on trip rule
     * @param hand2 the second hand in input
     * @param valueHand1 the value of the trip in hand1, if exist (to buy some time)
     * @param valueHand2 the value of the trip in hand2, if exist (to buy some time)
     * @return a explication on win result
     * @throws Exception two trip can't have the same value in 52-card deck
     */
    private Victory refereeOnTrip (Hand hand2, Card valueHand1, Card valueHand2) throws Exception {
        if (valueHand2 == null)
            valueHand2 = hand2.getBrelan();

        if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1, valueHand2);

            if (winner.equals(Victorieu.egalite)) {
                throw new Exception("two same value trips in a four colors games is impossible");
            }
            return new Victory(winner, ResultType.brelan,
                    winner.equals(Victorieu.main1) ? valueHand1.getValue() : valueHand2.getValue());
        } else if (valueHand1 != null)
            return new Victory(Victorieu.main1, ResultType.brelan, valueHand1.getValue());
        else
            return new Victory(Victorieu.main2, ResultType.brelan, valueHand2.getValue());
    }

    /**
     * This is a method to referee two hand on quad rule
     * @param hand2 the second hand in input
     * @param valueHand1 the value of the quad in hand1, if exist (to buy some time)
     * @param valueHand2 the value of the quad in hand2, if exist (to buy some time)
     * @return a explication on win result
     * @throws Exception two quad can't have the same value in 52-card deck
     */
    private Victory refereeOnQuad (Hand hand2, Card valueHand1, Card valueHand2) throws Exception {
        if (valueHand2 == null)
            valueHand2 = hand2.isSquare();

        if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1, valueHand2);

            if (winner.equals(Victorieu.egalite)) {
                throw new Exception("two same value quads in a four colors games is impossible");
            }
            return new Victory(winner, ResultType.carre,
                    (winner.equals(Victorieu.main1) ? valueHand1.getValue() : valueHand2.getValue()));
        } else if (valueHand1 != null)
            return new Victory(Victorieu.main1, ResultType.carre, valueHand1.getValue());
        else
            return new Victory(Victorieu.main2, ResultType.carre, valueHand2.getValue());
    }

    /**
     * This is a method to referee two hand on two pair rule
     * @param hand1 the first hand in input
     * @param hand2 the second hand in input
     * @return a explication on win result
     */
    private Victory refereeOnTwoPairs (Hand hand1, Hand hand2) {
        AbstractMap.SimpleEntry<Card, Card> valueHand1 = hand1.getDoublePairCards();
        AbstractMap.SimpleEntry<Card, Card> valueHand2 = hand2.getDoublePairCards();

        if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1.getKey(), valueHand2.getKey());

            if (winner.equals(Victorieu.egalite)) {
                winner = compareCards(valueHand1.getValue(), valueHand2.getValue());
                if (winner.equals(Victorieu.egalite)) {
                    hand1.removeCardsOfValue(valueHand1.getKey());
                    hand1.removeCardsOfValue(valueHand1.getValue());
                    hand2.removeCardsOfValue(valueHand2.getKey());
                    hand2.removeCardsOfValue(valueHand2.getValue());
                    return compareOnHighestCard(hand1, hand2);
                }
            }
            return new TwoCardVictory(winner, ResultType.doublePair,
                    (winner.equals(Victorieu.main1) ? valueHand1.getKey().getValue() : valueHand2.getKey().getValue()),
                    (winner.equals(Victorieu.main1) ? valueHand1.getValue().getValue() : valueHand2.getValue().getValue()));
        } else if (valueHand1 != null)
            return new TwoCardVictory(Victorieu.main1, ResultType.doublePair,
                    valueHand1.getKey().getValue(), valueHand1.getValue().getValue());
        else
            return new TwoCardVictory(Victorieu.main2, ResultType.doublePair,
                    valueHand2.getKey().getValue(), valueHand2.getKey().getValue());
    }

    /**
     * This is a method to referee two hand on straight rule.<br>
     * Here the first hand is only describe by it's greatest value
     *
     * @param hand2 the second hand in input
     * @param valueHand1 the value of the straight in hand1, if exist (to buy some time)
     * @param valueHand2 the value of the straight in hand2, if exist (to buy some time)
     * @return a explication on win result
     */
    private Victory refereeOnStraight (Hand hand2, Card valueHand1, Card valueHand2) {
        if (valueHand2 == null)
            valueHand2 = hand2.isStraight();

        if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1, valueHand2);
            return new Victory(winner, ResultType.suite,
                    (winner.equals(Victorieu.main1) ? valueHand1.getValue() : valueHand2.getValue()));
        } else if (valueHand1 != null)
            return new Victory(Victorieu.main1, ResultType.suite, valueHand1.getValue());
        else
            return new Victory(Victorieu.main2, ResultType.suite, valueHand2.getValue());
    }

    /**
     * This is a method to referee two hand on flush rule
     *
     * @param hand1 the first hand in input
     * @param hand2 the second hand in input
     * @param valueHand1 the value of the flush in hand1, if exist (to buy some time)
     * @param valueHand2 the value of the flush in hand2, if exist (to buy some time)
     * @return a explication on win result
     */
    private Victory refereeOnFlush (Hand hand1, Hand hand2, Card valueHand1, Card valueHand2) {
        if (valueHand2 == null)
            valueHand2 = hand2.isFlush();

        if (hand1.isEmpty() && hand2.isEmpty())
            return new Victory(Victorieu.egalite, ResultType.couleur, null);
        else if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1, valueHand2);

            if (winner.equals(Victorieu.egalite)) {
                hand1.removeCardsOfValue(valueHand1);
                hand2.removeCardsOfValue(valueHand2);

                return refereeOnFlush(hand1, hand2, hand1.getHighestCard(), hand2.getHighestCard());
            }

            return new ColorVictory(winner, ResultType.couleur,
                    (winner.equals(Victorieu.main1) ? valueHand1.getValue() : valueHand2.getValue()),
                    (winner.equals(Victorieu.main1) ? valueHand1.getColor() : valueHand2.getColor()));
        } else if (valueHand1 != null)
            return new ColorVictory(Victorieu.main1, ResultType.couleur,
                    valueHand1.getValue(), valueHand1.getColor());
        else
            return new ColorVictory(Victorieu.main2, ResultType.couleur, valueHand2.getValue(),
                    valueHand2.getColor());
    }

    /**
     * This is a method to referee two hand on straight flush rule.<br>
     * Here the first hand is only describe by it's greatest value
     *
     * @param hand2 the second hand in input
     * @param valueHand1 the value of the straight flush in hand1, if exist (to buy some time)
     * @param valueHand2 the value of the straight flush in hand2, if exist (to buy some time)
     * @return a explication on win result
     */
    private Victory refereeOnStraightFlush (Hand hand2, Card valueHand1, Card valueHand2) {
        if (valueHand2 == null)
            valueHand2 = hand2.isStraightFlush();

        if ((valueHand1 != null) && (valueHand2 != null)) {
            Victorieu winner = compareCards(valueHand1, valueHand2);
            return new ColorVictory(winner, ResultType.quinteFlush,
                    (winner.equals(Victorieu.main1) ? valueHand1.getValue() : valueHand2.getValue()),
                    (winner.equals(Victorieu.main1) ? valueHand1.getColor() : valueHand2.getColor()));
        } else if (valueHand1 != null)
            return new ColorVictory(Victorieu.main1, ResultType.quinteFlush, valueHand1.getValue(),
                    valueHand1.getColor());
        else
            return new ColorVictory(Victorieu.main2, ResultType.quinteFlush, valueHand2.getValue(),
                    valueHand2.getColor());
    }

    /**
     * Compare two hand value and return who win if only the card value is took
     *
     * @param valueHand1 a card of the first hand
     * @param valueHand2 a card of the second hand
     * @return a winner, return draw if necessary
     */
    private Victorieu compareCards (Card valueHand1, Card valueHand2) {
        if (valueHand1.compareTo(valueHand2) > 0)
            return Victorieu.main1;
        else if (valueHand1.compareTo(valueHand2) < 0)
            return Victorieu.main2;
        else {
            return Victorieu.egalite;
        }
    }

    private Victory refereeOnFull(Hand hand2, Value[] valueHand1, Value[] valueHand2) throws Exception {
        if (valueHand2 == null){
            valueHand2 = hand2.getFull();
        }
        if (valueHand1 != null && valueHand2 != null){
            if (valueHand1[0] == valueHand2[0]){
                //on a egalite
                throw new Exception("error");
            }
            if (valueHand1[0].compareTo(valueHand2[0]) > 0) {
                //la main 1 gagne
                return new TwoCardVictory(Victorieu.main1,ResultType.full,
                        valueHand1[0],valueHand1[1]);
            }

            //la main 2 gagne
            return new TwoCardVictory(Victorieu.main2,ResultType.full,
                    valueHand2[0],valueHand2[1]);

        }
        else if (valueHand1 != null) {
            // la main 1 gagne
            return new TwoCardVictory(Victorieu.main1,ResultType.full,
                    valueHand1[0],valueHand1[1]);
        }
        else if (valueHand2 != null){
            //la main 2 gagne
            return new TwoCardVictory(Victorieu.main2,ResultType.full,
                    valueHand2[0],valueHand2[1]);
        }
        return null;
    }
}
