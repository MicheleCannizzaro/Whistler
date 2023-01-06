package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Comment;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.EditCommentCommand;
import it.aps.whistler.ui.text.command.RemoveCommentCommand;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class ShowPostCommentsConsole implements Console{
	private final static Logger logger = Logger.getLogger(ShowPostCommentsConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private String postPid;
	private Page previousPage;
	private ArrayList<Comment> comments = new ArrayList<>();
	
	public ShowPostCommentsConsole(String userNickname, ArrayList<String> userInputs, Page previousPage) {
		this.userInputs = userInputs;
		this.userNickname = userNickname;
		this.previousPage = previousPage;
	}
		
	public void start() {
		welcomePage();
		
		try {
			if(!userInputs.get(0).isEmpty()) {
				this.postPid = userInputs.get(0);
			}
		}catch(java.lang.IndexOutOfBoundsException ex) {
			this.postPid = getPidFromStandardInput();
		}
		
		showComments();
		
		manageCommentConsoleCommand();
	}
	
	private void manageCommentConsoleCommand() {
		Command command;
		
		if(this.comments.isEmpty()) {
			printOnlyExitCommandCommentError(Page.SHOW_POST_COMMENTS_CONSOLE);
		}else {
			printAvailableCommands(Page.SHOW_POST_COMMENTS_CONSOLE);
		}
		
		try {
			command= Parser.getInstance().getCommand(Page.SHOW_POST_COMMENTS_CONSOLE);
			
			if(this.comments.isEmpty()) {
				if(command.getClass().equals(EditCommentCommand.class) || command.getClass().equals(RemoveCommentCommand.class)) {
					throw new java.lang.ArrayIndexOutOfBoundsException("Throwing ArrayIndexOutOfBoundsException ShowPostComment \"OnlyExit\"");
				}
			}
			
			if(command.getClass().equals(TurnBackCommand.class)) {
				command.run(userInputs,this.userNickname, this.previousPage);
			}else {
				userInputs.add(this.postPid);
				command.run(userInputs,this.userNickname,this.previousPage);
			}
			
		}catch(java.lang.NullPointerException ex){
			
			logger.logp(Level.WARNING, ShowPostCommentsConsole.class.getSimpleName(),"manageCommentConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException CommentConsole "+ex);
			
		}catch(java.lang.ArrayIndexOutOfBoundsException ex){
			
			System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, ShowPostCommentsConsole.class.getSimpleName(),"manageCommentErrorCommands","(OnlyExit) Command entered not in digit format or out of bounds: "+ex);
        	
        	manageCommentConsoleCommand();
		}
	}
	
	public String getPidFromStandardInput() {
		String postPid = Parser.getInstance().readCommand("\n Enter the PID of the post you want Whistler to show comments:");
		
		//UI preventive checks for better user experience
		while (postPid.length()<6 || postPid.length()>6) {
			System.out.println("<<Sorry, wrong PID! It must be exactly 6 number long.>>\n");
			logger.log(Level.INFO, "[getPidFromStandardInput] - The PID entered is of wrong lenght");
			
			printAvailableCommandsCommentError(Page.SHOW_POST_COMMENTS_CONSOLE);
			manageCommentErrorCommands();
		}
		
		while (!Whistler.getInstance().isPidPresentAndRelativeToPublicPost(postPid)) {    //while postPid is not present or it's not relative to a public post
			Post p = Whistler.getInstance().getPost(postPid);
			
			if(p!=null) {
				String postOwner = p.getOwner();
				
				if(this.userNickname.equals(postOwner)) {
					System.out.println("<<Sorry, you have to make post ("+postPid+") PUBLIC to be able to show its comments!>>\n");
				}else {
					System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
				}
			}
			
			logger.log(Level.WARNING, "[getPidFromStandardInput] - The PID entered is not present on Whistler or it's relative to a Private Post");
			System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
			
			printAvailableCommandsCommentError(Page.SHOW_POST_COMMENTS_CONSOLE);
			manageCommentErrorCommands();
		}
		return postPid;
		
	}
	
	private void manageCommentErrorCommands(){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageCommentErrorCommands] - ShowPostCommentsConsole turn back to HomeConsole");
						command = new TurnBackCommand(Page.SHOW_POST_COMMENTS_CONSOLE);
						command.run(userInputs, this.userNickname,this.previousPage);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[manageCommentErrorCommands] - ShowPostCommentsConsole (Retry)");
						this.postPid = getPidFromStandardInput();
						showComments();
						manageCommentConsoleCommand();	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, ShowPostCommentsConsole.class.getSimpleName(),"manageCommentErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageCommentErrorCommands();
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
				+" ╚════════════════════════════════╝      \n");
	}
	
	private void printAvailableCommandsCommentError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════╗     \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║  \n"
			   +" ║  "+Util.padRight("1:Retry",30)+  "║  \n"
			   +" ╚════════════════════════════════╝     \n");
	}
	
	private void printOnlyExitCommandCommentError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════╗     \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║  \n"
			   +" ╚════════════════════════════════╝     \n");
	}
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                                     ╔═╗╔═╗╔╦╗╔╦╗╔═╗╔╗╔╔╦╗╔═╗                                               \n"
						 + "                                     ║  ║ ║║║║║║║║╣ ║║║ ║ ╚═╗                                               \n"
				     	 + "                                     ╚═╝╚═╝╩ ╩╩ ╩╚═╝╝╚╝ ╩ ╚═╝                                               \n"
				         + "                                                                                                            \n"
				         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
				         + "                                                                                                            \n");
	}

	private void showComments() {
		
		if (this.postPid !=null) {
			comments = Whistler.getInstance().getPost(postPid).getAllPostComments();
		}
		
		if(!comments.isEmpty()) {
			//Sorting Comments in Reverse Chronological Order
			Collections.sort(comments, Comparator.comparing(Comment::getTimestamp).reversed());
			
			//printing comments 
			for (Comment c : comments) {	
				Util.printDetailedComment(c);
			}
			
		}else {
			System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			System.out.println(Util.padLeft("This post does not contain any comments yet\n", 65));
		}
	}
	
	
}
