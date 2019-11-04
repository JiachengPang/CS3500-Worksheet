package edu.cs3500.spreadsheets.model;

/**
 * Represents a visitor for a Cell.
 *
 * @param <S> the return type of this visitor
 */
public interface ContentVisitor<S> {


  /**
   * Visits a CellFunction and evaluate its arguments.
   *
   * @param func a CellFunction to visit
   * @return the result of visiting the CellFunction
   */
  S visitFunction(CellFunction func);

  /**
   * Visits a CellFunction and evaluate its inputs.
   *
   * @param ref a CellFunction to visit
   * @return the result of visiting the CellReference
   */
  S visitReference(CellReference ref);


  /**
   * Visits a DoubleValue and evaluates it.
   *
   * @param num a DoubleValue to visit
   * @return the result of visiting the DoubleValue
   */
  S visitDouble(DoubleValue num);

  /**
   * Visits a StringValue and evaluates it.
   *
   * @param str a StringValue to visit
   * @return the result of visiting the Cell
   */
  S visitString(StringValue str);

  /**
   * Visits a BooleanValue and evaluates it.
   *
   * @param bool a BooleanValue to visit
   * @return the result of visiting the BooleanValue
   */
  S visitBoolean(BooleanValue bool);

  /**
   * Visits a BlankValue and returns default.
   *
   * @param blank a BlankValue to visit
   * @return the result of visiting the BlankValue
   */
  S visitBlank(BlankValue blank);

}