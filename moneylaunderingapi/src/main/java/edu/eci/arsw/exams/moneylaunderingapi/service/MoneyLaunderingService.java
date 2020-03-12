package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingPersistence.MoneyLaunderingNotFoundException;
import edu.eci.arsw.exams.moneylaunderingPersistence.MoneyLaunderingPersistenceException;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyLaunderingNotFoundException;
    SuspectAccount getAccountStatus(String accountId);
    List<SuspectAccount> getSuspectAccounts();
	void addSuspectAccount(SuspectAccount suspectAccount) throws MoneyLaunderingPersistenceException;
}
