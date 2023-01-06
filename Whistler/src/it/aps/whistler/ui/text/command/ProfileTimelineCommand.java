package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.ProfileTimelineConsole;

public class ProfileTimelineCommand implements Command {
	
	public String getCommandDescription() {
		String descripition = "ProfileTimelineCommand takes to ProfileTimelineConsole which shows all user's public and private posts!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console profileTimelineConsole;
		
		if (enteredInputs.isEmpty()) {
																	//isOwner = true	whistleblowerNickname = null	
			profileTimelineConsole= new ProfileTimelineConsole(userNickname,true, null); 
		}else {			
																		//isOwner = false
			profileTimelineConsole = new ProfileTimelineConsole(userNickname,false, enteredInputs.get(0)); //enteredInputs.get(0) is whistleblowerNickname
		}
		
		profileTimelineConsole.start();
	}
}
