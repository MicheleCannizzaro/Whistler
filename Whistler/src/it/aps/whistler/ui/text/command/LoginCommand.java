package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.LoginConsole;

public class LoginCommand implements Command{
	
	public String getCommandDescription() {
		String descripition = "LoginCommand gets you to the LoginConsole";
		return descripition;
	}
	public void run(ArrayList<String> enteredInputs) {
		LoginConsole loginConsole= new LoginConsole();
		loginConsole.start();
	}
}
