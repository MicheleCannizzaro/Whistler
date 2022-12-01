package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class FollowConsole {
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public FollowConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		//Optional - add some random suggestion of people to follow
		
		String whisltlerblowerNickname = Parser.getInstance().readCommand(" Enter @nickname of the whistlerblower to follow:");
		
		userInputs.add(whisltlerblowerNickname);
		
		printAvailableCommands(Page.FOLLOW_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.FOLLOW_CONSOLE);
			command.run(userInputs, this.userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException FollowConsole"+ex);
		}
	}

	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                           ╔═╗╔═╗╦  ╦  ╔═╗╦ ╦                                                        \n"
				+ "                                           ╠╣ ║ ║║  ║  ║ ║║║║                                                        \n"
			    + "                                           ╚  ╚═╝╩═╝╩═╝╚═╝╚╩╝                                                        \n"
				+ "                                          ╚══════════════════╝                                                       \n");
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║    \n"
			   +" ╚════════════════════════════════╝       \n");
	}
}
