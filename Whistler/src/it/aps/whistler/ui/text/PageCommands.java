package it.aps.whistler.ui.text;

public class PageCommands {
	
	public enum Exit {
		YES,
		NO
	}
	
	public enum Whistler {
		EXIT_BACK,
		LOGIN,
		SIGNUP
	}
		
	public enum Signup {
		EXIT_BACK,
		CONFIRM
	}	
	
	public enum Login {
		EXIT_BACK,
		CONFIRM
	}	
	
	public enum Home{
		EXIT_BACK
	}
	
	private static final String[] exitConsoleCommands = {"0:YES","1:NO"};
	private static final String[] whistlerConsoleCommands = {"0:Exit","1:Login","2:SignUp"};
	private static final String[] signUpConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] loginConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] homeConsoleCommands = {"0:Logout"};
	 
	public static String[] getCommands(Page page){
			
		String commands[]=null;
			
			switch (page){
			    case EXIT_CONSOLE: commands = exitConsoleCommands; break;
				case WHISTLER_CONSOLE: commands = whistlerConsoleCommands; break;
				case SIGNUP_CONSOLE: commands = signUpConsoleCommands; break;
				case LOGIN_CONSOLE: commands = loginConsoleCommands; break;
				case HOME_CONSOLE: commands = homeConsoleCommands; break;
			};
			return commands;
		}
}
