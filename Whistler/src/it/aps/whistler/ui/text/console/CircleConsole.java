package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class CircleConsole {
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public CircleConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		Account userAccount = Whistler.getInstance().getAccount(userNickname);
		
		ArrayList<String> followedAccount = userAccount.getFollowedAccounts();
		
		if (followedAccount.isEmpty()) {
			System.out.println(Util.padLeft("Your Circle of Interests is empty, follow someone!", 79));
		}
		
		for(String nickname : followedAccount) {
			System.out.println(
					 		   Util.padRight("",43)+"╔════════════════════╗            \n"                    
					 		  +Util.padRight("",43)+"║ "+Util.padRight(nickname, 19)+"║\n"
					 		  +Util.padRight("",43)+"╚════════════════════╝              ");
		}
		
		
		printAvailableCommands(Page.CIRCLE_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.CIRCLE_CONSOLE);
			command.run(userInputs, this.userNickname); 
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException CircleConsole"+ex);
		}
	}

	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                             ╔═╗╦╦═╗╔═╗╦  ╔═╗  ╔═╗╔═╗  ╦╔╗╔╔╦╗╔═╗╦═╗╔═╗╔═╗╔╦╗╔═╗                                     \n"
				+ "                             ║  ║╠╦╝║  ║  ║╣   ║ ║╠╣   ║║║║ ║ ║╣ ╠╦╝║╣ ╚═╗ ║ ╚═╗                                     \n"
			    + "                             ╚═╝╩╩╚═╚═╝╩═╝╚═╝  ╚═╝╚    ╩╝╚╝ ╩ ╚═╝╩╚═╚═╝╚═╝ ╩ ╚═╝                                     \n"
				+ "                            ╚════════════════════════════════════════════════════╝                                   \n");
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔══════════════════════╗               \n"
				+" ║  "+Util.padRight(commands[0],20)+"║  \n"
				+" ║  "+Util.padRight(commands[1],20)+"║  \n"
				+" ║  "+Util.padRight(commands[2],20)+"║  \n"
				+" ╚══════════════════════╝               \n");
	}
}
