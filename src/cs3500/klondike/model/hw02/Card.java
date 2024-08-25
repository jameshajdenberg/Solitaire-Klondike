package cs3500.klondike.model.hw02;

/**
 * This (essentially empty) interface marks the idea of cards.  You will need to
 * implement this interface in order to use your model.
 *
 * <p>The only behavior guaranteed by this class is its {@link Card#toString()} method,
 * which will render the card as specified in the assignment.
 *
 * <p>In particular, you <i>do not</i> know what implementation of this interface is
 * used by the Examplar wheats and chaffs, so your tests must be defined sufficiently
 * broadly that you do not rely on any particular constructors or methods of cards.
 */
public interface Card {

  /**
   * Renders a card with its value followed by its suit as one of
   * the following symbols (♣, ♠, ♡, ♢).
   * For example, the 3 of Hearts is rendered as {@code "3♡"}.
   *     @return the formatted card
   */
  String toString();

  /**
   * Overrides the equals() method for a Card object.
   *     @param other the given Object to compare this Card to.
   *     @return whether this Card equals that Object.
   */
  boolean equals(Object other);

  /**
   * Overrides the hashCode() method for a Card object.
   *     @return the new, assigned hashCode for this Card.
   */
  int hashCode();

  /**
   * Assign a value to this Card based on its suit. Spades and Clubs have even values,
   * and Hearts and Diamonds have values divisible by 3. Clubs have a value of 2, Spades of 4,
   * Hearts of 3, and Diamonds of 9.
   *     @return this Card's assigned suit value.
   */
  int suitValue();

  /**
   * Assign a value to this Card based on its rank in a standard deck of Cards.
   * A = 1, 2 = 2, 3 = 3, ..., J = 11, Q = 12, K = 13.
   *     @return this Card's assigned rank value.
   */

  int rankValue();
}
