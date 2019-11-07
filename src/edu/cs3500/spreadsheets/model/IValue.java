package edu.cs3500.spreadsheets.model;

/**
 * Represents an IContent that is a cell value. A cell value can be - a DoubleValue - a BooleanValue
 * - a StringValue - a BlankValue.
 */
public interface IValue extends IContent {

  /**
   * Gets the raw value of this IValue.
   * @param <S> the type of the raw value
   * @return the raw value
   */
  <S> S getRawValue();
}
