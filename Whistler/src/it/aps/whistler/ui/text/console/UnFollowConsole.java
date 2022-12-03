package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class UnFollowConsole implements Console {
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public UnFollowConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		Account userAccount = Whistler.getInstance().getAccount(userNickname);
		ArrayList<String> followedAccount = userAccount.getFollowedAccounts();
		
		System.out.println(" This is, in short, your Circle of Interests:\n");
		
		if (followedAccount.isEmpty()) {
			System.out.println(" - Empty, you don't follow anyone.");
		}
		
		for(String nickname : followedAccount) {
			System.out.println(" - "+nickname);
		}
		
		
		String whisltlerblowerNickname = Parser.getInstance().readCommand("\n Enter @nickname of the whistlerblower to unfollow:");
		userInputs.add(whisltlerblowerNickname);
		
		printAvailableCommands(Page.UNFOLLOW_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.UNFOLLOW_CONSOLE);
			command.run(userInputs, this.userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException UnFollowConsole "+ex);
		}
	}

	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                          ╦ ╦╔╗╔╔═╗╔═╗╦  ╦  ╔═╗╦ ╦                                                   \n"
				+ "                                          ║ ║║║║╠╣ ║ ║║  ║  ║ ║║║║                                                   \n"
			    + "                                          ╚═╝╝╚╝╚  ╚═╝╩═╝╩═╝╚═╝╚╩╝                                                   \n"
				+ "                                         ╚════════════════════════╝                                                  \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗        \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║    \n"
			   +" ╚════════════════════════════════╝        \n");
	}
}
