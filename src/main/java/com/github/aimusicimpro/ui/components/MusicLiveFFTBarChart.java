package com.github.aimusicimpro.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MusicLiveFFTBarChart extends JComponent {

    private static final int BAR_WIDTH = 2;
    private static final int SPACING = 0;
    private static final int CHART_HEIGHT = 200;


    private float[] data;

    public MusicLiveFFTBarChart(float[] data) {
        this.data = data;
    }

    public void setData(float[] data) {
        this.data = data;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.GREEN);

        if( data != null && data.length > 0) {
            int numAmplitudes = data.length;
            int barWidth = Math.max(width / numAmplitudes, 1);

            for (int i = 0; i < numAmplitudes; i++) {
                int barHeight = (int) (data[i] * height);
                int barX = i * barWidth;
                int barY = height - barHeight;

                g.fillRect(barX, barY, barWidth, barHeight);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        int width = data.length * (BAR_WIDTH + SPACING) - SPACING;
        return new Dimension(width, CHART_HEIGHT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            float[] amplitudes = {0.1f, 0.4f, 0.8f, 0.5f, 0.3f, 0.2f, 0.6f};
            MusicLiveFFTBarChart chart = new MusicLiveFFTBarChart(amplitudes);

            JFrame frame = new JFrame("Bar Chart Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(chart);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
