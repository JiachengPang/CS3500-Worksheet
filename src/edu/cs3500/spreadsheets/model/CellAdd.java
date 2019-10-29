package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a cell that performs addition on its inputs.
 * String values and boolean values are treated as 0 in the process.
 */
public class CellAdd extends CellFunction {

  /**
   * Constructs a CellAdd with the given arguments.
   * @param args the given arguments
   */
  public CellAdd(List<CellFormula> args) {
    super(args);
  }

  @Override
  public DoubleValue evaluate() {
    Double result = 0.0;
    AddVisitor visitor = new AddVisitor();
    for (CellFormula cell : args) {
      result += cell.accept(visitor);
    }
    return new DoubleValue(result);
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitFunction(this);
  }
}
