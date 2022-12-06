package it.aps.whistler;

import java.util.logging.Level;
import java.util.logging.Logger;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.console.WhistlerConsole;
import it.aps.whistler.util.WhistlerLogger;

public class WhistlerMainAppText {
	private final static Logger logger = Logger.getLogger(WhistlerMainAppText.class.getName());

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		WhistlerLogger log = new WhistlerLogger();
		
		Whistler whistler = Whistler.getInstance();   //Initialization of whistler
		logger.logp(Level.INFO,WhistlerMainAppText.class.getSimpleName(),"main","Whistler initialization");
		
		WhistlerConsole console = new WhistlerConsole();
		console.start();
	}

}
