package ash_personal;

import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import org.jfugue.theory.Key;



public class ProgressionFiller extends Thread{

    private final static int SEED = 0;
    private final static int DIV_FACTOR_SAME_FUNCTION = 2;
    private final static int DIV_FACTOR_SKIP_FUNCTION = 5; //ie Tonic->dominant
    private final static int MULT_FACTOR_CORR_FUNCTION = 2;
    private final static int FILL_PROG_BONUS = 5; //multiplier to weight
    private final static int FILL_PROG_WRONG_DIV = 5;

    private final static Random random;
    private static int thread_count=0;

    static{
        random = new Random((long) SEED);
    }

    private final RhythmicChordProgression to_fill;
    private int front_idx;
    private int back_idx;
    private final int thread_id;
    private boolean finished_running = false;
    private boolean usable = true;
    private int prog_score;

    public ProgressionFiller(RhythmicChordProgression c){
        this.to_fill = c;
        this.thread_id = thread_count++;
        this.front_idx = 0;
        this.back_idx = 2;
    }

    @Override
    public void run(){
        System.out.println("Starting thread #" + thread_id); //test statment
        this.fill_progression();
        this.score_progression();
        finished_running = true;
    }

    public RhythmicChordProgression get_filled_list(){
        if(!finished_running){
            throw new IllegalStateException("Thread has not finished running.");
        }
        return this.to_fill;
    }

    public boolean is_usable(){
        return this.usable;
    }
    public void set_unusable(){
        this.usable = false;
    }
    public int get_score(){
        return this.prog_score;
    }


    private int score_progression(){
        int score = 0;
        //we go through the progression, adding and subtracting score
        //based on "good" things in it, and use it to pick which ones to show
        ChordInContext preceding = null;
        for(ChordInContext current : this.to_fill.getLL()){
            if(preceding == null){
                preceding = current;
                continue; //just skip
            }
            /**Need to figure out internal logic here. Once done, we can actually make the function*/

        }
        this.prog_score = score;
        return score;
    }

    /**
     * The major method that dictates how we fill a list, depending on how we add
     * to the progression. We take a random value after getting all possible progressions, 
     * and we pick based on each progression's weight.
     */
    private void fill_progression(){
        ArrayList<RhythmicChordProgression> availible_progs;
        boolean front_back;

        //first we fix the key we are in.
        while(!to_fill.get_start_key().getScale().equals(to_fill.get_end_key().getScale())){
            //first we check if we add to the front or back.
            synchronized(random){
                front_back = random.nextBoolean();
            }
            availible_progs = key_fix_progs(
                to_fill.getLL().get(front_idx).get_key(), to_fill.getLL().get(back_idx).get_key(), front_back);
            //now we pick the progression that we add.
            RhythmicChordProgression to_add = this.pick_from_list(availible_progs); 
            
            int temp = to_add.getLL().size();
            to_fill.add(to_add, front_back);
            front_idx += to_fill.getLL().size() - temp;
        }

        //now that the key is the same between the progressions, we now need to fill in
        //the rest of the spots

        while(to_fill.get_availible_beats() > 0.1f){
            synchronized(random){
                front_back = random.nextBoolean();
            }
            availible_progs = same_key_progs(this.to_fill.getLL().get(front_idx).get_key(), front_back);
            //now we pick the progression that we add.
            RhythmicChordProgression to_add = this.pick_from_list(availible_progs);
            if(front_back){
                front_idx += to_add.getLL().size(); //no dummy variables here at start/end
            }else{
                back_idx += to_add.getLL().size();
            }
            to_fill.add(to_add, front_back);
        }
    }

    /**
     * Picks a chord progression from those in the provided list randomly, based on
     * each progression's weight.
     * @param availible_progs
     * @return
     */
    private RhythmicChordProgression pick_from_list(ArrayList<RhythmicChordProgression> availible_progs){
        int sum = 0;
        for(RhythmicChordProgression current : availible_progs){
                sum += current.get_weight();
        }
        int value;
        synchronized(random){
            value = random.nextInt();
        }
        value = value % sum;
        sum = 0;
        for(RhythmicChordProgression current : availible_progs){
            sum += current.get_weight();
            if(value <= sum){
                return current;
            }
        }
        return null; //some error if here
    }


    private ArrayList<RhythmicChordProgression> key_fix_progs(Key start_key, Key end_key, boolean front_back){
        System.err.println("Not Implemented Yet");
        return null;
    }

