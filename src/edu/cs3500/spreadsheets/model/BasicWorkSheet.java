package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.ToContentVisitor;

/**
 * Represents a BasicWorksheet model. The model keeps track of the cells inside the grid and
 * provides several methods for manipulating these cells. Invariants: the grid is never null, the
 * column and row are non-negative.
 */
public class BasicWorksheet implements Worksheet {

  private HashMap<Coord, Cell> grid;

  /**
   * Represents a builder class for creating a BasicWorkSheet. The user needs to invoke the
   * createCell function for adding cells when initializing a new Worksheet.
   */
  public static class BasicWorksheetBuilder implements
          WorksheetReader.WorksheetBuilder<BasicWorksheet> {
    private BasicWorksheet worksheet = new BasicWorksheet();

    @Override
    public WorksheetReader.WorksheetBuilder<BasicWorksheet> createCell(
            int col, int row, String contents) {
      if (contents == null) {
        return this;
      }
      this.worksheet.setInitial(col, row, this.initialCell(new Coord(col, row), contents));
      return this;
    }

    /**
     * Creates the cell based on the input string and put the cell in place it's supposed to be.
     *
     * @param coord    the coord the cell should be
     * @param contents the given string representing contents of a cell
     * @throws IllegalArgumentException if the content string is empty
     */
    private Cell initialCell(Coord coord, String contents) {
      ToContentVisitor visitor = new ToContentVisitor(
              worksheet.grid, worksheet.initialFunctions(), coord);
      if (contents.length() == 0) {
        throw new IllegalArgumentException("Contents cannot be empty.");
      }
      if (contents.equals("=")) {
        return new Cell(coord, new BlankValue());
      }
      if (contents.charAt(0) == '=') {
        return new Cell(coord, Parser.parse(contents.substring(1)).accept(visitor));
      } else {
        return new Cell(coord, Parser.parse(contents).accept(visitor));
      }
    }

    @Override
    public BasicWorksheet createWorksheet() {
      return this.worksheet;
    }
  }


  /**
   * Private constructor for directly creating a worksheet with the given grid.
   *
   * @param grid the given grid.
   */
  private BasicWorksheet(HashMap<Coord, Cell> grid) {
    if (grid == null) {
      throw new IllegalArgumentException("The input gird cannot be null.");
    }
    this.grid = grid;
  }


  private HashMap<String, ContentVisitor> initialFunctions() {
    HashMap<String, ContentVisitor> result = new HashMap<>();
    result.put("SUM", new AddVisitor());
    result.put("PRODUCT", new ProductVisitor());
    result.put("APPEND", new AppendVisitor());
    result.put("<", new SmallerThanVisitor());
    return result;
  }


  /**
   * Get the length of the longest column.
   *
   * @param grid the grid
   * @return the length of the longest column
   */
  private int getMaxCol(HashMap<Coord, Cell> grid) {
    HashMap<Integer, Integer> colLengths = new HashMap<>();
    for (Coord coord : grid.keySet()) {
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

  private int getMaxRow(HashMap<Coord, Cell> grid) {
    HashMap<Integer, Integer> rowLengths = new HashMap<>();
    for (Coord coord : grid.keySet()) {
      if (rowLengths.containsKey(coord.col)) {
        rowLengths.put(coord.col, rowLengths.get(coord.col) + 1);
      } else {
        rowLengths.put(coord.col, 1);
      }
    }
    int max = -1;
    for (Integer length : rowLengths.values()) {
      if (length > max) {
        max = length;
      }
    }
    return max;
  }

  /**
   * Constructor for creating an empty BasicWorksheet.
   */
  public BasicWorksheet() {
    grid = new HashMap<>();
  }

  @Override
  public void clearAll() {
    grid.clear();
  }

  @Override
  public void clear(int col, int row) {
    Coord coord = new Coord(col, row);
    Cell oldCell = grid.get(coord);
    ContentVisitor removeRefVisitor = new RemoveRefVisitor(oldCell);
    oldCell.getContent().accept(removeRefVisitor);
    grid.remove(coord);
  }

  @Override
  public void set(int col, int row, Cell cell) {
    this.setInitial(col, row, cell);
    try {
      this.getValueAt(col, row);
    } catch (IllegalStateException e) {
      if (e.getMessage().equals("Reference cannot refer to itself.")
              || e.getMessage().equals("Functions cannot take in itself.")) {
        throw new IllegalStateException("Adding this cell will cause a cycle.");
      }
    }

  }

  private void setInitial(int col, int row, Cell cell) {
    if (cell == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    Coord coord = new Coord(col, row);
    Cell oldCell = grid.get(coord);
    if (oldCell == null) {
      grid.put(coord, cell);
      return;
    }
    for (Cell listener : oldCell.getDirectListeners()) {
      cell.acceptInterest(listener);
    }
    ContentVisitor removeRefVisitor = new RemoveRefVisitor(oldCell);
    oldCell.getContent().accept(removeRefVisitor);
    grid.put(coord, cell);
  }

  @Override
  public int getNumRows() {
    return this.getMaxRow(grid);
  }

  @Override
  public int getNumCols() {
    return this.getMaxCol(grid);
  }

  @Override
  public IValue getValueAt(int col, int row) {
    Coord coord = new Coord(col, row);
    if (!grid.containsKey(coord)) {
      throw new IllegalStateException("The cell is blank.");
    }
    return grid.get(coord).getCurrentValue();
  }

  @Override
  public List<Cell> getAllCells() {
    return new ArrayList<>(grid.values());
  }

  @Override
  public List<String> getFunctionNames() {
    return new ArrayList<>(this.initialFunctions().keySet());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    BasicWorksheet that = (BasicWorksheet) o;
    if (this.grid.keySet().size() != that.grid.keySet().size()) {
      return false;
    }
    for (Coord key : this.grid.keySet()) {
      if (!that.grid.containsKey(key)) {
        return false;
      }
      if (!this.grid.get(key).equals(that.grid.get(key))) {
        return false;
      }
    }
    return true;
  }
}
