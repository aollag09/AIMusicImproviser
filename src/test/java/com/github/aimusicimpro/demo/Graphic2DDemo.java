package com.github.aimusicimpro.demo;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Graphic2DDemo extends JPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new GridLayout(1, 2));
        panel.add(panel2, BorderLayout.CENTER);
        panel2.add(new Graphic2DDemo());
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(20, 20, 500, 500);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        Shape shape = new Rectangle2D.Float(100, 50, 80, 80);

        Graphics2D g2 = (Graphics2D) g;

        g2.fill(shape);
    }
}
