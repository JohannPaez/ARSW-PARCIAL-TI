package edu.eci.arsw.exams.moneylaunderingapi;

import edu.eci.arsw.exams.moneylaunderingPersistence.MoneyLaunderingNotFoundException;
import edu.eci.arsw.exams.moneylaunderingPersistence.MoneyLaunderingPersistenceException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
@RequestMapping(value = "/fraud-bank-accounts")
public class MoneyLaunderingController
{	
	
	@Autowired 
	@Qualifier("MoneyLaunderingServiceStub")
    MoneyLaunderingService moneyLaunderingService;

    
    @RequestMapping(method = GET )
    public ResponseEntity<?> offendingAccounts()
    {
			return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(),HttpStatus.ACCEPTED);
		
			
			
    }
    
    @RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<?> saveSuspectAccount(@RequestBody SuspectAccount suspectAccount){
	    	boolean flag = false;
	        //registrar SuspectAccount
			try {
				moneyLaunderingService.addSuspectAccount(suspectAccount);
				flag = true;
			} catch (MoneyLaunderingPersistenceException e) {
				return new ResponseEntity<>("Error, No SuspectAccount add",HttpStatus.NOT_FOUND);
			}
	        return new ResponseEntity<>(flag, HttpStatus.CREATED);
	}
    
    
    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllSuspectAccountById(@PathVariable String accountId){
    	
    	return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId),HttpStatus.ACCEPTED);
	}	
    
    
    
    
    @PutMapping(value = "/{accountId}")
	public ResponseEntity<?> getAllSuspectAccountByIdAndPut(@PathVariable String accountId, @RequestBody SuspectAccount suspectAccount){
		boolean flag = false;
		try {
			moneyLaunderingService.updateAccountStatus(suspectAccount);
			flag = true;
		} catch (MoneyLaunderingNotFoundException e) {
			return new ResponseEntity<>("Error, No SuspectAccount put",HttpStatus.NOT_FOUND);
		}
      
	    return new ResponseEntity<>(flag, HttpStatus.ACCEPTED);
	}

}
