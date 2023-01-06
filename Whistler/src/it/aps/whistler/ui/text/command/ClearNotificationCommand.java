package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.ClearNotificationConsole;
import it.aps.whistler.ui.text.console.Console;

public class ClearNotificationCommand implements Command {
	private boolean isForAllNotifications;
	
	public ClearNotificationCommand(boolean isForAllNotifications){
		this.isForAllNotifications=isForAllNotifications;
	}
	
	public String getCommandDescription() {
		String descripition = "ClearNotificationCommand takes to ClearNotificationConsole which delete a notification by nid or all notifications!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console clearNotificationConsole= new ClearNotificationConsole(userNickname, this.isForAllNotifications, previousPage);
		clearNotificationConsole.start();
	}
}
