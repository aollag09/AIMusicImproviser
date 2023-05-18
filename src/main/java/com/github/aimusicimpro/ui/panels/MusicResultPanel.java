package com.github.aimusicimpro.ui.panels;

import com.github.aimusicimpro.ui.components.MusicLiveBPMComponent;
import com.github.aimusicimpro.ui.components.MusicLiveCirleOfFifthsComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MusicResultPanel extends JPanel {

    public MusicResultPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("Result Key & BPM"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new MusicLiveCirleOfFifthsComponent(),
                new MusicLiveBPMComponent());
        splitPane.setDividerLocation(0.5);

        this.add(splitPane);
    }


}
