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

public class LikeConsole implements Console {
	private final static Logger logger = Logger.getLogger(LikeConsole.class.getName());

	private ArrayList<String> userInputs;
	private String userNickname;
	private boolean isLike;
	private Page previousPage;
	private Page currentPage;
	private String userInputPreviousPage;
	
	public LikeConsole(String userNickname, Page previousPage, boolean isLike, String userInputPreviousPage) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.previousPage = previousPage;
		this.isLike = isLike;
		this.userInputPreviousPage = userInputPreviousPage;
	}
	
	public void start() {
		
		if(this.isLike) {
			this.currentPage = Page.LIKE_CONSOLE;
		}else {
			this.currentPage = Page.DISLIKE_CONSOLE;
		}
		
		welcomePage();
		
		String postPid = getPidFromStandardInput();
		
		manageLikeConsoleCommand(postPid);
	}
	
	private void manageLikeConsoleCommand(String postPid) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(postPid);
		
		if(this.userInputPreviousPage!=null) {
			userInputs.add(this.userInputPreviousPage);
		}
		
		printAvailableCommands(this.currentPage);
		
		try {
			command= Parser.getInstance().getCommand(this.currentPage);
			command.run(userInputs,this.userNickname,this.previousPage);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, LikeConsole.class.getSimpleName(),"manageLikeConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException LikeConsole "+ex);
		}
	}
	
	private String getPidFromStandardInput() {
		String postPid = Parser.getInstance().readCommand("\n Enter the PID of the post you want to comment:");
		
		//UI preventive checks for better user experience
		while (postPid.length()<6 || postPid.length()>6) {
			System.out.println("<<Sorry, wrong PID! It must be exactly 6 number long.>>\n");
			logger.log(Level.INFO, "[getPidFromStandardInput] - The PID entered is of wrong lenght");
			
			
			printAvailableCommandsLikeError(this.currentPage);
			manageLikeErrorCommands();
		}
		
		while (!Whistler.getInstance().isPidPresentAndRelativeToPublicPost(postPid)) {    //while postPid is not present or it's not relative to a public post
			Post p = Whistler.getInstance().getPost(postPid);
			
			if(p!=null) {
				String postOwner = p.getOwner();
			
				if(this.userNickname.equals(postOwner)) {
					System.out.println("<<Sorry, you have to make post ("+postPid+") PUBLIC to be able to Like/Dislike that!>>\n");
				}else {
					System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
				}
			}
			
			logger.log(Level.WARNING, "[getPidFromStandardInput] - The PID entered is not present on Whistler or it's relative to a Private Post");
			System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
			
			printAvailableCommandsLikeError(this.currentPage);
			manageLikeErrorCommands();
		}
		return postPid;
	}
	
	private void manageLikeErrorCommands(){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[manageLikeErrorCommands] - LikeConsole turn back");
						
						if(this.userInputPreviousPage!=null) {
							userInputs.add(this.userInputPreviousPage);
						}
						
						command = new TurnBackCommand(this.currentPage);
						command.run(userInputs, this.userNickname,this.previousPage);
						break;
						
				case RETRY: 
						logger.log(Level.INFO, "[manageLikeErrorCommands] - LikeConsole (Retry)");
						String postPid = getPidFromStandardInput();
						manageLikeConsoleCommand(postPid);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, LikeConsole.class.getSimpleName(),"manageLikeErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageLikeErrorCommands();
        	}
	}	
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║    \n"
			   +" ╚════════════════════════════════╝       \n");
	}
	
	private void printAvailableCommandsLikeError(Page page) {
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
		if(this.isLike) {
		System.out.println(
				  " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                              ╦  ╦╦╔═╔═╗                                                    \n"
				+ "                                              ║  ║╠╩╗║╣                                                     \n"
				+ "                                              ╩═╝╩╩ ╩╚═╝                                                    \n"
				+ "                                             ╚══════════╝                                                   \n");
		}else {
			System.out.println(
				  " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			System.out.println(
				  "                                           ╔╦╗╦╔═╗╦  ╦╦╔═╔═╗                                                \n"
			    + "                                            ║║║╚═╗║  ║╠╩╗║╣                                                 \n"
				+ "                                           ═╩╝╩╚═╝╩═╝╩╩ ╩╚═╝                                                \n"
				+ "                                          ╚═════════════════╝                                               \n");
		}
	}
} 
