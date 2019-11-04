package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a cell that refers to one ore more cells.
 */
public class CellReference implements IContent {

  private Coord position;
  private Coord start;
  private Coord end;
  private HashMap<Coord, Cell> grid;

  /**
   * Constructs a CellReference with the given starting and ending coordinate. To create a
   * CellReference that refers to only one cell, the starting and ending coordinate should be the
   * same.
   *
   * @param position the position of the cell containing this CellReference
   * @param start    the starting coordinate
   * @param end      the ending coordinate
   * @param grid     the grid
   */
  public CellReference(Coord position, Coord start, Coord end, HashMap<Coord, Cell> grid) {
    super();
    if (position == null || start == null || end == null || grid == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    this.position = position;
    this.start = start;
    this.end = end;
    this.grid = grid;

    for (int i = start.row; i <= end.row; i++) {
      for (int j = start.col; j <= end.col; j++) {
        Coord coord = new Coord(j, i);
        if (!grid.containsKey(position)) {
          Cell temp = new Cell(position, new BlankValue());
          grid.put(position, temp);
        }
        if (grid.containsKey(coord)) {
          grid.get(coord).acceptInterest(grid.get(position));
        } else {
          Cell blank = new Cell(coord, new BlankValue());
          grid.put(coord, blank);
          blank.acceptInterest(grid.get(position));
        }
      }
    }
  }

  /**
   * Gets the list of cells that this cell is referring to.
   *
   * @return list of cells
   */
  public List<Cell> getReferences() {
    List<Cell> refs = new ArrayList<>();
    for (int i = start.row; i <= end.row; i++) {
      for (int j = start.col; j <= end.col; j++) {
        refs.add(grid.get(new Coord(j, i)));
      }
    }
    return refs;
  }

  @Override
  public <S> S accept(ContentVisitor<S> visitor) {
    return visitor.visitReference(this);
  }

  @Override
  public IValue getValue() {
    return grid.get(start).getCurrentValue();
  }
}
