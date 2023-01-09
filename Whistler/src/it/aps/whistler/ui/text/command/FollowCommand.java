package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.FollowConsole;

public class FollowCommand implements Command{
	
	public FollowCommand(){}

	public String getCommandDescription() {
		String descripition = "\"Follow\" - FollowCommand takes you to FollowConsole where you can decide which account to follow";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console followConsole= new FollowConsole(userNickname);
		followConsole.start();
	}
}
