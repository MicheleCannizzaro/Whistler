package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.EditCommentConsole;

public class EditCommentCommand implements Command {
	
	public EditCommentCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Edit Comment\" - EditCommentCommand takes to EditCommentConsole which allows you to edit the comment,\n     based on its cid!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		try {
			
			if (previousPage == Page.SHOW_RESULTS_CONSOLE || previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
																												//enteredInputs.get(1) = userInputPreviousPage
				Console editCommentConsole= new EditCommentConsole(userNickname, enteredInputs.get(0), previousPage, enteredInputs.get(1)); //enteredInputs.get(0) = postPid
				editCommentConsole.start();
				
			}else {
																												// userInputPreviousPage = null
				Console editCommentConsole= new EditCommentConsole(userNickname, enteredInputs.get(0), previousPage, null); //enteredInputs.get(0) = postPid
				editCommentConsole.start();
			}
			
		}catch(java.lang.IndexOutOfBoundsException ex) {					//postPid = null	// userInputPreviousPage = null
			Console editCommentConsole= new EditCommentConsole(userNickname, null, previousPage, null);
			editCommentConsole.start();
		}
	}
}
