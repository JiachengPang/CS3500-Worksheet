package edu.cs3500.spreadsheets.model;

import java.util.Objects;

/**
 * Represents an IValue that contains a String.
 */
public class StringValue implements IValue {

  private final String val;

  /**
   * Constructs a StringValue with the given string.
   * @param str the given string
   */
  public StringValue(String str) {
    this.val = str;
  }

  @Override
  public <S> S accept(ContentVisitor<S> visitor) {
    return visitor.visitString(this);
  }


  @Override
  public IValue getValue() {
    return this;
  }

  @Override
  public String getRawValue() {
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
}
