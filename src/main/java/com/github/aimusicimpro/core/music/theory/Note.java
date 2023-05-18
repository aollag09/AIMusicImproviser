package com.github.aimusicimpro.core.music.theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Music Note.
 * Basic structure block in music theory.
 *
 * @author Andy671
 */
public class Note implements Comparable<Note> {

    private String name;
    private int octave;
    private String letter;
    private int letterIndex;
    private int value;          //MIDI value

    /**
     * Initializes the note with a name and default octave 4.
     *
     * @param name the name of the note.
     */
    public Note(String name) {
        this(name, 4);
    }


    /**
     * Compute the note from the input Frequency
     *
     * @param iFrequency
     */
    public Note(double iFrequency) {
        // From frequency to closest midi
        //12*log2(fm/440 Hz) + 69
        double mididouble = 12 * Math.log(iFrequency / 440) / Math.log(2) + 69;
        int midi = (int) mididouble;
        fromMidi(midi);
    }

    /**
     * Create the note from the midi integer
     *
     * @param midi
     */
    public Note(int midi) {
        fromMidi(midi);
    }

    /**
     * Initializes the note with a name and octave.
     *
     * @param name   the name of the note.
     * @param octave the initial octave of the note.
     */
    public Note(String name, int octave) {
        this.name = name;
        this.octave = octave;

        letter = name.substring(0, 1);

        List<String> alphabet = new ArrayList<>(Arrays.asList(Music.Alphabet));

        letterIndex = alphabet.indexOf(letter);
        if (letterIndex == -1) {
            throw new MusicTheoryException("Wrong note name '" + letter + "' in '" + name + "'");
        }

        this.value = generateMidiValue();
    }

    /**
     * Moves the note up one semitone.
     *
     * @param note the note to move up.
     */
    public static void semitoneUp(Note note) {
        note.value++;

        if (note.name.charAt(note.name.length() - 1) == Music.FLAT) {
            note.name = note.name.substring(0, note.name.length() - 1);
        } else {
            note.name += Music.SHARP;
        }
    }

    /**
     * Moves the note down one semitone.
     *
     * @param note the note to move down.
     */
    public static void semitoneDown(Note note) {
        note.value--;

        if (note.name.charAt(note.name.length() - 1) == Music.SHARP) {
            note.name = note.name.substring(0, note.name.length() - 1);
        } else {
            note.name += Music.FLAT;
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 8; i++) {
            for (String note : Music.MidiToNotes.values()) {
                Note n = new Note(note, i);
                System.out.println(n.toString() + " => " + n.getFrequencyValue() + " ==>" +
                        (new Note(n.getFrequencyValue()).getKeyName()));
            }
        }


    }

    private void fromMidi(int midi) {
        value = midi;
        octave = midi / 12 - 1;
        name = Music.MidiToNotes.get(midi % 12);
    }

    private int generateMidiValue() {
        int midiValue = Music.Notes.get(letter);
        if (this.name.length() > 1) {
            char accident = this.name.charAt(name.length() - 1);

            if (accident == Music.SHARP) {
                midiValue++;
            } else if (accident == Music.FLAT) {
                midiValue--;
            } else {
                throw new MusicTheoryException("Wrong note accidental name '" + accident + "' in '" + name + "'");
            }
        }
        midiValue += (this.octave + 1) * 12;

        return midiValue;
    }

    /**
     * Generates a scale from a type, with the note as root.
     *
     * @param scaleType the type of scale to generate.
     * @return the generated Scale.
     */
    public Scale scale(String scaleType) {
        if (!Music.Scales.containsKey(scaleType)) {
            throw new MusicTheoryException("Unknown scale type name '" + scaleType + "'");
        }
        return new Scale(this, Music.Scales.get(scaleType));
    }

