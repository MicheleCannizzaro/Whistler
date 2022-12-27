package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Keyword;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;

import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class ExploreConsole implements Console{
	private final static Logger logger = Logger.getLogger(ExploreConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public ExploreConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
		
	public void start() {
		welcomePage();
		
		showTrendingKeywords();
		
		printAvailableCommands(Page.EXPLORE_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.EXPLORE_CONSOLE);
			command.run(userInputs, userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, ExploreConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ExploreConsole "+ex);
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
		System.out.println("                          ╔╦╗╦═╗╔═╗╔╗╔╔╦╗╦╔╗╔╔═╗  ╦╔═╔═╗╦ ╦╦ ╦╔═╗╦═╗╔╦╗╔═╗                                  \n"
						 + "                           ║ ╠╦╝║╣ ║║║ ║║║║║║║ ╦  ╠╩╗║╣ ╚╦╝║║║║ ║╠╦╝ ║║╚═╗                                  \n"
				     	 + "                           ╩ ╩╚═╚═╝╝╚╝═╩╝╩╝╚╝╚═╝  ╩ ╩╚═╝ ╩ ╚╩╝╚═╝╩╚══╩╝╚═╝                                  \n"
				         + "                                                                                                            \n"
				         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
				         + "                                                                                                            \n");
	}

	private void showTrendingKeywords() {
		ArrayList<Keyword> trendingKeywords = Whistler.getInstance().getTrendingKeywords();
		if(!trendingKeywords.isEmpty()) {
			System.out.println(
					 "         #1 Trending                     #2 Trending                     #3 Trending                                                                                                         \n"
					+" ╔═══════════════════════════╗   ╔═══════════════════════════╗   ╔═══════════════════════════╗                                                                                               \n"
					+" ║  "+Util.padRight(trendingKeywords.get(0).getWord(), 25)+"║   ║  "+Util.padRight(trendingKeywords.get(1).getWord(), 25)+"║   ║  "+Util.padRight(trendingKeywords.get(2).getWord(), 25)+"║  \n"
					+" ╚═══════════════════════════╝   ╚═══════════════════════════╝   ╚═══════════════════════════╝                                                                                               \n");
		}else {
			System.out.println("                      Sorry! No keywords were previously added on Whistler,\n"
							  +"                 be the first one to populate this wonderful microblogging platform!\n"
							  +"                      Go back to Home and Publish the first post with keywords!\n");
		}
		
	}
	
	
}
