package ash_personal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Intervals;
import org.jfugue.theory.Key;
import org.jfugue.theory.Note;
import org.jfugue.theory.Scale;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class TestDriver {

    private ShellPrompt shellObj;
    private final InputStream original_in = System.in;
    private ByteArrayInputStream in;
    private final PrintStream out = System.out;


    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    /*Can turn back into a test when I need it. Needs to be run unforked.*/
    @Disabled
    public void runMain(){
        Driver.main(null);
    }

    /*Tests the player. Very simple. Doesn't put out sound, not important right
    now.*/ 
    @Disabled
    public void player_run(){

        Player player = new Player();

        player.play("C F A C");
        assertTrue(true);
    }

    @Test
    public void progression_loader_check(){
        out.println("----Testing RhythmicChordProgression Static Block----\n");
        for(RhythmicChordProgression current : RhythmicChordProgression.in_key_progs){
            out.print(current.toString());
        }
    }

    @Test
    public void LL_testing(){
        out.println("----Testing RhythmicChordProgression Linked Lists----\n");
        RhythmicChordProgression c = this.sample_progression_part_full();
        out.print(c.toString());


        out.println("----Testing adding LLs to LLs----\n");
        //add to an empty progression.
        RhythmicChordProgression d = this.sample_progression_empty();
        d.add(c, false);
        out.print(d.toString());

    }

    @Test
    public void investigate_chord_note_constructor(){

        Chord chord = new Chord("CMIN7");
        out.println(chord.getRoot().toStringWithoutDuration());
        out.println(chord.toNoteString());
        out.println(chord.toHumanReadableString());
    }

    @Test
    public void investigate_intervals(){

        out.println("------Interval Test-----\n");

        Note a = new Note("A");
        Note c = new Note("C");
        Note[] temp = new Note[2];
        temp[0] = a;
        temp[1] = c;
        int check = Intervals.createIntervalsFromNotes(temp).toHalfstepArray()[1];
        out.println(check);
        assertTrue(check == 3 || check == 10);

    }


    /**
     * Have a more cohesive test now. This checked what it needed to.
     */
    @Disabled
    public void basic_chord_loading(){
        System.out.println("----STARTING BASIC GENPROG TEST----\n");
        this.run_test("genprog C maj C maj ggg CMAJ CMAJ7");

        this.testClose();
        assertTrue(true);
    }

    @Test
    public void genprog_input(){
        System.out.println("----STARTING BASIC GENPROG INPUT TEST----\n");
        this.run_test("genprog C maj C maj ggg CMAJ CMAJ7 3 4 8 exit");

        this.testClose();
        assertTrue(true);
    }

    @Test
    public void test_basic_shell(){

        System.out.println("----STARTING BASIC EXIT SHELL TEST----\n");

        String input = "Exit";
        this.run_test(input);

        this.testClose();
        
        assertTrue(true);
    }

    @Test
    public void test_last_command(){
        System.out.println("----STARTING LAST INPUT TEST----\n");
        
        String input = "d d Oly last prev a prev exit";

        this.run_test(input);

        this.testClose();

        assertTrue(true);

    }

    @Test
    public void echo_test(){
        System.out.println("----ECHO TEST----\n");
        String input = "echo lmao\n exit";
        this.run_test(input);
        this.testClose();
        assertTrue(true);
    }


    @Test
    public void set_chord_function(){
        out.println("----Chord Function Checker----\n");
        out.println(this.sample_progression_empty().get_start_chord());
        assertTrue(true);
    }

    private void run_test(String input){
        try {
            try {
                this.in = new ByteArrayInputStream(input.getBytes());
                shellObj = new ShellPrompt();
                shellObj.new_input_source(this.in);
                shellObj.new_output_source(this.out);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                assertTrue(false);
            }
            shellObj.startShell(); //actually runs the program
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            assertTrue(false);
        }
    }

    private void testClose(){
        try { 
            in.close();  
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private RhythmicChordProgression sample_progression_empty(){
        Chord sc = new Chord("C");
        Chord ec = new Chord("F");
        Key sk = new Key(new Note("C"), Scale.MAJOR);
        Key ek = new Key(new Note("F"), Scale.MAJOR);
        int[] time = {4, 4};
        int num_measures = 7;
        return new RhythmicChordProgression(sc, sk, time, num_measures, ec, ek);
    }

    private RhythmicChordProgression sample_progression_part_full(){
        RhythmicChordProgression test = this.sample_progression_empty();
        test.add(new ChordInContext(new Chord("DMIN7"), 
        new Key(new Note("C"), Scale.MAJOR), 4f), true);
        return test;
    }
}
