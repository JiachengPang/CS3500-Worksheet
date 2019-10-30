package edu.cs3500.spreadsheets.model;

/**
 * Represents a visitor for a cell(Formula).
 */
public interface CellVisitor<S> {


  S visitFunction(CellFunction func);

  S visitReference(CellReference ref);

  S visitValue(CellValue val);

  S visitDouble(DoubleValue num);

  S visitString(StringValue str);

  S visitBoolean(BooleanValue bool);

}
