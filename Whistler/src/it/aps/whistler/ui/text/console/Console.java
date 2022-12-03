package it.aps.whistler.ui.text.console;

import it.aps.whistler.ui.text.Page;

public interface Console {

	void start();
	void welcomePage();
	void printAvailableCommands(Page page);
}
