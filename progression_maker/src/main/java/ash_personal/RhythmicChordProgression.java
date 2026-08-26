package ash_personal;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;

import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;
import org.jfugue.theory.Scale;

/**
 * A chord progression with a rhythmic component added as well, implemented in a linked list. 
 * Stored in roman numeral form through private class {@link ChordInContext}.
 */
public class RhythmicChordProgression {

    private static int DEFAULT_WEIGHT = 10;

    public static final ArrayList<RhythmicChordProgression> in_key_progs = new ArrayList<>();
    public static final ArrayList<RhythmicChordProgression> change_key_progs = new ArrayList<>(); 

    /**
     * Sets up all sample progressions used in generating progressions. Very expensive.
     */
    static{
        //first we set up all the keys we will be in.
        ArrayList<Key> all_major_keys = new ArrayList<>();
        ArrayList<Key> all_minor_keys = new ArrayList<>();
        ArrayList<Note> twelve_tones = new ArrayList<>();
        ArrayList<int[]> time_signatures = new ArrayList<>();

        //won't work with odd numbers in time signatures, just use 6/8 instead of 3/4 instead
        time_signatures.add(new int[] {4, 4});
        time_signatures.add(new int[] {2, 2});
        time_signatures.add(new int[] {6, 8});

        twelve_tones.add(new Note("A"));
        twelve_tones.add(new Note("Bb"));
        twelve_tones.add(new Note("B"));
        twelve_tones.add(new Note("C"));
        twelve_tones.add(new Note("Db"));
        twelve_tones.add(new Note("D"));
        twelve_tones.add(new Note("Eb"));
        twelve_tones.add(new Note("E"));
        twelve_tones.add(new Note("F"));
        twelve_tones.add(new Note("Gb"));
        twelve_tones.add(new Note("G"));
        twelve_tones.add(new Note("Ab"));

        for(Note current : twelve_tones){
            all_major_keys.add(new Key(current, Scale.MAJOR));
            all_minor_keys.add(new Key(current, Scale.MINOR));
        }

        //now we have to make the progressions from the roman numerals that we want
        //for this, we will use jfugue's ChordProgression class to get our strings quickly.

        ArrayList<ChordProgression> major_progs = new ArrayList<>();
        ArrayList<ChordProgression> minor_progs = new ArrayList<>();

        //can keep on adding here, and it will be loaded into all keys.
        major_progs.add(new ChordProgression("ii V I"));
        major_progs.add(new ChordProgression("ii V"));
        major_progs.add(new ChordProgression("V I"));

        int[] major_prog_weights = new int[] {DEFAULT_WEIGHT, DEFAULT_WEIGHT, DEFAULT_WEIGHT};

        //add minor progressions here
        minor_progs.add(new ChordProgression("v i"));
        int[] minor_prog_weights = new int[] {DEFAULT_WEIGHT};

        //now that we have all the progressions, we need to turn them into the format we use.
        Chord[] temp_chord_holder;
        LinkedList<ChordInContext> translator;
        RhythmicChordProgression temp;
        int prog_weight_idx = 0;

        for(ChordProgression current_prog : major_progs){
            for(Key key : all_major_keys){
                current_prog.setKey(key);
                temp_chord_holder = current_prog.getChords();
                for(int[] time_signature : time_signatures){
                    translator = new LinkedList<>();
                    //one progression where each chord is one bar, one where each is half a measure
                    for(Chord chord : temp_chord_holder){
                        translator.add(new ChordInContext(chord, key, time_signature[0]));
                    }
                    temp = new RhythmicChordProgression(translator, time_signature);
                    //now we need to set the progression weight
                    temp.set_weight(major_prog_weights[prog_weight_idx]);
                    RhythmicChordProgression.in_key_progs.add(temp);

                    //now we add the half length progression
                    for(Chord chord : temp_chord_holder){
                        translator.add(new ChordInContext(chord, key, time_signature[0] / 2));
                    }
                    temp = new RhythmicChordProgression(translator, time_signature);
                    temp.set_weight(major_prog_weights[prog_weight_idx]);
                    RhythmicChordProgression.in_key_progs.add(temp);
                }
            }
            prog_weight_idx++; //advance one in the array to correspond with the new progression
        }
        prog_weight_idx = 0;
        //same process but for minor keys
        for(ChordProgression current_prog : minor_progs){
            for(Key key : all_minor_keys){
                current_prog.setKey(key);
                temp_chord_holder = current_prog.getChords();
                for(int[] time_signature : time_signatures){
                    translator = new LinkedList<>();
                    //one progression where each chord is one bar, one where each is half a measure
                    for(Chord chord : temp_chord_holder){
                        translator.add(new ChordInContext(chord, key, time_signature[0]));
                    }
                    temp = new RhythmicChordProgression(translator, time_signature);
                    //now we need to set the progression weight
                    temp.set_weight(minor_prog_weights[prog_weight_idx]);
                    RhythmicChordProgression.in_key_progs.add(temp);

                    //now we add the half length progression
                    for(Chord chord : temp_chord_holder){
                        translator.add(new ChordInContext(chord, key, time_signature[0] / 2));
                    }
                    temp = new RhythmicChordProgression(translator, time_signature);
                    temp.set_weight(minor_prog_weights[prog_weight_idx]);
                    RhythmicChordProgression.in_key_progs.add(temp);
                }
            }
            prog_weight_idx++; //advance one in the array to correspond with the new progression
        }

        //now we add the progressions that change keys, this must be done more by hand.

    }

    private ChordInContext start_chord;
    private ChordInContext end_chord;
    private LinkedList<ChordInContext> progression;
    private int[] time_signature;
    private int num_measures;
    private float availible_beats;
    private float num_beats;
    private Key start_key;
    private Key end_key;

