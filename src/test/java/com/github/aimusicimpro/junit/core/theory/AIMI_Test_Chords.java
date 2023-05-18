package com.github.aimusicimpro.junit.core.theory;

import com.github.aimusicimpro.core.music.theory.Chord;
import com.github.aimusicimpro.core.music.theory.Note;
import org.junit.Assert;
import org.junit.Test;

public class AIMI_Test_Chords {


    @Test
    public void name() {


        Note dSharpNote = new Note("E");
        Chord dSharpm7Chord = dSharpNote.chord("m7");
        Assert.assertEquals(4, dSharpm7Chord.getNotes().length);

        Assert.assertArrayEquals(
                new Note[]{new Note("E"),}, dSharpm7Chord.getNotes());
        System.out.println(dSharpm7Chord); // D#m7 inversion[0] {D#5[75], F#5[78], A#5[82], C#6[85]}


    }
}
