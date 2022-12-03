package it.aps.whistler.ui.text;

public class PageCommands {
	
	public enum Exit{
		YES,
		NO
	}
	
	public enum Whistler{
		EXIT_BACK,
		LOGIN,
		SIGNUP
	}
	
	public enum ConfirmOrNot{
		EXIT_BACK,
		CONFIRM
	}
	
	public enum Home{
		EXIT_BACK,
		CIRCLE_OF_INTERESTS,
		PUBLISH,
		PROFILE
	}

	public enum Circle{
		EXIT_BACK,
		FOLLOW,
		UNFOLLOW
	}
	
	public enum Profile{
		EXIT_BACK,
		SETTINGS,
		PROFILE_TIMELINE,
		REMOVE_ACCOUNT
	}
	
	public enum ProfileTimeline{
		EXIT_BACK,
		EDIT_POST,
		REMOVE_POST
	}
	
	public enum Error{
		EXIT,
		RETRY
	}
	
	private static final String[] exitConsoleCommands = {"0:YES","1:NO"};
	private static final String[] whistlerConsoleCommands = {"0:Exit","1:Login","2:SignUp"};
	private static final String[] signUpConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] loginConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] homeConsoleCommands = {"0:Logout","1:Circle of Interests", "2:Publish","3:Profile"};
	private static final String[] publishConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] circleOfInterestConsoleCommands = {"0:Go back to Home","1:Follow","2:Unfollow"};
	private static final String[] followConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] unFollowConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] profileConsoleCommands = {"0:Go back to Home","1:Settings","2:ProfileTimeline","3:Remove Account"};
	private static final String[] settingsConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] profileTimelineConsoleCommands = {"0:Go back to Profile","1:Edit Post","2:Remove Post"};
	private static final String[] editPostConsoleCommands = {"0:Discard and go back to Profile","1:Confirm"};
	private static final String[] removePostConsoleCommands = {"0:Discard and go back to Profile","1:Confirm"};
	private static final String[] removeAccountConsoleCommands = {"0:Discard and go back to Profile","1:Confirm"};
	 
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
				case PROFILE_CONSOLE: commands = profileConsoleCommands; break;
				case SETTINGS_CONSOLE: commands = settingsConsoleCommands; break;
				case PROFILE_TIMELINE_CONSOLE: commands = profileTimelineConsoleCommands; break;
				case EDIT_POST_CONSOLE: commands = editPostConsoleCommands; break;
				case REMOVE_POST_CONSOLE: commands = removePostConsoleCommands; break;
				case REMOVE_ACCOUNT_CONSOLE: commands = removeAccountConsoleCommands; break;
			};
			return commands;
		}
}
