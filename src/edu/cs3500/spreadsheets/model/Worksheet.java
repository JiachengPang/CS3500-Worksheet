package edu.cs3500.spreadsheets.model;

/**
 * Represents a Worksheet model. The model keeps track of a grid with a collection of cells. It
 * provides several methods for users to manipulate the cells, including adding new cells, removing
 * existing cells, evaluating existing cells, and modifying existing cells.
 */
public interface Worksheet {

  /**
   * Clear all cells in the grid.
   */
  void clearAll();

  /**
   * Clear the cell corresponding to the given coordinate.
   *
   * @param col column of the cell
   * @param row row of the cell
   */
  void clear(int col, int row);

  /**
   * Set a cell to the given coordinate. This method can add new cells as well as replacing old
   * cells.
   *
   * @param col  column of the cell
   * @param row  row of the cell
   * @param cell the new cell
   * @throws IllegalStateException If the given cell will cause a cycle
   */
  void set(int col, int row, Cell cell);

  /**
   * Get the maximum index of rows currently in the grid.
   *
   * @return the current maximum index of rows
   */
  int getNumRows();

  /**
   * Get the maximum index of columns currently in the grid.
   *
   * @return the current maximum index of columns
   */
  int getNumCols();

  /**
   * Get the IValue at the given coordinate that contains the final result of
   * evaluating the cell at the given coordinate.
   *
   * @param col column of the cell
   * @param row row of the cell
   * @return the evaluated IValue at the given coordinate
   * @throws IllegalStateException if the cell at the given coordinate is blank
   */
  IValue getValueAt(int col, int row);

}
