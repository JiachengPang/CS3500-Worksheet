import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFunction;
import edu.cs3500.spreadsheets.model.CellReference;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.SmallerThanVisitor;
import edu.cs3500.spreadsheets.model.StringValue;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;


/**
 * Test class for methods in the basic worksheet model {@link BasicWorksheet}.
 */
public class BasicWorksheetTest {

  private Worksheet w1;
  private Worksheet smallWorksheet;

  /**
   * Set all fields to default.
   */
  private void reset() {
    File file1 = new File("test/file1.txt");
    File smallFile = new File("test/smallFile.txt");
    w1 = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    smallWorksheet = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();


    try {
      w1 = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(file1));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    try {
      smallWorksheet = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(smallFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }


  @Test
  public void testCreateEmptyWorksheet() {
    Worksheet empty = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();

    try {
      empty.getValueAt(1, 1);
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }

    try {
      empty.getValueAt(100, 100);
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }

    try {
      empty.getValueAt(23, 23);
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }
  }

  @Test
  public void testReadWorksheet() {
    reset();

    assertEquals(new DoubleValue(1.0), smallWorksheet.getValueAt(1, 1));
    assertEquals(new DoubleValue(2.0), smallWorksheet.getValueAt(1, 2));
    assertEquals(new StringValue("ood"), smallWorksheet.getValueAt(2, 1));
    assertEquals(new BooleanValue(true), smallWorksheet.getValueAt(3, 3));
    assertEquals(new DoubleValue(3.0), smallWorksheet.getValueAt(52, 10));

    try {
      // check that this cell is blank
      smallWorksheet.getValueAt(703, 230);
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }
  }

  @Test
  public void testProhibitSelfReference() {
    reset();

    try {
      // direct reference to itself in a function F2
      w1.getValueAt(6, 2);
    } catch (IllegalStateException e) {
      assertEquals("Cell cannot refer to itself.", e.getMessage());
    }

    try {
      // direct reference to itself in a reference F5
      w1.getValueAt(6, 5);
    } catch (IllegalStateException e) {
      assertEquals("Cell cannot refer to itself.", e.getMessage());
    }


    try {
      // indirect self-reference in formula F3
      w1.getValueAt(6, 3);
    } catch (IllegalStateException e) {
      assertEquals("Cell cannot refer to itself.", e.getMessage());
    }


    try {
      // indirect self-reference in formula D4
      w1.getValueAt(4, 4);
    } catch (IllegalStateException e) {
      assertEquals("Cell cannot refer to itself.", e.getMessage());
    }
  }

  @Test
  public void testSum() {
    reset();
    assertEquals(new DoubleValue(6.0), w1.getValueAt(5, 1));
    assertEquals(new DoubleValue(5.0), w1.getValueAt(5, 8));
    assertEquals(new DoubleValue(5.0), w1.getValueAt(5, 9));
    assertEquals(new DoubleValue(8.0), w1.getValueAt(5, 10));
    assertEquals(new DoubleValue(8.0), w1.getValueAt(5, 11));
    assertEquals(new DoubleValue(100.0), w1.getValueAt(5, 12));

  }

  @Test
  public void testProduct() {
    reset();
    assertEquals(new DoubleValue(60.0), w1.getValueAt(18, 1));
    assertEquals(new DoubleValue(60.0), w1.getValueAt(18, 2));
    assertEquals(new DoubleValue(61.0), w1.getValueAt(18, 3));
    assertEquals(new DoubleValue(10.0), w1.getValueAt(18, 4));
    assertEquals(new DoubleValue(1080000.0), w1.getValueAt(18, 5));

  }

  @Test
  public void testSmallerThan() {
    reset();

    assertEquals(new BooleanValue(false), w1.getValueAt(19, 2));
    assertEquals(new BooleanValue(false), w1.getValueAt(19, 3));
    assertEquals(new BooleanValue(true), w1.getValueAt(19, 5));

    // tests for cases where the function should error
    try {
      w1.getValueAt(5, 4);
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than cannot compare types other than doubles.",
              e.getMessage());
    }

    try {
      w1.getValueAt(19, 1);
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than cannot compare types other than doubles.",
              e.getMessage());
    }

    try {
      w1.getValueAt(19, 4);
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than cannot compare types other than doubles.",
              e.getMessage());
    }

