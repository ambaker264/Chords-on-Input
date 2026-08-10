package ash_personal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Basic shell interface for the program, prompting the user for input. 
 */
public final class ShellPrompt {

    private final String INVALID_COMMAND_STRING =  "Command not recognized. Please check syntax and/or use the \"help\" command to input a valid option.";
    private static ShellPrompt singletonCheck = null;
    private Stack<String> prev_Commands;
    private Stack<String> prev_Commands_temp;
    private Scanner input_reader;
    private boolean mainloop_running = false;
    private boolean cont_loop = true;
    private PrintStream output_location;
    private ArrayList<ProgressionMaker> make_history;

    /**
     * Basic initialization.
     */
    public ShellPrompt(){
        prev_Commands = new Stack<String>();
        prev_Commands_temp = new Stack<String>();
    } 


    /**
     * Starts the shell prompt that runs the program. Only call this when ready
     * for the program to begin running.
     * @throws IllegalStateException if the main loop is already running i.e. trying to multithread
     * multiple instances of the object and calling {@code startShell()} twice.
     */
    public void startShell() throws IllegalStateException{
        if(mainloop_running){
            throw new IllegalStateException("Main Loop Already Running");
        }
        mainloop_running = true;
        cont_loop = true;
        this.mainLoop();
    }


    private String getUserInput() throws IOException{
       boolean cont = true;
       String userInput = null;
        while(cont){
            cont = false;
            output_location.print(">");
            if(input_reader.hasNext()){
                userInput = input_reader.next();
            }else{
                throw new IOException("Please finish IO stream with exit command.");
            }
            if(userInput.isBlank()){
                cont = true;
            }
        }
        return userInput;
    }
    
    private void mainLoop(){
        while(cont_loop){
            String input = null;
            try{     
                input = this.getUserInput();
                this.parseCommand(input);
            }catch(IOException e){
                cont_loop = false;
                System.err.println(e.getMessage());
            }
        }
        output_location.println("Exited Main Loop Successfully.");
        input_reader.close();
    }


    /*This is where all major commands are handled, and sent off to their respective functions.
    --"Exit" - quits the shell, shuts down.
    --"Last" - returns last command, or if last command was last, the next one off the stack.
    --"Echo" - repeats rest of the line of input.
    --"genprog" - generates the chord progression based on the input 
    */
    private void parseCommand(String command){
       command = command.toLowerCase();
       command = command.strip();
       switch(command){
            case "exit", "quit", "stop" -> {
                output_location.println("Stopping Shell, Shutting Down");
                this.setContLoop(false);
                this.mainloop_running = false;
            }
            case "last", "prev" -> {
                if(prev_Commands_temp.isEmpty()){
                    System.err.println("No Further Previous Commands.");
                }else{
                    output_location.println(prev_Commands_temp.pop());
                }
                return;
            }
            case "echo" -> {
                if(input_reader.hasNext()){
                    output_location.println(input_reader.nextLine().strip());
                }
            }
            case "generate progression", "genprog" -> {
               //send to the actual method in our progression maker class,
               //but first we get the input.
                ProgressionMaker prog_maker = new ProgressionMaker(input_reader, output_location);
                try {
                    make_history.add(prog_maker);                  
                } catch (NullPointerException e) {
                    System.err.println("Initialization of ProgressionMaker failed.");
                }
                if(!prog_maker.usable()){
                    System.err.println("Invalid input on this object, cannot Create Progression.");
                }else{
                    prog_maker.make_progression(); //this is the expensive task.
                }
            }
            default -> output_location.println(INVALID_COMMAND_STRING);
       }
       prev_Commands.push(command);

       prev_Commands_temp.clear();
       prev_Commands_temp.addAll(prev_Commands);
    }


    private boolean setContLoop(boolean cont_loop){
        this.cont_loop = cont_loop;
        return cont_loop;
    }

    public void new_input_source(InputStream input){
        this.input_reader = new Scanner(input);
    }

    public void new_output_source(PrintStream new_out){
        this.output_location = new_out;
    }
    
}
