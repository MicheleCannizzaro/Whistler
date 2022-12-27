package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ProfileConsole;

public class ProfileCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "ProfileCommand takes to ProfileConsole which shows account's infos and posts!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console profileConsole= new ProfileConsole(userNickname,true, null); //isOwner == true
		profileConsole.start();
	}
}
