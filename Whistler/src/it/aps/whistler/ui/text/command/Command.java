package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

public interface Command {
	String getCommandDescription();
	void run(ArrayList<String> enteredInputs, String userNickname);
}
