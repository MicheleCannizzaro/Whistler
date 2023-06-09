package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;

public interface Command {
	String getCommandDescription();
	void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage);
}