    try {
      w1.getValueAt(19, 6);
    } catch (IllegalArgumentException e) {
      assertEquals("Smaller-than has to take in exactly 2 numeric inputs.",
              e.getMessage());
    }
  }


  @Test
  public void testAppend() {
    reset();
    assertEquals(new StringValue("oodisfun\"Um\" I don't think \\ so \\."),
            w1.getValueAt(16, 20));
    assertEquals(new StringValue("sadtrue1.000000false\"life\""),
            w1.getValueAt(16, 21));
    assertEquals(new StringValue("1.0000001.000000false10.00000026.000000false"),
            w1.getValueAt(16, 22));
  }


  @Test
  public void testToString() {
    reset();
    assertEquals("2.000000", w1.getValueAt(1, 2).toString());
    assertEquals("\"ood\"", w1.getValueAt(2, 1).toString());
    assertEquals("true", w1.getValueAt(3, 1).toString());
    assertEquals("\"\\\"Um\\\" I don't think \\\\ so \\\\.\"",
            w1.getValueAt(2, 4).toString());
  }


  @Test
  public void testInvalidNullArguments() {
    reset();

    try {
      new CellReference(null, null, new Coord(3, 3), new HashMap<>());
    } catch (IllegalArgumentException e) {
      assertEquals("Inputs cannot be null.", e.getMessage());
    }

    try {
      new StringValue(null);
    } catch (IllegalArgumentException e) {
      assertEquals("Value cannot be null.", e.getMessage());
    }

    try {
      new CellFunction("SUM",null, new ArrayList<>());
    } catch (IllegalArgumentException e) {
      assertEquals("Inputs cannot be null.", e.getMessage());
    }
  }

  @Test
  public void testBuildWorksheet() {
    Worksheet worksheet = new BasicWorksheet.BasicWorksheetBuilder()
            .createCell(1, 1, "=1")
            .createCell(1, 2, "\"foo\"")
            .createCell(2, 3, "=A1:A2")
            .createCell(3, 3, "=(SUM A1 A2)")
            .createCell(4, 4, "=(SUM A1:A2)")
            .createCell(5, 5, "=(SUM B3)")
            .createCell(5, 4, "=A1")
            .createCell(3, 3, "=(< A1 D4)").createWorksheet();

    assertEquals(new DoubleValue(1.0), worksheet.getValueAt(1, 1));
    assertEquals(new StringValue("foo"), worksheet.getValueAt(1, 2));
    assertEquals(new DoubleValue(1.0), worksheet.getValueAt(2, 3));
    assertEquals(new DoubleValue(1.0), worksheet.getValueAt(4, 4));
    assertEquals(new BooleanValue(false), worksheet.getValueAt(3, 3));
  }

  @Test
  public void testClearAll() {
    reset();
    assertEquals(new DoubleValue(1.0), w1.getValueAt(1, 1));
    w1.clearAll();
    try {
      assertNull(w1.getValueAt(1, 1));
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }
  }

  @Test
  public void testClearOneCell() {
    reset();

    assertEquals(new DoubleValue(1.0), w1.getValueAt(1, 1));
    w1.clear(1, 1);
    try {
      w1.getValueAt(1, 1);
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }

    assertEquals(new StringValue("ood"), w1.getValueAt(2, 1));
    w1.clear(2, 1);
    try {
      w1.getValueAt(2, 1);
    } catch (IllegalStateException e) {
      assertEquals("The cell is blank.", e.getMessage());
    }
  }

  @Test
  public void testSetValue() {
    reset();
    assertEquals(new StringValue("is"), w1.getValueAt(2, 2));
    w1.set(2, 2, new Cell(new Coord(2,2), new DoubleValue(10.0)));
    assertEquals(new DoubleValue(10.0), w1.getValueAt(2, 2));

    assertEquals(new DoubleValue(6.0), w1.getValueAt(5, 1));
    w1.set(5, 1, new Cell(new Coord(5,1 ), new CellFunction("<",
            new SmallerThanVisitor(),
            Arrays.asList(new DoubleValue(5.0), new DoubleValue(10.0)))));
    assertEquals(new BooleanValue(true), w1.getValueAt(5, 1));
  }
}