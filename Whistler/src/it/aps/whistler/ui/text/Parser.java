package it.aps.whistler.ui.text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.ui.text.command.CircleCommand;
import it.aps.whistler.ui.text.command.ClearNotificationCommand;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.CommentCommand;
import it.aps.whistler.ui.text.command.ConfirmCommand;
import it.aps.whistler.ui.text.command.EditCommentCommand;
import it.aps.whistler.ui.text.command.EditPostCommand;
import it.aps.whistler.ui.text.command.ExploreCommand;
import it.aps.whistler.ui.text.command.FollowCommand;
import it.aps.whistler.ui.text.command.HelpCommand;
import it.aps.whistler.ui.text.command.LikeCommand;
import it.aps.whistler.ui.text.command.LoginCommand;
import it.aps.whistler.ui.text.command.ProfileCommand;
import it.aps.whistler.ui.text.command.ProfileTimelineCommand;
import it.aps.whistler.ui.text.command.PublishCommand;
import it.aps.whistler.ui.text.command.RemoveAccountCommand;
import it.aps.whistler.ui.text.command.RemoveCommentCommand;
import it.aps.whistler.ui.text.command.RemovePostCommand;
import it.aps.whistler.ui.text.command.SearchAccountCommand;
import it.aps.whistler.ui.text.command.SearchPostCommand;
import it.aps.whistler.ui.text.command.SettingsCommand;
import it.aps.whistler.ui.text.command.ShowCommentsCommand;
import it.aps.whistler.ui.text.command.ShowLikesCommand;
import it.aps.whistler.ui.text.command.ShowNotificationsCommand;
import it.aps.whistler.ui.text.command.SignUpCommand;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.ui.text.command.UnFollowCommand;
import it.aps.whistler.ui.text.command.UpdateCommand;

