package edu.cs3500.spreadsheets.view;
import java.awt.*;

import javax.swing.*;

import edu.cs3500.spreadsheets.model.Worksheet;

public class WorksheetGraphicsView extends JFrame implements WorksheetView {
  private GridPanel gridPanel;
  private Worksheet model;

  public WorksheetGraphicsView(Worksheet model) {
    super();
    this.model = model;
    this.setTitle("Worksheet :)");
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());
    gridPanel = new GridPanel();
    gridPanel.setPreferredSize(new Dimension(1000, 1000));
    this.add(gridPanel, BorderLayout.CENTER);
    gridPanel.setModel(model);
    this.pack();
  }


  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void render() {
    this.repaint();
  }

  @Override
  public void showErrorMessage(String error) {
    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
