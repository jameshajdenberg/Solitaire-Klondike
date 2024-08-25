package cs3500.klondike;

import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.Card;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Examplar class for the ExtendedModel tests.
 */
public class ExamplarExtendedModelTests {

  KlondikeModel whitehead;
  KlondikeModel limitedDraw;
  List<Card> deck;

  /**
   * Sorts the deck for a game of Klondike.
   * @return the sorted deck.
   */
  public static List<Card> sortDeck() {
    KlondikeModel model = new WhiteheadKlondike();
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
    whitehead = new WhiteheadKlondike();
    limitedDraw = new LimitedDrawKlondike(1);
    deck = sortDeck();
  }

  //0, 2
  @Test(expected = IllegalArgumentException.class)
  public void testWhiteheadMoveInvalidBuild() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveToFoundation(0, 0);
    whitehead.movePile(1, 1, 0);
    whitehead.movePile(1, 1, 0);
    whitehead.movePile(1, 2, 1);
  }

  //1
  @Test
  public void testLimitedDrawWorksBasically() {
    limitedDraw.startGame(deck, false, 9, 7);
    limitedDraw.discardDraw(); //recycle A
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle B
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
  }

  //2
  @Test
  public void testWhiteheadMoveToEmptyPile() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveToFoundation(0, 0); //move Ac to foundation
    whitehead.moveDraw(0); //move 8c to pile 0
    Assert.assertEquals(1, whitehead.getPileHeight(0)); //check that 8c did move
  }

  //0, 3
  @Test(expected = IllegalStateException.class)
  public void testIllegalWhiteheadMovePile() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.movePile(0, 1, 1); //move Ac to 2d on pile 1 (illegal!)
  }

  //4
  @Test(expected = IllegalStateException.class)
  public void testWhiteheadIllegalMoveCascade() {
    whitehead.startGame(deck, false, 7, 1);
    whitehead.moveToFoundation(0, 0); //move Ac to foundation
    whitehead.movePile(1, 1, 0); //move 2d to pile 0
    whitehead.movePile(1, 1, 0); //move Ah to 2d on pile 0
    whitehead.movePile(3, 1, 4); //move 5s to 6s on pile 4
    whitehead.movePile(3, 1, 4); //move 4s to 5s on pile 3
    whitehead.movePile(3, 1, 2); //move 3h to 4h on pile 2
    whitehead.movePile(0, 2, 2); //move 2d and Ah to 3h on pile 2 (illegal)
  }

  //1, 5
  @Test
  public void testLimitedDiscardDrawWorksCompletely() {
    limitedDraw.startGame(deck, false, 9, 7);

    limitedDraw.discardDraw(); //recycle A
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle B
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle C
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle D
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle E
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle F
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //recycle G
    Assert.assertEquals(7, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard A
    Assert.assertEquals(6, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard B
    Assert.assertEquals(5, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard C
    Assert.assertEquals(4, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard D
    Assert.assertEquals(3, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard E
    Assert.assertEquals(2, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard F
    Assert.assertEquals(1, limitedDraw.getDrawCards().size());
    limitedDraw.discardDraw(); //discard G
    Assert.assertEquals(0, limitedDraw.getDrawCards().size());
  }
}