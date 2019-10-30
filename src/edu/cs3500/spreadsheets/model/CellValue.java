package edu.cs3500.spreadsheets.model;

/**
 * Represents a cell that contains a value.
 * A value is currently one of (expandable)
 *  - a double
 *  - a boolean
 *  - a String
 */
public interface CellValue<S> extends CellFormula {

  /**
   * Get the value contained in the cell.
   * @return the value
   */
   S getValue();
}
