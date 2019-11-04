package edu.cs3500.spreadsheets.model;


/**
 * Represent a blank cell with no formula.
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
