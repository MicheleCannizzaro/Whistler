package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class ShowPostLikesConsole implements Console{
	private final static Logger logger = Logger.getLogger(ShowPostLikesConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private String postPid;
	private Page previousPage;
	private String userInputPreviousPage;
	private ArrayList<String> likes = new ArrayList<>();
	
	public ShowPostLikesConsole(String userNickname, Page previousPage,String userInputPreviousPage) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.previousPage = previousPage;
		this.userInputPreviousPage = userInputPreviousPage;
	}
		
	public void start() {
		welcomePage();
		
		try {
			if(!userInputs.get(0).isEmpty()) {  //userInputs.get(0) -> "postPid"
				this.postPid = userInputs.get(0);
			}
		}catch(java.lang.IndexOutOfBoundsException ex) {
			this.postPid = getPidFromStandardInput();
		}
		
		showLikes();
		
		manageLikeConsoleCommand();
	}
	
	private void manageLikeConsoleCommand() {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		
		if(this.userInputPreviousPage!=null) {
			userInputs.add(this.userInputPreviousPage);
		}
		
		printAvailableCommands(Page.SHOW_POST_LIKES_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.SHOW_POST_LIKES_CONSOLE);
			command.run(userInputs,this.userNickname, this.previousPage);
			
		}catch(java.lang.NullPointerException ex){
			
			logger.logp(Level.WARNING, ShowPostLikesConsole.class.getSimpleName(),"manageLikeConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException ShowPostLikesConsole "+ex);
			
		}catch(java.lang.ArrayIndexOutOfBoundsException ex){
			
			System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, ShowPostLikesConsole.class.getSimpleName(),"manageLikeConsoleCommand","Command entered not in digit format or out of bounds: "+ex);
        	
        	manageLikeConsoleCommand();
		}
	}
	
	private String getPidFromStandardInput() {
		String postPid = Parser.getInstance().readCommand("\n Enter the PID of the post you want Whistler to show likes:");
		
		//UI preventive checks for better user experience
		while (postPid.length()<6 || postPid.length()>6) {
			System.out.println("<<Sorry, wrong PID! It must be exactly 6 number long.>>\n");
			logger.log(Level.INFO, "[getPidFromStandardInput] - The PID entered is of wrong lenght");
			
			printAvailableCommandsLikeError(Page.SHOW_POST_LIKES_CONSOLE);
			manageLikeErrorCommands();
		}
		
		while (!Whistler.getInstance().isPidPresentAndRelativeToPublicPost(postPid)) {    //while postPid is not present or it's not relative to a public post
			Post p = Whistler.getInstance().getPost(postPid);
			
			if(p!=null) {
				String postOwner = p.getOwner();
				
				if(this.userNickname.equals(postOwner)) {
					System.out.println("<<Sorry, you have to make post ("+postPid+") PUBLIC to be able to show its Likes!>>\n");
				}else {
					System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
				}
			}
			
			logger.log(Level.WARNING, "[getPidFromStandardInput] - The PID entered is not present on Whistler or it's relative to a Private Post");
			System.out.println("<<Sorry, PID:"+postPid+" is not present on Whistler!>>\n");
			
			printAvailableCommandsLikeError(Page.SHOW_POST_LIKES_CONSOLE);
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
						logger.log(Level.INFO, "[manageLikeErrorCommands] - ShowPostLikesConsole turn back");
						
						if(this.userInputPreviousPage!=null) {
							userInputs.add(this.userInputPreviousPage);
						}
						
						command = new TurnBackCommand(Page.SHOW_POST_LIKES_CONSOLE);
						command.run(userInputs, this.userNickname,this.previousPage);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[manageLikeErrorCommands] - ShowPostLikesConsole (Retry)");
						this.postPid = getPidFromStandardInput();
						showLikes();
						manageLikeConsoleCommand();	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, ShowPostLikesConsole.class.getSimpleName(),"manageLikeErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	manageLikeErrorCommands();
        	}
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				" ╔════════════════════════════════╗     \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║  \n"
			   +" ╚════════════════════════════════╝     \n");
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
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                                    ╦  ╦╦╔═╔═╗╔═╗  ╦  ╦╔═╗╔╦╗                                               \n"
						 + "                                    ║  ║╠╩╗║╣ ╚═╗  ║  ║╚═╗ ║                                                \n"
				     	 + "                                    ╩═╝╩╩ ╩╚═╝╚═╝  ╩═╝╩╚═╝ ╩                                                \n"
				         + "                                                                                                            \n"
				         + "        Updated until: "+Util.getTimeString(LocalDateTime.now())+"                                          \n"
				         + "                                                                                                            \n");
	}

	private void showLikes() {
		
		if (this.postPid !=null) {
			likes = Whistler.getInstance().getPost(postPid).getLikes();
		}
		
		if(!likes.isEmpty()) {
			//Sorting likes nickname
			Collections.sort(likes);
			
			System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			//printing likes 
			for (String nickname : likes) {	
				System.out.println(
				 		   			Util.padRight("",43)+"╔════════════════════════╗        \n"                    
				 		   		   +Util.padRight("",43)+"║ "+Util.padRight(nickname, 23)+"║\n"
				 		   		   +Util.padRight("",43)+"╚════════════════════════╝        \n");
			}
			
		}else {
			System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
			System.out.println(Util.padLeft("This post does not contain any like yet\n", 65));
		}
	}
	
	
}
