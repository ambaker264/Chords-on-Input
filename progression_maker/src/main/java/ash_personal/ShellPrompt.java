package ash_personal;

import java.io.InputStream;
import java.util.Scanner;
import java.util.Stack;


/*Runs the shell for input. Singleton Class. Prompts are listed below, and  */
public final class ShellPrompt {

    private final String INVALID_COMMAND_STRING =  "Command not recognized. Please check syntax and/or use the \"help\" command to input a valid option.";
    private static ShellPrompt singletonCheck = null;
    private Stack<String> prev_Commands;
    private Stack<String> prev_Commands_temp;
    private Scanner input_reader;
    private boolean mainloop_running = false;
    private boolean cont_loop = true;


    private ShellPrompt(){
        prev_Commands = new Stack<String>();
        prev_Commands_temp = new Stack<String>();
    }

    public static ShellPrompt getInstance(){
        if(singletonCheck == null){
            singletonCheck = new ShellPrompt();
        }
        return singletonCheck;
    }


    /*Helps handle multithreading issues.*/
    public void startShell() throws IllegalStateException{
        if(mainloop_running){
            throw new IllegalStateException("Main Loop Already Running");
        }
        mainloop_running = true;
        cont_loop = true;
        this.mainLoop();
    }


    private String getUserInput(){
       boolean cont = true;
       String userInput = null;
        while(cont){
            cont = false;
            System.out.print(">");
            userInput = input_reader.next();
            if(userInput.isBlank()){
                cont = true;
            }
        }
        return userInput;
    }
    
    private void mainLoop(){
        while(cont_loop){
            String input = null;        
            input = this.getUserInput();
            this.parseCommand(input);
        }
        System.out.println("Exited Main Loop Successfully.");
        input_reader.close();
    }


    /*This is where all major commands are handled, and sent off to their respective functions.
    --"Exit" - quits the shell, shuts down.
    --"Last" - returns last command, or if last command was last, the next one off the stack.
    --"Echo" - repeats rest of the line of input.
    
    */
    private void parseCommand(String command){
       command = command.toLowerCase();
       command = command.strip();
       switch(command){
            case "exit", "quit", "stop" -> {
                System.out.println("Stopping Shell, Shutting Down");
                this.setContLoop(false);
                this.mainloop_running = false;
            }
            case "last", "prev" -> {
                if(prev_Commands_temp.isEmpty()){
                    System.err.println("No Further Previous Commands.");
                }else{
                    System.out.println(prev_Commands_temp.pop());
                }
                return;
            }
            case "echo" -> {
                if(input_reader.hasNext()){
                    System.out.println(input_reader.nextLine());
                }
            }
            case "generate progression", "genprog" ->{
               //send to the actual method in our progression maker class 
            }
            default -> System.out.println(INVALID_COMMAND_STRING);
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
    
}
