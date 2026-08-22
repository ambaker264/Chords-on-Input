package ash_personal;

import java.util.Random;



public class ProgressionFiller extends Thread{

    private final static int SEED = 0;
    private final static Random random;
    private static int thread_count=0;

    static{
        random = new Random((long) SEED);
    }

    private RhythmicChordProgression to_fill;
    private int thread_id;
    private boolean finished_running = false;
    private boolean usable = true;
    private int prog_score;

    public ProgressionFiller(RhythmicChordProgression c){
        this.to_fill = c;
        this.thread_id = thread_count++;
    }

    @Override
    public void run(){
        System.out.println("Starting thread #" + thread_id); //test statment
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
}
