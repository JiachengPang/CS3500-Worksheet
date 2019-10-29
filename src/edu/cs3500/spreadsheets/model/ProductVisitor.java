package edu.cs3500.spreadsheets.model;

public class ProductVisitor implements CellVisitor<Double> {

  @Override
  public Double visitFunction(CellFunction func) {
    return (func.evaluate()).accept(this);
  }

  @Override
  public Double visitReference(CellReference ref) {
    Double result = 1.0;
    for (CellFormula cell : ref.inputs) {
      result *= cell.accept(this);
    }
    return result;
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
    return 1.0;
  }

  @Override
  public Double visitBoolean(BooleanValue bool) {
    return 1.0;
  }
}
