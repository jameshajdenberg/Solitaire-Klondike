package cs3500.klondike.controller;

import java.io.InputStreamReader;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * Class to manually test the Klondike Controller.
 */

public class BasicKlondikeProgram {

  /**
   * Main class to run the Klondike Controller and Model.
   * @param args to take in the inputs
   */
  public static void main(String []args) {
    KlondikeModel model = new BasicKlondike();
    Readable rd = new InputStreamReader(System.in);
    Appendable ap = System.out;
    KlondikeController controller = new KlondikeTextualController(rd,ap);
    controller.playGame(model, model.getDeck(), true, 7, 2);
  }
}
