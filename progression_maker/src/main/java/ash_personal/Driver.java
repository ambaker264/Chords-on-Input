package ash_personal;

/**
 * Hello world!
 */
public class Driver {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        System.out.println("Setting Up");

        ShellPrompt shell = ShellPrompt.getInstance();
        shell.new_input_source(System.in);
        shell.new_output_source(System.out);

        Driver.init();


    }

    public static void init(){

    }
}
