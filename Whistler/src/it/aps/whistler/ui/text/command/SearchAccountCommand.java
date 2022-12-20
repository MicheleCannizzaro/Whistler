package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.SearchAccountConsole;

public class SearchAccountCommand implements Command {
	private Page page;
	
	public SearchAccountCommand(Page page){
		this.page=page;
	}
	
	public String getCommandDescription() {
		String descripition = "SearchProfileCommand takes to SearchConsole which then takes to searched account public profile.";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console searchAccountConsole = null;
		
		if (this.page == Page.HOME_CONSOLE) {							//isPreviousPageHome = true
			searchAccountConsole= new SearchAccountConsole(userNickname,true);
		}
		if (this.page == Page.SHOW_RESULTS_CONSOLE) {				   //isPreviousPageHome = false
			searchAccountConsole= new SearchAccountConsole(userNickname,false);
		}
		searchAccountConsole.start();
	}
}
