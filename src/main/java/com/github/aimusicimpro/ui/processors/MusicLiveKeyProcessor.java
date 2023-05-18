package com.github.aimusicimpro.ui.processors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchDetector;
import com.github.aimusicimpro.core.music.theory.Note;
import com.github.aimusicimpro.ui.panels.MusicResultPanel;

public class MusicLiveKeyProcessor implements AudioProcessor {

    /**
     * The UI panel
     */
    public MusicResultPanel panel;

    /**
     * Pitch detector
     */
    public PitchDetector detector;


    @Override
    public boolean process(AudioEvent audioEvent) {

        // Detect the pitch
        PitchDetectionResult result = detector.getPitch(audioEvent.getFloatBuffer());
        double pitchFreq = result.getPitch();

        // Retrieve the right note associated
        Note pitchNote = new Note(pitchFreq);

        //panel.draw(pitchNote);

        return false;
    }

    @Override
    public void processingFinished() {
    }


    public void setPitchDetector(PitchDetector iNewDetector) {
        detector = iNewDetector;
    }

}
