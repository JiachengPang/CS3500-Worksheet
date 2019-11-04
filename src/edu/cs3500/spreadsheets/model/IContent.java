package edu.cs3500.spreadsheets.model;

public interface IContent {

  /**
   * Accepts a ContentVisitor and return the result.
   * @param visitor a ContentVisitor
   * @param <S> the return type of the visitor
   * @return the result
   */
  <S> S accept(ContentVisitor<S> visitor);

  /**
   * Gets the final value of this cell content.
   * @return the final value
   */
  IValue getValue();
}