    private int progression_weight;

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
        Chord end_chord, Key end_key, int weight){

        this.start_key = start_key;
        this.end_key = end_key;
        
        this.start_chord = new ChordInContext(start_chord, start_key, 0);
        this.end_chord = new ChordInContext(end_chord, end_key, 0);

        time_signature = time;
        this.num_measures = num_measures;

        this.progression_weight = weight;

        this.availible_beats = (float) this.num_measures * this.time_signature[0];
        this.num_beats = this.availible_beats;

        progression = new LinkedList<ChordInContext>();

        progression.addFirst(this.start_chord);
        progression.add(null); //Null middle node, so we have delineator of front and back parts of the progression.
        progression.addLast(this.end_chord);
    }
    public RhythmicChordProgression(Chord start_chord, Key start_key, int[] time, int num_measures,
        Chord end_chord, Key end_key){
        this(start_chord, start_key, time, num_measures, end_chord, end_key, DEFAULT_WEIGHT);
    }

    public RhythmicChordProgression(RhythmicChordProgression to_copy){
        this.start_key = to_copy.get_start_key();
        this.end_key = to_copy.get_end_key();

        //think this is safe, should be, but maybe check?
        this.start_chord = to_copy.start_chord;
        this.end_chord = to_copy.end_chord;

        this.time_signature = to_copy.time_signature;
        this.availible_beats = to_copy.availible_beats;
        this.progression_weight = to_copy.progression_weight;
        this.num_measures = to_copy.num_measures;

        this.progression = to_copy.getLL(); //now we get a copy of the LL
    }

    /**
     * Creates a FULL chord progression from the linked list provided.
     * @param gen_from
     * @param time_signature
     */
    public RhythmicChordProgression(LinkedList<ChordInContext> gen_from, int[] time_signature){
        this.progression = new LinkedList<>();
        this.num_beats = 0;
        for(ChordInContext current : gen_from){
            if(current == null){
                throw new IllegalArgumentException("Progression Not Full");
            }
            this.progression.add(current);
            this.num_beats += current.get_num_beats();
        }
        this.start_chord = this.progression.getFirst();
        this.end_chord = this.progression.getLast();
        this.start_key = this.progression.getFirst().get_key();
        this.end_key = this.progression.getLast().get_key();
        this.time_signature = time_signature;
        this.availible_beats = 0f;
        this.num_measures = ((int)num_beats) / time_signature[1]; //truncation makes this messy, hopefully don't need it

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
     * @return True if the progression was added, false if not.
     */
    public boolean add(RhythmicChordProgression smaller_progression, boolean front_back){
        LinkedList<ChordInContext> smaller = smaller_progression.getLL(); 
        if(smaller_progression.get_progression_length() >= this.get_availible_beats()){
            return false; 
        }
        for(ChordInContext current : smaller){
            if(current != null && current.get_num_beats() < 0.1f){
                continue;
            }
            this.add(current, front_back);
        }
        return true;
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
    public boolean add(ChordInContext to_add, boolean front_back){
        if(to_add == null){
            return false;
        }
        if(to_add.get_num_beats() > this.get_availible_beats()){
            return false;
        }
        if(abs(to_add.get_num_beats() - this.get_availible_beats()) < 0.001f /*Handles float issues*/){
            //if we have completed the progression, then we get rid of the null in the center.
            int temp = this.add_index(true);
            this.progression.remove(null);
            this.progression.add(temp, to_add);
            this.availible_beats = 0;
            return true;
        }
        this.progression.add(this.add_index(front_back), to_add);
        this.availible_beats -= to_add.get_num_beats();
        return true;
    }

    /**
     * Helper function for adding to the linked list.
     * @return The index of where we will add to the linked list.
     */
    private int add_index(boolean front_back){
        boolean cont_while = true;
        int to_check = 1;
        ChordInContext temp;
        while(cont_while){
            if(front_back){
                //checking front
                temp = this.progression.get(to_check);
                if(temp == null){
                    return to_check;
                }else{
                    to_check++;
                }
            }else{
                temp = this.progression.get(progression.size() - to_check - 1);
                if(temp == null){
                    return progression.size() - to_check;
                }else{
                    to_check++;
                }
            }
        }
        return -1; //failure state
    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("---Chord Progression---\nProgression Length (Beats): ");
        out.append(this.get_progression_length()).append("\n");
        out.append("Chords:\n\n");
        for(ChordInContext current : this.progression){
            if(current == null){
                out.append("MIDDLE_OF_PROGRESSION_DELINEATOR\n");
            }else{
                out.append(current.toString());
                out.append("\n");
            }
            out.append("\n");
        }
        return out.toString();
    }


    //largely here for testing purposes.
    public String get_start_chord(){
        return start_chord.toString();
    }

    public float get_availible_beats(){
        return availible_beats;
    }

    public Key get_start_key(){
        return this.start_key;
    }

    public Key get_end_key(){
        return this.end_key;
    }

    public int get_weight(){
        return this.progression_weight;
    }

    public void set_weight(int weight){
        this.progression_weight = weight;
    }

    /** 
     * @return the length of the progression in beats in its time signature.
     */
    public float get_progression_length(){
        return (num_measures * time_signature[0]) - availible_beats;
    }

    /**
     * Copies down to the chords, but the chords are still mutable (shouldn't need to touch
     * them however.)
     * @return New copied linked list of all chords in the progression.
     */
    public LinkedList<ChordInContext> getLL(){
        LinkedList<ChordInContext> temp = new LinkedList<ChordInContext>();
        for(ChordInContext c : this.progression){
            temp.add(c);
        }
        return temp;
    }
    
}
