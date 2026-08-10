package ash_personal;

/**
 * Hello world!
 */
public class Driver {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        System.out.println("Setting Up");

        ShellPrompt shell = new ShellPrompt();
        shell.new_input_source(System.in);
        shell.new_output_source(System.out);

        Driver.init();

        shell.startShell();


    }

    private static void init(){

    }
}
