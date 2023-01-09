package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.PublishConsole;

public class PublishCommand implements Command{
	
	public PublishCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Publish\" - PublishCommand takes you to PublishConsole where you can write your posts on Whistler.";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console publishConsole= new PublishConsole(userNickname);
		publishConsole.start();
	}

}
