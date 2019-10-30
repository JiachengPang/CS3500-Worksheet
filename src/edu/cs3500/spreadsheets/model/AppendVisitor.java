package edu.cs3500.spreadsheets.model;

public class AppendVisitor implements CellVisitor<StringValue> {


  @Override
  public StringValue visitFunction(CellFunction func) {
    return this.visitReference(new CellReference(func.getArgs()));
  }

  @Override
  public StringValue visitReference(CellReference ref) {
    String result = "";
    for(CellFormula cell : ref.inputs) {
      result += cell.accept(this).getValue();
    }
    return new StringValue(result);
  }

  @Override
  public StringValue visitValue(CellValue val) {
    return val.accept(this);
  }

  @Override
  public StringValue visitDouble(DoubleValue num) {
    return new StringValue(num.toString());
  }

  @Override
  public StringValue visitString(StringValue str) {
    return str;
  }

  @Override
  public StringValue visitBoolean(BooleanValue bool) {
    return new StringValue(bool.toString());
  }
}
