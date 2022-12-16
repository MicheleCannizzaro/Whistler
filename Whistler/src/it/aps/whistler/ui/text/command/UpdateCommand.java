package it.aps.whistler.ui.text.command;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.ProfileTimelineConsole;

import java.util.ArrayList;


public class UpdateCommand implements Command{
	
	private Page page;
	
	public UpdateCommand(Page page){
		this.page=page;
	}
	
	public String getCommandDescription() {
		String descripition = "UpdateCommand let you update the console";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
				
		if (this.page == Page.HOME_CONSOLE) {
			Console homeConsole= new HomeConsole(userNickname);
			homeConsole.start();
		}
		
		if (this.page == Page.PROFILE_TIMELINE_CONSOLE) {
			Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true ,null); //isOwner == true
			profileTimelineConsole.start();
		}
		
		if (this.page == Page.ACCOUNT_TIMELINE_CONSOLE) {
			Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,false ,enteredInputs.get(0)); //isOwner == false enteredInputs.get(0) = whistleblowerNickname
			profileTimelineConsole.start();
		}
	}

}
