package ash_personal;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.jfugue.theory.Chord;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;
import org.jfugue.theory.Scale;

public class ProgressionMaker {
    private static int num_obj = 0;
    private static final int NUM_TO_GENERATE = 1; //number of threads that generate progressions to generate

    private int id;
    private Chord start_chord;
    private Chord end_chord;
    private int[] time_signature;
    private int num_measures;
    private Key start_key;
    private Key end_key;
    private final Scanner scanner;

    private final boolean usable_object;

    private final PrintStream out_stream;

    private RhythmicChordProgression output;

    /**
     * 
     * @param scanner
     * @param out_stream Output stream. Must accept strings.
     */
    public ProgressionMaker(Scanner scanner, PrintStream out_stream){
        this.scanner = scanner;
        id = num_obj;
        num_obj++;
        this.out_stream = out_stream;
        usable_object = this.get_input();
        if(!usable_object){
            System.err.println("Object unusable, quit during constructor.");
        }
    }

    /**
     * Generates a chord progression based on input recieved, and only if input was valid.
     * Computationally Expensive.
     * @throws IllegalStateException
     */
    public void make_progression() throws IllegalStateException{
        if(!usable_object){
            throw new IllegalStateException("Exited during input, unusable object.");
        }
        RhythmicChordProgression base = this.setup_progression();

        ArrayList<ProgressionFiller> all_threads = new ArrayList<>();

        for(int i = 0; i < NUM_TO_GENERATE; i++){
            all_threads.add(new ProgressionFiller(base));
        }
        //now that we have created all the objects, we need to start all of the threads, then join all of them.
        for(ProgressionFiller current : all_threads){
            current.start();
        }
        for(int i = 0; i < all_threads.size(); i++){
            try {
               all_threads.get(i).join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                all_threads.get(i).set_unusable();
            }
        }
        ArrayList<RhythmicChordProgression> finished_progs = new ArrayList<>();
        for(ProgressionFiller current : all_threads){
            finished_progs.add(current.get_filled_list());
        }

        //now we have to pick which progressions we like, how many of them to display, etc.
        
    }

    public Chord get_start_chord(){
        return start_chord;
    }

    public Chord get_end_chord(){
        return end_chord;
    }

    public Key get_start_key(){
        return start_key;
    }

    public Key get_end_key(){
        return end_key;
    }

    public int getID(){
        return id;
    }

    public boolean usable(){
        return usable_object;
    }

    private RhythmicChordProgression setup_progression() throws IllegalArgumentException{
        ChordInContext start = new ChordInContext(start_chord, start_key, 0);
        ChordInContext end = new ChordInContext(end_chord, end_key, 0);
        start.set_chord_function();
        end.set_chord_function();
        if(start.get_function() == ChordFunction.UNKNOWN || end.get_function() == ChordFunction.UNKNOWN){
            throw new IllegalArgumentException("Start or end chord not diatonic to key. Support not added yet.");
        } 
        return new RhythmicChordProgression(start_chord, start_key, time_signature, num_measures, end_chord, end_key);
    }

    private boolean get_input(){

        out_stream.println("\n----Starting Key:----\n");
        if(!get_key(true)){
            return false;
        }

        out_stream.println("\n----Ending Key:----\n");
        if(!get_key(false)){
            return false;
        }

        out_stream.println("\n----Starting Chord:----\n");
        if(!get_chord(true)){
            return false;
        }
 
        out_stream.println("\n----Ending Chord:----\n");
        if(!get_chord(false)){
            return false;
        }

        if(!get_time_constraints()){
            return false;
        }

        out_stream.println("Valid Input Recieved. Generating Progression.");

        return true;

    }

    private boolean get_time_constraints(){
        out_stream.println("\n----Time Info----\n");
        boolean cont_loop = true;
        while (cont_loop) { 
            cont_loop = false;
            out_stream.println("Please input the time signature as 2 ints (ie. 4 4):\n>");
            this.time_signature = new int[2];
            for(int i=0; i<=1; i++){
                if(scanner.hasNextInt()){
                    this.time_signature[i] = scanner.nextInt();
                }else{
                    String check_terminate;
                    check_terminate = scanner.next().toUpperCase().strip();
                    if(check_terminate.equals("EXIT") || check_terminate.equals("QUIT")
                         || check_terminate.equals("STOP")){
                        return false;
                    }
                    cont_loop = true;
                }
            }
            if(!(time_signature[1] == 2 || time_signature[1] == 4 || time_signature[1] == 8)){
                cont_loop = true;
            }
            if(time_signature[0] <= 1){
                cont_loop = true;
            }
            if(cont_loop){
                out_stream.println("Invalid inputs. Please try again.");
            } 
        }

        cont_loop = true;
        while(cont_loop){
            cont_loop = false;
            out_stream.println("Please input the number of measures between the start and end chord:\n>");
            if(scanner.hasNextInt()){
                this.num_measures = scanner.nextInt();
            }else{
                String check_terminate;
                check_terminate = scanner.next().toUpperCase().strip();
                if(check_terminate.equals("EXIT") || check_terminate.equals("QUIT")
                    || check_terminate.equals("STOP")){
                    return false;
                }
                cont_loop = true;
            }
            if(this.num_measures <= 0){
                cont_loop = true;
            }
            if(cont_loop){
                out_stream.println("Invalid input. Please try again.");
            }
        }
        return true;
    }

