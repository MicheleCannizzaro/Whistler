package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.SearchAccountConsole;

public class SearchAccountCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "SearchProfileCommand takes to SearchConsole which then takes to searched account public profile.";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console searchAccountConsole= new SearchAccountConsole(userNickname);
		searchAccountConsole.start();
	}
}
