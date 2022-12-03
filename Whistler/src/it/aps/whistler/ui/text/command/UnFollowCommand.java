package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.UnFollowConsole;

public class UnFollowCommand implements Command{

	public String getCommandDescription() {
		String descripition = "unFollowCommand takes you to unFollowConsole where you can decide which account to unfollow";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console unFollowConsole= new UnFollowConsole(userNickname);
		unFollowConsole.start();
	}
}
