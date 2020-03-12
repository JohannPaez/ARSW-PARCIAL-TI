package edu.eci.arsw.moneylaunderingThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.eci.arsw.moneylaundering.MoneyLaundering;
import edu.eci.arsw.moneylaundering.Transaction;
import edu.eci.arsw.moneylaundering.TransactionAnalyzer;

public class MoneyThread extends Thread {
	
	private ArrayList<List<Transaction>> transactions;
	private MoneyLaundering ml;
	
	public MoneyThread(ArrayList<List<Transaction>> transactions,  MoneyLaundering ml) {
		this.transactions = transactions;
		this.ml = ml;
	}
	
	
	@Override
	public void run() {
		for(List<Transaction> t : transactions) {
			if (DetectionThread.pausa) {
				try {
					synchronized(this) {
						wait();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for(Transaction transaction : t) {
				//System.out.println("ESTOY ACA");
				ml.getTransactionAnalyzer().addTransaction(transaction);
            }
			ml.getAmountOfFilesProcessed().incrementAndGet();
        }
	}
	
	public synchronized void contine() {
		notifyAll();
	}
}
