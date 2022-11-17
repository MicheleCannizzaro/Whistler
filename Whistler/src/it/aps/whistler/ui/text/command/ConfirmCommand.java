package it.aps.whistler.ui.text.command;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.LoginConsole;
import it.aps.whistler.ui.text.console.SignUpConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.util.ArrayList;

public class ConfirmCommand implements Command{
	
	private Page page;
	
	public ConfirmCommand(Page page){
		this.page=page;
	}
		
	public String getCommandDescription() {
		String descripition = "ConfirmCommand is a command that satisfies different requests based on the page in which it is inserted ";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs) {
		Whistler whistler = Whistler.getInstance();
		
		if (this.page == Page.SIGNUP_CONSOLE) {
			
			while (!whistler.signUp(enteredInputs.get(0),enteredInputs.get(1),enteredInputs.get(2),enteredInputs.get(3),enteredInputs.get(4))) {
				SignUpConsole signUpConsole = new SignUpConsole();
				signUpConsole.start();
			}
			
			WhistlerConsole whistlerConsole = new WhistlerConsole();
			whistlerConsole.start();
		}
			
		if (this.page == Page.LOGIN_CONSOLE) {
			
			while(!whistler.login(enteredInputs.get(0), enteredInputs.get(1))) {
				System.out.println("<<Wrong password. Try again!>>");
				LoginConsole loginConsole = new LoginConsole();
				loginConsole.start();
			}
			
			HomeConsole homeConsole = new HomeConsole(whistler.searchAccount(enteredInputs.get(0))); //Pass the correct  the logged account to the HomeConsole
			homeConsole.start();
					
		}
		
	}
}