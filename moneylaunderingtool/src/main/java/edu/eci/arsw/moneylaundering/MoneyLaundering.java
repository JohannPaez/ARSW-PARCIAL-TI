package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.eci.arsw.moneylaunderingThread.DetectionThread;
import edu.eci.arsw.moneylaunderingThread.MoneyThread;

public class MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private ArrayList<List<Transaction>> listaDeTransaccionesTotales;
    private static DetectionThread detectionThread;
    public static ArrayList<MoneyThread> threadList = new ArrayList<>();;
    public static MoneyLaundering moneyLaundering;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        
    }
    
    public TransactionAnalyzer getTransactionAnalyzer() {
    	return transactionAnalyzer;
    }
    
    
    public AtomicInteger getAmountOfFilesProcessed() {
    	return amountOfFilesProcessed;
    }
    
    /** El metodo lee primero todos los archivos para poder asignarle a cada hilo sus correspondientes archivos
     * Por cuestion de tiempo se puede probar cambiando a la ruta de lectura src/main/prueba/ en la que hay menos archivos
     * 
     * @throws InterruptedException
     */
    public void processTransactionData() throws InterruptedException
    {	
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        

        listaDeTransaccionesTotales = new ArrayList<List<Transaction>>();
        for(File transactionFile : transactionFiles)
        {   
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            listaDeTransaccionesTotales.add(transactions);
           
        }
        
        System.out.println("Termino de leer archivos");
        int threads = 5;
        int num = amountOfFilesTotal / threads;
        int modulo = amountOfFilesTotal % threads;
        int cont = 0;    
        for (int i = 0; i < threads; i++) {
        	MoneyThread hilo = null;
        	ArrayList<List<Transaction>> transactionsPorHilo = new ArrayList<List<Transaction>>();
    		if (modulo > 0 && i == threads - 1) {
    			for (int j = 0; j < num + modulo; j++) {
    				transactionsPorHilo.add(listaDeTransaccionesTotales.get(cont));
    				cont++;
    			}
        	} else {
        		for (int j = 0; j < num; j++) {
        			transactionsPorHilo.add(listaDeTransaccionesTotales.get(cont));
    				cont++;
    			}
        	}
        	
        	hilo = new MoneyThread(transactionsPorHilo, this);
        	threadList.add(hilo);
        	hilo.start();
        	
        }
        
        
        for (MoneyThread MT : threadList) {
			MT.join();
		}
        
        System.out.println("Finish");
        detectionThread.finalizar();
        System.out.println("Presione enter para finalizar:");
    }

    public List<String> getOffendingAccounts()
    {	
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        // NOTA: Cambiar src/main/resources/ por src/main/prueba/ para ver el funcionamiento
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
    	
    	detectionThread = new DetectionThread();
    	detectionThread.start();
        moneyLaundering = new MoneyLaundering();
        Thread processingThread = new Thread(() -> {
			try {
				moneyLaundering.processTransactionData();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        processingThread.start();
        /*while(true)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
                break;
            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
        }*/

    }
    
    public static void notificar() {
    	String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }
    


}
