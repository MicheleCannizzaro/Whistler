package it.aps.whistler.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Account;
import it.aps.whistler.domain.Comment;
import it.aps.whistler.domain.Keyword;
import it.aps.whistler.domain.Notification;
import it.aps.whistler.domain.Post;
import it.aps.whistler.domain.Whistler;

public class Util {
	
	public static boolean isNicknameCorrect(String nickname) {
		//check for white spaces
		Pattern pattern = Pattern.compile("\\s+");
	    Matcher matcher = pattern.matcher(nickname);
	    boolean whiteSpaces = matcher.find();
	    
	    //check for special characters
	    Pattern pattern1 = Pattern.compile("[!\\\"\\#\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\=\\>\\?\\[\\\\\\]\\^\\_\\`\\{\\|\\}\\~]");
	    Matcher matcher1 = pattern1.matcher(nickname);
	    boolean specialCharacters = matcher1.find();
	    
	    //check for multiple @
	    Pattern pattern2 = Pattern.compile("([@])");
	    Matcher atMatcher = pattern2.matcher(nickname);
	    int atCount = 0;
	    while (atMatcher.find()) atCount++;
	    
	    if(whiteSpaces)return false;
	    if(specialCharacters)return false;
	    if(nickname.equals("@"))return false;   	//check for blank nickname
	    if(atCount>=2) return false;				//check for multiple @
	    
		return true;
	}

	//Useful to generate pid value for post
	public static String getRandomHexString(){
        Random rand = new Random();
        int number = rand.nextInt(999999); //generate a random number from 0 to 999999 
        
        if(number!=0) {
        	return String.format("%06d", number);
        }
        return getRandomHexString();
	}
	
	//Useful for random suggestions
	public static int getRandomNumber(int maxValue){
        Random rand = new Random();
        int number = rand.nextInt(maxValue); //generate a random number from 0 to maxValue 
       
        return number;
	}
	
	public static ArrayList<String> randomSuggestions(String userNickname, boolean toFollow){
		ArrayList<String> whistlerAccountsNicknames = new ArrayList<>();
		ArrayList<String> randomSuggestions = new ArrayList<>();
		
		//Getting whistlerAccountsNicknames
		for (Account a : Whistler.getInstance().getWhistlerAccounts()) {
			whistlerAccountsNicknames.add(a.getNickname());
		}
		
		//Removing userNickname
		whistlerAccountsNicknames.remove(userNickname);
		
		if(toFollow) {
			//Removing already followed Accounts
			Account userAccount = Whistler.getInstance().getAccount(userNickname);
			
			for (String nickname : userAccount.getFollowedAccounts()) {
				whistlerAccountsNicknames.remove(nickname);
			}
			
			for(int i=0; i<whistlerAccountsNicknames.size();i++) {	
				
				if(i<4) {
															//getting randomNumber from 0 to whistlerAccountsNicknames size
					String suggestion = whistlerAccountsNicknames.get(getRandomNumber(whistlerAccountsNicknames.size()));
					while(randomSuggestions.contains(suggestion)) {	
						suggestion = whistlerAccountsNicknames.get(getRandomNumber(whistlerAccountsNicknames.size()));
					}
					randomSuggestions.add(suggestion);
				}
			}
			
			return randomSuggestions;
		}
		
		Collections.shuffle(whistlerAccountsNicknames); 
		return whistlerAccountsNicknames;	
	}
	
	//Useful to format string printing in console
	public static String padRight(String inputString, int length) {
		if (inputString.length() >= length) {
	        return inputString;
	    }
	    return String.format("%-" + length + "s", inputString);  
	}
	
	public static String padLeft(String inputString, int length) {
		
	    if (inputString.length() >= length) {
	        return inputString;
	    }
	    StringBuilder sb = new StringBuilder();
	    
	    while (sb.length() < length - inputString.length()) {
	        sb.append(' ');
	    }
	    
	    sb.append(inputString);

	    return sb.toString();
	}
	
