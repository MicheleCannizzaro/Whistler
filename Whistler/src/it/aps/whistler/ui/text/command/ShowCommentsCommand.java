package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ShowPostCommentsConsole;

public class ShowCommentsCommand implements Command {
	
	private Page previousPage;
	
	public ShowCommentsCommand(){}
	
	public ShowCommentsCommand(Page previousPage){
		this.previousPage=previousPage;
	}
	
	public String getCommandDescription() {
		String descripition = "\"Show Comments \" - ShowCommentsCommand takes to ShowPostCommentsConsole where you can see all comments\n     of a post";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console showPostCommentsConsole= new ShowPostCommentsConsole(userNickname,enteredInputs, this.previousPage);
		showPostCommentsConsole.start();
	}
}
