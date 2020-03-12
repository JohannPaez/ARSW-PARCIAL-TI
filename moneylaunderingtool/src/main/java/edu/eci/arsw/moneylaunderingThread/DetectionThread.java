package edu.eci.arsw.moneylaunderingThread;

import java.util.Scanner;

import edu.eci.arsw.moneylaundering.MoneyLaundering;

public class DetectionThread extends Thread {
	
	public static boolean pausa = false;
	int cont = 0;
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Running");
		while (scanner.nextLine() != null) {
			cont++;
			System.out.println("Enter# " + cont);
			pausa = !pausa;
			if (!pausa) {
				System.out.println("Running");
				for (MoneyThread mt: MoneyLaundering.threadList) {
					mt.contine();
				}				
			} else {
				System.out.println("Pausa");
				MoneyLaundering.notificar();
			}
		}
	}
	
	public void finalizar() {
		for (MoneyThread mt: MoneyLaundering.threadList) {
			mt.stop();
		}
		stop();
		MoneyLaundering.notificar();
	}
	
}
