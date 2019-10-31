package edu.cs3500.spreadsheets.model;

/**
 * Represent a CellVisitor that appends the string representation of the cell it visits,
 * and returns a StringValue containing the final result.
 */
public class AppendVisitor implements CellVisitor<StringValue> {

  @Override
  public StringValue visitFunction(CellFunction func) {
    String result = "";
    for(Cell cell : func.getArgs()) {
      result += cell.evaluate().accept(this).getValue();
    }
    return new StringValue(result);
  }

  @Override
  public StringValue visitReference(CellReference ref) {
    String result = "";
    for(Cell cell : ref.getReferences()) {
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

  @Override
  public StringValue visitBlank(CellBlank blank) {
    return new StringValue("");
  }
}
