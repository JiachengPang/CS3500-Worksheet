package edu.cs3500.spreadsheets.view;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.ToSexpVisitor;
import edu.cs3500.spreadsheets.model.Worksheet;

public class WorksheetTextualView implements WorksheetView {

  private Worksheet model;
  private Appendable out;

  public WorksheetTextualView(Worksheet model, String fileName) {
    if (model == null) {
      throw new IllegalArgumentException("Arguments cannot be null.");
    }
    this.model = model;
    try {
      this.out = new PrintWriter(fileName);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File not found.");
    }
  }


  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    ToSexpVisitor visitor = new ToSexpVisitor(model.getFunctionNames());
    for (Cell cell: model.getAllCells()) {
      result.append(cell.position.toString() + " =");
      result.append(cell.getContent().accept(visitor).toString() + "\n");
    }
    return result.toString();
  }

  @Override
  public void render() throws IOException {
    this.out.append(this.toString());
  }
}
