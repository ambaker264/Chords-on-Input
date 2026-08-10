package ash_personal;

import java.util.LinkedList;

import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;

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
        LinkedList<ChordInContext> smaller = smaller_progression.getLL(); 
        

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
                temp = this.progression.get(progression.size() - to_check);
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


        return "TO STRING NEEDS IMPLEMENTATION";
    }


    //largely here for testing purposes.
    public String get_start_chord(){
        return start_chord.toString();
    }

    public float get_availible_beats(){
        return availible_beats;
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
     * @return
     */
    public LinkedList<ChordInContext> getLL(){
        LinkedList<ChordInContext> temp = new LinkedList<ChordInContext>();
        for(ChordInContext c : this.progression){
            temp.add(c);
        }
        return temp;
    }
    
}
