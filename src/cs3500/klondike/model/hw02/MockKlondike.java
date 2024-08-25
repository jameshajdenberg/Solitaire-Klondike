package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mock implementation of a Klondike model.
 */
public class MockKlondike implements KlondikeModel {

  final StringBuilder log;

  /**
   * To represent the mock.
   * @param log to keep track of all used methods in the controller
   */
  public MockKlondike(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public List<Card> getDeck() {
    return new ArrayList<>();
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    // no need to append log, as the game running any other logs means the game has started.
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile) {
    log.append(String.format("Source pile = %d, numCards = %d, "
            + "destPile = %d\n", srcPile, numCards, destPile));
  }

  @Override
  public void moveDraw(int destPile) {
    log.append(String.format("destPile = %d\n", destPile));
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile) {
    log.append(String.format("Source pile = %d, foundationPile = %d\n", srcPile, foundationPile));
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) {
    log.append(String.format("foundationPile = %d\n", foundationPile));

  }

  @Override
  public void discardDraw() {
    log.append("Discarded top draw card\n");
  }

  @Override
  public int getNumRows() {
    return 0;
  }

  @Override
  public int getNumPiles() {
    return 0;
  }

  @Override
  public int getNumDraw() {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) {
    return 0;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) {
    return false;
  }

  @Override
  public Card getCardAt(int pileNum, int card) {
    return new BasicCard("", "");
  }

  @Override
  public Card getCardAt(int foundationPile) {
    return new BasicCard("", "");
  }

  @Override
  public List<Card> getDrawCards() {
    return new ArrayList<>();
  }

  @Override
  public int getNumFoundations() {
    return 0;
  }
}
