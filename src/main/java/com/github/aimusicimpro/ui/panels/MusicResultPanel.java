package com.github.aimusicimpro.ui.panels;

import com.github.aimusicimpro.core.music.theory.Note;
import com.github.aimusicimpro.ui.components.MusicLiveBPMComponent;
import com.github.aimusicimpro.ui.components.MusicLiveCirleOfFifthsComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MusicResultPanel extends JPanel {

    private final MusicLiveCirleOfFifthsComponent pitchComponent;

    public MusicResultPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Result Pitch & BPM"));
        this.pitchComponent = new MusicLiveCirleOfFifthsComponent();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                pitchComponent,
                new MusicLiveBPMComponent());
        splitPane.setDividerLocation(0.5);

        this.add(splitPane);
    }


    public void setPitchNote(Note pitchNote) {
        pitchComponent.setKey(pitchNote);
    }
}
