package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Post;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;

import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class ShowResultsConsole implements Console{
	private final static Logger logger = Logger.getLogger(ShowResultsConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private ArrayList<Post> posts;
	
	public ShowResultsConsole(String userNickname, ArrayList<Post> posts) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.posts = posts;
	}
		
	public void start() {
		welcomePage();
		
		showResults();
		
		printAvailableCommands(Page.SHOW_RESULTS_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.SHOW_RESULTS_CONSOLE);
			command.run(userInputs, userNickname);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, ShowResultsConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ShowResultsConsole "+ex);
		}
	}

	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔════════════════════════════════╗      \n"
				+" ║  "+Util.padRight(commands[0], 30)+"║  \n"
				+" ║  "+Util.padRight(commands[1], 30)+"║  \n"
				+" ╚════════════════════════════════╝      \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                               ╔═╗╔═╗╔═╗╦═╗╔═╗╦ ╦  ╦═╗╔═╗╔═╗╦ ╦╦ ╔╦╗╔═╗                                     \n"
						 + "                               ╚═╗║╣ ╠═╣╠╦╝║  ╠═╣  ╠╦╝║╣ ╚═╗║ ║║  ║ ╚═╗                                     \n"
				     	 + "                               ╚═╝╚═╝╩ ╩╩╚═╚═╝╩ ╩  ╩╚═╚═╝╚═╝╚═╝╩═╝╩ ╚═╝                                     \n"
				         + "                                                                                                            \n"
				         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
				         + "                                                                                                            \n");
	}

	private void showResults() {
		
		if (!this.posts.isEmpty()) {
			//printing post of the search
			for (Post p : this.posts) {	
				Util.printDetailedPost(p);
			}
		}else {
			System.out.println(Util.padLeft("Sorry, Whistler didn't find any results matching this search\n", 85));
		}
	}
	
	
}
