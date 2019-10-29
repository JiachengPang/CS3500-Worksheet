package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a cell that refers to one ore more cells.
 */
public class CellReference implements CellFormula {

  List<CellFormula> inputs;

  /**
   * Constructs a CellReference.
   * @param inputs some cells to refer to
   */
  public CellReference(List<CellFormula> inputs) {
    if (inputs == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.inputs = inputs;
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitReference(this);
  }

  @Override
  public CellValue evaluate() {
    return inputs.get(0).evaluate();
  }

  // method for string
}
