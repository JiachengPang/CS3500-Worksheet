package edu.cs3500.spreadsheets.view;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.ToSexpVisitor;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.Sexp;

public class WorksheetTextualView implements WorksheetView {

  private Worksheet model;
  private PrintWriter out;

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
    ToSexpVisitor visitor = new ToSexpVisitor();
    for (Cell cell : model.getAllCells()) {
      Sexp current = cell.getContent().accept(visitor);
      if (current == null) {
        break;
      }
      result.append(cell.position.toString() + " =");
      result.append(current.toString() + "\n");
    }
    return result.toString();
  }

  @Override
  public void makeVisible() {
    return;
  }

  @Override
  public void render() {
    this.out.append(this.toString());
    this.out.close();
  }

  @Override
  public void showErrorMessage(String error) {
    this.out.append("Error: " + error);
  }
}
