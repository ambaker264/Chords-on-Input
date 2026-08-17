package ash_personal;

import org.jfugue.theory.Chord;
import org.jfugue.theory.Intervals;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

/**
 * A chord in the greater context of a chord progression. Meant to be used in 
 * a {@link RhythmicChordProgression}.
 */
public class ChordInContext {

    private Chord chord;
    private Key key;
    private ChordFunction function;
    private String roman_numeral;
    private int twelve_tone_degree;
    private float num_beats;//might need to change this to a fraction class at some point.


    /**
     * 
     * @param chord
     * @param key
     * @param num_beats Please remember that num_beats is a float, 
     * and thereby needs to have an appended f after the value.
     */
    public ChordInContext(Chord chord, Key key, float num_beats) {
        this.chord = chord;
        this.key = key;
        this.num_beats = num_beats;
        this.roman_numeral = get_roman_numeral(chord, key);
    }

    public void set_chord_function(ChordFunction f){
        this.function = f;
    }

    public void set_chord_function(){
        this.function = ChordFunction.get_chord_function(this, this.key);
    }

    public Key get_key() {
        return key;
    }

    public int get_twelve_tone_degree(){
        return twelve_tone_degree;
    }

    public float get_num_beats(){
        return num_beats;
    }

    public ChordFunction get_function() {
        return function;
    }

    public String get_rom_num() {
        return roman_numeral;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Roman Numeral: ").append(roman_numeral).append("\n");
        s.append("Name: ").append(chord.toHumanReadableString()).append("\n");
        s.append("Notes: ").append(chord.toNoteString()).append('\n');
        s.append("Number of Beats: ").append(num_beats);
        return s.toString();
    }

    private String get_roman_numeral(Chord c, Key k) {
        String out = null;
        Note chord_root = c.getRoot();
        Note key_root = k.getRoot();
        Note[] temp = new Note[2];
        temp[1] = chord_root;
        temp[0] = key_root;
        //%12 handles an octave difference (12 semitones to an octave in western music)
        int num_semitones = Intervals.createIntervalsFromNotes(temp).toHalfstepArray()[1] % 12;
        this.twelve_tone_degree = num_semitones;

        switch (num_semitones) {
            case 0 ->
                out = chord_name_replace_with_numeral(c, "I");
            case 1 ->
                out = chord_name_replace_with_numeral(c, "bII");
            case 2 ->
                out = chord_name_replace_with_numeral(c, "II");
            case 3 ->
                out = chord_name_replace_with_numeral(c, "bIII");
            case 4 ->
                out = chord_name_replace_with_numeral(c, "III");
            case 5 ->
                out = chord_name_replace_with_numeral(c, "IV");
            case 6 ->
                out = chord_name_replace_with_numeral(c, "bV");
            case 7 ->
                out = chord_name_replace_with_numeral(c, "V");
            case 8 ->
                out = chord_name_replace_with_numeral(c, "bVI");
            case 9 ->
                out = chord_name_replace_with_numeral(c, "VI");
            case 10 ->
                out = chord_name_replace_with_numeral(c, "bVII");
            case 11 ->
                out = chord_name_replace_with_numeral(c, "VII");
        }
        return out;
    }

    private String chord_name_replace_with_numeral(Chord c, String rom_num_upper_case) {
        String out = c.toHumanReadableString();
        String to_rep = c.getRoot().toStringWithoutDuration();
        String rep_with = rom_num_upper_case;

        out = out.replaceFirst(to_rep, "");
        //now that the root is gone, we can check for substrings
        if (out.contains("MIN")) {
            rep_with = rom_num_upper_case.toLowerCase();
            out = out.replace("MIN", "");
        } else if (out.contains("MAJ")) {
            if (!(out.contains("MAJ7") || out.contains("MAJ9") || out.contains("MAJ13"))) {
                out = out.replace("MAJ", "");
            }
        } else if (out.contains("DOM")) {
            out = out.replace("DOM", "");
        }
        out = rep_with + out;
        return out;
    }

}
