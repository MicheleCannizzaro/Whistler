package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.LoginConsole;

public class LoginCommand implements Command{
	
	public LoginCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Login\" - LoginCommand takes you to the LoginConsole";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console loginConsole= new LoginConsole();
		loginConsole.start();
	}
}
