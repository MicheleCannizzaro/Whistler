package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class RemoveAccountConsole implements Console{
	private final static Logger logger = Logger.getLogger(RemoveAccountConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public RemoveAccountConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		String passwordPlainText = Parser.getInstance().readCommand(" Enter your Password:");
		
		printAvailableCommands(Page.REMOVE_ACCOUNT_CONSOLE);
		
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(passwordPlainText);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.REMOVE_ACCOUNT_CONSOLE);
			command.run(userInputs,this.userNickname);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, RemoveAccountConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException RemoveAccountConsole "+ex);
		}
		
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],34)+"║    \n"
			   +" ╚════════════════════════════════════╝       \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                     ╔╦╗╔═╗   ╦ ╦╔═╗╦ ╦  ╦═╗╔═╗╔═╗╦  ╦  ╦ ╦  ╦ ╦╔═╗╔╗╔╔╦╗  ╔╦╗╔═╗                           \n"
				          +"                      ║║║ ║   ╚╦╝║ ║║ ║  ╠╦╝║╣ ╠═╣║  ║  ╚╦╝  ║║║╠═╣║║║ ║    ║ ║ ║                           \n"
				          +"                     ═╩╝╚═╝    ╩ ╚═╝╚═╝  ╩╚═╚═╝╩ ╩╩═╝╩═╝ ╩   ╚╩╝╩ ╩╝╚╝ ╩    ╩ ╚═╝                           \n"
				          +"                       ╔╦╗╔═╗╦  ╔═╗╔╦╗╔═╗  ╦ ╦╔═╗╦ ╦╦═╗  ╔═╗╔═╗╔═╗╔═╗╦ ╦╔╗╔╔╦╗┌─┐                           \n"
				          +"                        ║║║╣ ║  ║╣  ║ ║╣   ╚╦╝║ ║║ ║╠╦╝  ╠═╣║  ║  ║ ║║ ║║║║ ║  ┌┘                           \n"
						  +"                       ═╩╝╚═╝╩═╝╚═╝ ╩ ╚═╝   ╩ ╚═╝╚═╝╩╚═  ╩ ╩╚═╝╚═╝╚═╝╚═╝╝╚╝ ╩  o                            \n"
				          +"                                               .     .                                                      \n"
				          +"                                                  ^                                                         \n"
				          +"      <<You will lose all personal information, posts, followers and you will never be able to go back.>>   \n");
	}
}
