package edu.cs3500.spreadsheets.model;

import java.util.Objects;

public class StringValue implements CellValue {

  private String val;

  public StringValue(String str) {
    if (str == null) {
      throw new IllegalArgumentException("Input cannot be null.");
    }
    this.val = str;
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitString(this);
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
    StringValue that = (StringValue) o;
    return val.equals(that.val);
  }

  @Override
  public int hashCode() {
    return Objects.hash(val);
  }

  @Override
  public String getValue() {
    return val;
  }

}
