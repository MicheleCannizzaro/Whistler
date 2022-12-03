package it.aps.whistler.ui.text.command;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.persistence.dao.AccountDao;
import it.aps.whistler.persistence.dao.PostDao;
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
import java.util.List;

public class ConfirmCommand implements Command{
	
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
		Whistler whistler = Whistler.getInstance();
		
		switch(this.page) {
			case SIGNUP_CONSOLE:
				confirmSignup(whistler,enteredInputs);
				break;
			case LOGIN_CONSOLE:
				confirmLogin(whistler,enteredInputs);
				break;
			case PUBLISH_CONSOLE:
				confirmPublishConsole(whistler,enteredInputs,userNickname);
				break;
			case FOLLOW_CONSOLE:
				confirmFollowConsole(whistler,enteredInputs,userNickname);
				break;
			case UNFOLLOW_CONSOLE:
				confirmUnFollowConsole(whistler,enteredInputs,userNickname);
				break;
			case SETTINGS_CONSOLE:
				confirmSettingsConsole(whistler,enteredInputs,userNickname);
				break;
			case EDIT_POST_CONSOLE:
				confirmEditPostConsole(enteredInputs,userNickname);
				break;
			case REMOVE_POST_CONSOLE:
				confirmRemovePostConsole(enteredInputs,userNickname);
				break;
			case REMOVE_ACCOUNT_CONSOLE:
				confirmRemoveAccountConsole(whistler,enteredInputs,userNickname);
				break;
			default:
				break;
		}	
	}
	
	private void confirmSignup(Whistler whistler, ArrayList<String> enteredInputs) {
		
		while (!whistler.signUp(enteredInputs.get(0),enteredInputs.get(1),enteredInputs.get(2),enteredInputs.get(3),enteredInputs.get(4))) {
			Console signUpConsole = new SignUpConsole();
			signUpConsole.start();
		}
		
		Console whistlerConsole = new WhistlerConsole();
		whistlerConsole.start();
	}
	
	private void confirmLogin(Whistler whistler, ArrayList<String> enteredInputs) {
		
		while(!whistler.login(enteredInputs.get(0), enteredInputs.get(1))) {
			System.out.println("<<Wrong password. Try again!>>");
			Console loginConsole = new LoginConsole();
			loginConsole.start();
		}
		
		Console homeConsole = new HomeConsole(enteredInputs.get(0)); //Pass the nickname of the account logged in correctly to HomeConsole
		homeConsole.start();
	}
	
	private void confirmPublishConsole(Whistler whistler, ArrayList<String> enteredInputs, String userNickname) {
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
	
	private void confirmFollowConsole(Whistler whistler, ArrayList<String> enteredInputs, String userNickname) {
		Account userAccount = whistler.getAccount(userNickname);
		
		userAccount.followAccount(enteredInputs.get(0));       //enteredInputs.get(0) is the nickname of the account to follow
		
		Console circleConsole = new CircleConsole(userNickname); 
		circleConsole.start();
	}
	
	private void confirmUnFollowConsole(Whistler whistler, ArrayList<String> enteredInputs, String userNickname) {
		Account userAccount = whistler.getAccount(userNickname);
		
		userAccount.unFollowAccount(enteredInputs.get(0));      //enteredInputs.get(0) is the nickname of the account to unfollow
	
		Console circleConsole = new CircleConsole(userNickname); 
		circleConsole.start();
	}
	
	private void confirmSettingsConsole(Whistler whistler, ArrayList<String> enteredInputs, String userNickname) {

		int fieldToEdit = Integer.parseInt(enteredInputs.get(0)); //enteredInputs.get(0) - contains the choice made about the field to edit
		Account userAccount = whistler.getAccount(userNickname);
		
		switch(editFieldSettings.values()[fieldToEdit]) {
			case NAME: 
				userAccount.setName(enteredInputs.get(1));
				AccountDao.getInstance().updateAccount(userAccount);
				break;
			case SURNAME: 
				userAccount.setSurname(enteredInputs.get(1));
				AccountDao.getInstance().updateAccount(userAccount);
				break;
			case EMAIL: 
				userAccount.setEmail(enteredInputs.get(1));
				AccountDao.getInstance().updateAccount(userAccount);
				break;
			case INFO_VISIBILITY:
				int visibilityIndex = Integer.valueOf(enteredInputs.get(1)); //enteredInputs.get(1) - contains the  visibility value
				userAccount.setVisibility(Visibility.values()[visibilityIndex]);
				AccountDao.getInstance().updateAccount(userAccount);
				break;
			case PASSWORD:                                                       
				while(!userAccount.setPassword(enteredInputs.get(1))) {
					Console settingsConsole = new SettingsConsole(userNickname);
					settingsConsole.start();
				}
				AccountDao.getInstance().updateAccount(userAccount);
				break;
		}
		
		if (editFieldSettings.values()[fieldToEdit]==editFieldSettings.PASSWORD) {
			Console whistlerConsole = new WhistlerConsole();
			whistlerConsole.start();
		}
		
		Console profileConsole= new ProfileConsole(userNickname);
		profileConsole.start();
			
	}
	
	private void confirmEditPostConsole(ArrayList<String> enteredInputs, String userNickname) {
		
		String postPid = enteredInputs.get(0);   //enteredInputs.get(0) - contains the pid
		Post postToEdit = PostDao.getInstance().getPostByPid(postPid);
		int fieldToEdit = Integer.parseInt(enteredInputs.get(1)); //enteredInputs.get(1) - contains the choice made about the field to edit
		
		switch(editFieldPost.values()[fieldToEdit]) {
			case TITLE: 
				postToEdit.setTitle(enteredInputs.get(2));  //enteredInputs.get(2) - contains the field value
				postToEdit.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
				PostDao.getInstance().updatePost(postToEdit);
				break;
			case BODY: 
				postToEdit.setBody(enteredInputs.get(2));
				postToEdit.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
				PostDao.getInstance().updatePost(postToEdit);
				break;
			case KEYWORDS:
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
					postToEdit.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
					PostDao.getInstance().updatePost(postToEdit);
				}
				break;
			case POST_VISIBILITY:
				int visibilityIndex = Integer.valueOf(enteredInputs.get(2)); ////enteredInputs.get(2) - contains the  visibility value
				postToEdit.setPostVisibility(Visibility.values()[visibilityIndex]);
				postToEdit.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
				PostDao.getInstance().updatePost(postToEdit);
				break;
		}
		
		Console profileTimelineConsole = new ProfileTimelineConsole(userNickname); 
		profileTimelineConsole.start();
	}
	
	private void confirmRemovePostConsole(ArrayList<String> enteredInputs, String userNickname) {
		
		String postPid = enteredInputs.get(0);   //enteredInputs.get(0) - contains the pid of the post to remove
		PostDao.getInstance().deletePost(postPid);
		
		Console profileTimelineConsole = new ProfileTimelineConsole(userNickname); 
		profileTimelineConsole.start();
	}
	
	private void confirmRemoveAccountConsole(Whistler whistler, ArrayList<String> enteredInputs, String userNickname) {

		while(!whistler.login(userNickname, enteredInputs.get(0))) {
			System.out.println("<<Wrong password. Try again!>>");
			Console removeAccountConsole = new RemoveAccountConsole(userNickname);
			removeAccountConsole.start();
		}
		
		Account userAccount = whistler.getAccount(userNickname);
		userAccount.removeAccount();
		
		Console whistlerConsole = new WhistlerConsole();
		whistlerConsole.start();
			
	}
	
}