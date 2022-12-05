package it.aps.whistler.util;

import java.util.ArrayList;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class CustomLogFilter implements Filter {
	public boolean isLoggable(LogRecord logRecord) {
		ArrayList<String> filteredLogRecords = new ArrayList<>(); 
		
		if (Pattern.matches("^com.mchange.*", logRecord.getLoggerName())) {
			filteredLogRecords.add(logRecord.getLoggerName());
		}
		
		if (Pattern.matches("^org.hibernate.*", logRecord.getLoggerName())) {
			filteredLogRecords.add(logRecord.getLoggerName());
		}
		
		if (Pattern.matches("^javax.xml.*", logRecord.getLoggerName())) {
			filteredLogRecords.add(logRecord.getLoggerName());
		}
		
		if (Pattern.matches("^org.jboss.*", logRecord.getLoggerName())) {
			filteredLogRecords.add(logRecord.getLoggerName());
		}
		
		if (Pattern.matches("^com.sun.*", logRecord.getLoggerName())) {
			filteredLogRecords.add(logRecord.getLoggerName());
		}
		
		if (filteredLogRecords.contains(logRecord.getLoggerName())) return false;
		
        return true;
    }

}
