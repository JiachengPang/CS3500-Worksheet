package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a boolean IValue.
 */
public class BooleanValue implements IValue {

  private final boolean val;

  /**
   * Constructs a BooleanValue with the given input.
   *
   * @param bool the given input.
   */
  public BooleanValue(boolean bool) {
    this.val = bool;
  }

  @Override
  public <S> S accept(ContentVisitor<S> visitor) {
    return visitor.visitBoolean(this);
  }

  @Override
  public IValue getValue() {
    return this;
  }

  @Override
  public Boolean getRawValue() {
    return val;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BooleanValue that = (BooleanValue) o;
    return val == that.val;
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public String toString() {
    return Boolean.toString(val);
  }

}
