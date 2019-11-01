package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a cell containing a function applied to one or more formulas as its inputs.
 */
public class CellFunction extends Cell {

  private CellVisitor visitor;
  private List<Cell> args;

  /**
   * Constructs a function cell.
   *
   * @param visitor the function this cell executes
   * @param args    a list of input coordinates
   * @throws IllegalArgumentException if the given list is null or any entry in the list is null
   */
  public CellFunction(CellVisitor visitor, List<Cell> args) {
    super();
    if (args == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    for (Cell arg : args) {
      if (arg == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
    }
    this.args = args;
    this.visitor = visitor;
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitFunction(this);
  }

  @Override
  public CellValue evaluate() {
    for (Cell arg : args) {
      if (arg.hasListener(this) || this.hasListener(arg)) {
        throw new IllegalArgumentException("Functions cannot take in themselves.");
      }
    }
    return (CellValue) visitor.visitFunction(this);
  }


  /**
   * Gets the list of arguments.
   *
   * @return the list of arguments.
   */
  public List<Cell> getArgs() {
    return new ArrayList<>(args);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CellFunction that = (CellFunction) o;
    return Objects.equals(visitor, that.visitor) &&
            Objects.equals(args, that.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(visitor, args);
  }
}
