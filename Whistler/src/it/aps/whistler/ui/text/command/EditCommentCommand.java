package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.EditCommentConsole;

public class EditCommentCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "EditCommentCommand takes to EditCommentConsole which allows you to edit the comment, based on its cid!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		try {
			Console editCommentConsole= new EditCommentConsole(userNickname, enteredInputs.get(0), previousPage); //enteredInputs.get(0) == postPid
			editCommentConsole.start();
			
		}catch(java.lang.IndexOutOfBoundsException ex) {					//postPid = null
			Console editCommentConsole= new EditCommentConsole(userNickname, null, previousPage);
			editCommentConsole.start();
		}
	}
}
