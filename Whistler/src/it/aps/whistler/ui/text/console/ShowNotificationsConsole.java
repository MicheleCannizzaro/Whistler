package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Notification;
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

public class ShowNotificationsConsole implements Console{
	private final static Logger logger = Logger.getLogger(ShowNotificationsConsole.class.getName());
	
	private String userNickname;
	private Account userAccount;
	private Page previousPage;
	
	public ShowNotificationsConsole(String userNickname, Page previousPage) {
		this.userNickname = userNickname;
		this.userAccount = Whistler.getInstance().getAccount(this.userNickname); 
		this.previousPage = previousPage;
	}
		
	public void start() {
		welcomePage();
		
		showResults();
		
		printAvailableCommands(Page.SHOW_NOTIFICATIONS_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.SHOW_NOTIFICATIONS_CONSOLE);
			          //userInputs = null
			command.run(null, userNickname,this.previousPage);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, ShowNotificationsConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ShowNotificationsConsole "+ex);
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
				+" ╚════════════════════════════════╝      \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                               ╔╗╔╔═╗╔╦╗╦╔═╗╦╔═╗╔═╗╔╦╗╦╔═╗╔╗╔╔═╗                                            \n"
						 + "                               ║║║║ ║ ║ ║╠╣ ║║  ╠═╣ ║ ║║ ║║║║╚═╗                                            \n"
				     	 + "                               ╝╚╝╚═╝ ╩ ╩╚  ╩╚═╝╩ ╩ ╩ ╩╚═╝╝╚╝╚═╝                                            \n"
				         + "                                                                                                            \n"
				         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
				         + "        NUMBER: "+this.userAccount.getAllAccountNotifications().size()+"                                    \n");
	}

	private void showResults() {
		ArrayList<Notification> notifications = new ArrayList<>(this.userAccount.getAllAccountNotifications());
		
		if (!notifications.isEmpty()) {
			
			//Sorting Posts in Reverse Chronological Order
			Collections.sort(notifications, Comparator.comparing(Notification::getTimestamp).reversed());
			
			//printing post of the search
			for (Notification n : notifications) {	
				Util.printDetailedNotification(n);
			}
		}else {
			System.out.println(Util.padLeft("No notifications! Enjoy the Real World!\n", 70));
		}
	}
	
	
}
