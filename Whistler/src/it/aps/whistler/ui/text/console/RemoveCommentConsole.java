package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Comment;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class RemoveCommentConsole implements Console {
	private final static Logger logger = Logger.getLogger(RemoveCommentConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private String postPid;
	private Page previousPage;
	
	public RemoveCommentConsole(String userNickname, String postPid, Page previousPage) { 
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.postPid = postPid;
		this.previousPage = previousPage;
	}
	
	public void start() {
		welcomePage();
		
		String commentCid = getCommentCidFromStandardInput();
		manageRemoveCommentConsoleCommand(commentCid);
		
	}
	
	private String getCommentCidFromStandardInput() {
		String commentCid = Parser.getInstance().readCommand("\n Enter comment's CID you wish to remove:");
		
		Comment c = Whistler.getInstance().getComment(commentCid);
		
		while (c==null || c.getCommentVisibility().equals(Visibility.PRIVATE)) {
			System.out.println("<<Sorry wrong CID, no public comment has this CID on Whistler. Retry or come back to Home.>>");
			
			printAvailableCommandsRemoveCommentError(Page.REMOVE_COMMENT_CONSOLE);
			manageRemoveCommentConsoleCommandError();
		}
		
		while(!c.getOwner().equals(this.userNickname)) {
			System.out.println("<<You can't remove comment of other users! Retry or come back to Home.>>");
			
			printAvailableCommandsRemoveCommentError(Page.REMOVE_COMMENT_CONSOLE);
			manageRemoveCommentConsoleCommandError();
		}
		return commentCid;
	}
	
	private void manageRemoveCommentConsoleCommand(String commentCid) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(this.postPid);
		userInputs.add(commentCid);
		
		printAvailableCommands(Page.REMOVE_COMMENT_CONSOLE);
		
		try {
			
			command= Parser.getInstance().getCommand(Page.REMOVE_COMMENT_CONSOLE);
			command.run(userInputs,this.userNickname,this.previousPage);
			
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, RemoveCommentConsole.class.getSimpleName(),"manageRemoveCommentConsoleCommand","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException RemoveCommentConsole "+ex);
		}
	}
	
	private void manageRemoveCommentConsoleCommandError() {
		Command command;
		userInputs.clear();
		
		int choice; 
		try {
			choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageRemoveCommentConsoleCommandError] - RemoveCommentConsole turn back to HomeConsole");
						userInputs.add(this.postPid);
						command = new TurnBackCommand(Page.REMOVE_COMMENT_CONSOLE);
						command.run(userInputs,this.userNickname,this.previousPage);
						break;
				case RETRY:
						logger.log(Level.INFO, "[manageRemoveCommentConsoleCommandError] - RemoveCommentConsole (Retry)");
						String commentCid = getCommentCidFromStandardInput();
						manageRemoveCommentConsoleCommand(commentCid);
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, RemoveCommentConsole.class.getSimpleName(),"manageRemoveCommentConsoleCommandError","("+userNickname+")"+" Command entered not in digit format or out of bounds: "+ex);
        	manageRemoveCommentConsoleCommandError();
        	}
	}

	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                      ╦═╗╔═╗╔╦╗╔═╗╦  ╦╔═╗  ╔═╗╔═╗╔╦╗╔╦╗╔═╗╔╗╔╔╦╗                                     \n"
				+ "                                      ╠╦╝║╣ ║║║║ ║╚╗╔╝║╣   ║  ║ ║║║║║║║║╣ ║║║ ║                                      \n"
			    + "                                      ╩╚═╚═╝╩ ╩╚═╝ ╚╝ ╚═╝  ╚═╝╚═╝╩ ╩╩ ╩╚═╝╝╚╝ ╩                                      \n"
				+ "                                     ╚═════════════════════════════════════════╝                                     \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║        \n"
			   +" ║  "+Util.padRight(commands[1],34)+"║        \n"
			   +" ╚════════════════════════════════════╝       \n");
	}
	
	private void printAvailableCommandsRemoveCommentError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗   \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║    \n"
			   +" ║  "+Util.padRight("1:Retry",34)+  "║    \n"
			   +" ╚════════════════════════════════════╝   \n");
	}
}
