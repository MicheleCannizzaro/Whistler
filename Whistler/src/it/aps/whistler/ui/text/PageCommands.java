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
		EXIT_BACK,
		CIRCLE_OF_INTERESTS,
		PUBLISH
	}
	
	public enum Publish{
		EXIT_BACK,
		CONFIRM
	}
	
	public enum Circle{
		EXIT_BACK,
		FOLLOW,
		UNFOLLOW
	}
	
	public enum Follow{
		EXIT_BACK,
		CONFIRM
	}
	
	public enum Unfollow{
		EXIT_BACK,
		CONFIRM
	}
	
	private static final String[] exitConsoleCommands = {"0:YES","1:NO"};
	private static final String[] whistlerConsoleCommands = {"0:Exit","1:Login","2:SignUp"};
	private static final String[] signUpConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] loginConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] homeConsoleCommands = {"0:Logout","1:Circle of Interests", "2:Publish"};
	private static final String[] publishConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] circleOfInterestConsoleCommands = {"0:Go back to Home","1:Follow","2:Unfollow"};
	private static final String[] followConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] unFollowConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	 
	public static String[] getCommands(Page page){
			
		String commands[]=null;
			
			switch (page){
			    case EXIT_CONSOLE: commands = exitConsoleCommands; break;
				case WHISTLER_CONSOLE: commands = whistlerConsoleCommands; break;
				case SIGNUP_CONSOLE: commands = signUpConsoleCommands; break;
				case LOGIN_CONSOLE: commands = loginConsoleCommands; break;
				case HOME_CONSOLE: commands = homeConsoleCommands; break;
				case PUBLISH_CONSOLE: commands = publishConsoleCommands; break;
				case CIRCLE_CONSOLE: commands = circleOfInterestConsoleCommands; break;
				case FOLLOW_CONSOLE: commands = followConsoleCommands; break;
				case UNFOLLOW_CONSOLE: commands = unFollowConsoleCommands; break;
			};
			return commands;
		}
}
