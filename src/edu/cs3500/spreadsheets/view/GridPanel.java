package edu.cs3500.spreadsheets.view;

import java.awt.*;

import javax.swing.*;

import edu.cs3500.spreadsheets.model.BasicWorksheet;
import edu.cs3500.spreadsheets.model.Worksheet;

public class GridPanel extends JPanel {
  private Worksheet model;
  private int numCol;
  private int numRow;
  private int cellWidth;
  private int cellHeight;


  public GridPanel() {
    super();
    this.setBackground(new Color(240, 240, 240));
    this.numCol = 10;
    this.numRow = 30;
    this.cellWidth = 85;
    this.cellHeight = 25;
    this.model = new BasicWorksheet();
  }

  public void setModel(Worksheet model) {
    this.model = model;
    numCol = Math.max(numCol, model.getNumCols());
    numRow = Math.max(numRow, model.getNumRows());
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.black);
    for (int i = 0; i <= numCol * cellWidth; i += cellWidth) {
      g2d.drawLine(i, 0, i, numRow * cellHeight);
    }
    for (int i = 0; i <= numRow * cellHeight; i += cellHeight) {
      g2d.drawLine(0, i, cellWidth * numCol, i);
    }

    for (int i = 0; i < numCol; i++) {
      for (int j = 0; j < numRow; j++) {
        String str = "";
        try {
          str = model.getValueAt(i + 1, j + 1).toString();
        } catch (IllegalStateException e) {
          if (e.getMessage().equals("The cell is blank.")) {
            break;
          }
        } catch (Exception e) {
          str = "ERROR!!!";
        }

        g2d.drawString(str, i * cellWidth + 5, (int) ((j + 0.5) * cellHeight));
      }
    }

  }
}
