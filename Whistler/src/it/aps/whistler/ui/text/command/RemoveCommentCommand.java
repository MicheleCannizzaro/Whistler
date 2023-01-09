package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.RemoveCommentConsole;

public class RemoveCommentCommand implements Command {
	
	public RemoveCommentCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Remove Comment\" - RemoveCommentCommand takes to RemoveCommentConsole which allows you to delete the\n     comment based on the cid!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		try {
			
			if (previousPage == Page.SHOW_RESULTS_CONSOLE || previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
																														//enteredInputs.get(1) = userInputPreviousPage
				Console removeCommentConsole= new RemoveCommentConsole(userNickname, enteredInputs.get(0), previousPage, enteredInputs.get(1)); //enteredInputs.get(0) = postPid
				removeCommentConsole.start();
				
			}else {
																												//userInputPreviousPage = null
				Console removeCommentConsole= new RemoveCommentConsole(userNickname, enteredInputs.get(0), previousPage, null); //enteredInputs.get(0) = postPid
				removeCommentConsole.start();
			}
			
		}catch(java.lang.IndexOutOfBoundsException ex) {						//postPid = null  //userInputPreviousPage = null
			Console removeCommentConsole= new RemoveCommentConsole(userNickname, null, previousPage,null);
			removeCommentConsole.start();
		}
	}
}
