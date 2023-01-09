package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class CommentConsole implements Console {
	private final static Logger logger = Logger.getLogger(CommentConsole.class.getName());

	private ArrayList<String> userInputs;
	private String userNickname;
	
	public CommentConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		
		welcomePage();
		
		String postPid = getPidFromStandardInput();
		String body = getBodyFromStandardInput();
		
		Util.commentPreview(userNickname, body);
		
		manageCommentConsoleCommand(postPid, body);
	}
	
	private void manageCommentConsoleCommand(String postPid, String body) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(postPid);
		userInputs.add(body);
		
		printAvailableCommands(Page.COMMENT_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.COMMENT_CONSOLE);
			command.run(userInputs,this.userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, CommentConsole.class.getSimpleName(),"manageCommentConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException CommentConsole "+ex);
		}
	}
	
	private String getPidFromStandardInput() {
		String postPid = Parser.getInstance().readCommand("\n Enter the PID of the post you want to comment:");
		
		//UI preventive checks for better user experience
		while (postPid.length()<6 || postPid.length()>6) {
			System.out.println("<<Sorry, wrong PID! It must be exactly 6 number long.>>\n");
			logger.log(Level.INFO, "[getPidFromStandardInput] - The PID entered is of wrong lenght");
			
			printAvailableCommandsCommentError(Page.COMMENT_CONSOLE);
			manageCommentErrorCommands();
		}
		
		while (!Whistler.getInstance().isPidPresentAndRelativeToPublicPost(postPid)) {    //while postPid is not present or it's not relative to a public post
			Post p = Whistler.getInstance().getPost(postPid);
			
			if(p!=null) {
				String postOwner = p.getOwner();
			
				if(this.userNickname.equals(postOwner)) {
					System.out.println("<<Sorry, you have to make post ("+postPid+") PUBLIC to be able to comment that!>>\n");
				}else {
					System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
				}
			}
			
			logger.log(Level.WARNING, "[getPidFromStandardInput] - The PID entered is not present on Whistler or it's relative to a Private Post");
			System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
			
			printAvailableCommandsCommentError(Page.COMMENT_CONSOLE);
			manageCommentErrorCommands();
		}
		return postPid;
	}
	
	private String getBodyFromStandardInput() {
		String body = Parser.getInstance().readCommand("\n Enter comment's Body:");
		
		//UI preventive checks for better user experience
		//check that the body isn't over 280 characters 
		while (body.length()>280) {
			System.out.println("<<Sorry the body exceeded 280 characters. Retry...>>\n");
			System.out.println("<<Reporting body to copy and resize:\n");
			System.out.println(Util.padRight(body.substring(0, 70), 73));
			System.out.println(Util.padRight(body.substring(70, 140), 73));
			System.out.println(Util.padRight(body.substring(140, 210), 73));
			System.out.println(Util.padRight(body.substring(210, 280), 73));
			System.out.println(Util.padRight(body.substring(280, body.length()), 73));
			System.out.println(Util.padRight("...", 73));
			logger.log(Level.INFO, "[getBodyFromStandardInput] - The body exceeded 280 characters");
			
			printAvailableCommandsCommentError(Page.COMMENT_CONSOLE);
			manageCommentErrorCommands();
		}
		
		return body;
	}
	
	private void manageCommentErrorCommands(){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageCommentErrorCommands] - CommentConsole turn back to HomeConsole");
						command = new TurnBackCommand(Page.COMMENT_CONSOLE);
						command.run(userInputs, this.userNickname,null);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[manageCommentErrorCommands] - CommentConsole (Retry)");
						String postPid = getPidFromStandardInput();
						String body = getBodyFromStandardInput();
						manageCommentConsoleCommand(postPid,body);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, CommentConsole.class.getSimpleName(),"manageCommentErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageCommentErrorCommands();
        	}
	}	
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗        \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║     \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║     \n"
			   +" ╚════════════════════════════════╝        \n");
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
	

	public void welcomePage() {
		System.out.println(
				  " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                            ╦╔╗╔╔═╗╔═╗╦═╗╔╦╗  ╔╗╔╔═╗╦ ╦  ╔═╗╔═╗╔╦╗╔╦╗╔═╗╔╗╔╔╦╗                                       \n"
				+ "                            ║║║║╚═╗║╣ ╠╦╝ ║   ║║║║╣ ║║║  ║  ║ ║║║║║║║║╣ ║║║ ║                                        \n"
			    + "                            ╩╝╚╝╚═╝╚═╝╩╚═ ╩   ╝╚╝╚═╝╚╩╝  ╚═╝╚═╝╩ ╩╩ ╩╚═╝╝╚╝ ╩                                        \n"
				+ "                           ╚══════════════════════════════════════════════════╝                                      \n");
	}
}
