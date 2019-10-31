package edu.cs3500.spreadsheets.model;

public class SmallerThanVisitor implements CellVisitor<BooleanValue> {

  @Override
  public BooleanValue visitFunction(CellFunction func) {
    if (func.getArgs().size() != 2) {
      throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
    }
    return new BooleanValue((Double) func.getArgs().get(0).evaluate().getValue() <
            (Double) func.getArgs().get(1).evaluate().getValue());
    // this casting is risky might throw
  }

  @Override
  public BooleanValue visitReference(CellReference ref) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }

  @Override
  public BooleanValue visitValue(CellValue val) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }

  @Override
  public BooleanValue visitDouble(DoubleValue num) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }

  @Override
  public BooleanValue visitString(StringValue str) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }

  @Override
  public BooleanValue visitBoolean(BooleanValue bool) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }

  @Override
  public BooleanValue visitBlank(CellBlank blank) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }
}
