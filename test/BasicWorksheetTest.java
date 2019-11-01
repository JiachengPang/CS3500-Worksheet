import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.cs3500.spreadsheets.model.AddVisitor;
import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.BooleanValue;
import edu.cs3500.spreadsheets.model.CellBlank;
import edu.cs3500.spreadsheets.model.CellValue;
import edu.cs3500.spreadsheets.model.DoubleValue;
import edu.cs3500.spreadsheets.model.StringValue;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;

import static junit.framework.TestCase.assertEquals;


public class BasicWorksheetTest {

  Worksheet w1;
  Worksheet smallWorksheet;
  AddVisitor addVisitor;

  public void reset() {
    File file1 = new File("test/file1.txt");
    File smallFile = new File("test/smallFile.txt");
    w1 = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    smallWorksheet = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();
    try {
      w1 = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(file1));
    } catch(FileNotFoundException e) {
      e.printStackTrace();
    }

    try {
      smallWorksheet = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
              new FileReader(smallFile));
    } catch(FileNotFoundException e) {
      e.printStackTrace();
    }


    addVisitor = new AddVisitor();
  }


  @Test
  public void testCreateEmptyWorksheet() {
    Worksheet empty = new BasicWorksheet.BasicWorksheetBuilder().createWorksheet();

    try {
      assertEquals(new CellBlank(), empty.getCellAt(1,1));
    } catch (IllegalArgumentException e) {
      assertEquals("The cell is blank.",e.getMessage());
    }

    try {
      assertEquals(new CellBlank(), empty.getCellAt(100,100));
    } catch (IllegalArgumentException e) {
      assertEquals("The cell is blank.",e.getMessage());
    }

    try {
      assertEquals(new CellBlank(), empty.getCellAt(23,23));
    } catch (IllegalArgumentException e) {
      assertEquals("The cell is blank.",e.getMessage());
    }
  }

  @Test
  public void testReadWorksheet() {
    reset();

    assertEquals(new DoubleValue(1.0),smallWorksheet.getCellAt(1,1));
    assertEquals(new DoubleValue(2.0),smallWorksheet.getCellAt(1,2));
    assertEquals(new StringValue("ood"),smallWorksheet.getCellAt(2,1));
    assertEquals(new BooleanValue(true),smallWorksheet.getCellAt(3,3));
    assertEquals(new DoubleValue(3.0),smallWorksheet.getCellAt(52,10));

    try {
      // check that this cell is blank
      assertEquals(new CellBlank(),smallWorksheet.getCellAt(703,230));
    } catch (IllegalStateException e) {
      assertEquals("Blank cell cannot be evaluated.", e.getMessage());
    }
  }

  @Test
  public void testProhibitSelfReference() {
    reset();
    CellValue cell;

    try {
      // direct reference to itself in a function F2
      cell = w1.getCellAt(6,2);
    } catch (IllegalArgumentException e) {
      assertEquals("Functions cannot take in themselves.", e.getMessage());
    }


    try {
      // direct reference to itself in a reference F5
      cell = w1.getCellAt(6,5);
    } catch (IllegalArgumentException e) {
      assertEquals("Reference cannot refer to itself.", e.getMessage());
    }

    try {
      // indirect self-reference in formula D4
      cell = w1.getCellAt(4,4);
    } catch (IllegalArgumentException e) {
      assertEquals("Functions cannot take in themselves.", e.getMessage());
    }

    /*
    try {
      // indirect self-reference in formula F6
      cell = w1.getCellAt(6,6);
    } catch (IllegalArgumentException e) {
      assertEquals("Reference cannot refer to itself.", e.getMessage());
    }
    */
  }





  @Test
  public void testSum() {
    reset();
    assertEquals(new DoubleValue(6.0), w1.getCellAt(5, 1));
    assertEquals(new DoubleValue(5.0), w1.getCellAt(5, 8));
    assertEquals(new DoubleValue(5.0), w1.getCellAt(5, 9));
    //assertEquals(new DoubleValue(8.0), w1.getCellAt(5, 10));

  }

  @Test
  public void testProduct() {
    reset();
    assertEquals(new DoubleValue(60.0), w1.getCellAt(18, 1));
    assertEquals(new DoubleValue(60.0), w1.getCellAt(18, 2));
    assertEquals(new DoubleValue(61.0), w1.getCellAt(18, 3));
    //assertEquals(new DoubleValue(10.0), w1.getCellAt(18,4));
    assertEquals(new DoubleValue(1080000.0), w1.getCellAt(18,5));

  }

  public void testSmallerThan() {

  }


  public void testAppend() {

  }


  @Test
  public void testToString() {
    reset();
    assertEquals("2.000000",w1.getCellAt(1,2).toString());
    assertEquals("\"ood\"", w1.getCellAt(2,1).toString());
    assertEquals("true", w1.getCellAt(3,1).toString());
    assertEquals("\"\\\"Um\\\" I don't thing \\\\ so \\\\.\"",
            w1.getCellAt(2,4).toString());
  }

}
