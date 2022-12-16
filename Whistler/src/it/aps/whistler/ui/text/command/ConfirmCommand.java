package it.aps.whistler.ui.text.command;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;

import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.CircleConsole;
import it.aps.whistler.ui.text.console.Console;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.LoginConsole;
import it.aps.whistler.ui.text.console.ProfileConsole;
import it.aps.whistler.ui.text.console.ProfileTimelineConsole;
import it.aps.whistler.ui.text.console.RemoveAccountConsole;
import it.aps.whistler.ui.text.console.SettingsConsole;
import it.aps.whistler.ui.text.console.SignUpConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmCommand implements Command{
	private final static Logger logger = Logger.getLogger(ConfirmCommand.class.getName());
	
	private enum editFieldSettings{ NAME, SURNAME, EMAIL, INFO_VISIBILITY, PASSWORD }
	private enum editFieldPost{ TITLE, BODY, KEYWORDS, POST_VISIBILITY }
	
	private Page page;
	
	public ConfirmCommand(Page page){
		this.page=page;
	}
		
	public String getCommandDescription() {
		String descripition = "ConfirmCommand is a command that satisfies different requests based on the page in which it is located";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		
		switch(this.page) {
			case SIGNUP_CONSOLE:
				confirmSignup(enteredInputs);
				break;
			case LOGIN_CONSOLE:
				confirmLogin(enteredInputs);
				break;
			case PUBLISH_CONSOLE:
				confirmPublishConsole(enteredInputs,userNickname);
				break;
			case FOLLOW_CONSOLE:
				confirmFollowConsole(enteredInputs,userNickname);
				break;
			case UNFOLLOW_CONSOLE:
				confirmUnFollowConsole(enteredInputs,userNickname);
				break;
			case SETTINGS_CONSOLE:
				confirmSettingsConsole(enteredInputs,userNickname);
				break;
			case EDIT_POST_CONSOLE:
				confirmEditPostConsole(enteredInputs,userNickname);
				break;
			case REMOVE_POST_CONSOLE:
				confirmRemovePostConsole(enteredInputs,userNickname);
				break;
			case REMOVE_ACCOUNT_CONSOLE:
				confirmRemoveAccountConsole(enteredInputs,userNickname);
				break;
			case SEARCH_ACCOUNT_CONSOLE:
				confirmSearchAccountConsole(enteredInputs,userNickname);
				break;
			default:
				break;
		}	
	}
	
	private void confirmSignup(ArrayList<String> enteredInputs) {
		Whistler whistler = Whistler.getInstance();
		
		while (!whistler.signUp(enteredInputs.get(0),enteredInputs.get(1),enteredInputs.get(2),enteredInputs.get(3),enteredInputs.get(4))) {
			Console signUpConsole = new SignUpConsole();
			signUpConsole.start();
		}
		
		Console whistlerConsole = new WhistlerConsole();
		whistlerConsole.start();
	}
	
	private void confirmLogin(ArrayList<String> enteredInputs) {
		Whistler whistler = Whistler.getInstance();
		
		while(!whistler.login(enteredInputs.get(0), enteredInputs.get(1))) {
			System.out.println("<<Wrong password. Try again!>>");
			Console loginConsole = new LoginConsole();
			loginConsole.start();
		}
		
		Console homeConsole = new HomeConsole(enteredInputs.get(0)); //Pass the nickname of the account logged in correctly to HomeConsole
		homeConsole.start();
	}
	
	private void confirmPublishConsole(ArrayList<String> enteredInputs, String userNickname) {
		Whistler whistler = Whistler.getInstance();
		Account userAccount = whistler.getAccount(userNickname);
		
		userAccount.enterNewPost(enteredInputs.get(0), enteredInputs.get(1));
		
		List<String> keywords = null; //keyword entered by user were appended at the end of the enteredInputs, after title(0) and body(1) visibility(2)
		if(enteredInputs.size()>3) {							//it can be entered maximum 3 keywords per post
			switch(enteredInputs.size()) {
				case 4: keywords = enteredInputs.subList(3,4);     
						break;
				case 5: keywords = enteredInputs.subList(3,5);
						break;
				case 6: keywords = enteredInputs.subList(3,6);
						break;
			}
			
			for (String keyword : keywords) {
				userAccount.addPostKeyword(keyword);
			}
		}
		userAccount.setPostOwner();
		int visibilityIndex = Integer.valueOf(enteredInputs.get(2));
		userAccount.setPostVisibility(Visibility.values()[visibilityIndex]);
		userAccount.confirmPost();
		
		Console homeConsole = new HomeConsole(userNickname);
		homeConsole.start();
	}
	
	private void confirmFollowConsole(ArrayList<String> enteredInputs, String userNickname) {
		Whistler whistler = Whistler.getInstance();
		Account userAccount = whistler.getAccount(userNickname);
		String nicknameAccountToFollow = enteredInputs.get(0); //enteredInputs.get(0) is the nickname of the account to follow
		
		if (userNickname.equals(nicknameAccountToFollow))
			System.out.println("<<It's not necessary to follow yourself!>>");
		
		userAccount.followAccount(nicknameAccountToFollow);       
		
		Console circleConsole = new CircleConsole(userNickname); 
		circleConsole.start();
	}
	
	private void confirmUnFollowConsole(ArrayList<String> enteredInputs, String userNickname) {
		Whistler whistler = Whistler.getInstance();
		Account userAccount = whistler.getAccount(userNickname);
		String nicknameAccountToUnfollow = enteredInputs.get(0); //enteredInputs.get(0) is the nickname of the account to unfollow
		
		if (userNickname.equals(nicknameAccountToUnfollow))
			System.out.println("<<You can't unfollow yourself!>>");
		
		userAccount.unFollowAccount(nicknameAccountToUnfollow);     
	
		Console circleConsole = new CircleConsole(userNickname); 
		circleConsole.start();
	}
	
	private void confirmSettingsConsole(ArrayList<String> enteredInputs, String userNickname) {
		
		Whistler whistler = Whistler.getInstance();
		int fieldToEdit = Integer.parseInt(enteredInputs.get(0)); //enteredInputs.get(0) - contains the choice made about the field to edit
		Account userAccount = whistler.getAccount(userNickname);
		
		switch(editFieldSettings.values()[fieldToEdit]) {
			case NAME: 
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmSettingsConsole",userNickname+" edits Name");
				userAccount.setName(enteredInputs.get(1));
				break;
			case SURNAME: 
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmSettingsConsole",userNickname+" edits Surname");
				userAccount.setSurname(enteredInputs.get(1));
				break;
			case EMAIL:
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmSettingsConsole",userNickname+" edits E-mail");
				userAccount.setEmail(enteredInputs.get(1));
				break;
			case INFO_VISIBILITY:
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmSettingsConsole",userNickname+" edits Info Visibility");
				HashMap<String,Visibility> visibility = new HashMap<>();
				String[] visibilityField = {"Name","Surname","E-mail"};
				
				for(int i=1; i<4;i++) {
					int visibilityIndex = Integer.valueOf(enteredInputs.get(i)); //enteredInputs.get(i) - contains the  visibility values (1: Name - 2: Surname - 3:E-mail)
					visibility.put(visibilityField[i-1], Visibility.values()[visibilityIndex]);
				}
				
				userAccount.setVisibility(visibility);
				break;
			case PASSWORD:
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmSettingsConsole",userNickname+" edits Password");
				while(!userAccount.setPassword(enteredInputs.get(1))) {
					Console settingsConsole = new SettingsConsole(userNickname);
					settingsConsole.start();
				}
				break;
		}
		
		whistler.updateAccount(userAccount);
		
		if (editFieldSettings.values()[fieldToEdit]==editFieldSettings.PASSWORD) {
			Console whistlerConsole = new WhistlerConsole();
			whistlerConsole.start();
		}
		
		Console profileConsole= new ProfileConsole(userNickname, true, null);  //isOwner == true
		profileConsole.start();
			
	}
	
	private void confirmEditPostConsole(ArrayList<String> enteredInputs, String userNickname) {
		
		Whistler whistler = Whistler.getInstance();
		String postPid = enteredInputs.get(0);   //enteredInputs.get(0) - contains the pid
		Post postToEdit = whistler.getPost(postPid);
		int fieldToEdit = Integer.parseInt(enteredInputs.get(1)); //enteredInputs.get(1) - contains the choice made about the field to edit
		
		switch(editFieldPost.values()[fieldToEdit]) {
			case TITLE:
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmEditPostConsole",userNickname+" edits post's Title ("+postPid+")");
				postToEdit.setTitle(enteredInputs.get(2));  //enteredInputs.get(2) - contains the field value
				break;
			case BODY: 
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmEditPostConsole",userNickname+" edits post's Body ("+postPid+")");
				postToEdit.setBody(enteredInputs.get(2));
				break;
			case KEYWORDS:
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmEditPostConsole",userNickname+" edits post's Keywords ("+postPid+")");
				postToEdit.clearKeyword();
				List<String> keywords = null;
				
				if(enteredInputs.size()>2) {								//it can be entered maximum 3 keywords per post
					switch(enteredInputs.size()) {
						case 3: keywords = enteredInputs.subList(2,3);      //if user chose keywords enteredInputs.sublist contains the keyword value
								break;
						case 4: keywords = enteredInputs.subList(2,4);
								break;
						case 5: keywords = enteredInputs.subList(2,5);
								break;
					}
					
					for (String keyword : keywords) {
						postToEdit.addPostKeyword(keyword);
					}
					
				}
				break;
			case POST_VISIBILITY:
				logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmEditPostConsole",userNickname+" edits pos's visibility ("+postPid+")");
				int visibilityIndex = Integer.valueOf(enteredInputs.get(2)); ////enteredInputs.get(2) - contains the  visibility value
				postToEdit.setPostVisibility(Visibility.values()[visibilityIndex]);
				break;
		}
		postToEdit.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
		whistler.updatePost(postToEdit);
		
		Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true,null); //isOwner == true
		profileTimelineConsole.start();
	}
	
	private void confirmRemovePostConsole(ArrayList<String> enteredInputs, String userNickname) {
		
		Whistler whistler = Whistler.getInstance();
		String postPid = enteredInputs.get(0);   //enteredInputs.get(0) - contains the pid of the post to remove
		whistler.getAccount(userNickname).removePost(postPid);
		
		logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmRemovePostConsole",userNickname+" deleted post with PID: "+postPid);
		
		Console profileTimelineConsole = new ProfileTimelineConsole(userNickname,true,null); //isOwner == true
		profileTimelineConsole.start();
	}
	
	
	private void confirmRemoveAccountConsole(ArrayList<String> enteredInputs, String userNickname) {
		Whistler whistler = Whistler.getInstance();
		
		while(!whistler.login(userNickname, enteredInputs.get(0))) {
			System.out.println("<<Wrong password. Try again!>>");
			Console removeAccountConsole = new RemoveAccountConsole(userNickname);
			removeAccountConsole.start();
		}
		
		whistler.removeAccount(userNickname);
		logger.logp(Level.INFO, ConfirmCommand.class.getSimpleName(),"confirmRemoveAccountConsole",userNickname+"'s account was deleted.");
		
		Console whistlerConsole = new WhistlerConsole();
		whistlerConsole.start();
			
	}
	
	
	private void confirmSearchAccountConsole(ArrayList<String> enteredInputs, String userNickname) {
		Console profileConsole;
		
		if (!enteredInputs.get(0).equals(userNickname)) { //enteredInputs.get(0) is the whistleblower searched
			
			profileConsole= new ProfileConsole(userNickname, false, enteredInputs.get(0)); // isOwner == false 
		}else {
			
			profileConsole= new ProfileConsole(userNickname, true, null); // isOwner == true
		}
		profileConsole.start();
	}
	
}