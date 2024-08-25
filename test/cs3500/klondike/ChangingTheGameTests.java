package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;

/**
 * Tests class for LimitedDraw, Whitehead, and Klondike.
 */

public class ChangingTheGameTests {
  KlondikeModel limited;
  KlondikeModel whitehead;
  List<Card> deck;
  KlondikeModel basic;
  KlondikeController controller;

  /**
   * Method to sort the provided deck by adding 52 cards from a standard deck of cards.
   * @return the sorted deck
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
    limited = new LimitedDrawKlondike(1);
    whitehead = new WhiteheadKlondike();
    basic = new BasicKlondike();
    deck = sortDeck();
  }

  @Test
  public void testKlondikeCreatorBasic() {
    StringReader in = new StringReader("mpf 1 1 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    controller.playGame(basic, sortDeck(),
            false, 7, 1);
    Assert.assertTrue(KlondikeCreator
            .create(KlondikeCreator.GameType.BASIC) instanceof BasicKlondike);
  }

  @Test
  public void testKlondikeCreatorWhitehead() {
    StringReader in = new StringReader("mpf 1 1 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    controller.playGame(whitehead, sortDeck(),
            false, 7, 1);
    Assert.assertTrue(KlondikeCreator
            .create(KlondikeCreator.GameType.WHITEHEAD) instanceof WhiteheadKlondike);
  }

  @Test
  public void testKlondikeCreatorLimited() {
    StringReader in = new StringReader("mpf 1 1 q");
    controller = new KlondikeTextualController(in, new StringBuilder());
    controller.playGame(limited, sortDeck(),
            false, 7, 1);
    Assert.assertTrue(KlondikeCreator
            .create(KlondikeCreator.GameType.LIMITED) instanceof LimitedDrawKlondike);
  }

  @Test(expected = IllegalStateException.class)
  public void testGameNotStartedExceptions() {
    //limited draw exceptions
    limited.discardDraw();
    limited.movePile(0, 1, 1);
    limited.moveDraw(0);
    limited.moveToFoundation(0, 0);
    limited.moveDrawToFoundation(0);
    limited.getNumRows();
    limited.getNumDraw();
    limited.isGameOver();
    limited.getScore();
    limited.getPileHeight(0);
    limited.isCardVisible(0, 0);
    limited.getCardAt(0, 0);
    limited.getCardAt(0);
    limited.getDrawCards();
    limited.getNumFoundations();

    // whitehead exceptions
    whitehead.discardDraw();
    whitehead.movePile(0, 1, 1);
    whitehead.moveDraw(0);
    whitehead.moveToFoundation(0, 0);
    whitehead.moveDrawToFoundation(0);
    whitehead.getNumRows();
    whitehead.getNumDraw();
    whitehead.isGameOver();
    whitehead.getScore();
    whitehead.getPileHeight(0);
    whitehead.isCardVisible(0, 0);
    whitehead.getCardAt(0, 0);
    whitehead.getCardAt(0);
    whitehead.getDrawCards();
    whitehead.getNumFoundations();
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawDiscardDrawFromEmptyDrawPile() {
    limited.startGame(sortDeck(), false, 7, 1);
    // cycles through the draw pile twice
    for (int i = 0; i < 48; i++) {
      limited.discardDraw();
    }
    limited.discardDraw();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawConstructorExceptions() {
    KlondikeModel badLimited1 = new LimitedDrawKlondike(-1);
    KlondikeModel badLimited2 = new LimitedDrawKlondike(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawStartGameArgumentExceptions() {
    limited.startGame(null, false, 7, 1);
    limited.startGame(new ArrayList<Card>(), false, 7, 1);
    limited.startGame(deck, false, 10, 1);
    limited.startGame(deck, false, 0, 1);
    limited.startGame(deck, false, 7, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawStartGameGameAlreadyStarted() {
    limited.startGame(deck, false, 7, 1);
    limited.startGame(deck, false, 7, 1);
  }

  @Test
  public void testLimitedDrawGetDeckWorksProperly() {
    Assert.assertTrue(limited.getDeck().toString().contains("A♣, A♡, A♠, A♢, 2♣, 2♡, 2♠, 2♢, 3♣, "
            + "3♡, 3♠, 3♢, 4♣, 4♡, 4♠, 4♢, 5♣, 5♡, 5♠, 5♢, 6♣, 6♡, 6♠, 6♢, 7♣, 7♡, 7♠, 7♢, 8♣, "
            + "8♡, 8♠, 8♢, 9♣, 9♡, 9♠, 9♢, 10♣, 10♡, 10♠, 10♢, J♣, J♡, J♠, J♢, Q♣, Q♡, Q♠, Q♢, K♣, "
            + "K♡, K♠, K♢"));
  }

  @Test
  public void testLimitedDrawMovePileWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, limited.getPileHeight(0));
    Assert.assertEquals(2, limited.getPileHeight(1));
    limited.movePile(0, 1, 1);
    Assert.assertEquals(0, limited.getPileHeight(0));
    Assert.assertEquals(3, limited.getPileHeight(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawMovePileArgumentExceptions() {
    limited.startGame(deck, false, 7, 1);
    // bad indices
    limited.movePile(-1, 1, 1);
    limited.movePile(0, -1, 1);
    limited.movePile(0, 1, -1);
    limited.movePile(0, 2, 1);
    limited.movePile(7, 1, 0);
    limited.movePile(0, 1, 7);
    limited.movePile(0, 0, -1);
    // moving to the same pile
    limited.movePile(0, 1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMovePileIllegalMove() {
    limited.startGame(deck, false, 7, 1);
    limited.movePile(1, 1, 0);
  }

  @Test
  public void testLimitedDrawMoveDrawWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    // move the Ace to a foundation
    limited.moveToFoundation(0, 0);
    for (int i = 0; i < 20; i++) {
      limited.discardDraw();
    }
    Assert.assertEquals("K♣", limited.getDrawCards().get(0).toString());
    limited.moveDraw(0);
    Assert.assertEquals("K♣", limited.getCardAt(0,
            limited.getPileHeight(0) - 1).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawMoveDrawInvalidDestPile() {
    limited.startGame(deck, false, 7, 1);
    limited.moveDraw(7);
    limited.moveDraw(-1);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMoveDrawIllegalMove() {
    limited.startGame(deck, false, 7, 1);
    limited.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMoveDrawEmptyDrawPile() {
    limited.startGame(deck, false, 7, 1);
    for (int i = 0; i < 48; i++) {
      limited.discardDraw();
    }
    limited.moveDraw(0);
  }

  @Test
  public void testLimitedDrawMoveToFoundationWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, limited.getPileHeight(0));
    Assert.assertNull(limited.getCardAt(0));
    limited.moveToFoundation(0, 0);
    Assert.assertEquals(0, limited.getPileHeight(0));
    Assert.assertEquals("A♣", limited.getCardAt(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawMoveToFoundationArgumentExceptions() {
    limited.startGame(deck, false, 7, 1);
    limited.moveToFoundation(-1, 0);
    limited.moveToFoundation(9, 0);
    limited.moveToFoundation(0, -1);
    limited.moveToFoundation(0, 4);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMoveToFoundationIllegalMove() {
    limited.startGame(deck, false, 7, 1);
    limited.moveToFoundation(1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMoveToFoundationEmptySrc() {
    limited.startGame(deck, false, 7, 1);
    limited.moveToFoundation(0, 0);
    limited.moveToFoundation(0, 0);
  }

  @Test
  public void testLimitedDrawMoveDrawToFoundationWorksProperly() {
    limited.startGame(deck, false, 2, 1);
    limited.moveToFoundation(0, 0);
    limited.moveToFoundation(1, 1);
    limited.moveToFoundation(1, 2);
    Assert.assertNull(limited.getCardAt(3));
    limited.moveDrawToFoundation(3);
    Assert.assertEquals("A♢", limited.getCardAt(3).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMoveDrawToFoundationEmptyDrawPile() {
    limited.startGame(deck, false, 7, 1);
    for (int i = 0; i < 48; i++) {
      limited.discardDraw();
    }
    limited.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawMoveDrawToInvalidFoundation() {
    limited.startGame(deck, false, 7, 1);
    limited.moveDrawToFoundation(-1);
    limited.moveDrawToFoundation(4);
  }

  @Test(expected = IllegalStateException.class)
  public void testLimitedDrawMoveDrawToIllegalFoundation() {
    limited.startGame(deck, false, 7, 1);
    limited.moveDrawToFoundation(0);
  }

  @Test
  public void testLimitedDrawGetNumRows() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(7, limited.getNumRows());
  }

  @Test
  public void testLimitedDrawGetNumPiles() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(7, limited.getNumPiles());
  }

  @Test
  public void testLimitedDrawGetNumDraw() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, limited.getNumDraw());
  }

  @Test
  public void testLimitedDrawIsGameOverEmptyDrawPile() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertFalse(limited.isGameOver());
    for (int i = 0; i < 48; i++) {
      limited.discardDraw();
    }
    Assert.assertTrue(limited.isGameOver());
  }

  @Test
  public void testLimitedDrawGetScoreWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(0, limited.getScore());
    limited.moveToFoundation(0, 0);
    Assert.assertEquals(1, limited.getScore());
  }

  @Test
  public void testLimitedDrawGetPileHeightWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, limited.getPileHeight(0));
    limited.moveToFoundation(0, 0);
    Assert.assertEquals(0, limited.getPileHeight(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawGetPileHeightInvalidPile() {
    limited.startGame(deck, false, 7, 1);
    limited.getPileHeight(-1);
    limited.getPileHeight(7);
  }

  @Test
  public void testLimitedDrawIsCardVisibleWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertTrue(limited.isCardVisible(0, 0));
    Assert.assertFalse(limited.isCardVisible(1, 0));
    Assert.assertTrue(limited.isCardVisible(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawIsCardVisibleInvalidPileInvalidCard() {
    limited.startGame(deck, false, 7, 1);
    limited.isCardVisible(-1, 0);
    limited.isCardVisible(7, 0);
    limited.isCardVisible(0, 1);
  }

  @Test
  public void testLimitedDrawGetCardAtWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals("A♣", limited.getCardAt(0, 0).toString());
    limited.moveToFoundation(0, 0);
    Assert.assertNull(limited.getCardAt(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawGetCardAtInvisibleCard() {
    limited.startGame(deck, false, 7, 1);
    limited.getCardAt(1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawGetCardInvalidCoords() {
    limited.startGame(deck, false, 7, 1);
    limited.getCardAt(-1, 1);
    limited.getCardAt(7, 1);
    limited.getCardAt(0, 1);
  }

  @Test
  public void testLimitedDrawGetCardAtFoundationWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertNull(limited.getCardAt(0));
    limited.moveToFoundation(0, 0);
    Assert.assertEquals("A♣", limited.getCardAt(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLimitedDrawGetCardAtInvalidFoundation() {
    limited.startGame(deck, false, 7, 1);
    limited.getCardAt(-1);
    limited.getCardAt(4);
  }

  @Test
  public void testLimitedDrawGetDrawCardsWorks1Card() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals("[8♣]", limited.getDrawCards().toString());
  }

  @Test
  public void testLimitedDrawGetDrawCardsWorks2Cards() {
    limited.startGame(deck, false, 7, 2);
    Assert.assertEquals("[8♣, 8♡]", limited.getDrawCards().toString());
    limited.discardDraw();
    Assert.assertEquals("[8♡, 8♠]", limited.getDrawCards().toString());
  }

  @Test
  public void testLimitedDrawGetDrawCardsWorksNoCards() {
    limited.startGame(deck, false, 7, 1);
    for (int i = 0; i < 48; i++) {
      limited.discardDraw();
    }
    Assert.assertEquals("[]", limited.getDrawCards().toString());
  }

  @Test
  public void testLimitedDrawGetNumFoundationsWorksProperly() {
    limited.startGame(deck, false, 7, 1);
    Assert.assertEquals(4, limited.getNumFoundations());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadStartGameArgumentExceptions() {
    whitehead.startGame(null, false, 7, 1);
    whitehead.startGame(new ArrayList<Card>(), false, 7, 1);
    whitehead.startGame(deck, false, 10, 1);
    whitehead.startGame(deck, false, 0, 1);
    whitehead.startGame(deck, false, 7, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadStartGameGameAlreadyStarted() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.startGame(deck, false, 7, 1);
  }

  @Test
  public void testWhiteheadGetDeckWorksProperly() {
    Assert.assertTrue(whitehead.getDeck().toString().contains("A♣, A♡, A♠, A♢, 2♣, 2♡, 2♠, 2♢, 3♣, "
            + "3♡, 3♠, 3♢, 4♣, 4♡, 4♠, 4♢, 5♣, 5♡, 5♠, 5♢, 6♣, 6♡, 6♠, 6♢, 7♣, 7♡, 7♠, 7♢, 8♣, "
            + "8♡, 8♠, 8♢, 9♣, 9♡, 9♠, 9♢, 10♣, 10♡, 10♠, 10♢, J♣, J♡, J♠, J♢, Q♣, Q♡, Q♠, Q♢, K♣, "
            + "K♡, K♠, K♢"));
  }

  @Test
  public void testWhiteheadMovePileWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(4, whitehead.getPileHeight(3));
    Assert.assertEquals(5, whitehead.getPileHeight(4));
    whitehead.movePile(3, 1, 4);
    Assert.assertEquals(3, whitehead.getPileHeight(3));
    Assert.assertEquals(6, whitehead.getPileHeight(4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadMovePileArgumentExceptions() {
    whitehead.startGame(deck, false, 7, 1);
    // bad indices
    whitehead.movePile(-1, 1, 1);
    whitehead.movePile(0, -1, 1);
    whitehead.movePile(0, 1, -1);
    whitehead.movePile(0, 2, 1);
    whitehead.movePile(7, 1, 0);
    whitehead.movePile(0, 1, 7);
    whitehead.movePile(0, 0, -1);
    // moving to the same pile
    whitehead.movePile(0, 1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMovePileIllegalMove() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.movePile(1, 1, 0);
  }

  @Test
  public void testWhiteheadMoveDrawWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    // move the Ace to a foundation
    whitehead.moveToFoundation(0, 0);
    whitehead.moveDraw(0);
    Assert.assertEquals("8♣", whitehead.getCardAt(0, 0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadMoveDrawInvalidDestPile() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveDraw(7);
    whitehead.moveDraw(-1);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveDrawIllegalMove() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveDrawEmptyDrawPile() {
    whitehead.startGame(deck, false, 7, 1);
    for (int i = 0; i < 24; i++) {
      whitehead.discardDraw();
    }
    whitehead.moveDraw(0);
  }

  @Test
  public void testWhiteheadMoveToFoundationWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, whitehead.getPileHeight(0));
    Assert.assertNull(whitehead.getCardAt(0));
    whitehead.moveToFoundation(0, 0);
    Assert.assertEquals(0, whitehead.getPileHeight(0));
    Assert.assertEquals("A♣", whitehead.getCardAt(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadMoveToFoundationArgumentExceptions() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveToFoundation(-1, 0);
    whitehead.moveToFoundation(9, 0);
    whitehead.moveToFoundation(0, -1);
    whitehead.moveToFoundation(0, 4);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveToFoundationIllegalMove() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveToFoundation(1, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveToFoundationEmptySrc() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveToFoundation(0, 0);
    whitehead.moveToFoundation(0, 0);
  }

  @Test
  public void testWhiteheadMoveDrawToFoundationWorksProperly() {
    whitehead.startGame(deck, false, 2, 1);
    whitehead.moveToFoundation(0, 0);
    whitehead.moveToFoundation(1, 1);
    whitehead.moveToFoundation(1, 2);
    Assert.assertNull(whitehead.getCardAt(3));
    whitehead.moveDrawToFoundation(3);
    Assert.assertEquals("A♢", whitehead.getCardAt(3).toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveDrawToFoundationEmptyDrawPile() {
    whitehead.startGame(deck, false, 7, 1);
    for (int i = 0; i < 24; i++) {
      whitehead.discardDraw();
    }
    whitehead.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadMoveDrawToInvalidFoundation() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveDrawToFoundation(-1);
    whitehead.moveDrawToFoundation(4);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveDrawToIllegalFoundation() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveDrawToFoundation(0);
  }

  @Test
  public void testWhiteheadGetNumRows() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(7, whitehead.getNumRows());
  }

  @Test
  public void testWhiteheadGetNumPiles() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(7, whitehead.getNumPiles());
  }

  @Test
  public void testWhiteheadGetNumDraw() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, whitehead.getNumDraw());
  }

  @Test
  public void testWhiteheadGetScoreWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(0, whitehead.getScore());
    whitehead.moveToFoundation(0, 0);
    Assert.assertEquals(1, whitehead.getScore());
  }

  @Test
  public void testWhiteheadGetPileHeightWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(1, whitehead.getPileHeight(0));
    whitehead.moveToFoundation(0, 0);
    Assert.assertEquals(0, whitehead.getPileHeight(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadGetPileHeightInvalidPile() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.getPileHeight(-1);
    whitehead.getPileHeight(7);
  }

  @Test
  public void testWhiteheadIsCardVisibleWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertTrue(whitehead.isCardVisible(0, 0));
    Assert.assertTrue(whitehead.isCardVisible(1, 0));
    Assert.assertTrue(whitehead.isCardVisible(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadIsCardVisibleInvalidPileInvalidCard() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.isCardVisible(-1, 0);
    whitehead.isCardVisible(7, 0);
    whitehead.isCardVisible(0, 1);
  }

  @Test
  public void testWhiteheadGetCardAtWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals("A♣", whitehead.getCardAt(0, 0).toString());
    whitehead.moveToFoundation(0, 0);
    Assert.assertNull(whitehead.getCardAt(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadGetCardInvalidCoords() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.getCardAt(-1, 1);
    whitehead.getCardAt(7, 1);
    whitehead.getCardAt(0, 1);
  }

  @Test
  public void testWhiteheadGetCardAtFoundationWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertNull(whitehead.getCardAt(0));
    whitehead.moveToFoundation(0, 0);
    Assert.assertEquals("A♣", whitehead.getCardAt(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadGetCardAtInvalidFoundation() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.getCardAt(-1);
    whitehead.getCardAt(4);
  }

  @Test
  public void testWhiteheadGetDrawCardsWorks1Card() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals("[8♣]", whitehead.getDrawCards().toString());
  }

  @Test
  public void testWhiteheadGetDrawCardsWorks2Cards() {
    whitehead.startGame(deck, false, 7, 2);
    Assert.assertEquals("[8♣, 8♡]", whitehead.getDrawCards().toString());
    whitehead.discardDraw();
    Assert.assertEquals("[8♡, 8♠]", whitehead.getDrawCards().toString());
  }

  @Test
  public void testWhiteheadGetNumFoundationsWorksProperly() {
    whitehead.startGame(deck, false, 7, 1);
    Assert.assertEquals(4, whitehead.getNumFoundations());
  }
}