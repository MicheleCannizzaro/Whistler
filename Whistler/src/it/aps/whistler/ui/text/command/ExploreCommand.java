package it.aps.whistler.ui.text.command;

import java.util.ArrayList;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ExploreConsole;

public class ExploreCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "ExploreCommand takes to ExploreConsole which shows you Trending Keywords on Whistler";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console exploreConsole= new ExploreConsole(userNickname);
		exploreConsole.start();
	}
}
