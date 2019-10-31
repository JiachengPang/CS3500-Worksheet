package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a cell containing a double.
 */
public class DoubleValue extends CellValue<Double> {

  /**
   * Constructs a DoubleValue with the given double value.
   *
   * @param num the given value
   */
  public DoubleValue(double num) {
    super(num);
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitDouble(this);
  }

  @Override
  public CellValue evaluate() {
    return this;
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
    return val.equals(that.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public Double getValue() {
    return val;
  }

  @Override
  public String toString() {
    return String.format("%f", val);
  }
}
