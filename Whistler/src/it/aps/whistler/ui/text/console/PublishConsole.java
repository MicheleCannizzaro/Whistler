package it.aps.whistler.ui.text.console;

import java.util.ArrayList;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.Parser;
import it.aps.whistler.ui.text.PageCommands;
import it.aps.whistler.ui.text.command.Command;
import it.aps.whistler.util.Util;

public class PublishConsole {

	private ArrayList<String> userInputs;
	private String userNickname;
	
	public PublishConsole(String userNickname) {
		this.userInputs = new ArrayList<>();
		this.userNickname = userNickname;
	}
	
	public void start() {
		
		ArrayList<String> postKeywordsFromInput = new ArrayList<>();
		
		welcomePage();
		
		String title = Parser.getInstance().readCommand(" Enter post's Title:");
		
		while(title.length()>70) {
			System.out.println("<<Sorry, title length can't be higher than 70 characters, the title entered was "+title.length()+" characters long! Please Retry!>>\n");
			title = Parser.getInstance().readCommand(" Enter post's Title:");
		}
		
		String body = Parser.getInstance().readCommand("\n Enter post's Body:");
		
		//To add - check that the body it's under 280 characters 

		//Gather maximum 3 keywords
		for (int i =0; i <3; i++) {
			String keyword = "#"+Parser.getInstance().readCommand("\n Enter Keyword n°"+(i+1));
			if (!keyword.equals("#")) 
				postKeywordsFromInput.add(keyword);
			
		}
		
		String postVisibility = null;
		
		System.out.println("\n<<Your post is almost ready to be published!>>");
		System.out.println("<<In order to set privacy policy for this post ->  Enter \"0:PUBLIC\" or \"1:PRIVATE\" >>\n");
		
		postVisibility = Parser.getInstance().readCommand(" What's the post privacy policy?");
		
		if (postVisibility!=null) {
			
			while (!postVisibility.equals("0") && !postVisibility.equals("1")) {
				System.out.println("<<Sorry, \""+postVisibility+"\" it's not a valid option! Please Retry!>>\n");
				postVisibility = Parser.getInstance().readCommand(" What's the post privacy policy?");
			}
		}
		
		userInputs.add(title);
		userInputs.add(body);
		userInputs.add(postVisibility);
		userInputs.addAll(postKeywordsFromInput);
		
		postPreview(title, body, postKeywordsFromInput, postVisibility);
		
		printAvailableCommands(Page.PUBLISH_CONSOLE);
		
		try {
			Command command= Parser.getInstance().getCommand(Page.PUBLISH_CONSOLE);
			command.run(userInputs,userNickname);
		}catch(java.lang.NullPointerException ex){
			throw new java.lang.NullPointerException("Throwing java.lang.NullPointerException PublishConsole"+ex);
		}
	}
	
	private void printAvailableCommands(Page page) {
		String[] commands = PageCommands.getCommands(page);
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(" Commands:");
		System.out.println(
				" ╔════════════════════════════════╗        \n"
			   +" ║  "+Util.padRight(commands[0],30)+"║    \n"
			   +" ║  "+Util.padRight(commands[1],30)+"║    \n"
			   +" ╚════════════════════════════════╝        \n");
	}
	

	private void welcomePage() {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                              ╦╔╗╔╔═╗╔═╗╦═╗╔╦╗  ╔╗╔╔═╗╦ ╦  ╔═╗╔═╗╔═╗╔╦╗                                              \n"
				+ "                              ║║║║╚═╗║╣ ╠╦╝ ║   ║║║║╣ ║║║  ╠═╝║ ║╚═╗ ║                                               \n"
			    + "                              ╩╝╚╝╚═╝╚═╝╩╚═ ╩   ╝╚╝╚═╝╚╩╝  ╩  ╚═╝╚═╝ ╩                                               \n"
				+ "                            ╚══════════════════════════════════════════╝                                             \n");
	}
	
	private void postPreview(String title, String body, ArrayList<String> postKeywordsFromInput, String postVisibility) {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                                ┌─┐┌─┐┌─┐┌┬┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬                                                   \n"
				+ "                                ├─┘│ │└─┐ │   ├─┘├┬┘├┤ └┐┌┘│├┤ │││                                                   \n"
			    + "                                ┴  └─┘└─┘ ┴   ┴  ┴└─└─┘ └┘ ┴└─┘└┴┘                                                   \n"
				+ "                              ╚════════════════════════════════════╝                                                 \n");
		
		System.out.println("         ╔═══════════════════════════════════════════════════════════════════════════════╗");
		
		if (title.length()<=70) {
			System.out.println("         ║ TITLE: "+Util.padRight(title, 71)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");  
		}
		
		if (body.length()<=70) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0,body.length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║"); 
		}
		
		if (body.length()>70 && body.length()<=140 ) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, body.length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (body.length()>140 && body.length()<=209) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(140,body.length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (body.length()>209 && body.length()<=280) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(140, 210), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(210, body.length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (postVisibility.equals("0")) {
			System.out.println("         ║ Visibility: PUBLIC                                                            ║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (postVisibility.equals("1")) {
			System.out.println("         ║ Visibility: PRIVATE                                                           ║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		System.out.println("         ║ Keywords:"+Util.padRight(postKeywordsFromInput.toString(), 69)+"║");
		System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
	}
}
