package edu.cs3500.spreadsheets.model;

public interface Formula {

  <S> S accept(CellVisitor visitor);

}
