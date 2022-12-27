package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Account;
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

public class HomeConsole implements Console {
	private final static Logger logger = Logger.getLogger(HomeConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public HomeConsole(String nickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = nickname;
	}
		
	public void start() {
		welcomePage();
		
		homeTimeline();
		
		printAvailableCommands(Page.HOME_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.HOME_CONSOLE);
			command.run(userInputs, userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, HomeConsole.class.getSimpleName(),"start","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException HomeConsole "+ex);
		}
	}

	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔═════════════════════════╗             \n"
				+" ║  "+Util.padRight(commands[0], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[1], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[2], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[3], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[4], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[5], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[6], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[7], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[8], 23)+"║  \n"
				+" ║  "+Util.padRight(commands[9], 23)+"║  \n"
				+" ╚═════════════════════════╝             \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                               ██╗  ██╗ ██████╗ ███╗   ███╗███████╗                                         \n"
					     + "                               ██║  ██║██╔═══██╗████╗ ████║██╔════╝        USER: "+      userNickname     +"\n"
					     + "                               ███████║██║   ██║██╔████╔██║█████╗          Updated until "+Util.getTimeString(LocalDateTime.now())+"\n"
				         + "                               ██╔══██║██║   ██║██║╚██╔╝██║██╔══╝                                           \n"
				         + "                               ██║  ██║╚██████╔╝██║ ╚═╝ ██║███████╗                                         \n"
				         + "                               ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝                                         \n");
	}

	private void homeTimeline() {
		Account userAccount = Whistler.getInstance().getAccount(this.userNickname);
		ArrayList<String> followedAccount = userAccount.getFollowedAccounts();
		ArrayList<Post> homeTimeline = new ArrayList<>();
		
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		
		//Adding public posts of followedAccount to homeTimeline
		for (String accountNickname: followedAccount) {
			homeTimeline.addAll(Whistler.getInstance().getAccountPublicPosts(accountNickname)); 
		}
		
		if (followedAccount.isEmpty()) {
			System.out.println(Util.padLeft("Your Timeline is Empty, because you still don't follow anyone, please follow someone.\n", 97));
		}else if(homeTimeline.isEmpty()){
			System.out.println(Util.padLeft("Your Timeline is Empty, because the accounts you follow haven't publish something yet!\n", 97));
		}
		
		//Sorting Posts in Reverse Chronological Order
		Collections.sort(homeTimeline, Comparator.comparing(Post::getTimestamp).reversed());
		
		//printing post of homeTimeline
		for (Post p : homeTimeline) {	
			Util.printDetailedPost(p);
		}
	}
}
