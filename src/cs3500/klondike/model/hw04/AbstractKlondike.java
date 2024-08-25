package cs3500.klondike.model.hw04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Abstract Klondike class to represent the broader types of Klondike games.
 */
public abstract class AbstractKlondike implements KlondikeModel {
  protected List<Card> deck;
  protected List<List<Card>> piles;
  protected List<Card> drawPile;
  protected List<List<Card>> foundationPiles;
  protected int numDraw;
  protected List<Card> visibleCards;
  protected boolean gameStarted;

  /**
   * To represent the generalized/abstract Klondike game.
   */
  public AbstractKlondike() {
    this.deck = new ArrayList<>();
    this.piles = new ArrayList<>();
    this.drawPile = new ArrayList<>();
    this.foundationPiles = new ArrayList<>();
    this.numDraw = 1;
    this.visibleCards = new ArrayList<>();
    this.gameStarted = false;
  }

  @Override
  public List<Card> getDeck() {
    List<Card> deck = new ArrayList<>();
    String[] suits = {"♣", "♡", "♠", "♢"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    for (String rank : ranks) {
      for (String suit : suits) {
        Card card = new BasicCard(rank, suit);
        deck.add(card);
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    if (gameStarted) {
      throw new IllegalStateException("Game already started");
    }

    if (deck == null || deck.contains(null)) {
      throw new IllegalArgumentException("Deck cannot be null or contain null cards");
    }
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Can't have an empty deck");
    }

    int suitChecker = 0;
    for (Card card : deck) {
      if (card.suitValue() % 2 == 0) {
        suitChecker++;
      }
    }
    if (suitChecker == 52) {
      throw new IllegalArgumentException("Deck only has one color");
    }
    suitChecker = 0;
    for (Card card : deck) {
      if (card.suitValue() % 3 == 0) {
        suitChecker++;
      }
    }
    if (suitChecker == 52) {
      throw new IllegalArgumentException("Deck only has one color");
    }

    // Check if a full cascade cannot be dealt with the given sizes
    if (numPiles * (numPiles + 1) / 2 > deck.size()) {
      throw new IllegalArgumentException("The specified number of piles and draw size "
              + "cannot accommodate a full cascade with the given deck.");
    }

    // Check if numPiles or numDraw is invalid
    if (numPiles <= 0 || numDraw <= 0) {
      throw new IllegalArgumentException("Number of piles and number of cards to draw "
              + "must be greater than zero.");
    }
    this.numDraw = numDraw;
    this.deck = new ArrayList<>(deck); // Make a copy of the deck to avoid modifying the original
    if (shuffle) {
      Collections.shuffle(this.deck);
    }

    // Clear existing piles and draw pile
    this.piles.clear();
    this.drawPile.clear();

    // Create empty piles
    for (int i = 0; i < numPiles; i++) {
      this.piles.add(new ArrayList<>());
    }

    for (int i = 0; i < 4; i++) {
      this.foundationPiles.add(new ArrayList<>());
    }

    int deckIndex = 0;
    // Deal cards to the piles in the desired order
    for (int pileIndex = 0; pileIndex < this.piles.size(); pileIndex++) {
      for (int cardIndex = pileIndex; cardIndex < this.piles.size(); cardIndex++) {
        this.piles.get(cardIndex).add(this.deck.get(deckIndex));
        deckIndex++;
      }
    }

    // Whatever cards are left in the deck, add them to the draw pile
    while (deckIndex < this.deck.size()) {
      this.drawPile.add(this.deck.get(deckIndex));
      deckIndex++;
    }

    initializeVisibleCards();

    this.gameStarted = true;
  }


  protected abstract void initializeVisibleCards();

  protected abstract void updateVisibleCards(int pile, int distFromEnd);

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    } else if (srcPile < 0 || srcPile > this.getNumPiles() || destPile > this.getNumPiles()
            || numCards > this.piles.get(srcPile).size() || this.piles.get(srcPile).isEmpty()
            || numCards <= 0 || destPile < 0) {
      throw new IllegalArgumentException("Invalid coordinates");
    } else if (srcPile == destPile) {
      throw new IllegalArgumentException("Source pile can't be the same as destination");
    }
    List<Card> src = this.piles.get(srcPile);
    List<Card> dest = this.piles.get(destPile);

    if (this.isMoveLegal(srcPile, numCards, destPile)) {
      while (numCards > 0) {
        if (src.size() > 1) {
          updateVisibleCards(srcPile, 2);
        } else {
          updateVisibleCards(srcPile, 1);
        }
        dest.add(src.remove(src.size() - numCards));
        numCards--;
        if (!this.isCardVisible(destPile, dest.size() - 1)) {
          updateVisibleCards(destPile, 1);
        }
      }
    } else {
      throw new IllegalStateException("Illegal move");
    }
  }

