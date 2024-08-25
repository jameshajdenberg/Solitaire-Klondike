package cs3500.klondike;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.klondike.model.hw02.BasicCard;
import cs3500.klondike.model.hw02.Card;

/**
 * Examples and testing class for Card/BasicCard interface/class.
 */
public class CardTests {
  Card aceOfClubs;
  Card aceOfClubs2;
  Card aceOfHearts;
  Card twoOfClubs;
  Card twoOfHearts;
  Card threeOfSpades;
  Card fourOfDiamonds;
  Card fiveOfWhat;
  Card whatOfSpades;
  Card nothingCard;

  /**
   * To initialize some example cards for testing.
   */
  @Before
  public void initData() {
    //(♣, ♠, ♡, ♢).
    aceOfClubs = new BasicCard("A", "♣");
    aceOfClubs2 = new BasicCard("A", "♣");
    aceOfHearts = new BasicCard("A", "♡");
    twoOfClubs = new BasicCard("2", "♣");
    twoOfHearts = new BasicCard("2", "♡");
    threeOfSpades = new BasicCard("3", "♠");
    fourOfDiamonds = new BasicCard("4", "♢");
  }

  /**
   * Tests that equals is overridden and that two identical cards are equal.
   */

  @Test
  public void testEquals() {
    Assert.assertEquals(aceOfClubs, aceOfClubs2);
  }

  /**
   * Tests that equals is overridden and that two cards of dissimilar ranks are not equal.
   */

  @Test
  public void testNotEqualsSameSuit() {
    Assert.assertNotEquals(aceOfClubs, twoOfClubs);
  }

  /**
   * Tests that equals is overridden and that two cards of dissimilar suits are not equal.
   */

  @Test
  public void testNotEqualsSameRank() {
    Assert.assertNotEquals(aceOfClubs, aceOfHearts);
  }

  /**
   * Tests that an empty card will throw an IllegalArgumentException.
   */

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyCardThrowsException() {
    nothingCard = new BasicCard("", "");
  }

  /**
   * Tests that a card with an invalid suit will throw an IllegalArgumentException.
   */

  @Test(expected = IllegalArgumentException.class)
  public void testFiveOfWhatThrowsException() {
    fiveOfWhat = new BasicCard("5", "What");
  }

  /**
   * Tests that a card with an invalid rank will throw an IllegalArgumentException.
   */

  @Test(expected = IllegalArgumentException.class)
  public void testWhatOfSpadesThrowsException() {
    whatOfSpades = new BasicCard("What", "♠");
  }

  /**
   * Tests that toString() converts the card into a String displaying the rank and suit.
   */

  @Test
  public void testToString() {
    Assert.assertEquals("A♣", aceOfClubs.toString());
  }

  /**
   * Tests that suitValue() obtains the correct value assigned to the suit (Clubs) and is
   * divisible by that value.
   */

  @Test
  public void testSuitValueClubs() {
    Assert.assertEquals(0, aceOfClubs.suitValue() % 2);
    Assert.assertEquals(2, aceOfClubs.suitValue());
  }

  /**
   * Tests that suitValue() obtains the correct value assigned to the suit (Hearts) and is
   * divisible by that value.
   */

  @Test
  public void testSuitValueHearts() {
    Assert.assertEquals(0, twoOfHearts.suitValue() % 3);
    Assert.assertEquals(3, twoOfHearts.suitValue());
  }

  /**
   * Tests that suitValue() obtains the correct value assigned to the suit (Spades) and is
   * divisible by that value.
   */

  @Test
  public void testSuitValueSpades() {
    Assert.assertEquals(0, threeOfSpades.suitValue() % 2);
    Assert.assertEquals(4, threeOfSpades.suitValue());
  }

  /**
   * Tests that suitValue() obtains the correct value assigned to the suit (Diamonds) and is
   * divisible by that value.
   */

  @Test
  public void testSuitValueDiamonds() {
    Assert.assertEquals(0, fourOfDiamonds.suitValue() % 3);
    Assert.assertEquals(9, fourOfDiamonds.suitValue());
  }

  /**
   * Tests that hashCode() is overridden and that two similar cards have the same hashCode.
   */

  @Test
  public void testHashcodeEquals() {
    Assert.assertEquals(aceOfClubs.hashCode(), aceOfClubs2.hashCode());
  }

  /**
   * Tests that hashCode() is overridden and that two dissimilar cards
   * do not have the same hashCode.
   */

  @Test
  public void testHashcodeNotEqual() {
    Assert.assertNotEquals(aceOfClubs.hashCode(), aceOfHearts.hashCode());
  }
}