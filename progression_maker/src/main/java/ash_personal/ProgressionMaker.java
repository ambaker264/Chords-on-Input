package ash_personal;

import java.io.PrintStream;
import java.util.Scanner;

import org.jfugue.theory.Chord;
import org.jfugue.theory.ChordProgression;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;
import org.jfugue.theory.Scale;

public class ProgressionMaker {
    private static int num_obj = 0;
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

    private ChordProgression output;

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

    public void make_progression() throws IllegalStateException{
        if(!usable_object){
            throw new IllegalStateException("Exited during input, unusable object.");
        }
    }

    public int getID(){
        return id;
    }

    public boolean usable(){
        return usable_object;
    }

    private boolean get_input(){

        out_stream.println("\n----Starting Key:----\n");
        if(!get_key(start_key)){
            return false;
        }

        out_stream.println("\n----Ending Key:----\n");
        if(!get_key(end_key)){
            return false;
        }

        out_stream.println("\n----Starting Chord:----\n");
        if(!get_chord(start_chord)){
            return false;
        }
 
        out_stream.println("\n----Ending Chord:----\n");
        if(!get_chord(end_chord)){
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
    private boolean get_chord(Chord to_get){
        boolean cont_while = true;
        while(cont_while){
            cont_while = false;
            out_stream.println("Please enter (in jFugue chord notation: ie. CMAJ7) the chord you want.\n>");
            String input = scanner.next().toUpperCase().strip();
            if(input.equals("EXIT") || input.equals("QUIT") || input.equals("STOP")){
                return false;
            }
            try{
                to_get = new Chord(input);
            }catch(NullPointerException e){
                out_stream.println("Invalid input, please try again, or consult jfugue style for chord input.");
                cont_while = true;
            } 
        }
        return true;
    }

    private boolean get_key(Key to_get){
        boolean getting_key = true;
        String key_root;
        String key_maj_min;
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

        return true;
    }
}
