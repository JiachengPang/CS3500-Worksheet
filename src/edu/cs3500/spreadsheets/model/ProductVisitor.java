package edu.cs3500.spreadsheets.model;

public class ProductVisitor implements CellVisitor<DoubleValue> {

  @Override
  public DoubleValue visitFunction(CellFunction func) {
    return this.visitReference(new CellReference(func.getArgs()));
  }

  @Override
  public DoubleValue visitReference(CellReference ref) {
    Double result = 1.0;
    for (CellFormula cell : ref.inputs) {
      result *= cell.accept(this).getValue();
    }
    return new DoubleValue(result);
  }

  @Override
  public DoubleValue visitValue(CellValue val) {
    return val.accept(this);
  }

  @Override
  public DoubleValue visitDouble(DoubleValue num) {
    return new DoubleValue(num.getValue());
  }

  @Override
  public DoubleValue visitString(StringValue str) {
    return new DoubleValue(1.0);
  }

  @Override
  public DoubleValue visitBoolean(BooleanValue bool) {
    return new DoubleValue(1.0);
  }
}
