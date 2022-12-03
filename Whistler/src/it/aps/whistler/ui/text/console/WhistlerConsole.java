package it.aps.whistler.ui.text.console;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.HibernateUtil;
import it.aps.whistler.util.Util;

import java.util.ArrayList;

public class WhistlerConsole implements Console{
	
	private ArrayList<String> userInputs;
	
	public WhistlerConsole() {
		this.userInputs = new ArrayList<>();
	}
		
	public void start() {
		welcomePage();
		printAvailableCommands(Page.WHISTLER_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.WHISTLER_CONSOLE);
			command.run(userInputs,null);
		}catch(java.lang.NullPointerException ex){
			System.out.println("BYE...");
			HibernateUtil.shutdown();
			System.exit(0);						// Terminate the program (EXIT)
		}
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔══════════════════════╗               \n"
			   +" ║  "+Util.padRight(commands[0],20)+"║  \n"
			   +" ║  "+Util.padRight(commands[1],20)+"║  \n"
			   +" ║  "+Util.padRight(commands[2],20)+"║  \n"
			   +" ╚══════════════════════╝               \n");
	}
	
	public void welcomePage() {
		System.out.println(
				      "                                                                                                            \n"
				    + " ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n"
				    + "                                                                                YPJ~.                       \n"
				    + "                                                                               7GBBBGPY!:                   \n"
				    + "                                                                                 :?GGGGBBJ  ^.              \n"
				    + "                 ██╗    ██╗██╗  ██╗██╗███████╗████████╗██╗     ███████╗██████╗     :PGGGGG5PBG5?:           \n"
				    + "                 ██║    ██║██║  ██║██║██╔════╝╚══██╔══╝██║     ██╔════╝██╔══██╗     ?GGGGG5YYPBGG5.         \n"
				    + "                 ██║ █╗ ██║███████║██║███████╗   ██║   ██║     █████╗  ██████╔╝     ?GGGY.    !GGGP         \n"
				    + "                 ██║███╗██║██╔══██║██║╚════██║   ██║   ██║     ██╔══╝  ██╔══██╗     ?GGB~      PGGG.        \n"
				    + "                 ╚███╔███╔╝██║  ██║██║███████║   ██║   ███████╗███████╗██║  ██║     :GGGP!. .:YGGBY         \n"
				    + "                  ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝╚══════╝   ╚═╝   ╚══════╝╚══════╝╚═╝  ╚═╝      :YGBBBGGBBGP7          \n"
				    + "                                                                                       .~?YYYJ7:            \n"
				    + "                                                                                                            \n");
	}
}