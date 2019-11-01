package edu.cs3500.spreadsheets.model;

public class RemoveRefVisitor implements CellVisitor<Cell> {

  @Override
  public Cell visitFunction(CellFunction func) {
    return func;
  }

  @Override
  public Cell visitReference(CellReference ref) {
    for (Cell cell : ref.getReferences()) {
      ref.removeInterest(cell);
    }
    return ref;
  }

  @Override
  public Cell visitValue(CellValue val) {
    return val;
  }

  @Override
  public Cell visitDouble(DoubleValue num) {
    return num;
  }

  @Override
  public Cell visitString(StringValue str) {
    return str;
  }

  @Override
  public Cell visitBoolean(BooleanValue bool) {
    return bool;
  }

  @Override
  public Cell visitBlank(CellBlank blank) {
    return blank;
  }
}
