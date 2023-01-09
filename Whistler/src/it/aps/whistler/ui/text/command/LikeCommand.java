package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.LikeConsole;

public class LikeCommand implements Command{
	private Page previousPage;
	private boolean isLike;
	
	public LikeCommand(){}
	
	public LikeCommand(boolean isLike, Page previousPage){
		this.isLike = isLike;
		this.previousPage = previousPage;
	}
	
	public String getCommandDescription() {
		String descripition = "\"Like \\ Dislike \" - LikeCommand allows you to Like the selected post or Dislike it.";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		
		if (this.previousPage == Page.SHOW_RESULTS_CONSOLE || this.previousPage == Page.ACCOUNT_TIMELINE_CONSOLE) {
			
			 // if Page.SHOW_RESULTS_CONSOLE ->  enteredInputs.get(0) = searchedKeyword
			 // if Page.ACCOUNT_TIMELINE_CONSOLE ->  enteredInputs.get(0) = whistleblowerNickaname
																								//userInputPreviousPage
			Console likeConsole= new LikeConsole(userNickname, this.previousPage, this.isLike, enteredInputs.get(0));
			likeConsole.start();
			
		}else {																
																					//userInputPreviousPage = null
			Console likeConsole= new LikeConsole(userNickname, this.previousPage, this.isLike, null);
			likeConsole.start();
		}
	}

}
