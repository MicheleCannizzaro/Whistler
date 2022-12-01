package it.aps.whistler.ui.text.command;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.ExitConsole;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.util.ArrayList;


public class TurnBackCommand implements Command{
	
	private Page page;
	
	public TurnBackCommand(Page page){
		this.page=page;
	}
	
	public String getCommandDescription() {
		String descripition = "TurnBackCommand takes you to the previous page or lets you exit the program";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		
		if (this.page == Page.SIGNUP_CONSOLE || this.page == Page.LOGIN_CONSOLE || this.page == Page.EXIT_CONSOLE) {
			WhistlerConsole whistlerConsole= new WhistlerConsole();
			whistlerConsole.start();
		}
		
		if (this.page == Page.HOME_CONSOLE) {
			ExitConsole exitConsole= new ExitConsole();
			exitConsole.start();
		}
		
		if (this.page == Page.PUBLISH_CONSOLE || this.page == Page.CIRCLE_CONSOLE || this.page == Page.FOLLOW_CONSOLE
				|| this.page == Page.UNFOLLOW_CONSOLE) {
			HomeConsole homeConsole= new HomeConsole(userNickname);
			homeConsole.start();
		}
	}

}
