package com.github.aimusicimpro.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.github.aimusicimpro.core.AudioInputConstants;
import com.github.aimusicimpro.ui.panels.MusicLiveAudioStreamPanel;
import com.github.aimusicimpro.ui.panels.MusicLiveBPMPanel;
import com.github.aimusicimpro.ui.panels.MusicLiveKeyPanel;
import com.github.aimusicimpro.ui.processors.MusicLiveAudioStreamProcessor;
import com.github.aimusicimpro.ui.processors.MusicLiveFFTProcessor;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.example.InputPanel;
import be.tarsos.dsp.example.SpectrogramPanel;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;

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

  /** Threadable application */
  private static final long serialVersionUID = -8031486444925100427L;

  /** The current frame */
  private JFrame frame;
  
  // Layout panels 
  
  /** The audio input panel */
  private JPanel panelAudioInput;
  
  /** User Inteface Panel */
  private JPanel panelCenter;

  /** Result Panel at the bottom */
  private JPanel panelResult;

  
  // Content Panels

  /** The spectogram panel dsplaying the result of the FFT computation */
  private SpectrogramPanel panelSpectogram;

  /** The Audio Stream Panel */
  private MusicLiveAudioStreamPanel panelAudioStream;

  /** The panel displaying the estimated BPM Value */
  private MusicLiveBPMPanel panelBPM;
  
  /** The panel dsiplaying the estimated Key value */
  private MusicLiveKeyPanel panelKey;
  
  
  // Audio Engine
  
  /** The Audio Engine that helps use compute all that is required */
  private AudioDispatcher dispatcher;



  public MusicLiveAnalyzerApplication() {
    super( "Music Live Analyzer Application" );
    frame = this;

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
   * Initialize the common layout of the frame 
   */
  private void initlayouts() {
    // Layout editing
    this.setLayout(new BorderLayout());
    this.setTitle( "Music Live Analyzer Application");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.pack(); 
    this.setSize( 800, 600 );
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

    int rows = 3;
    int cols = 0;
    int border = 5;
    panelCenter = new JPanel( new GridLayout( rows, cols ) );
    panelCenter.setBorder( BorderFactory.createEmptyBorder( border, border, border, border ) );
    this.add( panelCenter, BorderLayout.CENTER );
  }

  /** 
   * Initialize the input source panel
   */
  private void initInputPanel() {
    panelAudioInput = new InputPanel();
    panelAudioInput.setPreferredSize( new Dimension( 0, 100 ));
    panelAudioInput.addPropertyChangeListener("mixer",
        new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent e) {
        try {
          updateInput((Mixer) e.getNewValue());

          // remove the input panel
          panelCenter.remove( panelAudioInput );
          panelCenter.invalidate();
          panelCenter.validate();

        } catch (LineUnavailableException err) {
          err.printStackTrace();
        }
      }

      /**
       * Update the audio input
       * @throws LineUnavailableException 
       */
      private void updateInput( Mixer iNewMixer ) throws LineUnavailableException {

        if(dispatcher!= null){
          dispatcher.stop();
        }

        // Specifies a particular arrangement of data in a sound stream
        AudioFormat format = new AudioFormat( AudioInputConstants.SAMPLE_RATE,
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
        int numberOfSamples = AudioInputConstants.DEFAULT_BUFER_SIZE;
        line.open(format, numberOfSamples);
        line.start();

        // input stream is an input stream with a specified audio format and length
        AudioInputStream stream = new AudioInputStream(line);

        // Encapsulates an AudioInputStream to make it work with the core TarsosDSP library.
        JVMAudioInputStream audioStream = new JVMAudioInputStream(stream);

        // Init the audio dispatcher with all processors
        initAudioDispatcher( audioStream );

      }
    });
    this.add( panelAudioInput, BorderLayout.NORTH );
  }

  /**
   * Initialize the audio dispatcher with all needed processors
   * @param iAudioInputStream
   */
  private void initAudioDispatcher( JVMAudioInputStream iAudioInputStream ) {
    // Create a new dispatcher
    dispatcher = new AudioDispatcher( iAudioInputStream, 
        AudioInputConstants.DEFAULT_BUFER_SIZE,
        AudioInputConstants.DEFAULT_BUFER_OVERLAP );

    /// Add the Audio Processors to the dispatcher

    // The FFT Processor
    dispatcher.addAudioProcessor( 
        new MusicLiveFFTProcessor( panelSpectogram ) );

    // The Simple Audio Sound Wave
    dispatcher.addAudioProcessor(
        new MusicLiveAudioStreamProcessor( panelAudioStream ) );

    // Run the dispatcher (on a new thread).
    new Thread( dispatcher, "Audio dispatching" ).start();
  }

  /** 
   * Initialize the Spectogram
   */
  private void initSpectogramPanel() {

    // Create the panel
    panelSpectogram = new SpectrogramPanel();

    // Add to layout
    JPanel container = new JPanel( new BorderLayout() );
    container.add( panelSpectogram, BorderLayout.CENTER );
    container.setBorder( new TitledBorder( " The Spectogram : FFT computation ") );
    panelCenter.add( container );
  }


  private void initAudioStreamPanel() {

    // Create
    panelAudioStream = new MusicLiveAudioStreamPanel();

    // Add to layout
    JPanel containerPanel = new JPanel( new BorderLayout() );
    containerPanel.add( panelAudioStream, BorderLayout.CENTER );
    containerPanel.setBorder( new TitledBorder( " The Audio Stream Panel " ) ) ;
    panelCenter.add( containerPanel );
  }

  

  private void initResultPanel() {
    int rows = 1, cols = 2;
    panelResult = new JPanel( new BorderLayout() );
    panelKey = new MusicLiveKeyPanel();
    panelResult.add( panelKey );
    panelCenter.add( panelResult );
  }

  /**
   * Main
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    SwingUtilities.invokeLater(
        new Runnable() {

          @Override
          public void run() {

            // Run the application 
            MusicLiveAnalyzerApplication app = new MusicLiveAnalyzerApplication();
            app.setVisible( true );

          }
        });

  }
}


