package edu.cs3500.spreadsheets.model;

import java.util.Objects;

public class BooleanValue implements CellValue {

  private Boolean val;

  public BooleanValue(boolean bool) {
    this.val = bool;
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
}
