package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a cell that appends the string representations of its inputs.
 */
public class CellAppend extends CellFunction {

  /**
   * Constructs a CellAppend with the given arguments.
   * @param args the given arguments
   */
  public CellAppend(List<CellFormula> args) {
    super(args);
  }

  @Override
  public CellValue evaluate() {
    AppendVisitor visitor = new AppendVisitor();
    String result = "";
    for (CellFormula cell : args) {
      result += cell.accept(visitor);
    }
    return new StringValue(result);
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitFunction(this);
  }
}
