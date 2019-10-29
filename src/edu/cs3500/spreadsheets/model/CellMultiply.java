package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a cell that multiplies its inputs.
 * String and boolean values are treated as 1.
 */
public class CellMultiply extends CellFunction {

  /**
   * Constructs a CellMultiply with the given arguments/
   * @param args the given arguments
   */
  public CellMultiply(List<CellFormula> args) {
    super(args);
  }

  @Override
  public CellValue evaluate() {
    Double result = 1.0;
    ProductVisitor visitor = new ProductVisitor();
    for (CellFormula cell : args) {
      result *= cell.accept(visitor);
    }
    return new DoubleValue(result);
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitFunction(this);
  }
}
