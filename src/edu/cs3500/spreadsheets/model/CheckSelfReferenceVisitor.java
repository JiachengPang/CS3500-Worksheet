package edu.cs3500.spreadsheets.model;

public class CheckSelfReferenceVisitor implements CellVisitor<Boolean> {
  @Override
  public Boolean visitFunction(CellFunction func) {
    return false;
  }

  @Override
  public Boolean visitReference(CellReference ref) {
    return false;
  }

  @Override
  public Boolean visitValue(CellValue val) {
    return false;
  }

  @Override
  public Boolean visitDouble(DoubleValue num) {
    return false;
  }

  @Override
  public Boolean visitString(StringValue str) {
    return false;
  }

  @Override
  public Boolean visitBoolean(BooleanValue bool) {
    return false;
  }

  @Override
  public Boolean visitBlank(CellBlank blank) {
    return false;
  }
}
