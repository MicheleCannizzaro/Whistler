package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;

public class ExitConsole {
	
	private ArrayList<String> userInputs;
	
	public ExitConsole() {
		this.userInputs = new ArrayList<>();
	}
	
	public void start() {
		welcomePage();
		printAvailableCommands(Page.EXIT_CONSOLE);
		Command command= Parser.getInstance().getCommand(Page.EXIT_CONSOLE);
		command.run(userInputs);	
		
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔═════════════════════╗  \n"
				+" ║   "+commands[0]+"             ║  \n"
				+" ║   "+commands[1]+"              ║  \n"
				+" ╚═════════════════════╝  \n");
	}
	
	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("          ╔╦╗╔═╗   ╦ ╦╔═╗╦ ╦  ╦═╗╔═╗╔═╗╦  ╦  ╦ ╦  ╦ ╦╔═╗╔╗╔╔╦╗  ╔╦╗╔═╗  ╦  ╔═╗╔═╗╦  ╦╔═╗  ╦ ╦╔═╗┌─┐         \n"
				          +"           ║║║ ║   ╚╦╝║ ║║ ║  ╠╦╝║╣ ╠═╣║  ║  ╚╦╝  ║║║╠═╣║║║ ║    ║ ║ ║  ║  ║╣ ╠═╣╚╗╔╝║╣   ║ ║╚═╗ ┌┘         \n"
				          +"          ═╩╝╚═╝    ╩ ╚═╝╚═╝  ╩╚═╚═╝╩ ╩╩═╝╩═╝ ╩   ╚╩╝╩ ╩╝╚╝ ╩    ╩ ╚═╝  ╩═╝╚═╝╩ ╩ ╚╝ ╚═╝  ╚═╝╚═╝ o          \n"
				          +"                                               .     .                                                      \n"
				          +"                                                  ^                                                         \n");
	}
}
