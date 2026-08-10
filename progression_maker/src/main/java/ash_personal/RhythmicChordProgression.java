package ash_personal;

import java.util.LinkedList;

import org.jfugue.theory.Chord;
import org.jfugue.theory.Intervals;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;

/**
 * A chord progression with a rhythmic component added as well, implemented in a linked list. 
 * Stored in roman numeral form through private class {@link ChordInContext}.
 */
public class RhythmicChordProgression {

    private ChordInContext start_chord;
    private ChordInContext end_chord;
    private LinkedList<ChordInContext> progression;
    private int[] time_signature;
    private int num_measures;
    private float availible_beats;
    private Key start_key;
    private Key end_key;

    /**
     * 
     * @param start_chord
     * @param start_key
     * @param time Time signature. Only commonly used (4/4, 6/8 etc.) allowed.
     * @param num_measures No negatives or 0 allowed.
     * @param end_chord
     * @param end_key
     */
    public RhythmicChordProgression(Chord start_chord, Key start_key, int[] time, int num_measures,
        Chord end_chord, Key end_key){

        this.start_key = start_key;
        this.end_key = end_key;
        
        this.start_chord = new ChordInContext(start_chord, start_key, 0);
        this.end_chord = new ChordInContext(end_chord, end_key, 0);

        time_signature = time;
        this.num_measures = num_measures;

        progression = new LinkedList<ChordInContext>();

        progression.addFirst(this.start_chord);
        progression.add(null); //Null middle node, so we have delineator of front and back parts of the progression.
        progression.addLast(this.end_chord);
    }

    /**
     * Adds a smaller chord progression to the middle of this progression, attached to either
     * the front or back chord.
     * 
     * Note that enough space in the progression must be present.
     *      
     * i.e. If adding to front: start -> (smaller_progression) -> null -> other chords -> end 
     *  
     * @param smaller_progression First and last chord will be ignored (should have beats at 0 anyways).
     * @param front_back True for adding to the front, false for adding from the back in.
     */
    public void add(RhythmicChordProgression smaller_progression, boolean front_back){

    }
    /**
     * Adds a single chord to the middle of this progression, attached to either
     * the front or back chord.
     * 
     * Note that there must be enough space for this to work.
     *      
     * i.e. If adding to front: start -> (to_add) -> null -> other chords -> end 
     *  
     * @param to_add First and last chord will be ignored (should have beats at 0 anyways).
     * @param front_back True for adding to the front, false for adding from the back in.
     * @param length Length of the chord in beats.
     */
    public void add(Chord to_add, float length, boolean front_back){

    }

    @Override
    public String toString(){


        return "TO STRING NEEDS IMPLEMENTATION";
    }


    //largely here for testing purposes.
    public String get_start_chord(){
        return start_chord.toString();
    }

    public float get_availible_beats(){
        return availible_beats;
    }

    private class ChordInContext{
        Chord chord;
        Key key;
        ChordFunction function;
        String roman_numeral;
        float num_beats;//might need to change this to a fraction class at some point.

        ChordInContext(Chord chord, Key key, float num_beats){
            this.chord = chord;
            this.key = key;
            this.num_beats = num_beats;
            this.roman_numeral = get_roman_numeral(chord, key);
        }

        public Key get_key(){
            return key;
        }

        public ChordFunction get_function(){
            return function;
        }

        public String get_rom_num(){
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
        /**
         * Hellish function here.
         * @return
         */
        private String get_roman_numeral(Chord c, Key k){
            String out = null;
            Note chord_root = c.getRoot();
            Note key_root = k.getRoot();
            Note[] temp = new Note[2];
            temp[0] = chord_root;
            temp[1] = key_root;
             //%12 handles an octave difference (12 semitones to an octave in western music)
            int num_semitones = Intervals.createIntervalsFromNotes(temp).toHalfstepArray()[1] % 12;

        
            switch(num_semitones){
                case 0 -> out = chord_name_replace_with_numeral(c, "I");
                case 1 -> out = chord_name_replace_with_numeral(c, "bII");
                case 2 -> out = chord_name_replace_with_numeral(c, "II");
                case 3 -> out = chord_name_replace_with_numeral(c, "bIII");
                case 4 -> out = chord_name_replace_with_numeral(c, "III");
                case 5 -> out = chord_name_replace_with_numeral(c, "IV");
                case 6 -> out = chord_name_replace_with_numeral(c, "bV");
                case 7 -> out = chord_name_replace_with_numeral(c, "V");
                case 8 -> out = chord_name_replace_with_numeral(c, "bVI");
                case 9 -> out = chord_name_replace_with_numeral(c, "VI");
                case 10 -> out = chord_name_replace_with_numeral(c, "bVII");
                case 11 -> out = chord_name_replace_with_numeral(c, "VII");
            }
            return out;
        } 

        private String chord_name_replace_with_numeral(Chord c, String rom_num_upper_case){
            String out = c.toHumanReadableString();
            String to_rep = c.getRoot().toStringWithoutDuration();
            String rep_with = rom_num_upper_case;

            out = out.replaceFirst(to_rep, "");
            //now that the root is gone, we can check for substrings
            if(out.contains("MIN")){
               rep_with = rom_num_upper_case.toLowerCase();
               out = out.replace("MIN", "");
            }else if(out.contains("MAJ")){
                if(!(out.contains("MAJ7") || out.contains("MAJ9") || out.contains("MAJ13"))){
                    out = out.replace("MAJ", "");
                }
            }else if(out.contains("DOM")){
                out = out.replace("DOM", "");
            }            
            out = rep_with + out;
            return out;
        }
    }

    
}
