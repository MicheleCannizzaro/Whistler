package it.aps.whistler.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import it.aps.whistler.Visibility;
import it.aps.whistler.domain.Post;

public class Util {

	//Useful to generate pid value for post
	public static String getRandomHexString(){
        Random rand = new Random();
        int number = rand.nextInt(999999); //generate a random number from 0 to 999999 
        
        if(number!=0) {
        	return String.format("%06d", number);
        }
        return getRandomHexString();
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
		
		System.out.println("         ╔═══════════════════╗"+                 "═══════════════════╗"+                    "═════════════════════╗");
		System.out.println("         ║  "+Util.padRight(p.getOwner(),17)+"║"+" PID: "+Util.padRight(p.getPid(), 13)+"║"+" "+Util.getTimeString(p.getTimestamp())+" ║");
		System.out.println("         ║═══════════════════════════════════════════════════════════════════════════════╗");
		
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
			
		System.out.println("         ║ Keywords:"+Util.padRight(p.getPostKeywords().toString(), 69)+"║");
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
}
