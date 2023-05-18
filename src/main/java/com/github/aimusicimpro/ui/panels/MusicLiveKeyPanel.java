package com.github.aimusicimpro.ui.panels;

import com.github.aimusicimpro.ui.components.MusicLiveCirleOfFifthsComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MusicLiveKeyPanel extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * Label presenting the key value in a Circle of Fifths
     */
    private MusicLiveCirleOfFifthsComponent circle;


    public MusicLiveKeyPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder(" Key & Pitch "));
        initKeyLabel();
    }

    private void initKeyLabel() {

    }

}
