package com.github.aimusicimpro.ui;

import com.github.aimusicimpro.core.AudioInputConstants;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.example.SpectrogramPanel;
import be.tarsos.dsp.util.fft.FFT;

/**
 * The FFT Processor computing the spectrum analysis
 * @author MowMow
 */
public class MusicLiveFFTProcessor implements AudioProcessor{

  /** FFT algorithm API */
  private FFT fft;

  /** UI Panel */
  private SpectrogramPanel panel;

  public MusicLiveFFTProcessor( SpectrogramPanel iPanel ) {
    this.panel = iPanel;
    this.fft = new FFT( AudioInputConstants.DEFAULT_BUFER_SIZE );
  }


  @Override
  public boolean process( AudioEvent audioEvent ){

    float[] audioFloatBuffer = audioEvent.getFloatBuffer();
    float[] amplitudes = new float[ AudioInputConstants.DEFAULT_BUFER_SIZE / 2 ];
    float[] transformbuffer = new float[ AudioInputConstants.DEFAULT_BUFER_SIZE * 2 ];
    System.arraycopy(audioFloatBuffer, 0, transformbuffer, 0, audioFloatBuffer.length); 

    // Compute the DFT
    fft.forwardTransform(transformbuffer);
    fft.modulus(transformbuffer, amplitudes);

    if( panel != null ) {
      // Update the Spectrum panel
      panel.drawFFT(0, amplitudes,fft);
      panel.repaint();
      return true;
    }
    else
      return false;

  }

  @Override
  public void processingFinished() {
  }

}
