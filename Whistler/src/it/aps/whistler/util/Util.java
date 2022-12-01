package it.aps.whistler.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
}
