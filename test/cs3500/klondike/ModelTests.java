package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

/**
 * To test the Klondike methods.
 */
public class ModelTests {

  KlondikeModel model;
  List<Card> deck;

  /**
   * Sorts a hypothetical deck specifically for this examplar implementation.
   *
   * @return a sorted deck
   */
  public static List<Card> sortDeck() {
    KlondikeModel model = new BasicKlondike();
    List<Card> deck = model.getDeck();
    String[] suits = {"♣", "♡", "♠", "♢"};
    String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    List<Card> sortedDeck = new ArrayList<>();
    for (String rank : ranks) {
      for (String suit : suits) {
        for (Card card : deck) {
          if (card.toString().contains(rank) && card.toString().contains(suit)) {
            sortedDeck.add(card);
          }
        }
      }
    }
    return sortedDeck;
  }

  @Before
  public void initKlondike() {
    model = new BasicKlondike();
    deck = sortDeck();
  }

  // "♣", "♡", "♠", "♢"

  @Test
  public void testTextViewToString() {
    TextualView text = new KlondikeTextualView(model);
    model.startGame(deck, false, 7, 2);
    Assert.assertEquals("Draw: 8♣, 8♡\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♣  ?  ?  ?  ?  ?  ?\n"
            + "    2♢  ?  ?  ?  ?  ?\n"
            + "       4♡  ?  ?  ?  ?\n"
            + "          5♠  ?  ?  ?\n"
            + "             6♠  ?  ?\n"
            + "                7♡  ?\n"
            + "                   7♢\n", text.toString());
    model.movePile(2, 1, 3);
    Assert.assertEquals("Draw: 8♣, 8♡\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♣  ?  ?  ?  ?  ?  ?\n"
            + "    2♢ 3♣  ?  ?  ?  ?\n"
            + "           ?  ?  ?  ?\n"
            + "          5♠  ?  ?  ?\n"
            + "          4♡ 6♠  ?  ?\n"
            + "                7♡  ?\n"
            + "                   7♢\n", text.toString());
    model.movePile(2, 1, 3);
    Assert.assertEquals("Draw: 8♣, 8♡\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♣  ? A♠  ?  ?  ?  ?\n"
            + "    2♢     ?  ?  ?  ?\n"
            + "           ?  ?  ?  ?\n"
            + "          5♠  ?  ?  ?\n"
            + "          4♡ 6♠  ?  ?\n"
            + "          3♣    7♡  ?\n"
            + "                   7♢\n", text.toString());
    model.moveToFoundation(2, 0);
    Assert.assertEquals("Draw: 8♣, 8♡\n"
            + "Foundation: A♠, <none>, <none>, <none>\n"
            + " A♣  ?  X  ?  ?  ?  ?\n"
            + "    2♢     ?  ?  ?  ?\n"
            + "           ?  ?  ?  ?\n"
            + "          5♠  ?  ?  ?\n"
            + "          4♡ 6♠  ?  ?\n"
            + "          3♣    7♡  ?\n"
            + "                   7♢\n", text.toString());
  }

  // "♣", "♡", "♠", "♢"
  @Test
  public void testStartGame() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals(1, model.getPileHeight(0));
    Assert.assertEquals(2, model.getPileHeight(1));
    Assert.assertEquals(3, model.getPileHeight(2));
    Assert.assertEquals(4, model.getPileHeight(3));
    Assert.assertEquals(5, model.getPileHeight(4));
    Assert.assertEquals(6, model.getPileHeight(5));
    Assert.assertEquals(7, model.getPileHeight(6));
    Assert.assertEquals(24, model.getNumDraw());
    Assert.assertEquals("A♣", model.getCardAt(0, 0).toString());
    Assert.assertEquals("2♢", model.getCardAt(1, 1).toString());
    Assert.assertEquals("8♣", model.getDrawCards().get(0).toString());
    Assert.assertEquals(4, model.getNumFoundations());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThrowExceptionIfCascadeLessThanOne() {
    model.startGame(deck, false, 0, 2);
  }