    private ArrayList<RhythmicChordProgression> same_key_progs(Key key, boolean front_back){
        ArrayList<RhythmicChordProgression> out = new ArrayList<>();
        ChordInContext ref_chord = null;
        //find our reference chord
        LinkedList<ChordInContext> check_through = this.to_fill.getLL();
        boolean cont = true;
        int ref_idx = 0;

        //this loop shouldn't go infinite, it will throw an IndexOutOfBounds if an error exists.
        while(cont){
            ChordInContext current = check_through.get(ref_idx);
            if(check_through.get(ref_idx + 1) == null){
                if(front_back){
                    ref_chord = current;
                }else{
                    ref_chord = check_through.get(ref_idx + 2);
                }
                cont = false;
            }else{
                ref_idx++;
            }
        }
        
        //we will have a database of viable chord progressions in key, made on initialization.
        if(front_back){
            //from the front, so we follow our cycle of Subdominant->Dominant-->Tonic
            for(RhythmicChordProgression current : RhythmicChordProgression.in_key_progs){
 
                //we want to add to the list, and maybe edit the weight if there isn't something we want
                ChordFunction current_fun = current.get_schord().get_function();               
                //weeding out all the chords that don't work.
                //first check if diatonic
                if(!current.get_schord().get_key().getScale().equals(ref_chord.get_key().getScale())){
                    continue; //just skip the iteration if not diatonic
                }

                if(current.get_progression_length() > to_fill.get_availible_beats()){
                    continue;
                }else if(abs(current.get_progression_length() - to_fill.get_availible_beats()) < 0.1f){
                    //floats make this tricky, but I want to give a bonus to progressions that will 
                    //fill the progression completely, and finish it
                    int temp = current_fun.get_value() - ref_chord.get_function().get_value(); 
                    if(temp == -2 || temp == 1){
                        current = new RhythmicChordProgression(current).set_weight(current.get_weight() * FILL_PROG_BONUS);
                    }else{
                        //if it completes the progression, but with the wrong motion, we don't want that.
                        current = new RhythmicChordProgression(current).set_weight(current.get_weight() / FILL_PROG_WRONG_DIV);
                    }
                }
                if(current_fun == ChordFunction.UNKNOWN || ref_chord.get_function() == ChordFunction.UNKNOWN){
                    //if either is unknown, we can add support later, for now just skip it
                    continue;
                }

                switch(current_fun.get_value() - ref_chord.get_function().get_value()){
                    case 0 -> //i.e. dominant to dominant
                        out.add(new RhythmicChordProgression(current).set_weight(current.get_weight() / DIV_FACTOR_SAME_FUNCTION));
                    case -1, 2 -> //i.e. going subdominant to tonic, dominant to subdominant, 2 is tonic to dominant
                        out.add(new RhythmicChordProgression(current).set_weight(current.get_weight() / DIV_FACTOR_SKIP_FUNCTION));
                    case -2, 1 -> //correct motion, i.e. tonic to subdominant
                        out.add(new RhythmicChordProgression(current).set_weight(current.get_weight() * MULT_FACTOR_CORR_FUNCTION));
                }
            }
        }else{
            /**same deal, just going backwards instead*/
            for(RhythmicChordProgression current : RhythmicChordProgression.in_key_progs){ 
                //we want to add to the list, and maybe edit the weight if there isn't something we want
                ChordFunction current_fun = current.get_echord().get_function();               
                //weeding out all the chords that don't work.
                //first check if diatonic
                if(!current.get_echord().get_key().getScale().equals(ref_chord.get_key().getScale())){
                    continue; //just skip the iteration if not diatonic
                }
                if(current.get_progression_length() > to_fill.get_availible_beats()){
                    continue;
                }else if(abs(current.get_progression_length() - to_fill.get_availible_beats()) < 0.1f){
                    //floats make this tricky, but I want to give a bonus to progressions that will 
                    //fill the progression completely, and finish it
                    int temp = current_fun.get_value() - ref_chord.get_function().get_value(); 
                    if(temp == -1 || temp == 2){
                        current = new RhythmicChordProgression(current).set_weight(current.get_weight() * FILL_PROG_BONUS);
                    }else{
                        //if it completes the progression, but with the wrong motion, we don't want that.
                        current = new RhythmicChordProgression(current).set_weight(current.get_weight() / FILL_PROG_WRONG_DIV);
                    }
                }
                if(current_fun == ChordFunction.UNKNOWN || ref_chord.get_function() == ChordFunction.UNKNOWN){
                    //if either is unknown, we can add support later, for now just skip it
                    continue;
                }

                switch(current_fun.get_value() - ref_chord.get_function().get_value()){
                    case 0 ->
                        out.add(new RhythmicChordProgression(current).set_weight(current.get_weight() / DIV_FACTOR_SAME_FUNCTION));
                    case -1, 2 ->
                        out.add(new RhythmicChordProgression(current).set_weight(current.get_weight() * MULT_FACTOR_CORR_FUNCTION));
                    case -2, 1 ->
                        out.add(new RhythmicChordProgression(current).set_weight(current.get_weight() / DIV_FACTOR_SKIP_FUNCTION));
                }
            }
        }


        return out;
    }

}
