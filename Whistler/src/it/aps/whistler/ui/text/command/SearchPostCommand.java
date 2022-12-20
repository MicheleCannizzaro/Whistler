package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.SearchPostConsole;

public class SearchPostCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "SearchPostCommand takes to SearchPostConsole where you can search posts by keyword";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console searchPostConsole= new SearchPostConsole(userNickname);
		searchPostConsole.start();
	}
}