	//Useful to print LocalDateTime variables
	public static String getTimeString(LocalDateTime time) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
		return time.format(formatter);
	}
	
	//Useful for pretty printing on timelines
	public static void printDetailedPost(Post p) {
		
		System.out.println("         ╔═══════════════════╗"+                 "═══════════════════╗"+                    "═══════════════════════════════════════╗");
		System.out.println("         ║  "+Util.padRight(p.getOwner(),17)+"║"+" PID: "+Util.padRight(p.getPid(), 13)+"║"+"          "+Util.getTimeString(p.getTimestamp())+"          ║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		
		if (p.getTitle().length()<=70) {
			System.out.println("         ║ TITLE: "+Util.padRight(p.getTitle(), 71)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");  
		}
		
		if (p.getBody().length()<=70) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0,p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║"); 
		}
		
		if (p.getBody().length()>70 && p.getBody().length()<=140 ) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(70, p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getBody().length()>140 && p.getBody().length()<=209) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(140,p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getBody().length()>209 && p.getBody().length()<=280) {
			System.out.println("         ║ Body:"+Util.padRight(p.getBody().substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(140, 210), 73)+"║");
			System.out.println("         ║      "+Util.padRight(p.getBody().substring(210, p.getBody().length()), 73)+"║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getPostVisibility().equals(Visibility.PUBLIC)) {
			System.out.println("         ║ Visibility: PUBLIC                                                            ║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		if (p.getPostVisibility().equals(Visibility.PRIVATE)) {
			System.out.println("         ║ Visibility: PRIVATE                                                           ║");
			System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		}
		
		//Keywords Set in ArrayList
		ArrayList<Keyword> keywords = new ArrayList<>(p.getPostKeywords());
		
		//Sorting Keywords in Alphabetical Order
		Collections.sort(keywords, Comparator.comparing(Keyword::getWord));
		
		System.out.println("         ║ Keywords:"+Util.padRight(keywords.toString(), 69)+"║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");
		System.out.println("         ║ "+Util.padLeft(p.getAllPostComments().size()+"      Comments     ",37)+"║ "+Util.padLeft(p.getLikes().size()+"        Likes     ",39)+"║");
		System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
	}
	
	//Useful for post preview pretty printing
	public static void postPreview(String title, String body, ArrayList<String> postKeywordsFromInput, String postVisibility) {
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
	
	//Useful for comment preview pretty printing
	public static void commentPreview(String nickname, String body) {
		System.out.println(" ═══════════════════════════════════════════════════════════════════════════════════════════════════════════\n");
		System.out.println(
				  "                             ┌─┐┌─┐┌┬┐┌┬┐┌─┐┌┐┌┌┬┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬                                                   \n"
				+ "                             │  │ │││││││├┤ │││ │   ├─┘├┬┘├┤ └┐┌┘│├┤ │││                                                   \n"
			    + "                             └─┘└─┘┴ ┴┴ ┴└─┘┘└┘ ┴   ┴  ┴└─└─┘ └┘ ┴└─┘└┴┘                                                   \n"
				+ "                            ╚═══════════════════════════════════════════╝                                                 \n");
		
		System.out.println("         ╔═══════════════════════════════════════════════════════════════════════════════╗");
		
		System.out.println("         ║ "+Util.padRight(nickname, 78)+"║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");  
		
		
		if (body.length()<=70) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0,body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝"); 
		}
		
		if (body.length()>70 && body.length()<=140 ) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
		}
		
		if (body.length()>140 && body.length()<=209) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(140,body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
		}
		
		if (body.length()>209 && body.length()<=280) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(140, 210), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(210, body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
		}
		
	}
	
	public static void printDetailedComment(Comment comment) {
		String body= comment.getBody();
		
		System.out.println("         ╔═══════════════════╗"+                 "═══════════════════╗"+                    "═══════════════════════════════════════╗");
		System.out.println("         ║ "+Util.padRight(comment.getOwner(), 18)+"║"+" CID: "+Util.padRight(comment.getCid(), 13)+"║"+"          "+Util.getTimeString(comment.getTimestamp())+"          ║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");  
		
		
		if (body.length()<=70) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0,body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝"); 
		}
		
		if (body.length()>70 && body.length()<=140 ) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
		}
		
		if (body.length()>140 && body.length()<=209) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(140,body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
		}
		
		if (body.length()>209 && body.length()<=280) {
			System.out.println("         ║ Body:"+Util.padRight(body.substring(0, 70), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(70, 140), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(140, 210), 73)+"║");
			System.out.println("         ║      "+Util.padRight(body.substring(210, body.length()), 73)+"║");
			System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝");
		}
		
	}
	
	public static void printDetailedNotification(Notification notification) {
		Post p = Whistler.getInstance().getPost(notification.getItemIdentifier());  //itemIdentifier contains the postPid
		
		System.out.println("         ╔═══════════════════╗"+                 "═══════════════════╗"+                    "═══════════════════════════════════════╗");
		System.out.println("         ║ "+Util.padRight(notification.getWhistleblowerNickname(), 18)+"║"+" NID: "+Util.padRight(notification.getNid(), 13)+"║"+"          "+Util.getTimeString(notification.getTimestamp())+"          ║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════║");  
		
		System.out.println("         ║   "+Util.padRight("I've posted a new post - PID("+p.getPid()+") with title:", 76)+"║");
		System.out.println("         ║   "+Util.padRight("\""+p.getTitle()+"\"", 76)+"║");
		System.out.println("         ╚═══════════════════════════════════════════════════════════════════════════════╝"); 
		
		
	}
}
