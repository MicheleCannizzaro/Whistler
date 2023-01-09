package it.aps.whistler.ui.text.command;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ExitConsole;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.ProfileConsole;
import it.aps.whistler.ui.text.console.ProfileTimelineConsole;
import it.aps.whistler.ui.text.console.ShowPostCommentsConsole;
import it.aps.whistler.ui.text.console.ShowResultsConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.util.ArrayList;


public class TurnBackCommand implements Command{
	
	private Page page;
	
	public TurnBackCommand(){}
	
	public TurnBackCommand(Page page){
		this.page=page;
	}
	
	public String getCommandDescription() {
		String descripition = "\"Go back\" - TurnBackCommand takes you to the previous page or lets you exit the program";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		
		if (this.page == Page.SIGNUP_CONSOLE || this.page == Page.LOGIN_CONSOLE || this.page == Page.EXIT_CONSOLE) {
			Console whistlerConsole = new WhistlerConsole();
			whistlerConsole.start();
		}
		
		if (this.page == Page.HOME_CONSOLE) {
			Console exitConsole = new ExitConsole();
			exitConsole.start();
		}
		
		if (this.page == Page.PUBLISH_CONSOLE || this.page == Page.CIRCLE_CONSOLE || this.page == Page.FOLLOW_CONSOLE
				|| this.page == Page.UNFOLLOW_CONSOLE || this.page == Page.PROFILE_CONSOLE || this.page == Page.SEARCH_ACCOUNT_CONSOLE
				|| this.page == Page.ACCOUNT_PROFILE_CONSOLE || this.page == Page.ACCOUNT_TIMELINE_CONSOLE
				|| this.page == Page.SEARCH_POST_CONSOLE || this.page == Page.SHOW_RESULTS_CONSOLE || this.page == Page.EXPLORE_CONSOLE
				|| this.page == Page.COMMENT_CONSOLE || this.page == Page.HELP_CONSOLE) {
			Console homeConsole= new HomeConsole(userNickname);
			homeConsole.start();
		}
		
		if(this.page == Page.SHOW_POST_COMMENTS_CONSOLE) {
			if (previousPage == Page.PROFILE_TIMELINE_CONSOLE) {
				Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true,null); //isOwner == true
				profileTimelineConsole.start();
			}
			
			if (previousPage == Page.HOME_CONSOLE) {
				Console homeConsole = new HomeConsole(userNickname);
				homeConsole.start();
			}
			
			if (previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {	
				                                                                             //whistleblowerNickname
				Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,false,enteredInputs.get(0)); //isOwner == false
				profileTimelineConsole.start();
			}
			
			if (previousPage == Page.SHOW_RESULTS_CONSOLE) {					//searchedKeyword
				Console showResultsConsole = new ShowResultsConsole(userNickname, enteredInputs.get(0));
				showResultsConsole.start();
			}
		}
		
		if(this.page == Page.EDIT_COMMENT_CONSOLE) {
			if (previousPage == Page.SHOW_RESULTS_CONSOLE || previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
				String postPid;
				String userInputPreviousPage;
				
				if(enteredInputs.size()==4) {
					postPid = enteredInputs.get(0);
					userInputPreviousPage = enteredInputs.get(3);
				}else {
					postPid = enteredInputs.get(0);
					userInputPreviousPage = enteredInputs.get(1);
				}
				
				enteredInputs.clear();
				enteredInputs.add(postPid);
				enteredInputs.add(userInputPreviousPage);
			}
			
			Console showPostCommentsConsole = new ShowPostCommentsConsole(userNickname,enteredInputs,previousPage);
			showPostCommentsConsole.start();
		}
		
		if(this.page == Page.REMOVE_COMMENT_CONSOLE) {
			if (previousPage == Page.SHOW_RESULTS_CONSOLE || previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
				String postPid;
				String userInputPreviousPage;
				
				if(enteredInputs.size()==3) {
					postPid = enteredInputs.get(0);
					userInputPreviousPage = enteredInputs.get(2);
				}else {
					postPid = enteredInputs.get(0);
					userInputPreviousPage = enteredInputs.get(1);
				}
				
				enteredInputs.clear();
				enteredInputs.add(postPid);
				enteredInputs.add(userInputPreviousPage);
			}
			
			Console showPostCommentsConsole = new ShowPostCommentsConsole(userNickname,enteredInputs,previousPage);
			showPostCommentsConsole.start();
		}
		
		
		if (this.page == Page.SETTINGS_CONSOLE || this.page == Page.PROFILE_TIMELINE_CONSOLE 
				|| this.page == Page.REMOVE_POST_CONSOLE ||  this.page == Page.REMOVE_ACCOUNT_CONSOLE) {
			Console profileConsole= new ProfileConsole(userNickname, true, null); //isOwner == true
			profileConsole.start();
		}
		
		if (this.page == Page.EDIT_POST_CONSOLE ||this.page == Page.REMOVE_POST_CONSOLE) {
			Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true,null); //isOwner == true
			profileTimelineConsole.start();
		}
		
		if(this.page == Page.SHOW_NOTIFICATIONS_CONSOLE) {
			if (previousPage == Page.HOME_CONSOLE) {
				Console homeConsole = new HomeConsole(userNickname);
				homeConsole.start();
			}
			
			if (previousPage == Page.PROFILE_CONSOLE) {
				Console profileConsole= new ProfileConsole(userNickname, true, null); //isOwner == true
				profileConsole.start();
			}
		}
		
		if(this.page == Page.LIKE_CONSOLE || this.page == Page.DISLIKE_CONSOLE || this.page == Page.SHOW_POST_LIKES_CONSOLE) {
			if (previousPage == Page.HOME_CONSOLE) {
				Console homeConsole = new HomeConsole(userNickname);
				homeConsole.start();
			}
			
			if (previousPage == Page.PROFILE_TIMELINE_CONSOLE) {
				Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true,null); //isOwner == true
				profileTimelineConsole.start();
			}
			
			if (previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
				Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,false,enteredInputs.get(0)); //isOwner == false enteredInputs.get(0) = whistleblowerNickname
				profileTimelineConsole.start();
			}
			
			if (previousPage == Page.SHOW_RESULTS_CONSOLE) {
				Console showResultsConsole = new ShowResultsConsole(userNickname, enteredInputs.get(0)); //enteredInputs.get(1) = searchedKeyword
				showResultsConsole.start();
			}
		}
	}

}