    /*Just using jfugue's chord labeling system, not too bad. Will check for validity of course.*/
    private boolean get_chord(boolean start_end){
        boolean cont_while = true;
        while(cont_while){
            cont_while = false;
            out_stream.println("Please enter (in jFugue chord notation: ie. CMAJ7) the chord you want.\n>");
            String input = scanner.next().toUpperCase().strip();
            if(input.equals("EXIT") || input.equals("QUIT") || input.equals("STOP")){
                return false;
            }
            try{
                if(start_end){
                    this.start_chord = new Chord(input);
                }else{
                    this.end_chord = new Chord(input);
                }
            }catch(NullPointerException e){
                out_stream.println("Invalid input, please try again, or consult jfugue style for chord input.");
                cont_while = true;
            } 
        }
        return true;
    }

    private boolean get_key(boolean start_end){
        boolean getting_key = true;
        String key_root;
        String key_maj_min;
        Key to_get = null;
        while(getting_key){
            boolean root_valid;
            boolean majmin_valid;
            Scale maj_min = null;
            out_stream.print("Please enter the root of key:\n>");
            key_root = scanner.next();
            out_stream.print("Please enter if the key is major or minor:\n>");
            key_maj_min = scanner.next();
 
            key_maj_min = key_maj_min.toLowerCase();
            key_maj_min = key_maj_min.strip();

            switch(key_maj_min){
                case "major", "maj", "^" -> {
                    maj_min = Scale.MAJOR;
                    majmin_valid = true;
                }
                case "minor", "min", "-" -> {
                    maj_min = Scale.MINOR;
                    majmin_valid = true;
                }
                case "exit", "quit", "stop" -> {
                    return false;
                }
                default->{
                    majmin_valid = false;
                }
            }

            if(!majmin_valid){
                System.err.println("Invalid Choice of Major or Minor. Please try again.");
                continue;
            }

            key_root = key_root.toLowerCase();
            key_root = key_root.strip();

            switch(key_root){
                case "exit", "quit", "stop" -> {
                    return false;
                }
                case "c", "b#"->{
                    to_get = new Key(new Note("C"), maj_min);
                    root_valid = true;
                }
                case "d"->{
                     to_get = new Key(new Note("D"), maj_min);
                    root_valid = true;
                }
                case "e", "fb"->{
                    to_get = new Key(new Note("E"), maj_min);
                    root_valid = true;
                }
                case "f", "e#"->{
                    to_get = new Key(new Note("F"), maj_min);
                    root_valid = true;
                }
                case "g"->{
                    to_get = new Key(new Note("G"), maj_min);
                    root_valid = true;
                }
                case "a"->{
                    to_get = new Key(new Note("A"), maj_min);
                    root_valid = true;
                }
                case "b", "cb"->{
                    to_get = new Key(new Note("B"), maj_min);
                    root_valid = true;
                }
                case "bb", "a#"->{
                    to_get = new Key(new Note("Bb"), maj_min);
                    root_valid = true;
                }
                case "ab", "g#"->{
                    to_get = new Key(new Note("Ab"), maj_min);
                    root_valid = true;
                }
                case "eb", "d#"->{
                    to_get = new Key(new Note("Eb"), maj_min);
                    root_valid = true;
                }
                case "db", "c#"->{
                    to_get = new Key(new Note("Db"), maj_min);
                    root_valid = true;
                }
                case "gb", "f#"->{
                    to_get = new Key(new Note("Gb"), maj_min);
                    root_valid = true;
                }
                default ->{
                    root_valid = false;
                }
            }

            if(root_valid && majmin_valid){
                getting_key = false;
            }else{
                out_stream.println("Invalid Root or Major Minor Designation. Please Try again.");
            }
        }
        //finally assign the key
        if(start_end){
            this.start_key = to_get;
        }else{
            this.end_key = to_get;
        }

        return true;
    }
}
