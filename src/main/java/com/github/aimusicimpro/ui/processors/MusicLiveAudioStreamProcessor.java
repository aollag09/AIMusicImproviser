package com.github.aimusicimpro.ui.processors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import com.github.aimusicimpro.ui.panels.MusicLiveAudioStreamPanel;

public class MusicLiveAudioStreamProcessor implements AudioProcessor {

    private MusicLiveAudioStreamPanel panel;

    public MusicLiveAudioStreamProcessor(MusicLiveAudioStreamPanel iPanel) {
        this.panel = iPanel;
    }

    @Override
    public boolean process(AudioEvent audioEvent) {
        float[] audioFloatBuffer = audioEvent.getFloatBuffer();

        if (panel != null && panel.drawComponent != null) {
            panel.drawComponent.draw(audioFloatBuffer);
            panel.repaint();
        }
        return true;
    }

    @Override
    public void processingFinished() {

    }


}
