package edu.cs3500.spreadsheets.model;

/**
 * Represents a visitor for a CellFormula.
 * @param <S> the return type of this visitor
 */
public interface CellVisitor<S> {


  /**
   * Visits a CellFunction.
   * @param func a CellFunction
   * @return the result of visiting the CellFunction
   */
  S visitFunction(CellFunction func);

  /**
   * Visits a CellReference.
   * @param ref a CellFunction
   * @return the result of visiting the CellReference
   */
  S visitReference(CellReference ref);

  /**
   * Visits a CellValue.
   * @param val a CellValue
   * @return the result of visiting the CellValue
   */
  S visitValue(CellValue val);

  /**
   * Visits a DoubleValue.
   * @param num a DoubleValue
   * @return the result of visiting the DoubleValue
   */
  S visitDouble(DoubleValue num);

  /**
   * Visits a StringValue.
   * @param str a StringValue
   * @return the result of visiting the Cell
   */
  S visitString(StringValue str);

  /**
   * Visits a BooleanValue.
   * @param bool BooleanValue
   * @return the result of visiting the BooleanValue
   */
  S visitBoolean(BooleanValue bool);



  S visitBlank(CellBlank blank);

}
