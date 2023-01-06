package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.CircleConsole;
import it.aps.whistler.ui.text.console.Console;

public class CircleCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "CircleCommand takes to CircleConsole which shows you all the Whistleblowers you follow!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console circleConsole= new CircleConsole(userNickname);
		circleConsole.start();
	}
}
