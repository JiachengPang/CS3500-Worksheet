package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents a CellValue that contains a boolean value.
 */
public class BooleanValue extends CellValue<Boolean> {

  /**
   * Constructs a BooleanValue with the given input.
   * @param bool the given input.
   */
  public BooleanValue(boolean bool) {
    super(bool);
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitBoolean(this);
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

    BooleanValue that = (BooleanValue) o;
    return val.equals(that.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public Boolean getValue() {
    return val;
  }

  @Override
  public String toString() {
    return Boolean.toString(val);
  }
}
