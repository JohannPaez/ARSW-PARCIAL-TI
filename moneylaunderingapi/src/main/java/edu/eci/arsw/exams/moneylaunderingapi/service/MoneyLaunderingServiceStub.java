package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingPersistence.MoneyLaunderingNotFoundException;
import edu.eci.arsw.exams.moneylaunderingPersistence.MoneyLaunderingPersistenceException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


@Component("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
	
	ArrayList<SuspectAccount> sa = new ArrayList<SuspectAccount>();
	
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyLaunderingNotFoundException {
    	SuspectAccount suspect = getAccountStatus(suspectAccount.getAccountId());
    	if (suspect == null) throw new MoneyLaunderingNotFoundException("Not found Account" + suspectAccount);
 
    	for (SuspectAccount sa: this.sa) {
	    	if (sa.getAccountId().equals(suspectAccount.getAccountId())) {
	    		sa.setAmountOfSmallTransactions(suspectAccount.getAmountOfSmallTransactions());
	    	}
    	}
    	
    }
    
    @Override
    public void addSuspectAccount( SuspectAccount suspectAccount) throws MoneyLaunderingPersistenceException 
    {
    	
    	SuspectAccount sa = getAccountStatus(suspectAccount.getAccountId());
    	if (sa != null) {
    		throw new MoneyLaunderingPersistenceException("The given suspectAccount already exists:" + suspectAccount);
    	}
    	this.sa.add(suspectAccount);
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
    	for (SuspectAccount sa: this.sa) {
        	if (sa.getAccountId().equals(accountId)) return sa;
        }
        
        return null;
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        return sa;
    }
}
