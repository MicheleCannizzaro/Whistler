package it.aps.whistler.ui.text;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.aps.whistler.ui.text.command.CircleCommand;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.ConfirmCommand;
import it.aps.whistler.ui.text.command.FollowCommand;
import it.aps.whistler.ui.text.command.LoginCommand;
import it.aps.whistler.ui.text.command.PublishCommand;
import it.aps.whistler.ui.text.command.SignUpCommand;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.ui.text.command.UnFollowCommand;

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
							case CIRCLE_OF_INTERESTS:
								System.out.println("[Parser] - HomeConsole takes to CircleConsole");
								command = new CircleCommand();
								break;
							case PUBLISH:
								System.out.println("[Parser] - HomeConsole takes to PublishConsole");
								command = new PublishCommand();
								break;
						}
						break;
						
					case PUBLISH_CONSOLE:	
						switch(PageCommands.Publish.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - PublishConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.PUBLISH_CONSOLE);
								break;
							case CONFIRM:
								System.out.println("[Parser] - PublishConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.PUBLISH_CONSOLE);
								break;
						}
						break;
					
					case CIRCLE_CONSOLE:	
						switch(PageCommands.Circle.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - CircleConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.CIRCLE_CONSOLE);
								break;
							case FOLLOW:
								System.out.println("[Parser] - CircleConsole takes to FollowConsole");
								command = new FollowCommand();
								break;
							case UNFOLLOW:
								System.out.println("[Parser] - CircleConsole takes to unFollowConsole");
								command = new UnFollowCommand();
								break;
						}
						break;
					
					case FOLLOW_CONSOLE:	
						switch(PageCommands.Follow.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - FollowConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.FOLLOW_CONSOLE);
								break;
							case CONFIRM:
								System.out.println("[Parser] - FollowConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.FOLLOW_CONSOLE);
								break;
						}
						break;
						
					case UNFOLLOW_CONSOLE:	
						switch(PageCommands.Unfollow.values()[inputCommand]) {
							case EXIT_BACK:
								System.out.println("[Parser] - unFollowConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.UNFOLLOW_CONSOLE);
								break;
							case CONFIRM:
								System.out.println("[Parser] - unFollowConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.UNFOLLOW_CONSOLE);
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
