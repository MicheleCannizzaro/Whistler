package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.RemovePostConsole;

public class RemovePostCommand implements Command {
	
	public RemovePostCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Remove Post\" - RemovePostCommand takes to RemovePostConsole which allows you to delete the post based\n     on the pid!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console removePostConsole= new RemovePostConsole(userNickname);
		removePostConsole.start();
	}
}
