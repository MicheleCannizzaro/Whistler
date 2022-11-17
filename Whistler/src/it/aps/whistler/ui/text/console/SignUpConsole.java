package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;

import java.util.ArrayList;

public class SignUpConsole {
	
	private ArrayList<String> userInputs;
	
	public SignUpConsole() {
		this.userInputs = new ArrayList<>();
	}
		
	public void start() {
		Whistler whistler = Whistler.getInstance();
		welcomePage();
		
		String nickname = "@"+Parser.getInstance().readCommand(" Enter a new shiny Nickname:");
	
		//UI preventive checks for better user experience
		while (!isNicknameCorrect(nickname) || whistler.searchAccount(nickname) != null) {
			String message = null;
			if (!isNicknameCorrect(nickname)) message = "\n<<Sorry! Spaces are not allowed in \"@nickname\".>>\n Please choose another one:";
			else message = "\n<<Sorry! The Nickname you chose is already taken.>>\n Please choose another one:";
			nickname = "@"+Parser.getInstance().readCommand(message);
		}
		
		String name = Parser.getInstance().readCommand(" Enter a Name:");
		String surname = Parser.getInstance().readCommand(" Enter a Surname:");
		String email = Parser.getInstance().readCommand(" Enter an E-mail:");
		String passwordPlainText = Parser.getInstance().readCommand(" Enter a new Password:");
		
		//UI preventive checks for better user experience
		while(passwordPlainText.length()<8) {
			System.out.println("\n<<Password must be at least 8 characters. Please, take in mind and retry.>>\n");
			passwordPlainText = Parser.getInstance().readCommand(" Enter a new Password:");
		}
		
		userInputs.add(nickname);
		userInputs.add(name);
		userInputs.add(surname);
		userInputs.add(email);
		userInputs.add(passwordPlainText);
		
		printAvailableCommands(Page.SIGNUP_CONSOLE);
		Command command= Parser.getInstance().getCommand(Page.SIGNUP_CONSOLE);
		command.run(userInputs);	
	}
	
	private boolean isNicknameCorrect(String nickname) {
		if (!nickname.matches("\\S+")) return false;
		return true;
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔══════════════════════════════════╗  \n"
				+" ║   "+commands[0]+"          ║  \n"
				+" ║   "+commands[1]+"                      ║  \n"
				+" ╚══════════════════════════════════╝  \n");
	}
	
	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                           ╔═╗╦╔═╗╔╗╔╦ ╦╔═╗                                  \n"
				+ "                                           ╚═╗║║ ╦║║║║ ║╠═╝                                  \n"
			    + "                                           ╚═╝╩╚═╝╝╚╝╚═╝╩                                    \n"
				+ "                                         ╚══════════════════╝                                \n");
		
		System.out.println("                       WELCOME TO WHISTLER! Are you ready to make this a better world?!");
		System.out.println("              Whistler will ask you some questions to get your account fully set up. Let's GO!\n");
	}

}