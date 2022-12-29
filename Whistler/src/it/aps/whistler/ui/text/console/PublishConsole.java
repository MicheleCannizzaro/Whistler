package it.aps.whistler.ui.text.console;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.TurnBackCommand;
import it.aps.whistler.util.Util;

public class PublishConsole implements Console {
	private final static Logger logger = Logger.getLogger(PublishConsole.class.getName());

	private ArrayList<String> userInputs;
	private String userNickname;
	
	public PublishConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		
		String title = getTitleFromStandardInput();
		String body = getBodyFromStandardInput();
		ArrayList<String> postKeywordsFromInput = getPostKeywordsFromStandardInput();
		String postVisibility = getPostVisibility();
		
		managePostConsoleCommand(title,body,postVisibility,postKeywordsFromInput);
	}
	
	private void managePostConsoleCommand(String title, String body, String postVisibility, ArrayList<String> postKeywordsFromInput) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(title);
		userInputs.add(body);
		userInputs.add(postVisibility);
		userInputs.addAll(postKeywordsFromInput);
		
		Util.postPreview(title, body, postKeywordsFromInput, postVisibility);
		
		printAvailableCommands(Page.PUBLISH_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.PUBLISH_CONSOLE);
			command.run(userInputs,this.userNickname,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, PublishConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException PublishConsole "+ex);
		}
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
			
			printAvailableCommandsPostError(Page.PUBLISH_CONSOLE);
			managePostErrorCommands();
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
			
			printAvailableCommandsPostError(Page.PUBLISH_CONSOLE);
			managePostErrorCommands();
		}
		
		return body;
	}
	
	public ArrayList<String> getPostKeywordsFromStandardInput(){
		ArrayList<String> postKeywordsFromInput = new ArrayList<>();
		
		//Gather maximum 3 keywords
		for (int i =0; i <3; i++) {
			String keyword = "#"+Parser.getInstance().readCommand("\n Enter Keyword n°"+(i+1));
			
			while(keyword.length()>21) {
				System.out.println("<<Sorry, keyword length must not exceed 20 characters! Please try again!>>\n");
				keyword = "#"+Parser.getInstance().readCommand("\n Enter Keyword n°"+(i+1));
			}
			
			if (!keyword.equals("#")) {   //if keyword it's not blank
				if(!postKeywordsFromInput.contains(keyword)) {
					postKeywordsFromInput.add(keyword);
				}else{
					System.out.println("<<You already entered \""+keyword+"\"! Keyword n°"+(i+1)+" will be blank>>");
				}
			}
			
		}
		return postKeywordsFromInput;
	}
	
	public String getPostVisibility() {
		String postVisibility = null;
		
		System.out.println("\n<<Your post is almost ready to be published!>>");
		System.out.println("<<In order to set privacy policy for this post ->  Enter \"0:PUBLIC\" or \"1:PRIVATE\" >>\n");
		
		postVisibility = Parser.getInstance().readCommand(" What's the post privacy policy?");
		
		if (postVisibility!=null) {
			
			while (!postVisibility.equals("0") && !postVisibility.equals("1")) {
				System.out.println("<<Sorry, \""+postVisibility+"\" it's not a valid option! Please Retry!>>\n");
				postVisibility = Parser.getInstance().readCommand(" What's the post privacy policy?");
			}
		}
		
		return postVisibility;
	}
	
	private void managePostErrorCommands(){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (PageCommands.Error.values()[choice]) {
				case EXIT: 
						logger.log(Level.INFO, "[managePostErrorCommands] - PublishConsole turn back to HomeConsole");
						command = new TurnBackCommand(Page.PUBLISH_CONSOLE);
						command.run(userInputs, this.userNickname,null);
						break;
				case RETRY: 
						logger.log(Level.INFO, "[managePostErrorCommands] - PublishConsole (Retry)");
						String title = getTitleFromStandardInput();
						String body = getBodyFromStandardInput();
						ArrayList<String> postKeywordsFromInput = getPostKeywordsFromStandardInput();
						String postVisibility = getPostVisibility();
						managePostConsoleCommand(title,body,postVisibility,postKeywordsFromInput);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, PublishConsole.class.getSimpleName(),"managePostErrorCommands","Command entered not in digit format or out of bounds: "+ex);
        	managePostErrorCommands();
        	}
	}	
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗        \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║    \n"
			   +" ╚════════════════════════════════╝        \n");
	}
	
	private void printAvailableCommandsPostError(Page page) {
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
		System.out.println(
				  "                              ╦╔╗╔╔═╗╔═╗╦═╗╔╦╗  ╔╗╔╔═╗╦ ╦  ╔═╗╔═╗╔═╗╔╦╗                                              \n"
				+ "                              ║║║║╚═╗║╣ ╠╦╝ ║   ║║║║╣ ║║║  ╠═╝║ ║╚═╗ ║                                               \n"
			    + "                              ╩╝╚╝╚═╝╚═╝╩╚═ ╩   ╝╚╝╚═╝╚╩╝  ╩  ╚═╝╚═╝ ╩                                               \n"
				+ "                            ╚══════════════════════════════════════════╝                                             \n");
	}
}
