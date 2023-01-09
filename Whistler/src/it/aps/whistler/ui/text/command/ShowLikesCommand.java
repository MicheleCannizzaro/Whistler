package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ShowPostLikesConsole;

public class ShowLikesCommand implements Command {
	
	private Page previousPage;
	
	public ShowLikesCommand(){}
	
	public ShowLikesCommand(Page previousPage){
		this.previousPage=previousPage;
	}
	
	public String getCommandDescription() {
		String descripition = "\"Show Likes\" - ShowLikesCommand takes to ShowPostLikesConsole where you can see the nickname's list of\n     the accounts that liked a post";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		
		if (this.previousPage == Page.SHOW_RESULTS_CONSOLE || this.previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
			
			// if Page.SHOW_RESULTS_CONSOLE ->  enteredInputs.get(0) = searchedKeyword
			 // if Page.ACCOUNT_TIMELINE_CONSOLE ->  enteredInputs.get(0) = whistleblowerNickaname
																									//userInputPreviousPage
			Console showPostLikesConsole= new ShowPostLikesConsole(userNickname, this.previousPage, enteredInputs.get(0));
			showPostLikesConsole.start();
			
		}else {																					
																								//userInputPreviousPage = null
			Console showPostLikesConsole= new ShowPostLikesConsole(userNickname, this.previousPage, null);
			showPostLikesConsole.start();
		}
	}
}
