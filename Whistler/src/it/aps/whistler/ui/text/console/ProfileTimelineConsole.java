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

public class ProfileTimelineConsole implements Console{
	private final static Logger logger = Logger.getLogger(ProfileTimelineConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private String whistleblowerNickname;
	private boolean isOwner;
	
	public ProfileTimelineConsole(String nickname, boolean isOwner, String whistleblowerNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = nickname;
		this.whistleblowerNickname = whistleblowerNickname;
		this.isOwner = isOwner;
	}
		
	public void start() {
		welcomePage();
		
		profileTimeline();
		
		if(isOwner) {
			printAvailableCommands(Page.PROFILE_TIMELINE_CONSOLE);
		}else {
			printAvailableCommands(Page.ACCOUNT_TIMELINE_CONSOLE);
		}
		
		try {
			Page page = Page.PROFILE_TIMELINE_CONSOLE;
			
			if(!isOwner) {
				userInputs.clear();
				page = Page.ACCOUNT_TIMELINE_CONSOLE;
				userInputs.add(whistleblowerNickname);
			}
			
			Command command= Parser.getInstance().getCommand(page);
			command.run(userInputs, userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, ProfileTimelineConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ProfileTimelineConsole "+ex);
		}
	}

	public void printAvailableCommands(Page page) {
		if (page == Page.PROFILE_TIMELINE_CONSOLE) {
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
					+" ╚═════════════════════════╝             \n");
		}
		if (page.equals(Page.ACCOUNT_TIMELINE_CONSOLE)){
			String[] commands = PageCommands.getCommands(page);
			System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			System.out.println(" Commands:");
			System.out.println(
					 " ╔═════════════════════════╗             \n"
					+" ║  "+Util.padRight(commands[0], 23)+"║  \n"
					+" ║  "+Util.padRight(commands[1], 23)+"║  \n"
					+" ║  "+Util.padRight(commands[2], 23)+"║  \n"
					+" ║  "+Util.padRight(commands[3], 23)+"║  \n"
					+" ╚═════════════════════════╝             \n");
		}
	}
	
	public void welcomePage() {
		if(isOwner) {
			System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			System.out.println("                               ╔═╗╦═╗╔═╗╔═╗╦╦  ╔═╗  ╔╦╗╦╔╦╗╔═╗╦  ╦╔╗╔╔═╗                                    \n"
						     + "                               ╠═╝╠╦╝║ ║╠╣ ║║  ║╣    ║ ║║║║║╣ ║  ║║║║║╣                                     \n"
						     + "                               ╩  ╩╚═╚═╝╚  ╩╩═╝╚═╝   ╩ ╩╩ ╩╚═╝╩═╝╩╝╚╝╚═╝                                    \n"
					         + "        USER: "+userNickname+"                                                                              \n"
					         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
					         + "                                                                                                            \n");
		}else {
			System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			System.out.println("                             ╔═╗╔═╗╔═╗╔═╗╦ ╦╔╗╔╔╦╗  ╔╦╗╦╔╦╗╔═╗╦  ╦╔╗╔╔═╗                                    \n"
						     + "                             ╠═╣║  ║  ║ ║║ ║║║║ ║    ║ ║║║║║╣ ║  ║║║║║╣                                     \n"
						     + "                             ╩ ╩╚═╝╚═╝╚═╝╚═╝╝╚╝ ╩    ╩ ╩╩ ╩╚═╝╩═╝╩╝╚╝╚═╝                                    \n"
					         + "        USER: "+whistleblowerNickname+"                                                                     \n"
					         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
					         + "                                                                                                            \n");
		}
	}

	private void profileTimeline() {
		String nickname = this.userNickname;
		
		if (!isOwner) {
			nickname = this.whistleblowerNickname;
		}
		
		Account userAccount = Whistler.getInstance().getAccount(nickname);
		
		ArrayList<Post> profileTimeline = userAccount.getPosts();
		
		if(!isOwner) {
			profileTimeline = Whistler.getInstance().getAccountPublicPosts(nickname);
		}
		
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		
		if (profileTimeline.isEmpty()) {
			System.out.println(Util.padLeft("Your ProfileTimeline is Empty, start to post something on Whistler.\n", 85));
		}
		
		//Sorting Posts in Reverse Chronological Order
		Collections.sort(profileTimeline, Comparator.comparing(Post::getTimestamp).reversed());
		
		//printing post of profileTimeline
		for (Post p : profileTimeline) {	
			Util.printDetailedPost(p);
		}
	}
	
	
}
