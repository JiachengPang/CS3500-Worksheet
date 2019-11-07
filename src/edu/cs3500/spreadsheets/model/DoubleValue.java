package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a double IValue.
 */
public class DoubleValue implements IValue {

  private final double val;

  /**
   * Constructs a DoubleValue with the given double value.
   *
   * @param num the given value
   */
  public DoubleValue(double num) {
    this.val = num;
  }

  @Override
  public <S> S accept(ContentVisitor<S> visitor) {
    return visitor.visitDouble(this);
  }

  @Override
  public IValue getValue() {
    return this;
  }

  @Override
  public Double getRawValue() {
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
    DoubleValue that = (DoubleValue) o;
    return val == that.val;
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public String toString() {
    return String.format("%f", val);
  }
}
