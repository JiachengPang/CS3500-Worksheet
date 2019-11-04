package edu.cs3500.spreadsheets.model;

public interface IValue extends IContent {

  <S> S getRawValue();
}
