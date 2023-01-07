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

public class EditCommentConsole implements Console {
	private final static Logger logger = Logger.getLogger(EditCommentConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private String postPid;
	private Page previousPage;
	
	public EditCommentConsole(String userNickname, String postPid, Page previousPage) {
		this.userInputs = new ArrayList<String>();
		this.userNickname = userNickname;
		this.postPid = postPid;
		this.previousPage = previousPage;
	}
	
	public void start() {
		welcomePage();
		
		String commentCid = getCommentCidFromStandardInput();
		System.out.println("<<Ok, you are allowed to edit this comment>>");
		String body =  getNewBody(commentCid);
		
		manageEditCommentConsoleCommand(commentCid, body);
	
	}
	
	private void manageEditCommentConsoleCommand(String commentCid, String body) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(this.postPid);
		userInputs.add(commentCid);
		userInputs.add(body);
		
		printAvailableCommands(Page.EDIT_COMMENT_CONSOLE);
		
		try {
			
			command= Parser.getInstance().getCommand(Page.EDIT_COMMENT_CONSOLE);
			command.run(userInputs,this.userNickname,this.previousPage);
			
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, EditCommentConsole.class.getSimpleName(),"manageEditCommentConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException EditCommentConsole "+ex);
		}
	}
	
	private void manageEditCommentConsoleCommandError() {
		Command command;
		userInputs.clear();
		
		int choice; 
		try {
			choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
			
				case EXIT:
						logger.log(Level.INFO, "[manageEditCommentConsoleCommandError] - EditCommentConsole turn back to HomeConsole");
						userInputs.add(this.postPid);
						command = new TurnBackCommand(Page.EDIT_COMMENT_CONSOLE);
						command.run(userInputs,this.userNickname,this.previousPage);
						break;
						
				case RETRY:
						logger.log(Level.INFO, "[manageEditCommentConsoleCommandError] - EditCommentConsole (Retry)");
						String commentCid = getCommentCidFromStandardInput();
						System.out.println("<<Ok, you are allowed to edit this comment>>");
						String body =  getNewBody(commentCid); 
						manageEditCommentConsoleCommand(commentCid,body);
						break;
			}
			
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, EditCommentConsole.class.getSimpleName(),"manageEditCommentConsoleCommandError","("+userNickname+")"+" Command entered not in digit format or out of bounds: "+ex);
        	manageEditCommentConsoleCommandError();
        	}
	}
	
	private String getCommentCidFromStandardInput() {
		String commentCid = Parser.getInstance().readCommand("\n Enter comment's CID you wish to edit:");
		
		Comment c = Whistler.getInstance().getComment(commentCid);
		
		while (c==null || c.getCommentVisibility().equals(Visibility.PRIVATE)) {
			System.out.println("<<Sorry wrong CID, no comment has this CID on Whistler. Retry or come back to Home.>>");
			logger.logp(Level.INFO, EditCommentConsole.class.getSimpleName(),"getCommentCidFromStandardInput", userNickname+" entered wrong CID, no comment has this CID on Whistler");
			
			printAvailableCommandsEditCommentError(Page.EDIT_COMMENT_CONSOLE);
			manageEditCommentConsoleCommandError();
		}
		
		while(!c.getOwner().equals(this.userNickname)) {
			System.out.println("<<You can't edit comment of other users! Retry or come back to Home.>>");
			logger.logp(Level.INFO, EditCommentConsole.class.getSimpleName(),"getCommentCidFromStandardInput", userNickname+" has entered CID of a comment that does not belong to him");
			
			printAvailableCommandsEditCommentError(Page.EDIT_COMMENT_CONSOLE);
			manageEditCommentConsoleCommandError();
		}
		
		return commentCid;
	}
	
	private String getNewBody(String commentCid){
		
		String newBody = Parser.getInstance().readCommand(" Enter new Body:");
		
		//check that the new body isn't over 280 characters 
		while (newBody.length()>280) {
			System.out.println("<<Sorry the body exceeded 280 characters. Retry...>>\n");
			System.out.println("<<Reporting body to copy and resize:\n");
			System.out.println(Util.padRight(newBody.substring(0, 70), 73));
			System.out.println(Util.padRight(newBody.substring(70, 140), 73));
			System.out.println(Util.padRight(newBody.substring(140, 210), 73));
			System.out.println(Util.padRight(newBody.substring(210, 280), 73));
			System.out.println(Util.padRight(newBody.substring(280, newBody.length()), 73));
			System.out.println(Util.padRight("...", 73));
			logger.log(Level.INFO, "[getNewBodyFromStandardInput] - The body exceeded 280 characters");
			
			printAvailableCommandsEditCommentError(Page.EDIT_COMMENT_CONSOLE);
			manageEditCommentConsoleCommandError();
		}				
				
		return newBody;
	} 

	public void welcomePage() {
		System.out.println(
				  " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                     ╔═╗╔╦╗╦╔╦╗  ╔═╗╔═╗╔╦╗╔╦╗╔═╗╔╗╔╔╦╗                                      \n"
				+ "                                     ║╣  ║║║ ║   ║  ║ ║║║║║║║║╣ ║║║ ║                                       \n"
			    + "                                     ╚═╝═╩╝╩ ╩   ╚═╝╚═╝╩ ╩╩ ╩╚═╝╝╚╝ ╩                                       \n"
				+ "                                     ╚═══════════════════════════════╝                                      \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗ \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║  \n"
			   +" ║  "+Util.padRight(commands[1],34)+"║  \n"
			   +" ╚════════════════════════════════════╝ \n");
	}
	
	private void printAvailableCommandsEditCommentError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗ \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║  \n"
			   +" ║  "+Util.padRight("1:Retry",34)+  "║  \n"
			   +" ╚════════════════════════════════════╝ \n");
	}
}
