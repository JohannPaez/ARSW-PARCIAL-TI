package edu.eci.arsw.exams.moneylaunderingPersistence;

public class MoneyLaunderingNotFoundException extends Exception {
	
	 public MoneyLaunderingNotFoundException(String message) {
	        super(message);
	    }

	    public MoneyLaunderingNotFoundException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
