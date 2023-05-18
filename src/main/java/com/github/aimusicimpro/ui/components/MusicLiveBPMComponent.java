package com.github.aimusicimpro.ui.components;

import javax.swing.*;
import java.awt.*;

public class MusicLiveBPMComponent extends JComponent {

    private int bpm;

    private static final Font FONT = new Font("Arial", Font.BOLD, 24);

    public MusicLiveBPMComponent() {
        this.bpm = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(FONT);
        FontMetrics fontMetrics = g.getFontMetrics();

        int x = (getWidth() - fontMetrics.stringWidth(String.valueOf(this.bpm))) / 2;
        int y = (getHeight() - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.bpm), x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }


    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}
