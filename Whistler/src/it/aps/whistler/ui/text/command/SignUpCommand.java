package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.SignUpConsole;

public class SignUpCommand implements Command{

	public String getCommandDescription() {
		String descripition = "SignUpCommand takes you to the SignUpConsole where you can sign-up an account to Whistler";
		return descripition;
	}
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console signupConsole= new SignUpConsole();
		signupConsole.start();
	}
}
