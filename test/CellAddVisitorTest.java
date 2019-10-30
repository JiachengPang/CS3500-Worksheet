import org.junit.Test;

import java.util.Arrays;

import edu.cs3500.spreadsheets.model.AddVisitor;
import edu.cs3500.spreadsheets.model.AppendVisitor;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.CellFormula;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.CellValue;
import edu.cs3500.spreadsheets.model.CellVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.SmallerThanVisitor;
import edu.cs3500.spreadsheets.model.ProductVisitor;
import edu.cs3500.spreadsheets.model.StringValue;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;

public class CellAddVisitorTest {

  CellValue double1 = new DoubleValue(3.0);
  CellValue double2 = new DoubleValue(5.0);
  CellValue double3 = new DoubleValue(10.0);

  CellValue string1 = new StringValue("apple");
  CellValue string2 = new StringValue("banana");
  CellValue string3 = new StringValue("citrus");

  CellValue bool1 = new BooleanValue(true);
  CellValue bool2 = new BooleanValue(false);

  CellVisitor addVisitor = new AddVisitor();
  CellVisitor productVisitor = new ProductVisitor();
  CellVisitor smallerThanVisitor = new SmallerThanVisitor();
  CellVisitor appendVisitor = new AppendVisitor();

  CellReference ref1 = new CellReference(Arrays.asList(double1, double2, double3));

  CellFunction add1 = new CellFunction(addVisitor, Arrays.asList(double1, string1));
  CellFunction add2 = new CellFunction(addVisitor, Arrays.asList(double1, double2, double3, string1,
          string2, add1, bool1));

  CellFunction product1 = new CellFunction(productVisitor, Arrays.asList(double1, string1, bool1, add1,
          add2, string2));
  CellFunction product2 = new CellFunction(productVisitor, Arrays.asList(ref1, product1, string3, bool2, double3));
  CellFunction smallerThan1 = new CellFunction(smallerThanVisitor, Arrays.asList(double1, double2));
  CellFunction smallerThan2 = new CellFunction(smallerThanVisitor, Arrays.asList(product1, add1));
  CellFunction append1 = new CellFunction(appendVisitor, Arrays.asList(string1, string2, string3,
          ref1, bool1, double1, product2));
  CellFunction badSmallerThan = new CellFunction(smallerThanVisitor, Arrays.asList(double1));
  CellFunction badSmallerThan2 = new CellFunction(smallerThanVisitor, Arrays.asList(double1, string1, double3));

  Worksheet<CellFormula> worksheet = new BasicWorksheet.BasicWorksheetBuilder()
          .createCell(1, 1, "=1")
          .createCell(1, 2, "\"foo\"")
          .createCell(2, 3, "=A1:A2")
          .createCell(3, 3, "=(SUM A1 A2)")
          .createCell(4, 4, "=(SUM A1:A2)")
          .createCell(5, 5, "=(SUM B3)")
          .createCell(2,2,"=B2")
          .createCell(5,5,"=(SUM B2 A1)").createWorksheet();

  // need to give default value instead of passing in null when referencing a
  // to a new cell that is previously blank

  // need to check if cell can reference itself -> not allowed
  @Test
  public void testEval() {
    assertEquals(new DoubleValue(21.0), add2.evaluate());
    assertEquals(new DoubleValue(189.0), product1.evaluate());
    assertEquals(new DoubleValue(283500.0), product2.evaluate());
    assertEquals(bool1, smallerThan1.evaluate());
    assertEquals(new StringValue("applebananacitrus3.0000005.00000010.000000true3.000000283500.000000")
            , append1.evaluate());
    assertEquals(new DoubleValue(1.0), worksheet.getCellAt(new Coord(1, 1)).evaluate());
    assertEquals(new StringValue("foo"), worksheet.getCellAt(new Coord(1, 2)).evaluate());
    assertEquals(new DoubleValue(1.0),
            worksheet.getCellAt(new Coord(2, 3)));
    assertEquals(1.0, (Double) worksheet.getCellAt(new Coord(4, 4))
            .evaluate().getValue(), 0.0001);
    assertEquals(bool2, smallerThan2.evaluate());
    assertEquals(new DoubleValue(1.0), worksheet.getCellAt(new Coord(5,5)));

    worksheet.set(new Coord(2,2), new DoubleValue(2.0));
    assertEquals(new DoubleValue(2.0), worksheet.getCellAt(new Coord(2,2)));
    assertEquals(new DoubleValue(3.0), worksheet.getCellAt(new Coord(5,5)));

    try {
      badSmallerThan.evaluate();
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than has to take in exactly 2 numeric inputs.", e.getMessage());
    }
    try {
      badSmallerThan2.evaluate();
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than has to take in exactly 2 numeric inputs.", e.getMessage());
    }
  }

}
