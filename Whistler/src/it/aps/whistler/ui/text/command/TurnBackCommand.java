package it.aps.whistler.ui.text.command;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.ExitConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.util.ArrayList;


public class TurnBackCommand implements Command{
	
	private Page page;
	
	public TurnBackCommand(Page page){
		this.page=page;
	}
	
	public String getCommandDescription() {
		String descripition = "TurnBackCommand get you to the previous page or let you exit the program";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs) {
		
		if (this.page == Page.SIGNUP_CONSOLE || this.page == Page.LOGIN_CONSOLE || this.page == Page.EXIT_CONSOLE) {
			WhistlerConsole whistlerConsole= new WhistlerConsole();
			whistlerConsole.start();
		}
		
		if (this.page == Page.HOME_CONSOLE) {
			ExitConsole exitConsole= new ExitConsole();
			exitConsole.start();
		}
	}

}
