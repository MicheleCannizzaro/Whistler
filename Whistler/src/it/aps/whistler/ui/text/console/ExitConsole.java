package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class ExitConsole implements Console{
	private final static Logger logger = Logger.getLogger(ExitConsole.class.getName());
	
	private ArrayList<String> userInputs;
	
	public ExitConsole() {
		this.userInputs = new ArrayList<>();
	}
	
	public void start() {
		welcomePage();
		printAvailableCommands(Page.EXIT_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.EXIT_CONSOLE);
			command.run(userInputs,null,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, ExitConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ExitConsole "+ex);
		}
		
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔══════════════════════╗               \n"
				+" ║  "+Util.padRight(commands[0],20)+"║  \n"
				+" ║  "+Util.padRight(commands[1],20)+"║  \n"
				+" ╚══════════════════════╝               \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("          ╔╦╗╔═╗   ╦ ╦╔═╗╦ ╦  ╦═╗╔═╗╔═╗╦  ╦  ╦ ╦  ╦ ╦╔═╗╔╗╔╔╦╗  ╔╦╗╔═╗  ╦  ╔═╗╔═╗╦  ╦╔═╗  ╦ ╦╔═╗┌─┐         \n"
				          +"           ║║║ ║   ╚╦╝║ ║║ ║  ╠╦╝║╣ ╠═╣║  ║  ╚╦╝  ║║║╠═╣║║║ ║    ║ ║ ║  ║  ║╣ ╠═╣╚╗╔╝║╣   ║ ║╚═╗ ┌┘         \n"
				          +"          ═╩╝╚═╝    ╩ ╚═╝╚═╝  ╩╚═╚═╝╩ ╩╩═╝╩═╝ ╩   ╚╩╝╩ ╩╝╚╝ ╩    ╩ ╚═╝  ╩═╝╚═╝╩ ╩ ╚╝ ╚═╝  ╚═╝╚═╝ o          \n"
				          +"                                               .     .                                                      \n"
				          +"                                                  ^                                                         \n");
	}
}
