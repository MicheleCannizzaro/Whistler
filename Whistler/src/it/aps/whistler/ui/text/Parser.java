package it.aps.whistler.ui.text;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.ConfirmCommand;
import it.aps.whistler.ui.text.command.LoginCommand;
import it.aps.whistler.ui.text.command.SignUpCommand;
import it.aps.whistler.ui.text.command.TurnBackCommand;

public class Parser {
	
	private static Parser instance;
	
	public Parser() {}
	
	//Singleton pattern created by static method - Lazy Initialization
	public static synchronized Parser getInstance() {
		if (instance==null)
			instance=new Parser();
		return instance;
	}
	
	
	public String readCommand(String prompt) {
        String input = "";
        System.out.println(prompt);
        System.out.print(" > ");
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        try{
        	input = reader.readLine();
        }catch(java.io.IOException ex) {
            System.out.println("[Parser] - IO problems reading command"+ ex.getMessage());
        }
		return input;
    }
	
	
	public Command getCommand(Page page) {
		Command command = null;
		try {
				int inputCommand = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
				
				switch(page) {
				    case EXIT_CONSOLE:
				    	switch (PageCommands.Exit.values()[inputCommand]) {
				    		case YES:
				    			// Terminate the program (EXIT)
				    			break;
				    		case NO:
				    			command = new TurnBackCommand(Page.EXIT_CONSOLE);
				    			break;
				    	}
				    	break;
				    	
					case WHISTLER_CONSOLE:
						switch (PageCommands.Whistler.values()[inputCommand]) {
							case EXIT_BACK:
								// Terminate the program (EXIT)
								break;
							case LOGIN:
								command = new LoginCommand();  
								break;
							case SIGNUP:
								command = new SignUpCommand(); 
								break;
						}
						break;
						
					case SIGNUP_CONSOLE:	
						switch(PageCommands.Signup.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - SignUpConsole turn back to WhistlerConsole");
								command = new TurnBackCommand(Page.SIGNUP_CONSOLE);
								break;
							case CONFIRM:
								command = new ConfirmCommand(Page.SIGNUP_CONSOLE);
								break;
						}
						break;
						
					case LOGIN_CONSOLE:	
						switch(PageCommands.Login.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - LoginConsole turn back to WhistlerConsole");
								command = new TurnBackCommand(Page.LOGIN_CONSOLE);
								break;
							case CONFIRM:
								command = new ConfirmCommand(Page.LOGIN_CONSOLE);
								break;
						}
						break;
					
					case HOME_CONSOLE:	
						switch(PageCommands.Home.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - HomeConsole turn back to ExitConsole");
								command = new TurnBackCommand(Page.HOME_CONSOLE);
								break;
						}
						break;
				}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
			System.out.println ("You must enter a command in the list \"Commands\" [digit format]");
			command= Parser.getInstance().getCommand(page);
			}
		return command;
	}
}
