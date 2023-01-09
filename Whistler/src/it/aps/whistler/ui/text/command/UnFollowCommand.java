package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.UnFollowConsole;

public class UnFollowCommand implements Command{
	
	public UnFollowCommand(){}

	public String getCommandDescription() {
		String descripition = "\"Unfollow\" - unFollowCommand takes you to unFollowConsole where you can decide which account to unfollow";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console unFollowConsole= new UnFollowConsole(userNickname);
		unFollowConsole.start();
	}
}
