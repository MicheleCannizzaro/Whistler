package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class ExitConsole {
	
	private ArrayList<String> userInputs;
	
	public ExitConsole() {
		this.userInputs = new ArrayList<>();
	}
	
	public void start() {
		welcomePage();
		printAvailableCommands(Page.EXIT_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.EXIT_CONSOLE);
			command.run(userInputs,null);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ExitConsole"+ex);
		}
		
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔══════════════════════╗               \n"
				+" ║  "+Util.padRight(commands[0],20)+"║  \n"
				+" ║  "+Util.padRight(commands[1],20)+"║  \n"
				+" ╚══════════════════════╝               \n");
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
