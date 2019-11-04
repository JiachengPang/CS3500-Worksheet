package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.ToContentVisitor;

/**
 * Represents a BasicWorksheet model. The model keeps track of the cells inside the grid and
 * provides several methods for manipulating these cells. Invariants: the grid is never null, the
 * column and row are non-negative.
 */
public class BasicWorksheet implements Worksheet {

  public HashMap<Coord, Cell> grid;

  /**
   * Represents a builder class for creating a BasicWorkSheet. The user needs to invoke the
   * createCell function for adding cells when initializing a new Worksheet.
   */
  public static class BasicWorksheetBuilder implements
          WorksheetReader.WorksheetBuilder<BasicWorksheet> {
    private HashMap<Coord, Cell> grid = new HashMap<Coord, Cell>();
    private static Parser parser = new Parser();
    private HashMap<String, ContentVisitor> functions = this.initialFunctions();

    private HashMap<String, ContentVisitor> initialFunctions() {
      HashMap<String, ContentVisitor> result = new HashMap<String, ContentVisitor>();
      result.put("SUM", new AddVisitor());
      result.put("PRODUCT", new ProductVisitor());
      result.put("APPEND", new AppendVisitor());
      result.put("<", new SmallerThanVisitor());
      return result;
    }

    /**
     * Add new functions to this worksheet.
     *
     * @param name     The name of the function
     * @param function the CellVisitor representing the new function.
     * @return
     */
    public WorksheetReader.WorksheetBuilder<BasicWorksheet> addFunction(
            String name, ContentVisitor function) {
      if (name == null || function == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
      functions.put(name, function);
      return this;
    }

    @Override
    public WorksheetReader.WorksheetBuilder<BasicWorksheet> createCell(
            int col, int row, String contents) {
      if (contents == null) {
        return this;
      }
      this.initialCell(new Coord(col, row), contents);
      return this;
    }

    /**
     * Creates the cell based on the input string and put the cell in place it's supposed to be.
     *
     * @param coord    the coord the cell should be
     * @param contents the given string representing contents of a cell
     */
    private void initialCell(Coord coord, String contents) {
      ToContentVisitor visitor = new ToContentVisitor(grid, functions, coord);
      if (contents.length() == 0) {
        return;
      }
      if (contents.charAt(0) == '=') {
        this.set(coord, new Cell(coord, parser.parse(contents.substring(1)).accept(visitor)));
      } else {
        this.set(coord, new Cell(coord, parser.parse(contents).accept(visitor)));
      }
    }

    private void set(Coord coord, Cell cell) {
      if (cell == null) {
        throw new IllegalArgumentException("Inputs cannot be null.");
      }
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
    public BasicWorksheet createWorksheet() {
      return new BasicWorksheet(this.grid);
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

    try {
      this.getValueAt(coord.col, coord.row);
    } catch (IllegalStateException e) {
      if (e.getMessage().equals("Reference cannot refer to itself.")
              || e.getMessage().equals("Functions cannot take in itself.")) {
        grid.put(coord, oldCell);
        throw new IllegalStateException("Adding this cell will cause a cycle.");
      }
    }
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
  public IValue getValueAt(int col, int row) {
    Coord coord = new Coord(col, row);
    if (!grid.containsKey(coord)) {
      throw new IllegalStateException("The cell is blank.");
    }
    return grid.get(coord).getCurrentValue();
  }

}
