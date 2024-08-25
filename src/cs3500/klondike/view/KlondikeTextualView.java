package cs3500.klondike.view;

import java.io.IOException;

import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private Appendable appendable;

  /**
   * To represent a KlondikeModel's text view (for part 1).
   * @param model the KlondikeModel to display
   */
  public KlondikeTextualView(KlondikeModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    this.model = model;
  }

  /**
   * To represent a KlondikeModel's text view, to be used in the KlondikeController.
   * @param model the KlondikeModel to display
   * @param appendable the appendable to add onto for gameplay display
   */

  public KlondikeTextualView(KlondikeModel model, Appendable appendable) {
    this.model = model;
    this.appendable = appendable;
  }

  /**
   * toString() method to represent the Klondike game as a series of Strings.
   *
   * @return the Klondike game in String form
   */

  public String toString() {
    String drawCardsString = this.model.getDrawCards().toString();
    drawCardsString = drawCardsString.substring(1, drawCardsString.length() - 1);
    String finalMessage = "Draw: " + drawCardsString;
    finalMessage = finalMessage.concat(this.foundationToString());
    finalMessage = finalMessage.concat(this.cascadePilesToString());
    return finalMessage;
  }

  /**
   * Helper for toString, converts the foundation piles list to String.
   * @return the foundation piles in String form
   */
  private String foundationToString() {
    String message = "\n" + "Foundation: ";
    for (int i = 0; i < this.model.getNumFoundations(); i++) {
      if (this.model.getCardAt(i) == null && i < this.model.getNumFoundations() - 1) {
        message = message.concat("<none>, ");
      } else if (this.model.getCardAt(i) == null && i == this.model.getNumFoundations() - 1) {
        message = message.concat("<none>");
      } else if (this.model.getCardAt(i) != null && i == this.model.getNumFoundations() - 1) {
        message = message.concat(this.model.getCardAt(i).toString());
      }
      else if (this.model.getCardAt(i) != null) {
        message = message.concat(this.model.getCardAt(i).toString()) + ", ";
      }
    }
    message = message.concat("\n");
    return message;
  }

  /**
   * Helper for toString, converts the cascade piles list to String.
   * @return the cascade piles in String form
   */

  private String cascadePilesToString() {
    String message = "";
    for (int y = 0; y < this.model.getNumRows(); y++) {
      for (int x = 0; x < this.model.getNumPiles(); x++) {
        if (this.model.getPileHeight(x) == 0) {
          if (y == 0) {
            message = message.concat("  X");
          } else {
            message = message.concat("   ");
          }
        } else if (y < this.model.getPileHeight(x)) {
          if (!this.model.isCardVisible(x, y)) {
            message = message.concat("  ?");
          } else {
            if (this.model.getPileHeight(x) == 0) {
              message = message.concat(" X ");
            }
            if (this.model.getCardAt(x,y).toString().contains("10")) {
              message = message.concat(this.model.getCardAt(x, y).toString());
            }
            else {
              message = message.concat(" " + this.model.getCardAt(x, y).toString());
            }
          }
        } else {
          message = message.concat("   ");
        }
      }
      message = message.concat("\n");
    }
    return message;
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   */
  @Override
  public void render() {
    try {
      this.appendable.append(this.toString());
    } catch (IOException o) {
      throw new IllegalStateException("IOException caught");
    }
  }
}