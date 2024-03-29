package com.github.aimusicimpro.core.music.theory;

/**
 * @author Andy671
 */
public class Degree {
    private static final String REGEX_BASE_ROMAN = "^(?i)[" + Music.SHARP + Music.FLAT + "IV]*";
    private static final String REGEX_CHORD_TYPE = "(?i)[^IV]*$";
    private static final String REGEX_IN_KEY_CHORD_TYPE = "(?i)[^" + "ø" + "°" + "+" + "IV]*$";

    public static String toRomanNumeral(String baseRomanDegree, String chordType) {
        if (!Music.Chords.containsKey(chordType)) {
            throw new MusicTheoryException("Unknown chord type '" + chordType + "'");
        }

        String romanNumeral;

        if (Music.ChordsMajor.containsKey(chordType)) {
            romanNumeral = baseRomanDegree;
            romanNumeral += Music.ChordsMajor.get(chordType);
        } else {
            romanNumeral = baseRomanDegree.toLowerCase();
            romanNumeral += Music.ChordsMinor.get(chordType);
        }

        return romanNumeral;
    }

    public static String toChordType(String romanNumeral) {
        String baseRoman = romanNumeral.replaceAll(REGEX_CHORD_TYPE, "");
        String romanChordType = romanNumeral.replaceAll(REGEX_BASE_ROMAN, "");
        String chordType;
        if (baseRoman.toUpperCase().equals(baseRoman)) {  // major
            chordType = Music.ChordsMajorInverse.get(romanChordType);
        } else {                                          // minor
            chordType = Music.ChordsMinorInverse.get(romanChordType);
        }
        return chordType;
    }

    public static String toBaseRoman(String romanNumeral) {
        String baseRoman = romanNumeral.replaceAll(REGEX_CHORD_TYPE, "");
        return baseRoman;
    }

    public static String toInKeyRoman(String romanNumeral) {
        String baseRoman = romanNumeral.replaceAll(REGEX_IN_KEY_CHORD_TYPE, "");
        return baseRoman;
    }

    public static String toBaseRomanCaps(String romanNumeral) {
        String baseRoman = romanNumeral.replaceAll(REGEX_CHORD_TYPE, "");
        if (baseRoman.charAt(0) == Music.FLAT) {
            return baseRoman.substring(0, 1) + baseRoman.substring(1).toUpperCase();
        }
        return baseRoman.toUpperCase();
    }

}