  @Override
  public void moveDraw(int destPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    } else if (destPile > this.getNumPiles() - 1 || destPile < 0) {
      throw new IllegalArgumentException("Invalid pile number");
    } else if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Can't move from an empty draw pile");
    }
    List<Card> dest = this.piles.get(destPile);

    if (this.isDrawMoveLegal(destPile)) {
      dest.add(this.drawPile.remove(0));
      updateVisibleCards(destPile, 1);
    } else {
      throw new IllegalStateException("Move not allowed");
    }
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    } else if (foundationPile > 3 || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation number");
    } else if (srcPile > this.getNumPiles() - 1 || srcPile < 0) {
      throw new IllegalArgumentException("Invalid pile number");
    } else if (this.piles.get(srcPile).isEmpty()) {
      throw new IllegalStateException("Empty source pile");
    }
    List<Card> foundation = this.foundationPiles.get(foundationPile);
    List<Card> src = this.piles.get(srcPile);

    if (this.isFoundationMoveLegal(srcPile, foundationPile)) {
      foundation.add(src.get(src.size() - 1));
      src.remove(src.size() - 1);
      if (!src.isEmpty()) {
        updateVisibleCards(srcPile, 1);
      }
    } else {
      throw new IllegalStateException("Move not allowed");
    }
  }

  /**
   * Return whether moving a card from a pile to a foundation pile is legal, according to the rules
   * of Klondike.
   *
   * @param pile           the 0-based index (from the left) of the pile to move a card
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return whether the foundation move is legal
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the supplied pile is empty or if the given coordinates
   *                                  are invalid
   */

  protected boolean isFoundationMoveLegal(int pile, int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.foundationPiles.size() < foundationPile || this.getNumPiles() < pile
            || 0 > pile || 0 > foundationPile) {
      throw new IllegalArgumentException("Invalid coordinates");
    } else if (this.piles.get(pile).isEmpty()) {
      throw new IllegalArgumentException("Can't move from an empty pile");
    }

    List<Card> src = this.piles.get(pile);
    List<Card> foundation = this.foundationPiles.get(foundationPile);
    int moveFromIdx = src.size() - 1;
    int moveToIdx = foundation.size() - 1;

    if (foundation.isEmpty()) {
      return src.get(moveFromIdx).toString().contains("A");
    }
    return src.get(moveFromIdx).suitValue() == foundation.get(moveToIdx).suitValue()
            && src.get(moveFromIdx).rankValue() == foundation.get(moveToIdx).rankValue() + 1;
  }

  protected abstract boolean isMoveLegal(int srcPile, int numCards, int destPile);

  protected abstract boolean isDrawMoveLegal(int pile);

  /**
   * Return whether moving a card from the draw pile to a foundation pile is legal, according
   * to the rules of Klondike.
   *
   * @param foundationPile 0-based index (from the left) of the foundation pile
   * @return whether the foundation move is legal
   * @throws IllegalStateException    if the game hasn't been started yet
   * @throws IllegalArgumentException if the supplied foundation pile is invalid
   */

  protected boolean isDrawFoundationMoveLegal(int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    } else if (this.foundationPiles.size() < foundationPile || 0 > foundationPile) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }

    int moveToIdx = this.foundationPiles.get(foundationPile).size() - 1;

    if (this.foundationPiles.get(foundationPile).isEmpty()) {
      return this.drawPile.get(0).toString().contains("A") && !this.drawPile.isEmpty();
    }
    return (this.drawPile.get(0).suitValue()
            == this.foundationPiles.get(foundationPile).get(moveToIdx).suitValue()
            && this.drawPile.get(0).rankValue()
            == this.foundationPiles.get(foundationPile).get(moveToIdx).rankValue() + 1)
            && !this.drawPile.isEmpty();
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (drawPile.isEmpty()) {
      throw new IllegalStateException("No draw cards to move");
    }
    if (foundationPile > this.foundationPiles.size() - 1 || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation number");
    }
    if (this.isDrawFoundationMoveLegal(foundationPile)) {
      this.foundationPiles.get(foundationPile).add(this.drawPile.remove(0));
    } else {
      throw new IllegalStateException("Move not allowed");
    }
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.drawPile.isEmpty()) {
      throw new IllegalStateException("Can't discard from an empty draw pile");
    }
    if (this.numDraw > this.drawPile.size()) {
      this.numDraw = this.drawPile.size();
    }
    this.drawPile.add(this.drawPile.remove(0));
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    int rows = 0;
    for (int i = 0; i < this.piles.size(); i++) {
      rows = Math.max(rows, this.getPileHeight(i));
    }
    return rows;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return this.piles.size();
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.drawPile.size() < 2) {
      this.numDraw--;
    }
    return this.numDraw;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    boolean drawEmptyButGameStillPlayable = this.drawPile.isEmpty() && this.containsLegalMoves();
    boolean noMovesLeft = this.getScore() == this.deck.size() || this.drawPile.isEmpty();
    if (this.drawPile.isEmpty()) {
      return true;
    }

    return !drawEmptyButGameStillPlayable && noMovesLeft;
  }

  private boolean containsLegalMoves() {
    for (int i = 0; i < this.piles.size(); i++) {
      for (int j = 0; j < this.piles.size(); j++) {
        int cardCounter = this.piles.get(i).size();
        while (cardCounter > 0) {
          if (this.isMoveLegal(i, cardCounter, j)) {
            return true;
          } else {
            cardCounter--;
          }
        }
      }
    }
    for (int i = 0; i < this.piles.size(); i++) {
      for (int j = 0; j < this.foundationPiles.size(); j++) {
        while (!this.piles.get(i).isEmpty()) {
          if (this.isFoundationMoveLegal(i, j)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    int score = 0;
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    for (List<Card> foundationPile : this.foundationPiles) {
      for (Card card : foundationPile) {
        // if it's on the top of the pile
        if (foundationPile.indexOf(card) == foundationPile.size() - 1) {
          score += card.rankValue();
        }
      }
    }
    return score;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum > this.getNumPiles() - 1 || pileNum < 0) {
      throw new IllegalArgumentException("Invalid pile");
    }
    return this.piles.get(pileNum).size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    boolean nonEmptyInvalidIndices = card > this.getPileHeight(pileNum) - 1
            && !this.piles.get(pileNum).isEmpty();
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum < 0 || pileNum > this.getNumPiles() - 1
            || nonEmptyInvalidIndices || card < 0) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    if (this.piles.get(pileNum).get(card) == null) {
      throw new IllegalStateException("No card to check");
    }
    Card visCard = this.piles.get(pileNum).get(card);
    return this.visibleCards.contains(visCard);
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (pileNum < 0) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    if (pileNum > this.getNumPiles() - 1
            || this.piles.get(pileNum).size() < card || card < 0) {
      throw new IllegalArgumentException("Invalid coordinates");
    }
    if (this.piles.get(pileNum).isEmpty()) {
      return null;
    }
    if (!this.isCardVisible(pileNum, card)) {
      throw new IllegalArgumentException("Can't see card");
    }
    if (this.getNumRows() < 0) {
      throw new IllegalArgumentException("Invalid card");
    }
    return this.piles.get(pileNum).get(card);
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.foundationPiles.size() - 1 < foundationPile || foundationPile < 0) {
      throw new IllegalArgumentException("Invalid foundation pile");
    }
    if (this.foundationPiles.get(foundationPile).isEmpty()) {
      return null;
    }
    return this.foundationPiles.get(foundationPile)
            .get(this.foundationPiles.get(foundationPile).size() - 1);
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    if (this.getNumDraw() > 1 && this.drawPile.size() >= this.getNumDraw()) {
      return this.drawPile.subList(0, this.getNumDraw());
    } else if (this.drawPile.size() < this.getNumDraw()) {
      return this.drawPile.subList(0, this.drawPile.size());
    } else if (!this.drawPile.isEmpty()) {
      List<Card> singleDrawCardlist = new ArrayList<>();
      singleDrawCardlist.add(drawPile.get(0));
      return singleDrawCardlist;
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    if (!gameStarted) {
      throw new IllegalStateException("Game not started");
    }
    return this.foundationPiles.size();
  }
}