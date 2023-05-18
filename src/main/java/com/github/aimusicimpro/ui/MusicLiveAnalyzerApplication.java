package com.github.aimusicimpro.ui;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.example.InputPanel;
import be.tarsos.dsp.example.SpectrogramPanel;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.Yin;
import com.github.aimusicimpro.core.AudioInputConstants;
import com.github.aimusicimpro.ui.components.MusicLiveBPMComponent;
import com.github.aimusicimpro.ui.components.MusicLiveFFTBarChart;
import com.github.aimusicimpro.ui.panels.MusicLiveAudioStreamPanel;
import com.github.aimusicimpro.ui.panels.MusicResultPanel;
import com.github.aimusicimpro.ui.processors.MusicLiveAudioStreamProcessor;
import com.github.aimusicimpro.ui.processors.MusicLiveFFTProcessor;
import com.github.aimusicimpro.ui.processors.MusicLiveKeyProcessor;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/***
 * Simple Java Application with dedicated User Interface to analyse in real time :
 *  - The BPM
 *  - The Pitch
 *  - The FFT result : Spectogram Panel 
 *  - The tone ( key )
 *  - All keys part of the current chord
 *
 * @author Mowmow
 */
public class MusicLiveAnalyzerApplication extends JFrame {

    /**
     * Threadable application
     */
    private static final long serialVersionUID = -8031486444925100427L;

    // Layout panels

    /**
     * The audio input panel
     */
    private JPanel panelAudioInput;

    /**
     * User Inteface Panel
     */
    private JPanel panelCenter;

    /**
     * Result Panel at the bottom
     */
    private JPanel panelResult;


    // Content Panels

    /**
     * The spectogram panel dsplaying the result of the FFT computation
     */
    private SpectrogramPanel panelSpectogram;

    private MusicLiveFFTBarChart barChart;

    /**
     * The Audio Stream Panel
     */
    private MusicLiveAudioStreamPanel panelAudioStream;

    /**
     * The panel displaying the estimated BPM Value
     */
    private MusicLiveBPMComponent panelBPM;

    /**
     * The panel dsiplaying the estimated Key value
     */
    private MusicResultPanel panelKey;


    // Audio Engine

    /**
     * The Audio Engine that helps use compute all that is required
     */
    private AudioDispatcher dispatcher;


    public MusicLiveAnalyzerApplication() {
        super("Music Live Analyzer Application");
        /**
         * The current frame
         */
        JFrame frame = this;

        // initialize of all the layout
        initlayouts();

        // initialize the input source panel
        initInputPanel();

        // initialize the spectogram panel
        initSpectogramPanel();

        // initialize the audio stream panel
        initAudioStreamPanel();

        // initialize the result panel ( key + BPM )
        initResultPanel();
    }

    /**
     * Main
     *
     */
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            // Run the application
            MusicLiveAnalyzerApplication app = new MusicLiveAnalyzerApplication();
            app.setVisible(true);
        });
    }

    /**
     * Initialize the common layout of the frame
     */
    private void initlayouts() {
        // Layout editing
        this.setLayout(new BorderLayout());
        this.setTitle("Music Live Analyzer Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(800, 800);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

        int rows = 4;
        int cols = 0;
        int border = 5;
        panelCenter = new JPanel(new GridLayout(rows, cols));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        this.add(panelCenter, BorderLayout.CENTER);
    }

    /**
     * Initialize the input source panel
     */
    private void initInputPanel() {
        panelAudioInput = new InputPanel();
        panelAudioInput.setPreferredSize(new Dimension(0, 100));
        panelAudioInput.addPropertyChangeListener("mixer",
                new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        try {
                            updateInput((Mixer) e.getNewValue());

                            // remove the input panel
                            panelCenter.remove(panelAudioInput);
                            panelCenter.invalidate();
                            panelCenter.validate();

                        } catch (LineUnavailableException err) {
                            err.printStackTrace();
                        }
                    }

                    /**
                     * Update the audio input
                     */
                    private void updateInput(Mixer iNewMixer) throws LineUnavailableException {

                        if (dispatcher != null) {
                            dispatcher.stop();
                        }

                        // Specifies a particular arrangement of data in a sound stream
                        AudioFormat format = new AudioFormat(AudioInputConstants.SAMPLE_RATE,
                                16,
                                1,
                                true,
                                false);

                        DataLine.Info dataLineInfo = new DataLine.Info(
                                TargetDataLine.class, format);

                        // Get the input line
                        TargetDataLine line;
                        line = (TargetDataLine) iNewMixer.getLine(dataLineInfo);

                        // Opens the line with the specified format and requested buffer size
                        int numberOfSamples = AudioInputConstants.DEFAULT_BUFFER_SIZE;
                        line.open(format, numberOfSamples);
                        line.start();

                        // input stream is an input stream with a specified audio format and length
                        AudioInputStream stream = new AudioInputStream(line);

                        // Encapsulates an AudioInputStream to make it work with the core TarsosDSP library.
                        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);

                        // Init the audio dispatcher with all processors
                        initAudioDispatcher(audioStream);

                    }
                });
        this.add(panelAudioInput, BorderLayout.NORTH);
    }

    /**
     * Initialize the audio dispatcher with all needed processors
     *
     */
    private void initAudioDispatcher(JVMAudioInputStream iAudioInputStream) {
        // Create a new dispatcher
        dispatcher = new AudioDispatcher(iAudioInputStream,
                AudioInputConstants.DEFAULT_BUFFER_SIZE,
                AudioInputConstants.DEFAULT_BUFFER_OVERLAP);

        /// Add the Audio Processors to the dispatcher

        // The FFT Processor
        dispatcher.addAudioProcessor(
                new MusicLiveFFTProcessor(panelSpectogram, barChart));

        // The key detector
        dispatcher.addAudioProcessor(
                new MusicLiveKeyProcessor(panelKey,
                        new Yin(AudioInputConstants.SAMPLE_RATE,
                                AudioInputConstants.DEFAULT_BUFFER_SIZE)));

        // The Simple Audio Sound Wave
        dispatcher.addAudioProcessor(
                new MusicLiveAudioStreamProcessor(panelAudioStream));

        // Run the dispatcher (on a new thread).
        new Thread(dispatcher, "Audio dispatching").start();
    }

    /**
     * Initialize the Spectogram
     */
    private void initSpectogramPanel() {
        // Create the panel
        panelSpectogram = new SpectrogramPanel();

        barChart = new MusicLiveFFTBarChart(new float[]{});

        // Add to layout
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setPreferredSize(new Dimension(400, 400));
        panel.setBorder(new TitledBorder(" The Spectogram : FFT computation "));

        panel.add(panelSpectogram);

        panel.add(barChart);
        panelCenter.add(panel);
    }

    private void initAudioStreamPanel() {
        // Create
        panelAudioStream = new MusicLiveAudioStreamPanel();

        // Add to layout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(panelAudioStream, BorderLayout.CENTER);
        containerPanel.setBorder(new TitledBorder(" The Audio Stream Panel "));
        panelCenter.add(containerPanel);
    }

    private void initResultPanel() {
        int rows = 1, cols = 3;
        panelResult = new JPanel(new GridLayout(rows, cols));
        panelKey = new MusicResultPanel();
        panelResult.add(panelKey);
        panelCenter.add(panelResult);
    }
}


