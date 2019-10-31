package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Represents a cell that refers to one ore more cells.
 */
public class CellReference extends Cell {

  private Coord start;
  private Coord end;
  private HashMap<Coord, Cell> grid;
  private List<Cell> listeners;

  public CellReference(Coord start, Coord end, HashMap<Coord, Cell> grid) {
    super();
    if (start == null || end == null || grid == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }

    this.start = start;
    this.end = end;
    this.grid = grid;

    for (int i = start.row; i <= end.row; i++) {
      for (int j = start.col; j <= end.col; j++) {
        Coord coord = new Coord(j,i);
        if (grid.containsKey(coord)) {
          grid.get(coord).addInterest(this);
        } else {
          Cell blank = new CellBlank();
          grid.put(coord, blank);
          blank.addInterest(this);
        }
      }
    }
  }

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
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitReference(this);
  }

  @Override
  public CellValue evaluate() {
    return grid.get(start).evaluate();
  }

}
