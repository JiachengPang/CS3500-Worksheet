package edu.cs3500.spreadsheets.model;


/**
 * Represents a blank value, a cell that has a blank value is a blank cell.
 */
public class BlankValue implements IValue {


  @Override
  public <S> S getRawValue() {
    throw new IllegalStateException("Blank cell does not have a value.");
  }

  @Override
  public <S> S accept(ContentVisitor<S> visitor) {
    return visitor.visitBlank(this);
  }

  @Override
  public IValue getValue() {
    return this;
  }


  @Override
  public String toString() {
    return "BlankCell";
  }
}
