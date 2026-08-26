package ash_personal;

import java.util.ArrayList;
import java.util.Random;

import org.jfugue.theory.Key;



public class ProgressionFiller extends Thread{

    private final static int SEED = 0;
    private final static Random random;
    private static int thread_count=0;

    static{
        random = new Random((long) SEED);
    }

    private RhythmicChordProgression to_fill;
    private int front_idx;
    private int back_idx;
    private int thread_id;
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
            /**
             * need to change index for front and back based on what we are about to add
             * if add to front, front idx needs to have added the size of what we added.
             */
            to_fill.add(to_add, front_back);
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
                front_idx += to_add.getLL().size() - 3; //3 dummy variables.
            }else{
                back_idx += to_add.getLL().size() - 3;
            }
            to_fill.add(to_add, front_back);
        }

        //progression is now filled.

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

        //we will have a database of viable chord progressions in key, made on initialization.
        


        return out;
    }

}
