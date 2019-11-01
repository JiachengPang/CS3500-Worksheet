package edu.cs3500.spreadsheets.model;

/**
 * Represents a cell that contains a value.
 * A value is currently one of (expandable)
 *  - a double
 *  - a boolean
 *  - a String
 * @param <S> the type of the value
 */
public abstract class CellValue<S> extends Cell {

  S val;

  public CellValue(S val) {
    super();
    if (val == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    this.val = val;
  }

  /**
   * Get the value contained in the cell.
   * @return the value
   */
   public abstract S getValue();

}
