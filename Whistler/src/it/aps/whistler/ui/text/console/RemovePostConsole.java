package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.domain.Post;
import it.aps.whistler.persistence.dao.PostDao;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class RemovePostConsole implements Console {
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public RemovePostConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		String postPid = getPostPidFromStandardInput();
		manageRemovePostConsoleCommand(postPid);
		
	}
	
	private String getPostPidFromStandardInput() {
		String postPid = Parser.getInstance().readCommand("\n Enter post's PID you wish to edit:");
		
		Post p = PostDao.getInstance().getPostByPid(postPid);
		
		while (p==null) {
			System.out.println("<<Sorry wrong PID, no post has this PID on Whistler. Retry or come back to Profile.>>");
			
			printAvailableCommandsRemovePostError(Page.REMOVE_POST_CONSOLE);
			manageRemovePostConsoleCommandError();
		}
		
		while(!p.getOwner().equals(this.userNickname)) {
			System.out.println("<<You can't remove post of other users! Retry or come back to Profile.>>");
			
			printAvailableCommandsRemovePostError(Page.REMOVE_POST_CONSOLE);
			manageRemovePostConsoleCommandError();
		}
		return postPid;
	}
	
	private void manageRemovePostConsoleCommand(String postPid) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(postPid);
		
		printAvailableCommands(Page.REMOVE_POST_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.REMOVE_POST_CONSOLE);
			command.run(userInputs,this.userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException RemovePostConsole "+ex);
		}
	}
	
	private void manageRemovePostConsoleCommandError() {
		Command command;
		userInputs.clear();
		
		int choice; 
		try {
			choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						command = new TurnBackCommand(Page.PROFILE_TIMELINE_CONSOLE);
						command.run(userInputs,this.userNickname);
						break;
				case RETRY: 
						String postPid = getPostPidFromStandardInput();
						manageRemovePostConsoleCommand(postPid);
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	manageRemovePostConsoleCommandError();
        	}
	}

	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                           ╦═╗╔═╗╔╦╗╔═╗╦  ╦╔═╗  ╔═╗╔═╗╔═╗╔╦╗                                         \n"
				+ "                                           ╠╦╝║╣ ║║║║ ║╚╗╔╝║╣   ╠═╝║ ║╚═╗ ║                                          \n"
			    + "                                           ╩╚═╚═╝╩ ╩╚═╝ ╚╝ ╚═╝  ╩  ╚═╝╚═╝ ╩                                          \n"
				+ "                                          ╚═════════════════════════════════╝                                        \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],34)+"║    \n"
			   +" ╚════════════════════════════════════╝       \n");
	}
	
	private void printAvailableCommandsRemovePostError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗   \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║  \n"
			   +" ║  "+Util.padRight("1:Retry",34)+  "║  \n"
			   +" ╚════════════════════════════════════╝   \n");
	}
}
