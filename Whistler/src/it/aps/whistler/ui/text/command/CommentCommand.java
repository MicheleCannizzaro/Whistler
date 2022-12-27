package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.CommentConsole;
import it.aps.whistler.ui.text.console.Console;

public class CommentCommand implements Command{
	
	public String getCommandDescription() {
		String descripition = "CommentCommand takes you to CommentConsole where you can write your comment on a post.";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console commentConsole= new CommentConsole(userNickname);
		commentConsole.start();
	}

}
