package edu.cs3500.spreadsheets.model;

import java.util.Arrays;

/**
 * Represents a cell that compares its first value to its second value.
 */
public class CellSmallerThan extends CellFunction {

  /**
   * Constructs a CellSmallerThan with exactly 2 inputs.
   * @param left
   * @param right
   */
  public CellSmallerThan(CellFormula left, CellFormula right) {
    super(Arrays.asList(left, right));
  }

  @Override
  public CellValue evaluate() {
    EvalDoubleVisitor visitor = new EvalDoubleVisitor();
    try {
      return new BooleanValue(args.get(0).accept(visitor) < args.get(1).accept(visitor));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("The given input cannot be reduced "
              + "to a number for comparison");
    }
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitFunction(this);
  }
}
