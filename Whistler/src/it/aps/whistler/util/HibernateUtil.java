package it.aps.whistler.util;

import java.util.logging.Level;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
	
    private static SessionFactory sessionFactory = initHibernateUtil();

    private static SessionFactory initHibernateUtil() {
    	java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        try {
        	
            return new Configuration().configure("/it/aps/whistler/util/hibernate.cfg.xml").buildSessionFactory();
        } catch (HibernateException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
