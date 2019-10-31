package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetReader;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    /*
      TODO: For now, look in the args array to obtain a filename and a cell name,
      - read the file and build a model from it, 
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */

    Worksheet worksheet = new BasicWorksheet();
    Scanner scan = new Scanner(new InputStreamReader(System.in));

    while (scan.hasNext()) {
      if (scan.next().equals("-in")) {
        try {
          File toRead = new File(scan.next());
          worksheet = WorksheetReader.read(new BasicWorksheet.BasicWorksheetBuilder(),
                  new FileReader(toRead));
        } catch (FileNotFoundException e) {
          System.out.println(scan.next());
          return;
        }
      }

      if (scan.next().equals("-eval")) {
        String cellName = scan.next();
        char[] nameChar = cellName.toCharArray();
        int numIndex = 0;
        for (int i = 0; i < nameChar.length; i++) {
          if (Character.isDigit(nameChar[i])) {
            numIndex = i;
            break;
          }
        }

        try {
          Coord cell = new Coord(Coord.colNameToIndex(cellName.substring(0, numIndex)),
                  Integer.parseInt(cellName.substring(numIndex)));
          try {
            System.out.println(worksheet.getCellAt(cell).toString());
          } catch (Exception e) {
            System.out.println("Error in cell " + cellName + ": " + e.getMessage());
          }
        } catch (NumberFormatException e) {
          System.out.println("Command line is malformed.");
        }
      }
    }

  }
}
