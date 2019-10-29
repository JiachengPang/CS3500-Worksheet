package edu.cs3500.spreadsheets.model;

import java.util.function.Function;

/**
 * Represents a visitor for a cell(Formula).
 * @param <S> the return type of the visitor
 */
public interface CellVisitor<S> {

  S visitFunction(CellFunction func);

  S visitReference(CellReference ref);

  S visitValue(CellValue val);

  S visitDouble(DoubleValue num);

  S visitString(StringValue str);

  S visitBoolean(BooleanValue bool);

}
