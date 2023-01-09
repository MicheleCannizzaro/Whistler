package it.aps.whistler.ui.text.command;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.EditPostConsole;

public class EditPostCommand implements Command {
	
	public EditPostCommand(){}
	
	public String getCommandDescription() {
		String descripition = "\"Edit Post\" - EditPostCommand takes to EditPostConsole which allows you to edit the post's field, based\n     on its pid!";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname, Page previousPage) {
		Console editPostConsole= new EditPostConsole(userNickname);
		editPostConsole.start();
	}
}
