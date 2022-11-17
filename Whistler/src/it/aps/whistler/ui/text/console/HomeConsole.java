package it.aps.whistler.ui.text.console;

import it.aps.whistler.domain.Account;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;

import java.util.ArrayList;

public class HomeConsole {
	
	private ArrayList<String> userInputs;
	private Account userAccount;
	
	public HomeConsole(Account userAccount) {
		this.userInputs = new ArrayList<>();
		this.userAccount = userAccount;
	}
		
	public void start() {
		welcomePage();
		
		printAvailableCommands(Page.HOME_CONSOLE);
		
		try {
		Command command= Parser.getInstance().getCommand(Page.HOME_CONSOLE);
		command.run(userInputs);	
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException WhistlerConsole");
		}
	}
	

	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				 " ╔═════════════════════════╗  \n"
				+" ║   "+commands[0]+"              ║  \n"
				+" ╚═════════════════════════╝  \n");
	}
	
	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println("                               ██╗  ██╗ ██████╗ ███╗   ███╗███████╗                                         \n"
					     + "                               ██║  ██║██╔═══██╗████╗ ████║██╔════╝          USER: "+this.userAccount.getNickname()+"\n"
					     + "                               ███████║██║   ██║██╔████╔██║█████╗                                           \n"
				         + "                               ██╔══██║██║   ██║██║╚██╔╝██║██╔══╝                                           \n"
				         + "                               ██║  ██║╚██████╔╝██║ ╚═╝ ██║███████╗                                         \n"
				         + "                               ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝╚══════╝                                         \n");
	}


}