  @Test(expected = IllegalStateException.class)
  public void testGameAlreadyStarted() {
    model.startGame(deck, false, 1, 2);
    model.startGame(deck, false, 1, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testThrowExceptionIfDrawLessThanOne() {
    model.startGame(deck, false, 7, 0);
  }

  @Test
  public void testDeckShuffled() {
    model.startGame(deck, true, 7, 2);
    Assert.assertNotEquals("A♣", model.getCardAt(0, 0).toString());
  }

  @Test
  public void testGetDeck() {
    Assert.assertFalse(model.getDeck().isEmpty());
    Assert.assertEquals("A♣", model.getDeck().get(0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testSuitMatches() {
    model.startGame(deck, false, 7, 2);
    model.moveToFoundation(0, 0);
    model.moveToFoundation(1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testNotStartedGameMovePile() {
    model.movePile(0, 1, 2);
  }

  @Test
  public void testGeneralMovePileWorksCorrectly() {
    model.startGame(deck, false, 7, 24);
    model.movePile(4, 1, 5);
    Assert.assertEquals(7, model.getPileHeight(5));
    Assert.assertEquals(4, model.getPileHeight(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidSrcPile() {
    model.startGame(deck, false, 7, 24);
    model.movePile(8, 3, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveFromEmptySrcPile() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(0, 0);
    model.movePile(0, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidDestPile() {
    model.startGame(deck, false, 7, 24);
    model.movePile(0, 3, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidNumCards() {
    model.startGame(deck, false, 7, 24);
    model.movePile(0, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileZeroNumCards() {
    model.startGame(deck, false, 7, 24);
    model.movePile(0, 0, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testGameNotStartedMoveToFoundation() {
    model.moveToFoundation(0, 0);
  }

  @Test
  public void testMovePileToEmptyFoundation() {
    model.startGame(deck, false, 7, 24);
    Card testCard = model.getCardAt(0, 0);
    model.moveToFoundation(0, 0);
    Assert.assertEquals(model.getCardAt(0), testCard);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveNotAceToEmptyFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(1, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveInvalidCardToFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(0, 0);
    model.moveToFoundation(1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveNegativePileToFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveInvalidPileToFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(8, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileToInvalidFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(0, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileToNegativeFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(0, -1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveFromEmptySrcPileToFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(0, 0);
    model.moveToFoundation(0, 0);
  }

  // need to test for a moveToFoundation that works on a non-empty foundation

  @Test(expected = IllegalStateException.class)
  public void testGameNotStartedMoveDraw() {
    model.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidDrawCardToPile() {
    model.startGame(deck, false, 7, 24);
    model.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawFromEmptyDrawPile() {
    model.startGame(deck, false, 7, 24);
    for (int i = 0; i < 24; i++) {
      model.discardDraw();
    }
    model.moveDraw(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawToInvalidPile() {
    model.startGame(deck, false, 7, 24);
    model.moveDraw(9);
  }

  @Test(expected = IllegalStateException.class)
  public void testGameNotStartedMoveDrawToFoundation() {
    model.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawToNonexistentFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveDrawToFoundation(5);
  }

  @Test
  public void testMoveDrawToNonEmptyFoundation() {
    model.startGame(deck, false, 2, 2);
    model.moveToFoundation(0, 0);
    model.moveDrawToFoundation(1);
    model.moveDrawToFoundation(0);
    Assert.assertTrue(model.getCardAt(0).toString().contains("2"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveIllegalDrawToFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawGameNotStarted() {
    model.discardDraw();
  }

  @Test
  public void testDiscardDrawActuallyWorks() {
    model.startGame(deck, false, 7, 3);
    Assert.assertEquals("8♣", model.getDrawCards().get(0).toString());
    model.discardDraw();
    Assert.assertEquals("8♡", model.getDrawCards().get(0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumRowsGameNotStarted() {
    model.getNumRows();
  }

  @Test
  public void testGetNumRowsActuallyWorks() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals(7, model.getNumRows());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumPilesGameNotStarted() {
    model.getNumPiles();
  }

  @Test
  public void testGetNumPilesActuallyWorks() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals(7, model.getNumPiles());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumDrawGameNotStarted() {
    model.getNumDraw();
  }

  @Test
  public void testGetNumDrawActuallyWorks() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals(24, model.getNumDraw());
  }

  @Test(expected = IllegalStateException.class)
  public void testIsGameOverGameNotStarted() {
    model.isGameOver();
  }

  @Test
  public void testIsGameOverNoItsNot() {
    model.startGame(deck, false, 7, 24);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverYesItIs() {
    deck.clear();
    deck.add(new BasicCard("A", "♣"));
    model.startGame(deck, false, 1, 1);
    model.moveToFoundation(0, 0);
    Assert.assertTrue(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetScoreGameNotStarted() {
    model.getScore();
  }

  @Test
  public void testGetScore() {
    model.startGame(deck, false, 2, 1);
    Assert.assertEquals(0, model.getScore());
    model.moveToFoundation(0, 0);
    Assert.assertEquals(1, model.getScore());
    model.moveToFoundation(1, 1);
    Assert.assertEquals(2, model.getScore());
    model.moveToFoundation(1, 2);
    Assert.assertEquals(3, model.getScore());
    model.moveDrawToFoundation(3);
    Assert.assertEquals(4, model.getScore());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetPileHeightGameNotStarted() {
    model.getPileHeight(0);
  }

  @Test
  public void testGetPileHeight() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals(1, model.getPileHeight(0));
    Assert.assertEquals(2, model.getPileHeight(1));
    Assert.assertEquals(3, model.getPileHeight(2));
    Assert.assertEquals(4, model.getPileHeight(3));
    Assert.assertEquals(5, model.getPileHeight(4));
    Assert.assertEquals(6, model.getPileHeight(5));
    Assert.assertEquals(7, model.getPileHeight(6));
    // test if the pile height registers a change
    model.moveToFoundation(0, 0);
    Assert.assertEquals(0, model.getPileHeight(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsCardVisibleGameNotStarted() {
    model.isCardVisible(0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidPileNumber() {
    model.startGame(deck, false, 7, 24);
    model.isCardVisible(9, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidCardNumber() {
    model.startGame(deck, false, 7, 24);
    model.isCardVisible(0, 2);
  }

  @Test
  public void testIsCardVisibleNoItsNot() {
    model.startGame(deck, false, 7, 24);
    Assert.assertFalse(model.isCardVisible(1, 0));
  }

  @Test
  public void testIsCardVisibleYesItIs() {
    model.startGame(deck, false, 7, 24);
    Assert.assertTrue(model.isCardVisible(0, 0));
    Assert.assertTrue(model.isCardVisible(1, 1));
  }

  @Test
  public void testIsCardVisibleAfterMovement() {
    model.startGame(deck, false, 7, 24);
    Assert.assertFalse(model.isCardVisible(2, 1));
    model.movePile(2, 1, 3);
    Assert.assertTrue(model.isCardVisible(2, 1));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCardAtPileGameNotStarted() {
    model.getCardAt(0, 1);
  }

  @Test
  public void testGetCardAtPile() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals("A♣", model.getCardAt(0, 0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtPileInvalidCard() {
    model.startGame(deck, false, 7, 24);
    model.getCardAt(0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtPileInvalidPile() {
    model.startGame(deck, false, 7, 24);
    model.getCardAt(7, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtPileInvisibleCard() {
    model.startGame(deck, false, 7, 24);
    model.getCardAt(1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCardAtFoundationGameNotStarted() {
    model.getCardAt(0);
  }

  @Test
  public void testGetCardAtFoundation() {
    model.startGame(deck, false, 7, 24);
    model.moveToFoundation(0, 0);
    Assert.assertEquals("A♣", model.getCardAt(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCardAtFoundationInvalidCoordinate() {
    model.startGame(deck, false, 7, 24);
    model.getCardAt(4);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetDrawCardsGameNotStarted() {
    model.getDrawCards();
  }

  @Test
  public void testGetDrawCards() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals("8♣", model.getDrawCards().get(0).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNumFoundationsGameNotStarted() {
    model.getNumFoundations();
  }

  @Test
  public void testGetNumFoundations() {
    model.startGame(deck, false, 7, 24);
    Assert.assertEquals(4, model.getNumFoundations());
  }
}