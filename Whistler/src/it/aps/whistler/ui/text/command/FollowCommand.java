package it.aps.whistler.ui.text.command;

import java.util.ArrayList;
import it.aps.whistler.ui.text.console.FollowConsole;

public class FollowCommand implements Command{

	public String getCommandDescription() {
		String descripition = "FollowCommand takes you to FollowConsole where you can decide which other account to follow";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		FollowConsole followConsole= new FollowConsole(userNickname);
		followConsole.start();
	}
}
