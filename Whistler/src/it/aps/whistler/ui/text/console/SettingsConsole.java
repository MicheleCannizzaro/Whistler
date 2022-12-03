package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class SettingsConsole implements Console {
	
	private enum editAccountInfoField{ NAME, SURNAME, EMAIL, INFO_VISIBILITY, PASSWORD } //Ho lavorato qui!
	
	private ArrayList<String> userInputs;
	private String userNickname;
	
	public SettingsConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		welcomePage();
		ArrayList<String> inputs =  getNewFieldValueToEdit();
		
		manageSettingsConsoleCommand(inputs);	
	}
	
	private void manageSettingsConsoleCommand(ArrayList<String> inputs) {
		Command command;
		userInputs.clear();			//Cleans the inputs of the various retries maintaining the current one
		userInputs.addAll(inputs);
		
		printAvailableCommands(Page.SETTINGS_CONSOLE);
		
		try {
			command= Parser.getInstance().getCommand(Page.SETTINGS_CONSOLE);
			command.run(userInputs,this.userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException SettingsConsole "+ex);
		}
	}
	
	private ArrayList<String> getNewFieldValueToEdit(){
		ArrayList<String> fields = new ArrayList<>();
		System.out.println("\n Choose one of this field to edit:");
		System.out.println(" [Commands]: \"0:Name\" - \"1:Surname\" - \"2:E-mail\" - \"3:Info Visibility\" - \"4:Password\"");
		
		try {
			int fieldToEdit = Integer.parseInt(Parser.getInstance().readCommand("\n Which Field you want to modify?"));
			fields.add(String.valueOf(fieldToEdit));  //adding choice made to store the field to edit
			
			switch(editAccountInfoField.values()[fieldToEdit]) {
				case NAME: 
					String newName = Parser.getInstance().readCommand(" Edit Name:");
					fields.add(newName);
					break;
				case SURNAME: 
					String newSurname = Parser.getInstance().readCommand(" Edit Surname :");
					fields.add(newSurname);
					break;
				case EMAIL:
					String newEmail = Parser.getInstance().readCommand(" Edit E-mail :");
					fields.add(newEmail);
					break;
				case INFO_VISIBILITY: 
					System.out.println("<<In order to set privacy policy for this post ->  Enter \"0:PUBLIC\" or \"1:PRIVATE\" >>\n");
					String infoVisibility = Parser.getInstance().readCommand(" What's the new info privacy policy?");
					if (infoVisibility!=null) {
						
						while (!infoVisibility.equals("0") && !infoVisibility.equals("1")) {
							System.out.println("<<Sorry, \""+infoVisibility+"\" it's not a valid option! Please Retry!>>\n");
							infoVisibility = Parser.getInstance().readCommand(" What's the new info privacy policy?");
						}
						
						fields.add(infoVisibility);
					}
					break;
				case PASSWORD: 
					String newPassword = Parser.getInstance().readCommand(" Edit Password :");
					fields.add(newPassword);
					break;
				}
		}catch(java.lang.NumberFormatException | java.lang.ArrayIndexOutOfBoundsException ex) {
			
        	System.out.println ("\n<<You must enter a command in the list \"Commands\" [digit format]>>");
        	ArrayList<String> inputs =  getNewFieldValueToEdit();
        	manageSettingsConsoleCommand(inputs);
        }
		return fields;
	} 

	public void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                      ╔═╗╔═╗╔╦╗╔╦╗╦╔╗╔╔═╗╔═╗                                     \n"
				+ "                                      ╚═╗║╣  ║  ║ ║║║║║ ╦╚═╗                                     \n"
			    + "                                      ╚═╝╚═╝ ╩  ╩ ╩╝╚╝╚═╝╚═╝                                     \n"
				+ "                                     ╚══════════════════════╝                                    \n");
	}
	
	public void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════════╗       \n"
			   +" ║  "+Util.padRight(commands[0],34)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],34)+"║    \n"
			   +" ╚════════════════════════════════════╝       \n");
	}
}
