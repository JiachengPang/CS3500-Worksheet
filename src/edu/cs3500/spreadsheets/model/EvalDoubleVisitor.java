package edu.cs3500.spreadsheets.model;

public class EvalDoubleVisitor implements CellVisitor<Double> {

  @Override
  public Double visitFunction(CellFunction func) {
    return func.evaluate().accept(this);
  }

  @Override
  public Double visitReference(CellReference ref) {
    throw new IllegalArgumentException("Cannot reduce a reference cell to a double.");
  }

  @Override
  public Double visitValue(CellValue val) {
    return val.accept(this);
  }

  @Override
  public Double visitDouble(DoubleValue num) {
    return num.getValue();
  }

  @Override
  public Double visitString(StringValue str) {
    throw new IllegalArgumentException("Cannot reduce a string cell to a double.");
  }

  @Override
  public Double visitBoolean(BooleanValue bool) {
    throw new IllegalArgumentException("Cannot reduce a boolean cell to a double.");
  }
}
