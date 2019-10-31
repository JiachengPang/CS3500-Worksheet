package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cell that contains a formula in a worksheet. A formula is currently one of
 * (expandable) - a value - a reference to a rectangular region of cells in the worksheet - a
 * function applied to one or more formulas as its arguments A value is currently one of
 * (expandable) - a double - a boolean - a String
 */
public abstract class Cell {
  private List<Cell> listeners;

  public Cell(List<Cell> listeners) {
    if (listeners == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.listeners = listeners;
  }

  public Cell() {
    this.listeners = new ArrayList<>();
  }

  /**
   * Evaluates this cell, reducing it to a CellValue containing the final result of the evaluation.
   *
   * @return a CellValue that contains the final result.
   */
  public abstract CellValue evaluate();


  public boolean hasListener(Cell cell) {
    if (this.listeners.contains(cell)) {
      return true;
    }
    for (Cell listener : this.listeners) {
      if (listener.hasListener(cell)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Accepts a CellVisitor and return the result.
   *
   * @param visitor a CellVisitor
   * @param <S>     the return type of the Cellvisitor
   * @return
   */
  public abstract <S> S accept(CellVisitor<S> visitor);


  public List<Cell> getListeners() {
    return new ArrayList<>(listeners);
  }

  public void addInterest(Cell listener) {
    listeners.add(listener);
  }

  public void removeInterest(Cell listener) {
    listeners.remove(listener);
  }

}
