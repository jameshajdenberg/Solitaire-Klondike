package cs3500.klondike.view;

/** A marker interface for all text-based views, to be used in the Klondike game. */
public interface TextualView {

  /**
   * Converts this class into a text input.
   * @return the TextView as a String.
   */
  String toString();

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   */
  void render();

}
