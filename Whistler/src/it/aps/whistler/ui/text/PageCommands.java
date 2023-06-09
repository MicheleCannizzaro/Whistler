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
		COMMENT,
		SHOW_POST_COMMENT,
		LIKE,
		DISLIKE,
		SHOW_POST_LIKES,
		PROFILE,
		SHOW_NOTIFICATIONS,
		SEARCH_ACCOUNT,
		SEARCH_POST,
		EXPLORE,
		HELP,
		UPDATE
	}

	public enum Circle{
		EXIT_BACK,
		FOLLOW,
		UNFOLLOW
	}
	
	public enum Profile{
		EXIT_BACK,
		PROFILE_TIMELINE,
		SETTINGS,
		SHOW_NOTIFICATIONS,
		REMOVE_ACCOUNT
	}
	
	public enum AccountProfile{
		EXIT_BACK,
		ACCOUNT_PROFILE_TIMELINE
	}
	
	public enum ProfileTimeline{
		EXIT_BACK,
		EDIT_POST,
		REMOVE_POST,
		COMMENT,
		SHOW_POST_COMMENT,
		LIKE,
		DISLIKE,
		SHOW_POST_LIKES,
		UPDATE
	}
	
	public enum AccountTimeline{
		EXIT_BACK,
		COMMENT,
		SHOW_POST_COMMENT,
		LIKE,
		DISLIKE,
		SHOW_POST_LIKES,
		UPDATE
	}
	
	public enum Error{
		EXIT,
		RETRY
	}
	
	public enum SearchPost{
		EXIT_BACK,
		SEARCH_ACCOUNT,
		COMMENT,
		SHOW_POST_COMMENT,
		LIKE,
		DISLIKE,
		SHOW_POST_LIKES
		
	}
	
	public enum Explore{
		EXIT_BACK,
		SEARCH_POST
	}
	
	public enum ShowComments{
		EXIT_BACK,
		EDIT_COMMENT,
		REMOVE_COMMENT
	}
	
	public enum ShowNotifications{
		EXIT_BACK,
		CLEAR_NOTIFICATION,
		CLEAR_ALL_NOTIFICATIONS,
		UPDATE
	}
	
	public enum Turnback{
		EXIT_BACK
	}
	
	
	private static final String[] exitConsoleCommands = {"0:YES","1:NO"};
	private static final String[] whistlerConsoleCommands = {"0:Exit","1:Login","2:SignUp"};
	private static final String[] signUpConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] loginConsoleCommands = {"0:Go back to Whistler","1:Confirm"};
	private static final String[] homeConsoleCommands = {"0:Logout","1:Circle of Interests", "2:Publish","3:Comment","4:Show Comments","5:Like","6:Dislike", "7:Show Likes","8:Profile", "9:Show Notifications", "10:Search Account","11:Search Post","12:Explore","13:Help","14:Update"};
	private static final String[] publishConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] circleOfInterestConsoleCommands = {"0:Go back to Home","1:Follow","2:Unfollow"};
	private static final String[] followConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] unFollowConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] profileConsoleCommands = {"0:Go back to Home","1:ProfileTimeline","2:Settings","3:Show Notifications","4:Remove Account"};
	private static final String[] settingsConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] profileTimelineConsoleCommands = {"0:Go back to Profile","1:Edit Post","2:Remove Post","3:Comment","4:Show Comments","5:Like","6:Dislike","7:Show Likes","8:Update"};
	private static final String[] editPostConsoleCommands = {"0:Discard and go back to Profile","1:Confirm"};
	private static final String[] removePostConsoleCommands = {"0:Discard and go back to Profile","1:Confirm"};
	private static final String[] removeAccountConsoleCommands = {"0:Discard and go back to Profile","1:Confirm"};
	private static final String[] searchAccountConsoleCommands = {"0:Discard and go back to Home","1:Show"};
	private static final String[] accountProfileConsoleCommands = {"0:Go back to Home","1:Account Timeline"};
	private static final String[] accountTimelineConsoleCommands = {"0:Go back to Home","1:Comment","2:Show Comments","3:Like","4:Dislike","5:Show Likes","6:Update"};
	private static final String[] searchPostConsoleCommands = {"0:Discard and go back to Home","1:Search"};
	private static final String[] showResultsConsoleCommands = {"0:Discard and go back to Home", "1:Search Account", "2:Comment","3:Show Comments","4:Like","5:Dislike","6:Show Likes"};
	private static final String[] exploreConsoleCommands = {"0:Go back to Home", "1:Show Keyword posts"};
	private static final String[] commentConsoleCommands = {"0:Discard and go back to Home","1:Confirm"};
	private static final String[] showPostCommentConsoleCommands = {"0:Go back","1:Edit Comment","2:Remove Comment"};
	private static final String[] removeCommentConsoleCommands = {"0:Discard and go back","1:Confirm"};
	private static final String[] editCommentConsoleCommands = {"0:Discard and go back","1:Confirm"};
	private static final String[] showNotificationsConsoleCommands = {"0:Go back","1:Clear Notification", "2:Clear All Notifications", "3:Update"};
	private static final String[] likeConsoleCommands = {"0:Go back","1:Confirm"};
	private static final String[] dislikeConsoleCommands = {"0:Go back","1:Confirm"};
	private static final String[] showPostLikesConsoleCommands = {"0:Go back"};
	private static final String[] helpConsoleCommands = {"0:Go back"};
	 
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
				case SEARCH_ACCOUNT_CONSOLE: commands = searchAccountConsoleCommands; break;
				case ACCOUNT_PROFILE_CONSOLE: commands = accountProfileConsoleCommands; break;
				case ACCOUNT_TIMELINE_CONSOLE: commands = accountTimelineConsoleCommands; break;
				case SEARCH_POST_CONSOLE: commands = searchPostConsoleCommands; break;
				case SHOW_RESULTS_CONSOLE: commands = showResultsConsoleCommands; break;
				case EXPLORE_CONSOLE: commands = exploreConsoleCommands; break;
				case COMMENT_CONSOLE: commands = commentConsoleCommands; break;
				case SHOW_POST_COMMENTS_CONSOLE: commands = showPostCommentConsoleCommands; break;
				case REMOVE_COMMENT_CONSOLE: commands = removeCommentConsoleCommands; break;
				case EDIT_COMMENT_CONSOLE: commands = editCommentConsoleCommands; break;
				case SHOW_NOTIFICATIONS_CONSOLE: commands = showNotificationsConsoleCommands; break;
				case LIKE_CONSOLE: commands = likeConsoleCommands; break;
				case DISLIKE_CONSOLE: commands = dislikeConsoleCommands; break;
				case SHOW_POST_LIKES_CONSOLE: commands = showPostLikesConsoleCommands; break;
				case HELP_CONSOLE: commands = helpConsoleCommands; break;
			};
			return commands;
		}
}
