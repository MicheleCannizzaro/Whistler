package it.aps.whistler.util;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class WhistlerLogger {
	private final static Logger logger = Logger.getLogger(WhistlerLogger.class.getName());
	static {
		try {
			
			InputStream stream = WhistlerLogger.class.getClassLoader().getResourceAsStream("it/aps/whistler/util/logging.properties");
			LogManager.getLogManager().readConfiguration(stream);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.warning("[WhistlerLogger] Initialized properly.");
		}
		
		logger.setFilter(new CustomLogFilter());
		logger.logp(Level.INFO,WhistlerLogger.class.getSimpleName(),"no metod static initialization","WhistlerLogger was initialized properly.");
	}
}
