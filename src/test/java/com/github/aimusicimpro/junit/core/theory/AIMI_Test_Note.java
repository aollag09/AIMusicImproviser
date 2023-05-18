package com.github.aimusicimpro.junit.core.theory;

import com.github.aimusicimpro.core.music.theory.Note;
import org.junit.Assert;
import org.junit.Test;

public class AIMI_Test_Note {

    @Test
    public void test_note_from_name() {
        Note dSharpNote = new Note("D#");
        System.out.println(dSharpNote.getName()); // D#4[63]
        Assert.assertEquals( "D#4[63]", dSharpNote.toString()  );
        Assert.assertEquals( "D#", dSharpNote.getName()  );
        Assert.assertEquals( 63, dSharpNote.getMidiValue(), 0 );
    }

    @Test
    public void test_note_from_name_octave() {
        Note dSharpNote = new Note("D#", 5);
        System.out.println(dSharpNote); // D#5[75]
        Assert.assertEquals( "D#5[75]", dSharpNote.toString()  );
    }

    @Test
    public void test_node_from_midi() {
        Note dSharpNote = new Note( 75 );
        Note dSharpNote2 = new Note("D#", 5);
        Assert.assertEquals( dSharpNote2, dSharpNote );
        Assert.assertEquals( dSharpNote2.getMidiValue(), dSharpNote.getMidiValue() );
        Assert.assertEquals( dSharpNote2.getFrequencyValue(), dSharpNote.getFrequencyValue(), 0 );
    }
}