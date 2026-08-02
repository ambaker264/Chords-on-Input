package ash_personal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.jfugue.player.Player;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class TestDriver {

    private ShellPrompt shellObj;
    private final InputStream originalIn = System.in;
    private ByteArrayInputStream in;
    private final PrintStream out = System.out;


    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
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
                shellObj = ShellPrompt.getInstance();
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
