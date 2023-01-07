package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;

import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class ShowResultsConsole implements Console{
	private final static Logger logger = Logger.getLogger(ShowResultsConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private String searchedKeyword;
	private ArrayList<Post> posts;
	
	public ShowResultsConsole(String userNickname,String searchedKeyword) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.searchedKeyword = searchedKeyword;
		this.posts = Whistler.getInstance().searchPosts(searchedKeyword);
	}
		
	public void start() {
		welcomePage();
		
		showResults();
		
		printAvailableCommands(Page.SHOW_RESULTS_CONSOLE);
		
		try {
			userInputs.add(this.searchedKeyword);
			Command command= Parser.getInstance().getCommand(Page.SHOW_RESULTS_CONSOLE);
			command.run(userInputs, userNickname,null);
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
				+" ║  "+Util.padRight(commands[2], 30)+"║  \n"
				+" ║  "+Util.padRight(commands[3], 30)+"║  \n"
				+" ║  "+Util.padRight(commands[4], 30)+"║  \n"
				+" ║  "+Util.padRight(commands[5], 30)+"║  \n"
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
			
			//Sorting Posts in Reverse Chronological Order
			Collections.sort(this.posts, Comparator.comparing(Post::getTimestamp).reversed());
			
			//printing post of the search
			for (Post p : this.posts) {	
				Util.printDetailedPost(p);
			}
		}else {
			System.out.println(Util.padLeft("Sorry, Whistler didn't find any results matching this search\n", 85));
		}
	}
	
	
}
