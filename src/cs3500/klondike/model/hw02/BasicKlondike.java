package cs3500.klondike.model.hw02;

import java.util.List;

import cs3500.klondike.model.hw04.AbstractKlondike;

/**
 * This is a stub implementation of the {@link KlondikeModel}
 * interface. You may assume that the actual implementation of BasicKlondike will have a
 * zero-argument (i.e. default) constructor, and that all the methods below will be
 * implemented.  You may not make any other assumptions about the implementation of this
 * class (e.g. what fields it might have, or helper methods, etc.).
 * 
 * <p>Once you've implemented all the constructors and methods on your own, you can
 * delete the placeholderWarning() method.
 */
public class BasicKlondike extends AbstractKlondike {

  /**
   * Initialize a game of Normal Klondike.
   */
  public BasicKlondike() {
    super();
  }

  @Override
  protected void initializeVisibleCards() {
    for (List<Card> pile : this.piles) {
      this.visibleCards.add(pile.get(pile.size() - 1));
    }
  }

  @Override
  protected void updateVisibleCards(int pile, int distFromEnd) {
    this.visibleCards.add(this.piles.get(pile).get(this.piles.get(pile).size() - distFromEnd));
  }

  /**
   * Return whether moving a card from pile to pile is legal, according to the rules of Basic
   * Klondike.
   *     @param srcPile  the 0-based index (from the left) of the pile to move a card
   *     @param numCards number of cards being moved from srcPile to destPile
   *     @param destPile destPile the 0-based index (from the left) of the destination pile for the
   *                     card
   *     @return whether the move is legal
   *     @throws IllegalStateException if the game hasn't been started yet
   *     @throws IllegalArgumentException if the supplied pile is empty or if the given coordinates
   *     are invalid
   */

  protected boolean isMoveLegal(int srcPile, int numCards, int destPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.getNumPiles() < srcPile || this.getNumPiles() < destPile
            || this.piles.get(srcPile).size() < numCards || 0 > numCards || 0 > destPile) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    else if (this.piles.get(srcPile).isEmpty()) {
      throw new IllegalStateException("Can't move from an empty pile");
    }

    int moveFromIdx = this.piles.get(srcPile).size() - numCards;
    int moveToIdx = this.piles.get(destPile).size() - 1;

    if (this.piles.get(destPile).isEmpty()) {
      return this.piles.get(srcPile).get(moveFromIdx).toString().contains("K");
    }

    return
            // black cards
            (this.piles.get(srcPile).get(moveFromIdx).suitValue() % 2 == 0
                    && this.piles.get(destPile).get(moveToIdx).suitValue() % 3 == 0
                    && this.piles.get(srcPile).get(moveFromIdx).rankValue()
                    == this.piles.get(destPile).get(moveToIdx).rankValue() - 1)

                    // red cards
                    || (this.piles.get(srcPile).get(moveFromIdx).suitValue() % 3 == 0
                    && this.piles.get(destPile).get(moveToIdx).suitValue() % 2 == 0
                    && this.piles.get(srcPile).get(moveFromIdx).rankValue()
                    == this.piles.get(destPile).get(moveToIdx).rankValue() - 1);
  }

  /**
   * Return whether moving a card from the draw pile to another pile is legal,
   * according to the rules of Klondike.
   *     @param pile the 0-based index (from the left) of the pile to move a card
   *     @return whether the move is legal
   *     @throws IllegalStateException if the game hasn't been started yet
   *     @throws IllegalArgumentException if the supplied pile is invalid
   */

  protected boolean isDrawMoveLegal(int pile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    else if (this.getNumPiles() < pile || 0 > pile) {
      throw new IllegalArgumentException("Invalid pile");
    }

    int moveToIdx = this.piles.get(pile).size() - 1;

    if (this.piles.get(pile).isEmpty()) {
      return this.drawPile.get(0).toString().contains("K") && !this.drawPile.isEmpty();
    }
    return  ((this.piles.get(pile).get(moveToIdx).suitValue() % 2 == 0
            && this.drawPile.get(0).suitValue() % 3 == 0
            && this.piles.get(pile).get(moveToIdx).rankValue()
            == this.drawPile.get(0).rankValue() + 1)

            // red cards
            || (this.piles.get(pile).get(moveToIdx).suitValue() % 3 == 0
            && this.drawPile.get(0).suitValue() % 2 == 0
            && this.piles.get(pile).get(moveToIdx).rankValue()
            == this.drawPile.get(0).rankValue() + 1)) && !this.drawPile.isEmpty();
  }
}