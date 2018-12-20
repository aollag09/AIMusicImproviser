package com.github.aimusicimpro.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MusicLiveAudioStreamPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  /** Panel Constants */
  public static final double MAX_VALUE = 2.5;
  public static final int MAX_SPEED = 25;
  public static final int MIN_SPEED = 1;
  public static final int DEFAULT_SPEED = 3;
  
  /** UI Components */
  public AudioStreamDrawComponent drawComponent;
  public JSlider speedSlide;

  /** Panel Parameters */
  public int speed = DEFAULT_SPEED;

  public MusicLiveAudioStreamPanel() {

    super();
    this.setLayout( new BorderLayout() );
    drawComponent = new AudioStreamDrawComponent();
    this.add( drawComponent, BorderLayout.CENTER );

    initSlider();

  }

  /** 
   * Init the slider for the speed
   */
  private void initSlider() {
    JPanel speedpanel = new JPanel(new BorderLayout());
    speedpanel.setPreferredSize( new Dimension( 70 , 480*4));
    speedpanel.setBorder( new TitledBorder( "Zoom" ) );
    speedSlide = new JSlider( SwingConstants.VERTICAL );
    speedSlide.setMaximum( MAX_SPEED );
    speedSlide.setMinimum( MIN_SPEED );
    speedSlide.setPaintTicks(true);
    speedSlide.setMajorTickSpacing( 2 );
    speedSlide.setValue( 3 );
    speedSlide.addChangeListener( new ChangeListener() {

      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
          int fps = (int)source.getValue();
          speed = fps;
          ( ( TitledBorder ) (( JPanel ) source.getParent() ).getBorder() ).setTitle("Zoom " + speed );
          drawComponent.resetDrawing();
        }
      }
    });
    speedpanel.add( speedSlide );
    this.add( speedpanel, BorderLayout.LINE_END );

  }

  public class AudioStreamDrawComponent extends JComponent implements ComponentListener {

    /** */
    private static final long serialVersionUID = 1L;

    /** UI Components */
    private BufferedImage bufferedImage;
    private Graphics2D bufferedGraphics;

    private int position;

    public AudioStreamDrawComponent() {
      super();
      bufferedImage = new BufferedImage( 640*4,
          480*4,
          BufferedImage.TYPE_INT_RGB );
      bufferedGraphics = bufferedImage.createGraphics();
      this.addComponentListener( this );
    }

    @Override
    protected void paintComponent(Graphics g) {
      g.drawImage( bufferedImage, 0, 0, null );
    }

    public void draw( float[] iAudio ) {

      if( position == 0 ) 
        resetDrawing();

      int mspeed = speed;
      int delta = iAudio.length / mspeed;
      for(int i = 0; i< mspeed; i ++ ) {

        int start = i * delta;
        int end = ( i + 1 ) * delta;

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int j = start; j < end; j++) {
          if( iAudio[ j ] < min )
            min = iAudio[ j ];
          if( iAudio[ j ] > max )
            max = iAudio[ j ];
        }

        // Scale min & max with current dimension
        int height = getHeight() / 2;
        double ampl = max - min;
        int amplint = (int) (ampl * height / MAX_VALUE);

        // Draw the line at the current position
        bufferedGraphics.setColor( Color.BLACK );
        bufferedGraphics.fillRect( position, height - amplint, 1, amplint * 2 );

        position ++;
        if( position > getWidth() ) {
          position = position % getWidth();
        }

        // Clear existing informations
        int size = 1;
        // background
        bufferedGraphics.setColor( new Color( 241, 240, 206 ) );
        bufferedGraphics.fillRect(position, 0, size, getHeight());

        // center
        bufferedGraphics.setColor( Color.BLACK );
        bufferedGraphics.fillRect(position, getHeight()/2, size, 1);

      }

      repaint();
    }

    public void resetDrawing( ) {

      position = 0; 

      // background
      bufferedGraphics.setColor( new Color( 241, 240, 206 ) );
      bufferedGraphics.fillRect(0, 0, getWidth(), getHeight());

      // center
      bufferedGraphics.setColor( Color.BLACK );
      bufferedGraphics.fillRect(0, getHeight()/2, getWidth(), 1);
    }

    @Override
    public void componentHidden(ComponentEvent e) {
      // TODO Auto-generated method stub

    }

    @Override
    public void componentMoved(ComponentEvent e) {
      // TODO Auto-generated method stub

    }

    @Override
    public void componentResized(ComponentEvent e) {
      if( getWidth() > 0 && getHeight() > 0) {
        bufferedImage = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedGraphics = bufferedImage.createGraphics();
        position = 0;
      }
    }

    @Override
    public void componentShown(ComponentEvent e) {

    }
  }
}