    /**
     * Generates a chord from a chordType, with the note as root.
     *
     * @param chordType the type of chord to generate.
     * @return chord
     */
    public Chord chord(String chordType) {
        if (!Music.Chords.containsKey(chordType)) {
            throw new MusicTheoryException("Unknown chord type name '" + chordType + "'");
        }
        return new Chord(this, Music.Chords.get(chordType));
    }

    /**
     * @return The relative minor of a key note ( 1.5 tone down )
     */
    public Note getRelativeMinor() {
        Note minor = this.add("M6");
        minor.octave--;
        return minor;
    }

    /**
     * @return The relative minor of a key note ( 1.5 tone up )
     */
    public Note getRelativeMajor() {
        Note major = this.add("m3");
        return major;
    }

    /**
     * Generate a new note after adding the passed interval.
     *
     * @param intervalSymbol the symbol of the interval to add to the note (e.g. m2).
     * @return the new note.
     */
    public Note add(String intervalSymbol) {
        if (!Music.Intervals.containsKey(intervalSymbol)) {
            throw new MusicTheoryException("Unknown interval symbol name '" + intervalSymbol + "'");
        }
        Interval interval = Music.Intervals.get(intervalSymbol);
        int noteLetterValue = Music.Notes.get(this.letter);
        int resultantLetterIndex = (this.letterIndex + interval.degree) % Music.Alphabet.length;
        String resultantNoteName = Music.Alphabet[resultantLetterIndex];
        int resultantLetterValue = Music.Notes.get(resultantNoteName);

        int resultantNoteValue = this.value + interval.steps;
        int newOctave = this.octave;

        if (resultantLetterValue <= noteLetterValue) {
            newOctave += 1;
        }

        resultantLetterValue += (newOctave + 1) * 12;

        int accidentals = resultantNoteValue - resultantLetterValue;

        if (accidentals != 0) {
            char accidentalSymbol = accidentals > 0 ? Music.SHARP : Music.FLAT;
            int numberOfAccidentals = Math.abs(accidentals);

            for (int i = 0; i < numberOfAccidentals; i++) {
                resultantNoteName += accidentalSymbol;
            }
        }

        Note newNote = new Note(resultantNoteName, newOctave);
        newNote.setMidiValue(resultantNoteValue);
        return newNote;
    }

    /**
     * @return the midi value of the note.
     */
    public int getMidiValue() {
        return value;
    }

    private void setMidiValue(int value) {
        this.value = value;
    }

    /**
     * @return the name of the note A[4].
     */
    public String getName() {
        return name;
    }

    /**
     * @return the key name of the note ex : Ab
     */
    public String getKeyName() {
        int i = name.indexOf("[");
        if (i < 1)
            i = name.length();
        return name.substring(0, i);
    }

    /**
     * @return the current octave of the note.
     */
    public int getOctave() {
        return octave;
    }

    /**
     * Sets custom octave. It automatically changes midi value.
     *
     * @param octave the new octave.
     */
    public void setOctave(int octave) {
        int diff = this.octave - octave;
        setMidiValue(diff * 12 + getMidiValue());
        this.octave += diff;
    }

    /**
     * Copies the Note.
     *
     * @return the copy of the note.
     */
    public Note copy() {
        Note newNote = new Note(name.substring(0), octave);
        newNote.setMidiValue(value);
        return newNote;
    }

    /**
     * @return The frequency value of the current note
     */
    public double getFrequencyValue() {
        // https://newt.phys.unsw.edu.au/jw/notes.html
        double freq = 440 * Math.pow(2, ((getMidiValue() - 69.0) / 12.0));
        return freq;
    }

    /**
     * True if the two note have the same name ( octave are not taken into account )
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Note) {
            Note n = (Note) obj;
            return value % 12 == n.value % 12;
        }
        return false;
    }

    @Override
    public int compareTo(Note otherNote) {
        return (this.value - otherNote.value);
    }

    @Override
    public String toString() {
        return name + String.valueOf(octave) + "[" + String.valueOf(value) + "]";
    }

}
