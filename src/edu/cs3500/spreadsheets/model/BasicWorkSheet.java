package edu.cs3500.spreadsheets.model;

import java.util.HashMap;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.ToCellVisitor;

public class BasicWorksheet implements Worksheet {

  public HashMap<Coord, Cell> grid;

  public static class BasicWorksheetBuilder implements WorksheetReader.WorksheetBuilder<BasicWorksheet> {
    private HashMap<Coord, Cell> grid = new HashMap<Coord, Cell>();
    private static Parser parser = new Parser();
    private HashMap<String, CellVisitor> functions = this.initialFunctions();

    private HashMap<String, CellVisitor> initialFunctions() {
      HashMap<String, CellVisitor> result = new HashMap<String, CellVisitor>();
      result.put("SUM", new AddVisitor());
      result.put("PRODUCT", new ProductVisitor());
      result.put("APPEND", new AppendVisitor());
      result.put("<", new SmallerThanVisitor());
      return result;
    }


    public WorksheetReader.WorksheetBuilder<BasicWorksheet> addFunction(
            String name, CellVisitor function) {
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

    private void initialCell(Coord coord, String contents) {
      ToCellVisitor visitor = new ToCellVisitor(grid, functions);
      if (contents.length() == 0) {
        return;
      }
      if (contents.charAt(0) == '=') {
        grid.put(coord, parser.parse(contents.substring(1)).accept(visitor));
      } else {
        grid.put(coord, parser.parse(contents).accept(visitor));
      }
    }

    @Override
    public BasicWorksheet createWorksheet() {
      return new BasicWorksheet(this.grid);
    }
  }

  private BasicWorksheet(HashMap<Coord, Cell> grid) {
    if (grid == null) {
      throw new IllegalArgumentException("The input gird cannot be null.");
    }
    this.grid = grid;
  }

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
    Cell blank = new CellBlank();
    for (Cell listener: oldCell.getListeners()) {
      listener.removeInterest(oldCell);
      listener.addInterest(blank);
    }
    grid.put(coord, blank);
  }

  @Override
  public void set(int col, int row, Cell cell) {
    Coord coord = new Coord(col, row);
    Cell oldCell = grid.get(coord);
    if (cell == null) {
      throw new IllegalArgumentException("Inputs cannot be null.");
    }
    for (Cell listener : oldCell.getListeners()) {
      listener.addInterest(cell);
      listener.removeInterest(oldCell);
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
  public CellValue getCellAt(Coord coord) {
    return grid.get(coord).evaluate();
  }

}
