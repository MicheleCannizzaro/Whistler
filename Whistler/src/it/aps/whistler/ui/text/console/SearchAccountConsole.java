package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class SearchAccountConsole implements Console {
	private final static Logger logger = Logger.getLogger(SearchAccountConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public SearchAccountConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		String whisltlerblowerNickname = getWhistleblowerNicknameFromStandardInput();		
		manageSearchConsoleCommand(whisltlerblowerNickname);
	}
	
	private void manageSearchConsoleCommand(String whisltlerblowerNickname) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(whisltlerblowerNickname);
		
		printAvailableCommands(Page.SEARCH_ACCOUNT_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.SEARCH_ACCOUNT_CONSOLE);
			command.run(userInputs,this.userNickname);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, SearchAccountConsole.class.getSimpleName(),"manageSearchConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException SearchAccountConsole "+ex);
		}
	}
	
	public String getWhistleblowerNicknameFromStandardInput() {
		String whisltlerblowerNickname = Parser.getInstance().readCommand(" Enter @nickname of the whistlerblower account you want to search:");
		
		//UI preventive checks for better user experience
		while (!Whistler.getInstance().isPresent(whisltlerblowerNickname)) {
			System.out.println("\n<<The Nickname is incorrect or non-existent!>>");
			logger.log(Level.INFO, "[getWhistleblowerNicknameFromStandardInput] - The Nickname is incorrect or non-existent!");
			
			printAvailableCommandsSearchError(Page.SEARCH_ACCOUNT_CONSOLE);
			manageSearchErrorCommands(whisltlerblowerNickname);
		}
		return whisltlerblowerNickname;
	}
	
	private void manageSearchErrorCommands(String nickname){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageSearchErrorCommands] - SearchConsole turn back to HomeConsole");
						command = new TurnBackCommand(Page.SEARCH_ACCOUNT_CONSOLE);
						command.run(userInputs, this.userNickname);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[manageSearchErrorCommands] - SearchConsole (Retry)");
						nickname = getWhistleblowerNicknameFromStandardInput();
						manageSearchConsoleCommand(nickname);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, SearchAccountConsole.class.getSimpleName(),"manageSearchErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageSearchErrorCommands(nickname);
        	}
	}		

	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                         ╔═╗╔═╗╔═╗╦═╗╔═╗╦ ╦  ╔═╗╔═╗╔═╗╔═╗╦ ╦╔╗╔╔╦╗                                    \n"
				+ "                                         ╚═╗║╣ ╠═╣╠╦╝║  ╠═╣  ╠═╣║  ║  ║ ║║ ║║║║ ║                                     \n"
			    + "                                         ╚═╝╚═╝╩ ╩╩╚═╚═╝╩ ╩  ╩ ╩╚═╝╚═╝╚═╝╚═╝╝╚╝ ╩                                     \n"
				+ "                                         ╚═══════════════════════════════════════╝                                    \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║    \n"
			   +" ╚════════════════════════════════╝       \n");
	}

	private void printAvailableCommandsSearchError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════╗     \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║  \n"
			   +" ║  "+Util.padRight("1:Retry",30)+  "║  \n"
			   +" ╚════════════════════════════════╝     \n");
	}
}
