package com.github.aimusicimpro.ui.processors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchDetector;
import com.github.aimusicimpro.core.music.theory.Note;
import com.github.aimusicimpro.ui.panels.MusicResultPanel;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicLiveKeyProcessor implements AudioProcessor {

    public static final Logger LOGGER = LoggerFactory.getLogger(MusicLiveKeyProcessor.class);
    /**
     * The UI panel
     */
    public MusicResultPanel panel;

    /**
     * Pitch detector
     */
    public PitchDetector detector;

    public MusicLiveKeyProcessor(MusicResultPanel panel, PitchDetector detector) {
        this.panel = panel;
        this.detector = detector;
    }

    @Override
    public boolean process(AudioEvent audioEvent) {

        // Detect the pitch
        PitchDetectionResult result = detector.getPitch(audioEvent.getFloatBuffer());

        if (result.isPitched()) {
            // Retrieve the right note associated
            double pitchFreq = result.getPitch();
            Note pitchNote = new Note(pitchFreq);
            panel.setPitchNote(pitchNote);
            LOGGER.info("New Pitch Note : {} and result :  {}", pitchNote, new Gson().toJson(result));
        } else {
            panel.setPitchNote(null);
        }

        return false;
    }

    @Override
    public void processingFinished() {
    }


}
