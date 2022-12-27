package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.RemoveCommentConsole;

public class RemoveCommentCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "RemoveCommentCommand takes to RemoveCommentConsole which allows you to delete the comment based on the cid!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		try {
			Console removeCommentConsole= new RemoveCommentConsole(userNickname, enteredInputs.get(0), previousPage); //enteredInputs.get(0) == postPid
			removeCommentConsole.start();
		}catch(java.lang.IndexOutOfBoundsException ex) {
			Console removeCommentConsole= new RemoveCommentConsole(userNickname, null, previousPage);
			removeCommentConsole.start();
		}
	}
}
