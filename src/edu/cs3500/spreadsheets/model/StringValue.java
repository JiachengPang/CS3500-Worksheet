package edu.cs3500.spreadsheets.model;

import java.util.Objects;

public class StringValue extends CellValue<String> {


  public StringValue(String str) {
    super(str);
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
  public String toString() {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < val.length(); i++) {
      switch (val.charAt(i)) {
        case '\"':
          str.append(String.format("\\%s", '\"'));
          break;
        case '\\':
          str.append(String.format("\\%s", '\\'));
          break;
        default:
          str.append(val.charAt(i));
      }
    }
    return String.format("\"%s\"", str.toString());
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
