package it.aps.whistler.ui.text.command;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ExitConsole;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.ProfileConsole;
import it.aps.whistler.ui.text.console.ProfileTimelineConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.util.ArrayList;


public class TurnBackCommand implements Command{
	
	private Page page;
	
	public TurnBackCommand(Page page){
		this.page=page;
	}
	
	public String getCommandDescription() {
		String descripition = "TurnBackCommand takes you to the previous page or lets you exit the program";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		
		if (this.page == Page.SIGNUP_CONSOLE || this.page == Page.LOGIN_CONSOLE || this.page == Page.EXIT_CONSOLE) {
			Console whistlerConsole= new WhistlerConsole();
			whistlerConsole.start();
		}
		
		if (this.page == Page.HOME_CONSOLE) {
			Console exitConsole= new ExitConsole();
			exitConsole.start();
		}
		
		if (this.page == Page.PUBLISH_CONSOLE || this.page == Page.CIRCLE_CONSOLE || this.page == Page.FOLLOW_CONSOLE
				|| this.page == Page.UNFOLLOW_CONSOLE || this.page == Page.PROFILE_CONSOLE || this.page == Page.SEARCH_ACCOUNT_CONSOLE
				|| this.page == Page.ACCOUNT_PROFILE_CONSOLE || this.page == Page.ACCOUNT_TIMELINE_CONSOLE) {
			Console homeConsole= new HomeConsole(userNickname);
			homeConsole.start();
		}
		
		if (this.page == Page.SETTINGS_CONSOLE || this.page == Page.PROFILE_TIMELINE_CONSOLE || this.page == Page.EDIT_POST_CONSOLE
				|| this.page == Page.REMOVE_POST_CONSOLE ||  this.page == Page.REMOVE_ACCOUNT_CONSOLE) {
			Console profileConsole= new ProfileConsole(userNickname, true, null); //isOwner == true
			profileConsole.start();
		}
		
		if (this.page == Page.EDIT_POST_CONSOLE ||this.page == Page.REMOVE_POST_CONSOLE) {
			Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true ,null); //isOwner == true
			profileTimelineConsole.start();
		}
	}

}
