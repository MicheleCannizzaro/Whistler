package it.aps.whistler.ui.text.console;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;

import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class HomeConsole {
	
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
			command.run(userInputs, userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException HomeConsole"+ex);
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
				+" ╚═════════════════════════╝             \n");
	}
	
	private void welcomePage() {
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
		
		if (followedAccount.isEmpty()) {
			System.out.println(Util.padLeft("Your Timeline is Empty, because you still don't follow anyone, please follow someone.\n", 97));
		}
		
		//Adding public posts of followedAccount to homeTimeline
		for (String accountNickname: followedAccount) {
			homeTimeline.addAll(Whistler.getInstance().getAccountPublicPosts(accountNickname)); 
		}
		
		//printing post of homeTimeline
		for (Post p : homeTimeline) {	
			printDetailedPost(p);
		}
	}
	
	private void printDetailedPost(Post p) {
		
		System.out.println("         ╔═══════════════════╗"+                 "═══════════════════╗"+                    "═════════════════════╗");
		System.out.println("         ║  "+Util.padRight(p.getOwner(),17)+"║"+" PID: "+Util.padRight(p.getPid(), 13)+"║"+" "+Util.getTimeString(p.getTimestamp())+" ║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════╗");
		
		if (p.getTitle().length()<=70) {
			System.out.println("         ║ TITLE: "+Util.padRight(p.getTitle(), 71)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");  
		}
		
		if (p.getBody().length()<=70) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0,p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║"); 
		}
		
		if (p.getBody().length()>70 && p.getBody().length()<=140 ) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(70, p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getBody().length()>140 && p.getBody().length()<=209) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(140,p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getBody().length()>209 && p.getBody().length()<=280) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(140, 210), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(210, p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getPostVisibility().equals(Visibility.PUBLIC)) {
			System.out.println("         ║ Visibility: PUBLIC                                                            ║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getPostVisibility().equals(Visibility.PRIVATE)) {
			System.out.println("         ║ Visibility: PRIVATE                                                           ║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
			
		System.out.println("         ║ Keywords:"+Util.padRight(p.getPostKeywords().toString(), 69)+"║");
		System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
	}
}
