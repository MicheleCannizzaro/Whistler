package it.aps.whistler.ui.text.command;

import java.util.ArrayList;
import it.aps.whistler.ui.text.console.CircleConsole;
import it.aps.whistler.ui.text.console.Console;

public class CircleCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "CircleCommand takes to CicrcleConsole which shows you all the Whistlerblowers you follow!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Console circleConsole= new CircleConsole(userNickname);
		circleConsole.start();
	}
}
