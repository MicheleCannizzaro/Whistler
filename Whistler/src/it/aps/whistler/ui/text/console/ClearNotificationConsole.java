package it.aps.whistler.ui.text.console;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class ClearNotificationConsole implements Console {
	private final static Logger logger = Logger.getLogger(ClearNotificationConsole.class.getName());
	
	private String userNickname;
	private Account userAccount;
	private boolean isForAllNotifications;
	private Page previousPageOfShowNotification;
	
	public ClearNotificationConsole(String userNickname, boolean isForAllNotifications, Page previousPageOfShowNotification) {
		this.userNickname = userNickname;
		this.userAccount = Whistler.getInstance().getAccount(this.userNickname);
		this.isForAllNotifications = isForAllNotifications;
		this.previousPageOfShowNotification = previousPageOfShowNotification;
		
	}
	
	public void start() {
		String nid = null;
		if (!this.isForAllNotifications) {
			nid = getNidFromStandardInput();	
		}
		manageClearNotificationConsoleCommand(nid);
	}
	
	private void manageClearNotificationConsoleCommand(String nid) {
		
		if (nid!=null) {
			if (this.userAccount.clearNotification(nid)) {
				System.out.println("  Cleared!");
			}
		}else {
			
			if (this.userAccount.clearAllNotifications()) {
				System.out.println(" ...wait\n");
				System.out.println("  All Cleared!\n");
			}
		}
		
		Console showNotificationsConsole = new ShowNotificationsConsole(this.userNickname, this.previousPageOfShowNotification);
		showNotificationsConsole.start();
		
	}
	
	private String getNidFromStandardInput() {
		String nid = Parser.getInstance().readCommand(" Enter Notification's NID to clear :");
		
		//UI preventive checks for better user experience
		while (nid.length()<6 || nid.length()>6) {
			System.out.println("<<Sorry, wrong NID! It must be exactly 6 number long.>>\n");
			logger.log(Level.INFO, "[getNidFromStandardInput] - The NID entered is of wrong lenght");
			
			printAvailableCommands(null);
			manageClearNotificationErrorCommands();
		}
		
		while (!this.userAccount.isNidPresent(nid)) {
			System.out.println("\n<<Wrong nid! Please Retry>>");
			logger.log(Level.INFO, "[getNidFromStandardInput] - The Nid entered it's relative to a notification not owned from the userAccount or does not exist.");
			
			printAvailableCommands(null);
			manageClearNotificationErrorCommands();
		}
		return nid;
	}
	
	private void manageClearNotificationErrorCommands(){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageClearNotificationErrorCommands] - ClearNotificationConsole turn back");
						command = new TurnBackCommand(Page.SHOW_NOTIFICATIONS_CONSOLE);
								//userInputs = null
						command.run(null, this.userNickname,this.previousPageOfShowNotification);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[manageClearNotificationErrorCommands] - ClearNotificationConsole (Retry)");
						String nid = getNidFromStandardInput();
						manageClearNotificationConsoleCommand(nid);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, ClearNotificationConsole.class.getSimpleName(),"manageClearNotificationErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageClearNotificationErrorCommands();
        	}
	}		

	public void welcomePage() {}
	
	public void printAvailableCommands(Page page) {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════╗     \n"
			   +" ║  "+Util.padRight("0:Go back",30)+"║  \n"
			   +" ║  "+Util.padRight("1:Retry",30)+  "║  \n"
			   +" ╚════════════════════════════════╝     \n");
		
	}
}
