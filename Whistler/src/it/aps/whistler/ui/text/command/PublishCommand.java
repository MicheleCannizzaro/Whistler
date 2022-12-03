package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.PublishConsole;

public class PublishCommand implements Command{
	
	public String getCommandDescription() {
		String descripition = "PublishCommand takes you to PublishConsole where you can write your posts on Whistler.";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console publishConsole= new PublishConsole(userNickname);
		publishConsole.start();
	}

}
