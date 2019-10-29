package edu.cs3500.spreadsheets.model;

public class AppendVisitor implements CellVisitor<String> {

  @Override
  public String visitFunction(CellFunction func) {
    return func.evaluate().accept(this);
  }

  @Override
  public String visitReference(CellReference ref) {
    String result = "";
    for(CellFormula cell : ref.inputs) {
      result += cell.accept(this);
    }
    return result;
  }

  @Override
  public String visitValue(CellValue val) {
    return val.accept(this);
  }

  @Override
  public String visitDouble(DoubleValue num) {
    return num.getValue().toString();
  }

  @Override
  public String visitString(StringValue str) {
    return str.getValue();
  }

  @Override
  public String visitBoolean(BooleanValue bool) {
    return bool.getValue().toString();
  }
}
