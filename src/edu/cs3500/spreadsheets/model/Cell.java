package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;


/**
 * Represents a Cell in a worksheet. The cell stores its position in the worksheet, an IContent, its
 * current value, and a list of Cells that are currently referring to it.
 */
public class Cell {

  public final Coord position;
  private IContent content;
  private IValue currentValue;
  private List<Cell> listeners;

  /**
   * Constructs a Cell with the given position and IContent. The initial listeners is an empty list.
   * The currentValue field is only calculated when the method evaluate is called.
   */
  public Cell(Coord position, IContent content) {
    if (position == null || content == null) {
      throw new IllegalArgumentException("Content cannot be null.");
    }
    this.position = position;
    this.content = content;
    this.listeners = new ArrayList<>();
  }


  /**
   * Get the list of cells that are directly referring to this cell.
   *
   * @return list of cells
   */
  protected List<Cell> getDirectListeners() {
    return new ArrayList<>(this.listeners);
  }

  /**
   * Adds a listener to the list of listeners of this cell.
   *
   * @param listener the listener to add the list of listeners of this cell
   */
  protected void acceptInterest(Cell listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    listeners.add(listener);
  }


  /**
   * Removes a listener to the list of listeners of this cell.
   *
   * @param listener the listener to remove from list of listeners of this cell
   */
  protected void removeInterest(Cell listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    listeners.remove(listener);
  }

  /**
   * Determines if the target cell is a direct or indirect listener of this cell.
   *
   * @param target the target listener cell to find.
   * @throws IllegalStateException if the target cell is a direct/indirect listener of this cell
   */
  private void listenedByTarget(Cell target, Stack<Cell> stack) {
    for (Cell c : this.listeners) {
      if (c.position.equals(target.position)) {
        throw new IllegalStateException("Cell cannot refer to itself.");
      } else {
        stack.push(c);
      }
      this.listenedByTargetHelp(target, stack);
    }
  }

  private void listenedByTargetHelp(Cell target, Stack<Cell> stack) {
    if (!stack.empty()) {
      Cell current = stack.peek();
      stack.pop();
      current.listenedByTarget(target, stack);
    }
  }

  /**
   * Gets the current value of the cell.
   *
   * @return the value
   * @throws IllegalStateException if the cell is blank
   */
  public IValue getCurrentValue() {
    this.evaluate();
    return this.currentValue;
  }

  /**
   * Gets the content of this cell.
   *
   * @return the content
   * @throws IllegalStateException if the cell is blank
   */
  public IContent getContent() {
    return this.content;
  }


  /**
   * Evaluates the cell using its contents and update its current value.
   */
  private void evaluate() {
    this.listenedByTarget(this, new Stack<>());
    currentValue = content.getValue();
  }

  @Override
  public String toString() {
    return position.toString() + content.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell cell = (Cell) o;
    return Objects.equals(position, cell.position) &&
            Objects.equals(content, cell.content) &&
            Objects.equals(currentValue, cell.currentValue) &&
            Objects.equals(listeners, cell.listeners);
  }

}
