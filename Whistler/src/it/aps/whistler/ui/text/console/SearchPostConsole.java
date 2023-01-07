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

public class SearchPostConsole implements Console {
	private final static Logger logger = Logger.getLogger(SearchPostConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public SearchPostConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		String searchedKeyword = getKeywordFromStandardInput();	
		System.out.println("\n<<Keyword found! Want to search for posts with this keyword?>>");
		manageSearchConsoleCommand(searchedKeyword);
	}
	
	private void manageSearchConsoleCommand(String searchedKeyword) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(searchedKeyword);
		
		printAvailableCommands(Page.SEARCH_POST_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.SEARCH_POST_CONSOLE);
			command.run(userInputs,this.userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, SearchPostConsole.class.getSimpleName(),"manageSearchConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException SearchAccountConsole "+ex);
		}
	}
	
	public String getKeywordFromStandardInput() {
		String searchedKeyword = Parser.getInstance().readCommand(" Enter #keyword to search:");
		
		if (searchedKeyword.charAt(0)!='#') {
			searchedKeyword = "#"+searchedKeyword;
		}
		
		//UI preventive checks for better user experience
		while (!Whistler.getInstance().isKeywordPresent(searchedKeyword)) {
			System.out.println("\n<<Sorry this keyword hasn't been used yet on Whistler>>");
			logger.log(Level.INFO, "[getKeywordFromStandardInput] - The Keyword entered hasn't been used yet on Whistler!");
			
			printAvailableCommandsSearchError(Page.SEARCH_POST_CONSOLE);
			manageSearchErrorCommands(searchedKeyword);
		}
		return searchedKeyword;
	}
	
	private void manageSearchErrorCommands(String searchedKeyword){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageSearchErrorCommands] - SearchPostConsole turn back to HomeConsole");
						command = new TurnBackCommand(Page.SEARCH_POST_CONSOLE);
						command.run(userInputs, this.userNickname,null);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[manageSearchErrorCommands] - SearchPostConsole (Retry)");
						searchedKeyword = getKeywordFromStandardInput();
						manageSearchConsoleCommand(searchedKeyword);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, SearchPostConsole.class.getSimpleName(),"manageSearchErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageSearchErrorCommands(searchedKeyword);
        	}
	}		

	public void welcomePage() {
		System.out.println(
				  " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                           ╔═╗╔═╗╔═╗╦═╗╔═╗╦ ╦                                               \n"
				+ "                                           ╚═╗║╣ ╠═╣╠╦╝║  ╠═╣                                               \n"
			    + "                                           ╚═╝╚═╝╩ ╩╩╚═╚═╝╩ ╩                                               \n"
				+ "                                          ╚══════════════════╝                                              \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗      \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║   \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║   \n"
			   +" ╚════════════════════════════════╝      \n");
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
