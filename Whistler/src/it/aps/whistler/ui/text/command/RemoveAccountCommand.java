package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.RemoveAccountConsole;

public class RemoveAccountCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "RemoveAccountCommand takes to RemoveAccountConsole which guides you to remove your Whistler Account!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console removeAccountConsole = new RemoveAccountConsole(userNickname);
		removeAccountConsole.start();
	}
}