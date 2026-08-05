package ash_personal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
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

    /*Clogs space.*/
    @Disabled
    public void investigate_chord_constructor(){
        Chord chord = new Chord("CMIN7");
        out.println(chord.toNoteString());
    }

    @Test
    public void basic_chord_loading(){
        System.out.println("----STARTING BASIC GENPROG TEST----\n");
        this.run_test("genprog C maj C maj ggg CMAJ CMAJ7");

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
}