public class Parser {
	private final static Logger logger = Logger.getLogger(Parser.class.getName());
	
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
            //System.out.println("[Parser] - IO problems reading command"+ ex.getMessage());
            logger.logp(Level.SEVERE, Parser.class.getSimpleName(),"readCommand","IOException: "+ex);
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
				    			logger.log(Level.INFO, "[Parser] - ExitConsole (YES)");
				    			// Terminate the program (EXIT)
				    			break;
				    		case NO:
				    			logger.log(Level.INFO, "[Parser] - ExitConsole (NO) takes to WhistlerConsole");
				    			command = new TurnBackCommand(Page.EXIT_CONSOLE);
				    			break;
				    	}
				    	break;
				    	
					case WHISTLER_CONSOLE:
						switch (PageCommands.Whistler.values()[inputCommand]) {
							case EXIT_BACK:
								// Terminate the program (EXIT)
								logger.log(Level.INFO, "[Parser] - WhistlerConsole Exit");
								break;
							case LOGIN:
								logger.log(Level.INFO, "[Parser] - WhistlerConsole takes to LoginConsole");
								command = new LoginCommand();  
								break;
							case SIGNUP:
								logger.log(Level.INFO, "[Parser] - WhistlerConsole takes to SignUpConsole");
								command = new SignUpCommand(); 
								break;
						}
						break;
						
					case SIGNUP_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - SignUpConsole turn back to WhistlerConsole");
								command = new TurnBackCommand(Page.SIGNUP_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO, "[Parser] - SignUpConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.SIGNUP_CONSOLE);
								break;
						}
						break;
						
					case LOGIN_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - LoginConsole turn back to WhistlerConsole");
								command = new TurnBackCommand(Page.LOGIN_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO, "[Parser] - LoginConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.LOGIN_CONSOLE);
								break;
						}
						break;
					
					case HOME_CONSOLE:	
						switch(PageCommands.Home.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - HomeConsole turn back to ExitConsole");
								command = new TurnBackCommand(Page.HOME_CONSOLE);
								break;
							case CIRCLE_OF_INTERESTS:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to CircleConsole");
								command = new CircleCommand();
								break;
							case PUBLISH:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to PublishConsole");
								command = new PublishCommand();
								break;
							case COMMENT:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to CommentConsole");
								command = new CommentCommand();
								break;
							case SHOW_POST_COMMENT:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to ShowPostCommentConsole");
								command = new ShowCommentsCommand(Page.HOME_CONSOLE);
								break;
							case LIKE:
								logger.log(Level.INFO,"[Parser] - HomeConsole takes to LikeConsole");
								command = new LikeCommand(true, Page.HOME_CONSOLE);	  //isLike = true previousPage = Home
								break;
							case DISLIKE:
								logger.log(Level.INFO,"[Parser] - HomeConsole takes to DislikeConsole");
								command = new LikeCommand(false, Page.HOME_CONSOLE);  //isLike = false previousPage = Home
								break;
							case SHOW_POST_LIKES:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to ShowPostLikesConsole");
								command = new ShowLikesCommand(Page.HOME_CONSOLE);
								break;
							case PROFILE:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to ProfileConsole");
								command = new ProfileCommand();
								break;
							case SHOW_NOTIFICATIONS:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to ShowNotificationsConsole");
								command = new ShowNotificationsCommand(Page.HOME_CONSOLE);
								break;
							case SEARCH_ACCOUNT:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to SearchAccountConsole");
								command = new SearchAccountCommand(Page.HOME_CONSOLE);
								break;
							case SEARCH_POST:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to SearchPostConsole");
								command = new SearchPostCommand();
								break;
							case EXPLORE:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to ExploreConsole");
								command = new ExploreCommand();
								break;
							case HELP:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to HelpConsole");
								command = new HelpCommand(Page.HOME_CONSOLE);
								break;
							case UPDATE:
								logger.log(Level.INFO, "[Parser] - HomeConsole takes to HomeConsole");
								command = new UpdateCommand(Page.HOME_CONSOLE);
								break;
						}
						break;
						
					case PUBLISH_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - PublishConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.PUBLISH_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO, "[Parser] - PublishConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.PUBLISH_CONSOLE);
								break;
						}
						break;
						
					case COMMENT_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - CommentConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.COMMENT_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO, "[Parser] - CommentConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.COMMENT_CONSOLE);
								break;
						}
						break;
					
					case CIRCLE_CONSOLE:	
						switch(PageCommands.Circle.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - CircleConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.CIRCLE_CONSOLE);
								break;
							case FOLLOW:
								logger.log(Level.INFO, "[Parser] - CircleConsole takes to FollowConsole");
								command = new FollowCommand();
								break;
							case UNFOLLOW:
								logger.log(Level.INFO, "[Parser] - CircleConsole takes to unFollowConsole");
								command = new UnFollowCommand();
								break;
						}
						break;
					
					case FOLLOW_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - FollowConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.FOLLOW_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO, "[Parser] - FollowConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.FOLLOW_CONSOLE);
								break;
						}
						break;
						
					case UNFOLLOW_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - unFollowConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.UNFOLLOW_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO, "[Parser] - unFollowConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.UNFOLLOW_CONSOLE);
								break;
						}
						break;
						
					case PROFILE_CONSOLE:	
						switch(PageCommands.Profile.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - ProfileConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.PROFILE_CONSOLE);
								break;
							case PROFILE_TIMELINE:
								logger.log(Level.INFO, "[Parser] - ProfileConsole takes to ProfileTimelineConsole");
								command = new ProfileTimelineCommand();
								break;
							case SETTINGS:
								logger.log(Level.INFO, "[Parser] - ProfileConsole takes to SettingsConsole");
								command = new SettingsCommand();
								break;
							case SHOW_NOTIFICATIONS:
								logger.log(Level.INFO, "[Parser] - ProfileConsole takes to ShowNotificationsConsole");
								command = new ShowNotificationsCommand(Page.PROFILE_CONSOLE);
								break;
							case REMOVE_ACCOUNT:
								logger.log(Level.INFO, "[Parser] - ProfileConsole takes to RemoveAccountConsole");
								command = new RemoveAccountCommand();
								break;
						}
						break;
					
					case SETTINGS_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - SettingsConsole turn back to ProfileConsole");
								command = new TurnBackCommand(Page.SETTINGS_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - SettingsConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.SETTINGS_CONSOLE);  
								break;
						}
						break;
					
					case PROFILE_TIMELINE_CONSOLE:	
						switch(PageCommands.ProfileTimeline.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - ProfileTimelineConsole turn back to ProfileConsole");
								command = new TurnBackCommand(Page.PROFILE_TIMELINE_CONSOLE);
								break;
							case EDIT_POST:
								logger.log(Level.INFO,"[Parser] - ProfileTimelineConsole takes to EditPostConsole");
								command = new EditPostCommand();
								break;
							case REMOVE_POST:
								logger.log(Level.INFO,"[Parser] - ProfileTimelineConsole takes to RemovePostConsole");
								command = new RemovePostCommand();  
								break;
							case COMMENT:
								logger.log(Level.INFO, "[Parser] - ProfileTimelineConsole takes to CommentConsole");
								command = new CommentCommand();
								break;
							case SHOW_POST_COMMENT:
								logger.log(Level.INFO, "[Parser] - ProfileTimelineConsole takes to ShowPostCommentConsole");
								command = new ShowCommentsCommand(Page.PROFILE_TIMELINE_CONSOLE);
								break;
							case LIKE:
								logger.log(Level.INFO,"[Parser] - ProfileTimelineConsole takes to LikeConsole");
								command = new LikeCommand(true, Page.PROFILE_TIMELINE_CONSOLE);	  //isLike = true previousPage = ProfileTimelineConsole
								break;
							case DISLIKE:
								logger.log(Level.INFO,"[Parser] - ProfileTimelineConsole takes to DislikeConsole");
								command = new LikeCommand(false, Page.PROFILE_TIMELINE_CONSOLE);  //isLike = false previousPage = ProfileTimelineConsole
								break;
							case SHOW_POST_LIKES:
								logger.log(Level.INFO, "[Parser] - ProfileTimelineConsole takes to ShowPostLikesConsole");
								command = new ShowLikesCommand(Page.PROFILE_TIMELINE_CONSOLE);
								break;
							case UPDATE:
								logger.log(Level.INFO, "[Parser] - ProfileTimelineConsole takes to ProfileTimelineConsole");
								command = new UpdateCommand(Page.PROFILE_TIMELINE_CONSOLE);
								break;
						}
						break;
					
					case EDIT_POST_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - EditPostConsole turn back to ProfileTimelineConsole");
								command = new TurnBackCommand(Page.EDIT_POST_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - EditPostConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.EDIT_POST_CONSOLE);
								break;
						}
						break;
					
					case REMOVE_POST_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - RemovePostConsole turn back to ProfileTimelineConsole");
								command = new TurnBackCommand(Page.REMOVE_POST_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - RemovePostConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.REMOVE_POST_CONSOLE);  
								break;
						}
						break;
					
					case REMOVE_ACCOUNT_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - RemoveAccountConsole turn back to ProfileConsole");
								command = new TurnBackCommand(Page.REMOVE_ACCOUNT_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - RemoveAccountConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.REMOVE_ACCOUNT_CONSOLE);  
								break;
						}
						break;
						
					case SEARCH_ACCOUNT_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - SearchAccountConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.SEARCH_ACCOUNT_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - SearchAccountConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.SEARCH_ACCOUNT_CONSOLE);  
								break;
						}
						break;
						
					case ACCOUNT_PROFILE_CONSOLE:	
						switch(PageCommands.AccountProfile.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - AccountProfileConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.ACCOUNT_PROFILE_CONSOLE);
								break;
							case ACCOUNT_PROFILE_TIMELINE:
								logger.log(Level.INFO, "[Parser] - AccountProfileConsole takes to ProfileTimelineConsole");
								command = new ProfileTimelineCommand();
								break;
						}
						break;
					
					case ACCOUNT_TIMELINE_CONSOLE:
						switch(PageCommands.AccountTimeline.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO, "[Parser] - AccountTimelineConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.ACCOUNT_TIMELINE_CONSOLE);
								break;
							case COMMENT:
								logger.log(Level.INFO, "[Parser] - AccountTimelineConsole takes to CommentConsole");
								command = new CommentCommand();
								break;
							case SHOW_POST_COMMENT:
								logger.log(Level.INFO, "[Parser] - AccountTimelineConsole takes to ShowPostCommentConsole");
								command = new ShowCommentsCommand(Page.ACCOUNT_TIMELINE_CONSOLE);
								break;
							case LIKE:
								logger.log(Level.INFO,"[Parser] - AccountTimelineConsole takes to LikeConsole");
								command = new LikeCommand(true, Page.ACCOUNT_TIMELINE_CONSOLE);	  //isLike = true previousPage = AccountTimelineConsole
								break;
							case DISLIKE:
								logger.log(Level.INFO,"[Parser] - AccountTimelineConsole takes to DislikeConsole");
								command = new LikeCommand(false, Page.ACCOUNT_TIMELINE_CONSOLE);  //isLike = false previousPage = AccountTimelineConsole
								break;
							case SHOW_POST_LIKES:
								logger.log(Level.INFO, "[Parser] - AccountTimelineConsole takes to ShowPostLikesConsole");
								command = new ShowLikesCommand(Page.ACCOUNT_TIMELINE_CONSOLE);
								break;
							case UPDATE:
								logger.log(Level.INFO, "[Parser] - AccountTimelineConsole takes to ProfileTimelineConsole");
								command = new UpdateCommand(Page.ACCOUNT_TIMELINE_CONSOLE);
								break;
						}
						break;
						
					case SEARCH_POST_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - SearchPostConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.SEARCH_POST_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - SearchPostConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.SEARCH_POST_CONSOLE);  
								break;
						}
						break;
					
					case SHOW_RESULTS_CONSOLE:	
						switch(PageCommands.SearchPost.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - ShowResultConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.SHOW_RESULTS_CONSOLE);
								break;
							case SEARCH_ACCOUNT:
								logger.log(Level.INFO, "[Parser] - ShowResultConsole takes to SearchAccountConsole");
								command = new SearchAccountCommand(Page.SHOW_RESULTS_CONSOLE);
								break;
							case COMMENT:
								logger.log(Level.INFO, "[Parser] - ShowResultConsole takes to CommentConsole");
								command = new CommentCommand();
								break;
							case SHOW_POST_COMMENT:
								logger.log(Level.INFO, "[Parser] - ShowResultConsole takes to ShowPostCommentConsole");
								command = new ShowCommentsCommand(Page.SHOW_RESULTS_CONSOLE);
								break;
							case LIKE:
								logger.log(Level.INFO,"[Parser] - ShowResultConsole takes to LikeConsole");
								command = new LikeCommand(true, Page.SHOW_RESULTS_CONSOLE);	  //isLike = true previousPage = ShowResultConsole
								break;
							case DISLIKE:
								logger.log(Level.INFO,"[Parser] - ShowResultConsole takes to DislikeConsole");
								command = new LikeCommand(false, Page.SHOW_RESULTS_CONSOLE);  //isLike = false previousPage = ShowResultConsole
								break;
							case SHOW_POST_LIKES:
								logger.log(Level.INFO, "[Parser] - ShowResultConsole takes to ShowPostLikesConsole");
								command = new ShowLikesCommand(Page.SHOW_RESULTS_CONSOLE);
								break;
						}
						break;
					
					case EXPLORE_CONSOLE:	
						switch(PageCommands.Explore.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - ExploreConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.EXPLORE_CONSOLE);
								break;
							case SEARCH_POST:
								logger.log(Level.INFO, "[Parser] - ExploreConsole takes to SearchPostConsole");
								command = new SearchPostCommand();
								break;
						}
						break;
					
					case SHOW_POST_COMMENTS_CONSOLE:	
						switch(PageCommands.ShowComments.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - ShowPostCommentConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.SHOW_POST_COMMENTS_CONSOLE);
								break;
							case EDIT_COMMENT:
								logger.log(Level.INFO,"[Parser] - ShowPostCommentConsole takes to EditCommentConsole");
								command = new EditCommentCommand();
								break;
							case REMOVE_COMMENT:
								logger.log(Level.INFO,"[Parser] - ShowPostCommentConsole takes to RemoveCommentConsole");
								command = new RemoveCommentCommand();
								break;
						}
						break;
						
					case REMOVE_COMMENT_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - RemoveCommentConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.REMOVE_COMMENT_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - RemoveCommentConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.REMOVE_COMMENT_CONSOLE);  
								break;
						}
						break;
						
					case EDIT_COMMENT_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - EditCommentConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.EDIT_COMMENT_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - EditCommentConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.EDIT_COMMENT_CONSOLE);
								break;
						}
						break;
					
					case SHOW_NOTIFICATIONS_CONSOLE:
						switch(PageCommands.ShowNotifications.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - ShowNotificationsConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.SHOW_NOTIFICATIONS_CONSOLE);
								break;
							case CLEAR_NOTIFICATION:
								logger.log(Level.INFO,"[Parser] - ShowNotificationsConsole clears notification with a given nid");
								command = new ClearNotificationCommand(false); //isForAllNotifications? False
								break;
							case CLEAR_ALL_NOTIFICATIONS:
								logger.log(Level.INFO,"[Parser] - ShowNotificationsConsole clears all account notifications");
								command = new ClearNotificationCommand(true); //isForAllNotifications? True
								break;
							case UPDATE:
								logger.log(Level.INFO, "[Parser] - ShowNotificationsConsole takes to ShowNotificationsConsole");
								command = new UpdateCommand(Page.SHOW_NOTIFICATIONS_CONSOLE);
								break;
						}
						break;
					
					case LIKE_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - LikeConsole turn back");
								command = new TurnBackCommand(Page.LIKE_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - LikeConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.LIKE_CONSOLE);
								break;
						}
						break;
					
					case DISLIKE_CONSOLE:	
						switch(PageCommands.ConfirmOrNot.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - DislikeConsole turn back");
								command = new TurnBackCommand(Page.DISLIKE_CONSOLE);
								break;
							case CONFIRM:
								logger.log(Level.INFO,"[Parser] - DislikeConsole takes to ConfirmCommand");
								command = new ConfirmCommand(Page.DISLIKE_CONSOLE);
								break;
						}
						break;
					
					case SHOW_POST_LIKES_CONSOLE:	
						switch(PageCommands.Turnback.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - ShowPostLikesConsole turn back");
								command = new TurnBackCommand(Page.SHOW_POST_LIKES_CONSOLE);
								break;
						}
						break;
					
					case HELP_CONSOLE:	
						switch(PageCommands.Turnback.values()[inputCommand]) {
							case EXIT_BACK:
								logger.log(Level.INFO,"[Parser] - HelpConsole turn back to HomeConsole");
								command = new TurnBackCommand(Page.HELP_CONSOLE);
								break;
						}
						break;
					
				}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
			System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
			logger.logp(Level.WARNING, Parser.class.getSimpleName(),"getCommand","Command entered not in digit format or out of bounds: "+ex);
			
			command= Parser.getInstance().getCommand(page);
			}
		return command;
	}
}
