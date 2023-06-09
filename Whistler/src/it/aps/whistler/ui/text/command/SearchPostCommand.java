package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.SearchPostConsole;

public class SearchPostCommand implements Command {
	
	public SearchPostCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Search Post\" - SearchPostCommand takes to SearchPostConsole where you can search posts by keyword";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console searchPostConsole= new SearchPostConsole(userNickname);
		searchPostConsole.start();
	}
}
