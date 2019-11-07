package edu.cs3500.spreadsheets.view;

/**
 * Renders a {@link edu.cs3500.spreadsheets.model.Worksheet} in some manner.
 */
public interface WorksheetView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   */
  void render();


  /**
   * Transmit an error message to the view, in case
   * the command could not be processed correctly
   *
   * @param error the error message
   */
  void showErrorMessage(String error);

}
