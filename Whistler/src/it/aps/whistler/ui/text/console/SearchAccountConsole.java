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
	private boolean isPreviousPageHome;
	
	public SearchAccountConsole(String userNickname, boolean isPreviousPageHome) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.isPreviousPageHome = isPreviousPageHome;
	}
	
	public void start() {
		welcomePage();
		
		String whistleblowerNickname = getWhistleblowerNicknameFromStandardInput();
		System.out.println("\n<<Account found! Want to show this account?>>");
		manageSearchConsoleCommand(whistleblowerNickname);
	}
	
	private void manageSearchConsoleCommand(String whistleblowerNickname) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(whistleblowerNickname);
		
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
		String whistleblowerNickname = Parser.getInstance().readCommand(" Enter @nickname of the whistleblower account you want to search:");
		
		if (whistleblowerNickname.charAt(0)!='@') {
			whistleblowerNickname = "@"+whistleblowerNickname;
		}
		
		//UI preventive checks for better user experience
		while (!Whistler.getInstance().isAccountPresent(whistleblowerNickname)) {
			System.out.println("\n<<The Nickname is incorrect or non-existent!>>");
			logger.log(Level.INFO, "[getWhistleblowerNicknameFromStandardInput] - The Nickname is incorrect or non-existent!");
			
			printAvailableCommandsSearchError(Page.SEARCH_ACCOUNT_CONSOLE);
			manageSearchErrorCommands(whistleblowerNickname);
		}
		return whistleblowerNickname;
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
				  "                                    ╔═╗╔═╗╔═╗╦═╗╔═╗╦ ╦  ╔═╗╔═╗╔═╗╔═╗╦ ╦╔╗╔╔╦╗                                        \n"
				+ "                                    ╚═╗║╣ ╠═╣╠╦╝║  ╠═╣  ╠═╣║  ║  ║ ║║ ║║║║ ║                                         \n"
			    + "                                    ╚═╝╚═╝╩ ╩╩╚═╚═╝╩ ╩  ╩ ╩╚═╝╚═╝╚═╝╚═╝╝╚╝ ╩                                         \n"
				+ "                                    ╚═══════════════════════════════════════╝                                        \n");
		if(isPreviousPageHome) {
			//Suggestions							//toFollow=false
			if (!Util.randomSuggestions(userNickname, false).isEmpty()){
				System.out.println(Util.padLeft("\n These are some random suggestions:", 79));
				
				for (String suggestion : Util.randomSuggestions(userNickname, true)) {
					System.out.println("  "+suggestion);
				}
				System.out.println("\n");
			}else {
				System.out.println(Util.padLeft("\n <<Sorry, no suggestions, you're the only one on Whistler right now>>", 79));
			}
		}
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
