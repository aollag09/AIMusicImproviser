package com.github.aimusicimpro.junit;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JPanel {

  public void paint(Graphics g) {
    Shape shape = new Rectangle2D.Float(100, 50, 80, 80);
    
    Graphics2D g2 = (Graphics2D) g;
    
    g2.fill(shape);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel( new  BorderLayout());
    JPanel panel2 = new JPanel( new  GridLayout(1,2));
    panel.add( panel2, BorderLayout.CENTER );
    panel2.add( new Main());
    frame.add( panel );
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(20,20, 500,500);
    frame.setVisible(true);
  }
}