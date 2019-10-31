package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

public class CellBlank extends Cell {

  private List<Cell> listeners;

  public CellBlank() {
    this.listeners = new ArrayList<>();
  }

  public CellBlank(List<Cell> listeners) {
    if (listeners == null) {
      throw new IllegalArgumentException("Argument cannot be null.");
    }
    this.listeners = listeners;
  }

  @Override
  public CellValue evaluate() {
    throw new IllegalStateException("Blank cells cannot be evaluated.");
  }

  @Override
  public <S> S accept(CellVisitor<S> visitor) {
    return visitor.visitBlank(this);
  }

}
