package edu.cs3500.spreadsheets.model;

/**
 * Represents a cell that contains a formula in a worksheet.
 * A formula is currently one of (expandable)
 *  - a value
 *  - a reference to a rectangular region of cells in the worksheet
 *  - a function applied to one or more formulas as its arguments
 * A value is currently one of (expandable)
 *  - a double
 *  - a boolean
 *  - a String
 */
public interface CellFormula {

  /**
   * Accepts a CellVisitor and perform a behavior dictated by the visitor.
   * @param visitor a CellVisitor
   * @return the result of performing the behavior
   */
  <S> S accept(CellVisitor<S> visitor);

  /**
   * Evaluates this cell, reducing it to a CellValue containing the final result of the evaluation.
   * @return a CellValue that contains the final result.
   */
  CellValue evaluate();

}
