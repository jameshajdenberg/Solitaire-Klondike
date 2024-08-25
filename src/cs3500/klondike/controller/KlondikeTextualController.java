package cs3500.klondike.controller;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

/**
 * Class to implement a functioning Klondike Controller.
 */

public class KlondikeTextualController implements KlondikeController {

  private Readable r;
  private Appendable a;
  private boolean quit;

  /**
   * To represent a Klondike Controller.
   *
   * @param r the readable input that the controller takes in
   * @param a the appendable output the controller displays
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Readable or appendable is null");
    }
    this.r = r;
    this.a = a;
    this.quit = false;
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                       int numDraw) {

    if (model == null) {
      throw new IllegalArgumentException("Invalid model");
    }
    if (deck.isEmpty()) {
      throw new IllegalStateException("Empty deck");
    }

    model.startGame(deck, shuffle, numPiles, numDraw);
    Scanner scan = new Scanner(r);
    String userInstruction;
    TextualView view = new KlondikeTextualView(model, a);
    while (!quit && !model.isGameOver()) {
      view.render();
      writeMessage("Score: " + model.getScore() + System.lineSeparator());
      if (scan.hasNext()) {
        userInstruction = scan.next();
        try {
          handleUserInput(model, userInstruction, scan);
          if (quit) {
            break;
          }
        } catch (InputMismatchException e) {
          writeMessage("Invalid move. Play again. " + e.getMessage() + System.lineSeparator());
          scan.next();
        }
      } else {
        if (quit) {
          break;
        }
        throw new IllegalStateException("No input provided");
      }
    }
    if (model.isGameOver()) {
      gameOverMessage(model);
      return;
    }
    quitMessage(model);
  }

  private void handleUserInput(KlondikeModel model, String userInstruction, Scanner scan) {
    try {
      int srcPile;
      int foundationPile;
      int destPile;
      int numCards;

      switch (userInstruction) {
        case "q":
        case "Q":
          quit = true;
          break;
        case "mpp":
          srcPile = parseIntFromScanner(scan) - 1;
          numCards = parseIntFromScanner(scan);
          destPile = parseIntFromScanner(scan) - 1;
          model.movePile(srcPile, numCards, destPile);
          break;
        case "md":
          destPile = parseIntFromScanner(scan) - 1;
          model.moveDraw(destPile);
          break;
        case "mpf":
          srcPile = parseIntFromScanner(scan) - 1;
          foundationPile = parseIntFromScanner(scan) - 1;
          model.moveToFoundation(srcPile, foundationPile);
          break;
        case "mdf":
          foundationPile = parseIntFromScanner(scan) - 1;
          model.moveDrawToFoundation(foundationPile);
          break;
        case "dd":
          model.discardDraw();
          break;
        default:
          writeMessage("Invalid move. Play again. Enter an actual command."
                  + System.lineSeparator());
      }
    } catch (IllegalArgumentException | IllegalStateException e) {
      if (!quit) {
        writeMessage("Invalid move. Play again. " + e.getMessage() + System.lineSeparator());
      }
    }
  }


  private void writeMessage(String message) throws IllegalStateException {
    try {
      a.append(message);

    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private void quitMessage(KlondikeModel model) throws IllegalStateException {
    TextualView view = new KlondikeTextualView(model, a);
    writeMessage("Game quit!\n");
    writeMessage("State of game when quit:\n");
    view.render();
    writeMessage("Score: " + model.getScore() + System.lineSeparator());
  }

  private void gameOverMessage(KlondikeModel model) throws IllegalStateException {
    TextualView view = new KlondikeTextualView(model, a);
    view.render();
    if (model.getScore() == model.getDeck().size() || !model.getDeck().isEmpty()) {
      writeMessage("You win!" + System.lineSeparator());
      return;
    }
    writeMessage("Game over. Score: " + model.getScore() + System.lineSeparator());
  }

  private int parseIntFromScanner(Scanner scan) {
    while (scan.hasNext()) {
      if (scan.hasNextInt()) {
        return scan.nextInt(); // Return the integer if available
      } else {
        String input = scan.next();
        if (input.equals("q")) {
          quit = true;
          return -1; // Return a dummy value (0) to indicate "q" input
        }
      }
    }
    return 0; // Return a dummy value (0) for other cases where input is missing
  }

}