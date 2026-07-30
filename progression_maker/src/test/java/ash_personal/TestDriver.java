package ash_personal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jfugue.player.Player;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class TestDriver {

    private ShellPrompt shellObj;
    private InputStream originalIn = System.in;


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
        testUtilSetup();

        String input = "Exit";
        
        this.sendInputString(input);
        this.run_test();

        testUtilShutdown();
        assertTrue(true);
    }

    /*Sets up input lines, makes the shell and starts it, etc. */
    private void testUtilSetup(){
        shellObj = ShellPrompt.getInstance(); 
    }
    private void testUtilShutdown(){
        
    }

    private void run_test(){
        try {
            shellObj.startShell();
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    private void sendInputString(String input){
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);
        } catch (Exception e) {
            System.setIn(originalIn);
            assertTrue(false);
        }
        System.setIn(originalIn);
    }
}
