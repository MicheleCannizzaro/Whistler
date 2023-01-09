package it.aps.whistler.ui.text.console;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpConsole implements Console{
	private final static Logger logger = Logger.getLogger(HelpConsole.class.getName());
	
	private ArrayList<String> userInputs;
	private String userNickname;
	private Page previousPage;
	
	public HelpConsole(String userNickname, Page previousPage) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
		this.previousPage = previousPage;
	}
		
	public void start() {
		welcomePage();
		
		showCommandsList();
		
		manageHelpConsoleCommand();
	}
	
	private void manageHelpConsoleCommand() {
		Command command;
		
		printAvailableCommands(Page.HELP_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.HELP_CONSOLE);
			command.run(userInputs,this.userNickname, this.previousPage);
			
		}catch(java.lang.NullPointerException ex){
			
			logger.logp(Level.WARNING, HelpConsole.class.getSimpleName(),"manageHelpConsoleCommand","("+userNickname+")"+" NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException HelpConsole "+ex);
			
		}catch(java.lang.ArrayIndexOutOfBoundsException ex){
			
			System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	logger.logp(Level.WARNING, HelpConsole.class.getSimpleName(),"manageHelpConsoleCommand","Command entered not in digit format or out of bounds: "+ex);
        	
        	manageHelpConsoleCommand();
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
	
	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                                ╔═╗╔═╗╔╦╗╔╦╗╔═╗╔╗╔╔╦╗╔═╗  ╦  ╦╔═╗╔╦╗                                        \n"
						 + "                                ║  ║ ║║║║║║║╠═╣║║║ ║║╚═╗  ║  ║╚═╗ ║                                         \n"
				     	 + "                                ╚═╝╚═╝╩ ╩╩ ╩╩ ╩╝╚╝═╩╝╚═╝  ╩═╝╩╚═╝ ╩                                         \n"
				         + "                                                                                                            \n");
	}

	private void showCommandsList() {
		
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		
		ArrayList<String> whistlerCommands = new ArrayList<>();
		whistlerCommands.add("LoginCommand");
		whistlerCommands.add("SignUpCommand");
		
		whistlerCommands.add("CircleCommand");
		whistlerCommands.add("PublishCommand");
		whistlerCommands.add("CommentCommand");
		whistlerCommands.add("ShowCommentsCommand");
		whistlerCommands.add("LikeCommand");
		whistlerCommands.add("ShowLikesCommand");
		whistlerCommands.add("ProfileCommand");
		whistlerCommands.add("ShowNotificationsCommand");
		whistlerCommands.add("SearchAccountCommand");
		whistlerCommands.add("SearchPostCommand");
		whistlerCommands.add("ExploreCommand");
		whistlerCommands.add("HelpCommand");
		whistlerCommands.add("UpdateCommand");
		
		whistlerCommands.add("FollowCommand");
		whistlerCommands.add("UnFollowCommand");
		whistlerCommands.add("EditCommentCommand");
		whistlerCommands.add("RemoveCommentCommand");
		whistlerCommands.add("ProfileTimelineCommand");
		whistlerCommands.add("EditPostCommand");
		whistlerCommands.add("RemovePostCommand");
		whistlerCommands.add("SettingsCommand");
		whistlerCommands.add("RemoveAccountCommand");
		whistlerCommands.add("ClearNotificationCommand");
		whistlerCommands.add("ConfirmCommand");
		whistlerCommands.add("TurnBackCommand");
		
		
		for(String commandName : whistlerCommands) {
			
			Class<?> command;
			
			try {
				
				command = Class.forName("it.aps.whistler.ui.text.command."+commandName);
				Command c = (Command) command.getDeclaredConstructor().newInstance();		//reflection
				System.out.println("  - "+c.getCommandDescription()+"\n");
				
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | 
					 InvocationTargetException | NoSuchMethodException | SecurityException ex) {
				//ex.printStackTrace();
				logger.logp(Level.WARNING, HelpConsole.class.getSimpleName(),"showCommandsList","("+userNickname+")"+" NullPointerException: "+ex);
			}	
		}
		
		
			
	}
	
	
}
