package edu.cs3500.spreadsheets.model;


import java.util.HashMap;

public class BasicWorksheet implements Worksheet<CellFormula> {

  private HashMap<Coord, CellFormula> grid;

  static class BasicWorksheetBuilder implements WorksheetReader.WorksheetBuilder<BasicWorksheet> {
    private HashMap<Coord, CellFormula> grid = new HashMap<Coord, CellFormula>();

    @Override
    public WorksheetReader.WorksheetBuilder<BasicWorksheet> createCell(
            int col, int row, String contents) {
      grid.put(new Coord(row, col), this.initialCell(contents));
      return this;
    }

    private CellFormula initialCell(String contents) {


      return null;
    }

    @Override
    public BasicWorksheet createWorksheet() {
      return new BasicWorksheet(grid);
    }
  }

  private BasicWorksheet(HashMap<Coord, CellFormula> grid) {
    if (grid == null) {
      throw new IllegalArgumentException("The input gird cannot be null.");
    }
    this.grid = grid;
  }

  private int getMaxCol(HashMap<Coord, CellFormula> grid) {
    HashMap<Integer, Integer> colLengths = new HashMap<>();
    for(Coord coord : grid.keySet()) {
      if (colLengths.containsKey(coord.row)) {
        colLengths.put(coord.row, colLengths.get(coord.row) + 1);
      } else {
        colLengths.put(coord.row, 1);
      }
    }
    int max = -1;
    for (Integer length : colLengths.values()) {
      if (length > max) {
        max = length;
      }
    }
    return max;
  }

  public BasicWorksheet() {
    grid = new HashMap<Coord, CellFormula>();
  }

  @Override
  public void clearAll() {
    grid.clear();
  }

  @Override
  public void clear(Coord coord) {
    if (coord == null) {
      throw new IllegalArgumentException("Coord cannot be null.");
    }
    grid.remove(coord);
  }

  @Override
  public void set(Coord coord, CellFormula cell) {
    if (coord == null || cell == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    grid.put(coord, cell);
  }

  @Override
  public int getNumRows() {
    return grid.size();
  }

  @Override
  public int getNumCols() {
    return this.getMaxCol(grid);
  }

  @Override
  public CellValue getValueAt(Coord coord) {
    return grid.get(coord).evaluate();
  }

}
