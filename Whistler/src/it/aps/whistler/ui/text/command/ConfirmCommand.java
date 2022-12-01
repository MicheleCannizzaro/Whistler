package it.aps.whistler.ui.text.command;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Whistler;
import it.aps.whistler.ui.text.Page;
import it.aps.whistler.ui.text.console.CircleConsole;
import it.aps.whistler.ui.text.console.HomeConsole;
import it.aps.whistler.ui.text.console.LoginConsole;
import it.aps.whistler.ui.text.console.SignUpConsole;
import it.aps.whistler.ui.text.console.WhistlerConsole;

import java.util.ArrayList;
import java.util.List;

public class ConfirmCommand implements Command{
	
	private Page page;
	
	public ConfirmCommand(Page page){
		this.page=page;
	}
		
	public String getCommandDescription() {
		String descripition = "ConfirmCommand is a command that satisfies different requests based on the page in which it is inserted ";
		return descripition;
	}
	
	public void run(ArrayList<String> enteredInputs, String userNickname) {
		Whistler whistler = Whistler.getInstance();
		
		if (this.page == Page.SIGNUP_CONSOLE) {
			
			while (!whistler.signUp(enteredInputs.get(0),enteredInputs.get(1),enteredInputs.get(2),enteredInputs.get(3),enteredInputs.get(4))) {
				SignUpConsole signUpConsole = new SignUpConsole();
				signUpConsole.start();
			}
			
			WhistlerConsole whistlerConsole = new WhistlerConsole();
			whistlerConsole.start();
		}
			
		if (this.page == Page.LOGIN_CONSOLE) {
			
			while(!whistler.login(enteredInputs.get(0), enteredInputs.get(1))) {
				System.out.println("<<Wrong password. Try again!>>");
				LoginConsole loginConsole = new LoginConsole();
				loginConsole.start();
			}
			
			HomeConsole homeConsole = new HomeConsole(enteredInputs.get(0)); //Pass the nickname of the account logged in correctly to HomeConsole
			homeConsole.start();
					
		}
		
		if (this.page == Page.PUBLISH_CONSOLE) {
			Account userAccount = whistler.getAccount(userNickname);
		
			userAccount.enterNewPost(enteredInputs.get(0), enteredInputs.get(1));
			
			List<String> keywords = null; //keyword entered by user were appended at the end of the enteredInputs, after title(0) and body(1) visibility(2)
			if(enteredInputs.size()>3) {
				switch(enteredInputs.size()) {
					case 4: keywords = enteredInputs.subList(3,4);     //it can be entered maximum 3 keywords per post
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
			
			HomeConsole homeConsole = new HomeConsole(userNickname);
			homeConsole.start();
					
		}
		
		if (this.page == Page.FOLLOW_CONSOLE) {
			
			Account userAccount = whistler.getAccount(userNickname);
			
			userAccount.followAccount(enteredInputs.get(0));       //enteredInputs.get(0) is the nickname of the account to follow
			
			CircleConsole circleConsole = new CircleConsole(userNickname); 
			circleConsole.start();
				
		}
		
		if (this.page == Page.UNFOLLOW_CONSOLE) {
			
			Account userAccount = whistler.getAccount(userNickname);
			
			userAccount.unFollowAccount(enteredInputs.get(0));      //enteredInputs.get(0) is the nickname of the account to unfollow
		
			CircleConsole circleConsole = new CircleConsole(userNickname); 
			circleConsole.start();
				
		}
		
	}
}