package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class CircleConsole implements Console {
	private final static Logger logger = Logger.getLogger(CircleConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private Account userAccount;
	
	public CircleConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.userAccount = Whistler.getInstance().getAccount(this.userNickname);
	}
	
	public void start() {
		welcomePage();
		
		ArrayList<String> followedAccount = this.userAccount.getFollowedAccounts();
		
		if (followedAccount.isEmpty()) {
			System.out.println(Util.padLeft("Your Circle of Interests is empty, follow someone!", 79));
		}
		
		//Suggestions							//toFollow=true
		if (!Util.randomSuggestions(this.userNickname, true).isEmpty()){
			System.out.println(Util.padLeft("\n These are some random suggestions:", 79));
			
			for (String suggestion : Util.randomSuggestions(this.userNickname, true)) {
				System.out.println("  "+suggestion);
			}
		}
		
		for(String nickname : followedAccount) {
			System.out.println(
			 		   			Util.padRight("",43)+"╔════════════════════════╗        \n"                    
			 		   		   +Util.padRight("",43)+"║ "+Util.padRight(nickname, 23)+"║\n"
			 		   		   +Util.padRight("",43)+"╚════════════════════════╝        \n");
		}
		
		
		printAvailableCommands(Page.CIRCLE_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.CIRCLE_CONSOLE);
			command.run(userInputs, this.userNickname,null); 
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.SEVERE, CircleConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException CircleConsole "+ex);
		}
	}

	public void welcomePage() {
		System.out.println(
				  " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                            ╔═╗╦╦═╗╔═╗╦  ╔═╗  ╔═╗╔═╗  ╦╔╗╔╔╦╗╔═╗╦═╗╔═╗╔═╗╔╦╗╔═╗                             \n"
				+ "                            ║  ║╠╦╝║  ║  ║╣   ║ ║╠╣   ║║║║ ║ ║╣ ╠╦╝║╣ ╚═╗ ║ ╚═╗                             \n"
			    + "                            ╚═╝╩╩╚═╚═╝╩═╝╚═╝  ╚═╝╚    ╩╝╚╝ ╩ ╚═╝╩╚═╚═╝╚═╝ ╩ ╚═╝                             \n"
				+ "                           ╚════════════════════════════════════════════════════╝                           \n");
	}
	
	public void printAvailableCommands(Page page) {
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
