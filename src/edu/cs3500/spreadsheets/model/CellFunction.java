package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cell containing a function applied to one or more formulas as its inputs.
 */
public class CellFunction implements CellFormula {

  private CellVisitor visitor;
  private List<CellFormula> args;

  /**
   * Constructs a function cell.
   * @param args list of inputs
   * @throws IllegalArgumentException if the given list is null or any entry in the list is null
   */
  public CellFunction(CellVisitor visitor, List<CellFormula> args) {
    if (args == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    for (CellFormula arg: args) {
      if (arg == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
    }
    this.args = args;
    this.visitor = visitor;
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitValue(this.evaluate());
  }

  @Override
  public CellValue evaluate() {
    return (CellValue) visitor.visitFunction(this);
  }

  public List<CellFormula> getArgs() {
    return new ArrayList<>(args);
  }

  /*
  @Override
  public CellValue evaluate() {
    CellReference ref = new CellReference(args);
    return ref.accept(visitor);
  }
   */
}
