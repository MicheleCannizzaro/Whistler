package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.HelpConsole;

public class HelpCommand implements Command {
	
	private Page previousPage;
	
	public HelpCommand(){}
	
	public HelpCommand(Page previousPage){
		this.previousPage=previousPage;
	}
	
	public String getCommandDescription() {
		String descripition = "\"Help\" - HelpCommand takes to HelpConsole where you can see the list of Whistler Commands";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console helpConsole= new HelpConsole(userNickname, this.previousPage);
		helpConsole.start();
	}
}
