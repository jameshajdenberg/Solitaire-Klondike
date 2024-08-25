package cs3500.klondike;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.controller.KlondikeController;
import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw02.MockKlondike;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

/**
 * Further testing the Klondike controller.
 */

public class ControllerTests {

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

  @Test
  public void testMovePileToFoundation() throws IOException {
    Reader in = new StringReader("mpf 1 1");
    StringBuilder dontCareOutput = new StringBuilder();

    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);
    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, sortDeck(),false, 7, 2);
    Assert.assertEquals("Source pile = 0, foundationPile = 0\n",log.toString());
  }

  @Test
  public void testMoveDrawToFoundation() throws IOException {
    Reader in = new StringReader("mdf 1");
    StringBuilder dontCareOutput = new StringBuilder();

    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);
    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, sortDeck(),false, 7, 2);
    Assert.assertEquals("foundationPile = 0\n",log.toString());
  }

  @Test
  public void testDiscardDraw() throws IOException {
    Reader in = new StringReader("dd");
    StringBuilder dontCareOutput = new StringBuilder();

    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);
    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, sortDeck(),false, 7, 2);
    Assert.assertEquals("Discarded top draw card\n",log.toString());
  }

  @Test
  public void testMoveDrawToPile() throws IOException {
    Reader in = new StringReader("md 1");
    StringBuilder dontCareOutput = new StringBuilder();

    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);
    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, sortDeck(),false, 7, 2);
    Assert.assertEquals("destPile = 0\n",log.toString());
  }

  @Test
  public void testBadInput() throws IOException {
    Reader in = new StringReader("vb;jbsdfbjdsfgds");
    StringBuilder dontCareOutput = new StringBuilder();

    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);
    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, sortDeck(),false, 7, 2);
    Assert.assertEquals("",log.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() throws IOException {
    Reader in = new StringReader("vb;jbsdfbjdsfgds");
    StringBuilder dontCareOutput = new StringBuilder();

    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(null, sortDeck(),false, 7, 2);
  }

  @Test(expected = IllegalStateException.class)
  public void testEmptyDeck() throws IOException {
    Reader in = new StringReader("vb;jbsdfbjdsfgds");
    StringBuilder dontCareOutput = new StringBuilder();
    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);

    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, mock.getDeck(),false, 7, 2);
  }

  @Test
  public void testInvalidInputIsStillTakenIn() throws IOException {
    Reader in = new StringReader("mpp -1 0 1");
    StringBuilder dontCareOutput = new StringBuilder();
    StringBuilder log = new StringBuilder();
    KlondikeModel mock = new MockKlondike(log);

    KlondikeController c = new KlondikeTextualController(in, dontCareOutput);
    c.playGame(mock, sortDeck(),false, 7, 2);
    Assert.assertEquals("Source pile = -2, numCards = 0, destPile = 0\n", log.toString());
    // it still takes in the input, but will prompt the user to re-enter it when using an actual
    // model.
  }

  @Test
  public void testInvalidInputIsNotAccepted() throws IOException {
    KlondikeModel model = new BasicKlondike();
    Reader in = new StringReader("mpp -1 0 1");
    StringBuilder out = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(model, sortDeck(),false, 7, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again. Invalid coordinates"));
  }

  @Test
  public void testIllegalPileMoveIsNotAccepted() throws IOException {
    KlondikeModel model = new BasicKlondike();
    Reader in = new StringReader("mpp 2 1 1");
    StringBuilder out = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(model, sortDeck(),false, 7, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again. Illegal move"));
  }

  @Test
  public void testIllegalFoundationMoveIsNotAccepted() throws IOException {
    KlondikeModel model = new BasicKlondike();
    Reader in = new StringReader("mpf 2 1");
    StringBuilder out = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(model, sortDeck(),false, 7, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again. Move not allowed"));
  }

  @Test
  public void testIllegalDrawMoveIsNotAccepted() throws IOException {
    KlondikeModel model = new BasicKlondike();
    Reader in = new StringReader("md 1");
    StringBuilder out = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(model, sortDeck(),false, 7, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again. Move not allowed"));
  }

  @Test
  public void testIllegalDrawFoundationMoveIsNotAccepted() throws IOException {
    KlondikeModel model = new BasicKlondike();
    Reader in = new StringReader("mdf 1");
    StringBuilder out = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(model, sortDeck(),false, 7, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again. Move not allowed"));
  }

  @Test
  public void testMoveFromEmptySourcePile() throws IOException {
    KlondikeModel model = new BasicKlondike();
    Reader in = new StringReader("mpf 1 1 mpf 1 2");
    StringBuilder out = new StringBuilder();
    KlondikeController c = new KlondikeTextualController(in, out);
    c.playGame(model, sortDeck(),false, 7, 2);
    Assert.assertTrue(out.toString().contains("Invalid move. Play again. Empty source pile"));
  }

  @Test
  public void testRender() {
    KlondikeModel model = new BasicKlondike();
    Appendable a = new StringBuilder();
    TextualView view = new KlondikeTextualView(model, a);
    model.startGame(sortDeck(), false, 7, 2);
    view.render();
    Assert.assertTrue(a.toString().contains("Draw: 8♣, 8♡\n"
            + "Foundation: <none>, <none>, <none>, <none>\n"
            + " A♣  ?  ?  ?  ?  ?  ?\n"
            + "    2♢  ?  ?  ?  ?  ?\n"
            + "       4♡  ?  ?  ?  ?\n"
            + "          5♠  ?  ?  ?\n"
            + "             6♠  ?  ?\n"
            + "                7♡  ?\n"
            + "                   7♢\n"));
  }
}