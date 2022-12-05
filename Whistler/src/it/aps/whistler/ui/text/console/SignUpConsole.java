package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignUpConsole implements Console {
	private final static Logger logger = Logger.getLogger(SignUpConsole.class.getName());
	
	private ArrayList<String> userInputs;
	
	public SignUpConsole() {
		this.userInputs = new ArrayList<>();
	}
		
	public void start() {
		Whistler whistler = Whistler.getInstance();
		welcomePage();
		
		String nickname = "@"+Parser.getInstance().readCommand(" Enter a new shiny Nickname:");
	
		//UI preventive checks for better user experience
		while (!isNicknameCorrect(nickname) || whistler.getAccount(nickname) != null) {
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
		
		try {
			Command command= Parser.getInstance().getCommand(Page.SIGNUP_CONSOLE);
			command.run(userInputs,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, SignUpConsole.class.getSimpleName(),"start","NullPointerException: "+ex);
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException SignUpConsole "+ex);
		}
	}
	
	private boolean isNicknameCorrect(String nickname) {
		if (!nickname.matches("\\S+")) return false;
		return true;
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔════════════════════════╗               \n"
				+" ║  "+Util.padRight(commands[0],22)+"║    \n"
				+" ║  "+Util.padRight(commands[1],22)+"║    \n"
				+" ╚════════════════════════╝               \n");
	}
	
	public void welcomePage() {
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
