package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ShowNotificationsConsole;

public class ShowNotificationsCommand implements Command {
	
	private Page previousPage;
	
	public ShowNotificationsCommand(Page previousPage){
		this.previousPage=previousPage;
	}
	
	public String getCommandDescription() {
		String descripition = "ShowNotificationsCommand takes to ShowNotificationsConsole which shows all your notifications!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console showNotificationsConsole= new ShowNotificationsConsole(userNickname, this.previousPage);
		showNotificationsConsole.start();
	}
}
