package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.ui.text.command.SignUpCommand;
import it.aps.whistler.ui.text.command.TurnBackCommand;

import java.util.ArrayList;

public class LoginConsole {
	
	private ArrayList<String> userInputs;
	
	public LoginConsole() {
		this.userInputs = new ArrayList<>();
	}
	
	public void start() {
		welcomePage();
		
		String nickname = getNicknameFromStandardInput();
		String passwordPlainText = Parser.getInstance().readCommand(" Enter your Password:");
		manageLoginConsoleCommand(nickname, passwordPlainText);
		
	}
	
	private void manageLoginConsoleCommand(String nickname, String passwordPlainText) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.add(nickname);
		userInputs.add(passwordPlainText);
		
		printAvailableCommands(Page.LOGIN_CONSOLE);
		command= Parser.getInstance().getCommand(Page.LOGIN_CONSOLE);
		command.run(userInputs);
	}
	
	private void manageLoginErrorCommands(String nickname){
		
		Command command;
		try {
			int choice = Integer.parseInt(Parser.getInstance().readCommand(" Enter your choice here:"));
			
			switch (choice) {
				case 0: command = new TurnBackCommand(Page.LOGIN_CONSOLE);
						command.run(userInputs);
						break;
				case 1: command = new SignUpCommand();
						command.run(userInputs);
						break;
				case 2: 
						nickname = getNicknameFromStandardInput();
						String passwordPlainText = Parser.getInstance().readCommand(" Enter your Password:");
						manageLoginConsoleCommand(nickname, passwordPlainText);	
						break;
			}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	manageLoginErrorCommands(nickname);
        	}
	}		
	
	private String getNicknameFromStandardInput() {
		Whistler whistler = Whistler.getInstance();
		String nickname = "@"+Parser.getInstance().readCommand(" Enter your Nickname:");
		
		//UI preventive checks for better user experience
		while (whistler.getAccount(nickname)==null) {
			System.out.println("\n<<The Nickname is incorrect or non-existent! Please Sign-up first or enter a correct one>>");
			
			printAvailableCommandsLoginError(Page.LOGIN_CONSOLE);
			manageLoginErrorCommands(nickname);
		}
		return nickname;
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				 " ╔══════════════════════════════════╗  \n"
				+" ║   "+commands[0]+"          ║  \n"
				+" ║   "+commands[1]+"                      ║  \n"
				+" ╚══════════════════════════════════╝  \n");
	}
	
	private void printAvailableCommandsLoginError(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("  Commands:");
		System.out.println(
				 " ╔══════════════════════════════════╗  \n"
				+" ║   "+commands[0]+"          ║  \n"
				+" ║   "+"1:SignUp"+"                       ║  \n"
				+" ║   "+"2:Retry"+"                        ║  \n"
				+" ╚══════════════════════════════════╝  \n");
	}
	
	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                           ╦  ╔═╗╔═╗╦╔╗╔                                     \n"
				+ "                                           ║  ║ ║║ ╦║║║║                                     \n"
			    + "                                           ╩═╝╚═╝╚═╝╩╝╚╝                                     \n"
				+ "                                         ╚═══════════════╝                                   \n");
	}
}
