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
import java.time.LocalDateTime;

public class ProfileTimelineConsole implements Console{
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public ProfileTimelineConsole(String nickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = nickname;
	}
		
	public void start() {
		welcomePage();
		
		profileTimeline();
		
		printAvailableCommands(Page.PROFILE_TIMELINE_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.PROFILE_TIMELINE_CONSOLE);
			command.run(userInputs, userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ProfileTimelineConsole "+ex);
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
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                               ╔═╗╦═╗╔═╗╔═╗╦╦  ╔═╗  ╔╦╗╦╔╦╗╔═╗╦  ╦╔╗╔╔═╗                                    \n"
					     + "                               ╠═╝╠╦╝║ ║╠╣ ║║  ║╣    ║ ║║║║║╣ ║  ║║║║║╣                                     \n"
					     + "                               ╩  ╩╚═╚═╝╚  ╩╩═╝╚═╝   ╩ ╩╩ ╩╚═╝╩═╝╩╝╚╝╚═╝                                    \n"
				         + "        USER: "+userNickname+"                                                                              \n"
				         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
				         + "                                                                                                            \n");
	}

	private void profileTimeline() {
		Account userAccount = Whistler.getInstance().getAccount(this.userNickname);
		ArrayList<Post> profileTimeline = userAccount.getPosts();
		
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		
		if (profileTimeline.isEmpty()) {
			System.out.println(Util.padLeft("Your ProfileTimeline is Empty, start to post something on Whistler.\n", 85));
		}
		
		//printing post of profileTimeline
		for (Post p : profileTimeline) {	
			Util.printDetailedPost(p);
		}
	}
}
