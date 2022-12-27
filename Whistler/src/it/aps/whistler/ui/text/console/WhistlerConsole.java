package it.aps.whistler.ui.text.console;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.HibernateUtil;
import it.aps.whistler.util.Util;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WhistlerConsole implements Console{
	private final static Logger logger = Logger.getLogger(WhistlerConsole.class.getName());
	
	private ArrayList<String> userInputs;
	
	public WhistlerConsole() {
		this.userInputs = new ArrayList<>();
	}
		
	public void start() {
		welcomePage();
		printAvailableCommands(Page.WHISTLER_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.WHISTLER_CONSOLE);
			command.run(userInputs,null,null);
		}catch(java.lang.NullPointerException ex){
			logger.logp(Level.WARNING, WhistlerConsole.class.getSimpleName(),"start","(BYE): NullPointerException: "+ex);
			System.out.println("closing...");
			HibernateUtil.shutdown();
			System.out.println("BYE!");
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