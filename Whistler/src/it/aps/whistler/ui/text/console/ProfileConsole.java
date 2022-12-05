package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Account;
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

public class ProfileConsole implements Console{
	private final static Logger logger = Logger.getLogger(ProfileConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private Account userAccount;
	
	public ProfileConsole(String nickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = nickname;
		this.userAccount = Whistler.getInstance().getAccount(userNickname);
	}
		
	public void start() {
		welcomePage();
		
		personalInfo();
		
		printAvailableCommands(Page.PROFILE_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.PROFILE_CONSOLE);
			command.run(userInputs, userNickname);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, ProfileConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ProfileConsole "+ex);
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
				+" ╚═════════════════════════╝             \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                                            ╔═╗╦═╗╔═╗╔═╗╦╦  ╔═╗                                             \n"
					     + "                                            ╠═╝╠╦╝║ ║╠╣ ║║  ║╣                                              \n"
					     + "                                            ╩  ╩╚═╚═╝╚  ╩╩═╝╚═╝                                             \n"
				         + "        USER: "+userNickname+"                                                                              \n"
				         + "        DATE: "+Util.getTimeString(LocalDateTime.now())+"                                                   \n"
				         + "                                                                                                            \n");
	}
	
	private void personalInfo() {
		System.out.println("                      PERSONAL INFO                                                                         \n"
						  +"       ╔══════════════════════════════════════════╗                                                         \n"
						  +"       ║  "+Util.padRight("Name: "+userAccount.getName(), 30)+Util.padRight(" ("+userAccount.getVisibility().get(0).toString()+")",10)+"║      \n"
						  +"       ║  "+Util.padRight("Surname: "+userAccount.getSurname(), 30)+Util.padRight(" ("+userAccount.getVisibility().get(1).toString()+")",10)+"║\n"
						  +"       ║  "+Util.padRight("E-mail: "+userAccount.getEmail(), 30)+Util.padRight(" ("+userAccount.getVisibility().get(2).toString()+")",10)+"║   \n"
			  			  +"       ║══════════════════════════════════════════║                                                         \n"
			  			  +"       ║  "+Util.padRight("Number of posts: "+userAccount.getPosts().size(), 40)+"║                         \n"
	  					  +"       ║  "+Util.padRight("Followers: "+userAccount.getFollowers().size(), 40)+"║                           \n"
	  					  +"       ║  "+Util.padRight("Followed: "+userAccount.getFollowedAccounts().size(), 40)+"║                     \n"
						  +"       ╚══════════════════════════════════════════╝                                                         \n"                                                   
				          +"                                                                                                            \n");
	}
}
