package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a cell containing a function applied to one or more formulas as its inputs.
 */
public abstract class CellFunction implements CellFormula {

  List<CellFormula> args;

  /**
   * Constructs a function cell.
   * @param args list of inputs
   * @throws IllegalArgumentException if the given list is null or any entry in the list is null
   */
  public CellFunction(List<CellFormula> args) {
    if (args == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    for (CellFormula arg: args) {
      if (arg == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
    }
    this.args = args;
  }

}
