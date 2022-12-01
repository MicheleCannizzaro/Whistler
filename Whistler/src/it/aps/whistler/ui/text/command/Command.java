package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

public interface Command {
	public String getCommandDescription();
	public void run(ArrayList<String> enteredInputs, String userNickname);
}
