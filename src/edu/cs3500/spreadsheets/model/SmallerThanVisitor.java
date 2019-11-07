package edu.cs3500.spreadsheets.model;

/**
 * Represents a ContentVisitor that returns a BooleanValue containing the result of comparing 2
 * inputs. This class throws IllegalArgumentException if the 2 value being compared are not doubles
 * or if the input size is not strictly 2.
 */
public class SmallerThanVisitor implements ContentVisitor<BooleanValue> {

  @Override
  public BooleanValue visitFunction(CellFunction func) {
    if (func.getArgs().size() != 2) {
      throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
    }

    try {
      Double left = (Double) func.getArgs().get(0).getValue().getRawValue();
      Double right = (Double) func.getArgs().get(1).getValue().getRawValue();
      return new BooleanValue(left < right);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Smaller-than cannot compare types other than doubles.");
    }
  }

  @Override
  public BooleanValue visitReference(CellReference ref) {
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
  public BooleanValue visitBlank(BlankValue blank) {
    throw new IllegalArgumentException("Smaller-than has to take in exactly 2 numeric inputs.");
  }

}
