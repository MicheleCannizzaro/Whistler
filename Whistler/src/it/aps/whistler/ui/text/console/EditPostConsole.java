package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class EditPostConsole implements Console {
	private final static Logger logger = Logger.getLogger(EditPostConsole.class.getName());
	
	private enum editPostField{ EXIT, TITLE, BODY, KEYWORDS, POST_VISIBILITY }
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public EditPostConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		String postPid = getPostPidFromStandardInput();
		System.out.println("<<Ok, you are allowed to edit this post>>");
		ArrayList<String> inputs =  getNewFieldValueToEdit(postPid);
		
		manageEditPostConsoleCommand(postPid, inputs);
	
	}
	
	private void manageEditPostConsoleCommand(String postPid, ArrayList<String> inputs) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(postPid);
		userInputs.addAll(inputs);
		
		printAvailableCommands(Page.EDIT_POST_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.EDIT_POST_CONSOLE);
			command.run(userInputs,this.userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, EditPostConsole.class.getSimpleName(),"manageEditPostConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException EditPostConsole "+ex);
		}
	}
	
	private void manageEditPostConsoleCommandError() {
		Command command;
		userInputs.clear();
		
		int choice; 
		try {
			choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
			
				case EXIT:
						logger.log(Level.INFO, "[manageEditPostConsoleCommandError] - EditPostConsole turn back to ProfileConsole");
						command = new TurnBackCommand(Page.EDIT_POST_CONSOLE);
						command.run(userInputs,this.userNickname,null);
						break;
						
				case RETRY:
						logger.log(Level.INFO, "[manageEditPostConsoleCommandError] - EditPostConsole (Retry)");
						String postPid = getPostPidFromStandardInput();
						System.out.println("<<Ok, you are allowed to edit this post>>");
						ArrayList<String> inputs =  getNewFieldValueToEdit(postPid); 
						manageEditPostConsoleCommand(postPid,inputs);
						break;
			}
			
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, EditPostConsole.class.getSimpleName(),"manageEditPostConsoleCommandError","("+userNickname+")"+" Command entered not in digit format or out of bounds: "+ex);
        	manageEditPostConsoleCommandError();
        	}
	}
	
	private String getPostPidFromStandardInput() {
		String postPid = Parser.getInstance().readCommand("\n Enter post's PID you wish to edit:");
		
		Post p = Whistler.getInstance().getPost(postPid);
		
		while (p==null) {
			System.out.println("<<Sorry wrong PID, no post has this PID on Whistler. Retry or come back to Profile.>>");
			logger.logp(Level.INFO, EditPostConsole.class.getSimpleName(),"getPostPidFromStandardInput", userNickname+" entered wrong PID, no post has this PID on Whistler");
			
			printAvailableCommandsEditPostError(Page.EDIT_POST_CONSOLE);
			manageEditPostConsoleCommandError();
		}
		
		while(!p.getOwner().equals(this.userNickname)) {
			System.out.println("<<You can't edit post of other users! Retry or come back to Profile.>>");
			logger.logp(Level.INFO, EditPostConsole.class.getSimpleName(),"getPostPidFromStandardInput", userNickname+" has entered PID of a post that does not belong to him");
			
			printAvailableCommandsEditPostError(Page.EDIT_POST_CONSOLE);
			manageEditPostConsoleCommandError();
		}
		
		return postPid;
	}
	
	private ArrayList<String> getNewFieldValueToEdit(String postPid){
		ArrayList<String> fields = new ArrayList<>();
		System.out.println("\n Choose one of this field to edit:");
		System.out.println(" [Commands]: \"0:Exit\" - \"1:Title\" - \"2:Body\" - \"3:Keywords\" - \"4:Post Visibility\"");
		
		try {
			int fieldToEdit = Integer.parseInt(Parser.getInstance().readCommand("\n Which Field you want to modify?"));
			fields.add(String.valueOf(fieldToEdit));  //adding choice made to store the field to edit
			
			switch(editPostField.values()[fieldToEdit]) {
				case EXIT: 
					logger.log(Level.INFO, "[getNewFieldValueToEdit] - EditPostConsole turn back to ProfileTimeline");
					Command command = new TurnBackCommand(Page.EDIT_POST_CONSOLE);
					userInputs.clear();
					command.run(userInputs,this.userNickname,null);
					break;
				case TITLE: 
						String newTitle = getTitleFromStandardInput();
						fields.add(newTitle);
						break;
						
				case BODY: 
						String newBody = getBodyFromStandardInput();
						fields.add(newBody);
						break;
						
				case KEYWORDS:
						ArrayList<String> postKeywordsFromInput = new ArrayList<>();
						
						//Gather maximum 3 keywords
						for (int i =0; i <3; i++) {
							String keyword = "#"+Parser.getInstance().readCommand("\n Enter new Keyword n°"+(i+1));
							
							while(keyword.length()>21) {
								System.out.println("<<Sorry, keyword length must not exceed 20 characters! Please try again!>>\n");
								keyword = "#"+Parser.getInstance().readCommand("\n Enter Keyword n°"+(i+1));
							}
							
							if (!keyword.equals("#")) {   //if keyword it's not blank
								if(!postKeywordsFromInput.contains(keyword)) {
									postKeywordsFromInput.add(keyword);
								}else{
									System.out.println("<<You already entered \""+keyword+"\"! Keyword n°"+(i+1)+" will be blank>>\n");
								}
							}
						}
						
						fields.addAll(postKeywordsFromInput);
						break;
						
				case POST_VISIBILITY: 
						System.out.println("<<In order to set privacy policy for this post ->  Enter \"0:PUBLIC\" or \"1:PRIVATE\" >>\n");
						String postVisibility = Parser.getInstance().readCommand(" What's the new post privacy policy?");
						
						if (postVisibility!=null) {
							
							while (!postVisibility.equals("0") && !postVisibility.equals("1")) {
								System.out.println("<<Sorry, \""+postVisibility+"\" it's not a valid option! Please Retry!>>\n");
								postVisibility = Parser.getInstance().readCommand(" What's the new post privacy policy?");
							}
							
							fields.add(postVisibility);
						}
						break;
			}
			
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
			
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, EditPostConsole.class.getSimpleName(),"getNewFieldValueToEdit","("+userNickname+")"+" Command entered not in digit format or out of bounds: "+ex);
    		ArrayList<String> inputs =  getNewFieldValueToEdit(postPid);
    		manageEditPostConsoleCommand(postPid, inputs);
        }
		
		return fields;
	} 
	
	public String getTitleFromStandardInput() {
		String title = Parser.getInstance().readCommand(" Enter post's Title:");
		
		//UI preventive checks for better user experience
		//check that the title isn't over 70 characters 
		while(title.length()>70) {
			System.out.println("<<Sorry, title length can't be higher than 70 characters, the title entered was "+title.length()+" characters long! Please Retry!>>\n");
			System.out.println("<<Reporting title to copy and resize:\n");
			System.out.println(Util.padRight(title.substring(0, 70), 73));
			System.out.println(Util.padRight("...", 73));
			logger.log(Level.INFO, "[getTitleFromStandardInput] - The title exceeded 70 characters");
			
			printAvailableCommandsEditPostError(Page.EDIT_POST_CONSOLE);
			manageEditPostConsoleCommandError();
		}
		
		return title;
	}
	
	public String getBodyFromStandardInput() {
		String body = Parser.getInstance().readCommand("\n Enter post's Body:");
		
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
			
			printAvailableCommandsEditPostError(Page.EDIT_POST_CONSOLE);
			manageEditPostConsoleCommandError();
		}
		
		return body;
	}

	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                        ╔═╗╔╦╗╦╔╦╗  ╔═╗╔═╗╔═╗╔╦╗                                                     \n"
				+ "                                        ║╣  ║║║ ║   ╠═╝║ ║╚═╗ ║                                                      \n"
			    + "                                        ╚═╝═╩╝╩ ╩   ╩  ╚═╝╚═╝ ╩                                                      \n"
				+ "                                       ╚════════════════════════╝                                                    \n");
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
	
	private void printAvailableCommandsEditPostError(Page page) {
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
