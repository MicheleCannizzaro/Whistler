package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.SettingsConsole;

public class SettingsCommand implements Command {
	
	public SettingsCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Settings\" - SettingsCommand takes to SettingsConsole where you can edit your personal info!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPages) {
		Console settingsConsole= new SettingsConsole(userNickname);
		settingsConsole.start();
	}
}
