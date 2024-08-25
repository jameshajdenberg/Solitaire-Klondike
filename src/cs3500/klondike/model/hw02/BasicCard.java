package cs3500.klondike.model.hw02;

import java.util.HashMap;
import java.util.Map;

/**
 * To represent a basic card in a standard deck of cards.
 */
public class BasicCard implements Card {
  private final String cardNumber;
  private final String cardSuit;

  /**
   * To create the basic card for us.
   *     @param cardNumber is the card number. It's not an int, because of face cards (J, Q, K, A).
   *     @param cardSuit is the card suit (♣, ♠, ♡, ♢).
   *     @throws IllegalArgumentException if the card has an invalid rank.
   *     @throws IllegalArgumentException if the card has an invalid suit.
   */
  public BasicCard(String cardNumber, String cardSuit) {
    this.cardNumber = cardNumber;
    this.cardSuit = cardSuit;

    if (!this.cardNumber.equals("A") && !this.cardNumber.equals("2")
            && !this.cardNumber.equals("3") && !this.cardNumber.equals("4")
            && !this.cardNumber.equals("5") && !this.cardNumber.equals("6")
            && !this.cardNumber.equals("7") && !this.cardNumber.equals("8")
            && !this.cardNumber.equals("9") && !this.cardNumber.equals("10")
            && !this.cardNumber.equals("J") && !this.cardNumber.equals("Q")
            && !this.cardNumber.equals("K")) {
      throw new IllegalArgumentException("Your card has an invalid rank");
    }

    if (!this.cardSuit.equals("♣") && !this.cardSuit.equals("♠")
            && !this.cardSuit.equals("♡") && !this.cardSuit.equals("♢")) {
      throw new IllegalArgumentException("Card must have one of ♣, ♠, ♡, ♢");
    }
  }

  /**
   * Converts the card into a String.
   *     @return the card as a String.
   */
  public String toString() {
    return this.cardNumber + this.cardSuit;
  }

  /**
   * Assign a value to this card's suit. Black suits will have an even value, and red suits will
   * have a value divisible by 3. Clubs have a value of 2, Spades of 4, Hearts of 3, and
   * Diamonds of 9.
   *     @return this BasicCard's assigned suit value.
   */

  public int suitValue() {
    if (this.toString().contains("♣")) {
      return 2;
    }
    else if (this.toString().contains("♠")) {
      return 4;
    }
    else if (this.toString().contains("♡")) {
      return 3;
    }
    else if (this.toString().contains("♢")) {
      return 9;
    }
    throw new IllegalArgumentException("Your card has an invalid suit");
  }

  /**
   * Assigns a value to the rank based on how it's identified in a standard deck of cards.
   * I.E., A = 1, 2 = 2, 3 = 3, ..., J = 11, Q = 12, K = 13.
   *     @return the rank value of this BasicCard.
   */
  public int rankValue() {
    Map<String, Integer> rankValues = new HashMap<>();
    rankValues.put("A", 1);
    rankValues.put("2", 2);
    rankValues.put("3", 3);
    rankValues.put("4", 4);
    rankValues.put("5", 5);
    rankValues.put("6", 6);
    rankValues.put("7", 7);
    rankValues.put("8", 8);
    rankValues.put("9", 9);
    rankValues.put("10", 10);
    rankValues.put("J", 11);
    rankValues.put("Q", 12);
    rankValues.put("K", 13);
    if (rankValues.get(this.cardNumber) == null) {
      throw new IllegalArgumentException("Your card has an invalid rank");
    }
    return rankValues.get(this.cardNumber);
  }

  /**
   * Override the equals method for BasicCard.
   *     @param other the Object to compare this card to.
   *     @return whether this BasicCard is equal to that Object.
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof BasicCard) {
      BasicCard that = (BasicCard) other;
      return this.cardNumber.equals(that.cardNumber)
              && this.cardSuit.equals(that.cardSuit);
    }
    return false;
  }

  /**
   * Override the hashCode method for BasicCard.
   *    @return the new hashCode for BasicCard, i.e.
   *        the hashCode of the cardRank and of the cardSuit added together.
   */
  @Override
  public int hashCode() {
    return this.cardNumber.hashCode() + this.cardSuit.hashCode();
  }
}