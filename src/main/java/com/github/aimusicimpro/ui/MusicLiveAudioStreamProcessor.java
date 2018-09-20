package com.github.aimusicimpro.ui;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class MusicLiveAudioStreamProcessor implements AudioProcessor {

  private AudioStreamPanel panel;

  public MusicLiveAudioStreamProcessor( AudioStreamPanel iPanel ) {
    this.panel = iPanel;
  }

  @Override
  public boolean process(AudioEvent audioEvent) {
    float[] audioFloatBuffer = audioEvent.getFloatBuffer();

    if( panel != null && panel.drawComponent != null ) {
      panel.drawComponent.draw(audioFloatBuffer);
      panel.repaint();
    }
    return true;
  }

  @Override
  public void processingFinished() {

  }




}
