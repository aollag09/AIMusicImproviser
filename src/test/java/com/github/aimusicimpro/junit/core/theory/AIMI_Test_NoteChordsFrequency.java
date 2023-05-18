package com.github.aimusicimpro.junit.core.theory;

import com.github.aimusicimpro.core.music.theory.Chord;
import com.github.aimusicimpro.core.music.theory.Key;
import com.github.aimusicimpro.core.music.theory.Note;
import com.github.aimusicimpro.core.music.theory.Scale;

public class AIMI_Test_NoteChordsFrequency {

    public static void main(String[] args) {

        Note dSharpNote = new Note("D#");
        System.out.println(dSharpNote); // D#4[63]
        System.out.println(dSharpNote.getFrequencyValue()); // D#4[63]

        dSharpNote = new Note("D#", 5);
        System.out.println(dSharpNote); // D#5[75]

        Chord dSharpm7Chord = dSharpNote.chord("m7");
        System.out.println(dSharpm7Chord); // D#m7 inversion[0] {D#5[75], F#5[78], A#5[82], C#6[85]}

        Scale dSharpScale = dSharpNote.scale("minor");
        System.out.println(
                dSharpScale); // D# minor scale {D#5[75], E#5[77], F#5[78], G#5[80], A#5[82], B5[83], C#6[85]}

        Key cFlatMajorKey = new Key("Cb", "major");
        System.out.println(cFlatMajorKey); // Cb major key

        Chord thirdFlatDegreeAugChord = cFlatMajorKey.chord("bIII", "aug");
        System.out.println(thirdFlatDegreeAugChord); // Ebbaug inversion[0] {Ebb4[62], Gb4[66], Bb4[70]}

        thirdFlatDegreeAugChord.setPosition(1);
        System.out.println(thirdFlatDegreeAugChord); // Ebbaug inversion[1] {Bb5[82], Ebb4[62], Gb4[66]}

        thirdFlatDegreeAugChord.sort();
        System.out.println(thirdFlatDegreeAugChord); // Ebbaug inversion[1] {Ebb4[62], Gb4[66], Bb5[82]}
    }

}
