package it.aps.whistler;

import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.console.WhistlerConsole;

public class WhistlerMainAppText {

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Whistler whistler = Whistler.getInstance();   //Initialization of whistler
		
		WhistlerConsole console = new WhistlerConsole();
		console.start();
	}

}
