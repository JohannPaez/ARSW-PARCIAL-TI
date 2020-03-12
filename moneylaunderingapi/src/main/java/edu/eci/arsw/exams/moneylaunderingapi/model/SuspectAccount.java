package edu.eci.arsw.exams.moneylaunderingapi.model;

public class SuspectAccount {
    public String accountId;
    public int amountOfSmallTransactions;
    
    public SuspectAccount()
    {
    }

    public String getAccountId()
    {
        return accountId;
    }


    public String getAmountOfSmallTransactions()
    {
        return Integer.toString(amountOfSmallTransactions);
    }

    public void setAmountOfSmallTransactions( String amountOfSmallTransactions )
    {
        this.amountOfSmallTransactions = Integer.parseInt(amountOfSmallTransactions);
    }
}
