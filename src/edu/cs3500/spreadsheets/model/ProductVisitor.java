package edu.cs3500.spreadsheets.model;

/**
 * Represent a CellVisitor that multiplies the cell it visits
 * and returns a DoubleValue containing the final result.
 * StringValue and BooleanValue are treated as 1.
 */
public class ProductVisitor implements CellVisitor<DoubleValue> {

  @Override
  public DoubleValue visitFunction(CellFunction func) {
    Double result = 1.0;
    for (Cell cell : func.getArgs()) {
      result *= cell.evaluate().accept(this).getValue();
    }
    return new DoubleValue(result);
  }

  @Override
  public DoubleValue visitReference(CellReference ref) {
    Double result = 1.0;
    for (Cell cell : ref.getReferences()) {
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

  @Override
  public DoubleValue visitBlank(CellBlank blank) {
    return new DoubleValue(1.0);
  }
}
