/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankAccountApp;

import java.util.Scanner;

/**
 *
 * @author jay
 */
public class BankAccountApp {


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		// Person accountHolder = null;
		// BankAccount acc1 = null;
		String operation = "";
		boolean endProgram = false;
		String options = "";
		int number = 0;
		// double averageBalance = 0;
		boolean mainmenu = true;
		boolean notMainMenu = false;
		boolean newAccountOperations = false;
		int ifloadaccManager = 0;
		Scanner scan = new Scanner(System.in);
		Bank accManager = new Bank();
		BankAccount bankAccountManager = new BankAccount();
		String text = "C:\\Users\\jay4k\\Desktop\\stuff\\Bankaccountinfo\\BankAccountinfotext.text";
		// accountsLoaded = bankAccountManager.loadFromText(text);
		accManager.setAccountsLoaded(bankAccountManager.loadFromText(text));

		System.out.println("Loaded " + accManager.getAccountsLoaded() + " accounts.");

		while (endProgram == false) {
			while (mainmenu == true) {

				System.out.println("To create a new account enter new,  " + "\n"
						+ "to perform operations in an existing account enter account , " + "\n"
						+ "To delete an existing account enter delete " + "\n"
						+ "to diplay the average of all account balances enter Average " + "\n"
						+ "To display the maximum account balance enter maximum" + "\n"
						+ "to display the minimum account balance enter minimum" + "\n" + "to quit enter quit");

				options = scan.next();
				if (options.equalsIgnoreCase("new")) {
					if (accManager.getAccountsLoaded() > 0) {
						ifloadaccManager = 2;
					} else if (accManager.getAccountsLoaded() == 0) {
						ifloadaccManager = 0;
					}
					notMainMenu = true;
					while (notMainMenu == true) {
						System.out.println(
								"Enter account holder's name, gender, age,weight, height, hair color, eye color, email, "
										+ " initial amount of money,withdraw limit and date created");
						String name = scan.next();
						char gender = scan.next().charAt(0);
						int age = scan.nextInt();
						float weight = scan.nextFloat();
						float height = scan.nextFloat();
						String hairColor = scan.next();
						String eyeColor = scan.next();
						String email = scan.next();
						double initMoneyAmount = scan.nextDouble();
						double withdrawLimit = scan.nextDouble();
						String dateCreated = scan.next();
						Person accountHolder = null;
						try {
							accountHolder = new Person(name, gender, age, weight, height, hairColor, eyeColor,
									email);
						} catch (Exception e) {
							e.printStackTrace();
						}
						BankAccount acc1 = new BankAccount(initMoneyAmount, withdrawLimit, dateCreated, accountHolder);

						accManager.addAccount(acc1, ifloadaccManager);
						System.out.println("Your account number is " + acc1.getAccountNumber());
						newAccountOperations = true;
						while (newAccountOperations == true) {
							System.out.println("Enter one of these operations:" + "/n"
									+ " DEPOSIT, WITHDRAW, BALANCE, or MAINMENU ");
							operation = scan.next();

							if (operation.equalsIgnoreCase("BALANCE")) {
								System.out.println("Balance is: " + acc1.getBalance());
							}

							if (operation.equalsIgnoreCase("DEPOSIT")) {
								System.out.println("Enter an amount to deposit");
								double depositAmount = scan.nextDouble();
								acc1.depositMoney(depositAmount);
							}
							if (operation.equalsIgnoreCase("WITHDRAW")) {
								System.out.println("Enter an amount to withdraw");
								double withdrawAmount = scan.nextDouble();
								boolean success = acc1.withdrawMoney(withdrawAmount);
								if (success == false) {
									System.out.println("Unable to complete transaction"
											+ " because of insufficient funds or reached" + " withdraw limit. ");
								}
							}

							if (operation.equalsIgnoreCase("MAINMENU")) {
								newAccountOperations = false;
								notMainMenu = false;
								break;
							}
						}
					}
				}

				if (options.equalsIgnoreCase("AVERAGE")) {
					System.out.println("Average of accounts is " + accManager.getAverageBalance());
				}
				
				if (options.equalsIgnoreCase("MAXIMUM")) {
					System.out.println("The maximum of all accounts is " + accManager.getMaximumBalance());
				}
				if (options.equalsIgnoreCase("minimum")) {
					System.out.println("The minimum of all accounts is " + accManager.getMinimumBalance());
				}
				if (options.equalsIgnoreCase("quit")) {
					endProgram = true;
					mainmenu = false;
				}
				if (options.equalsIgnoreCase("delete")) {
					System.out.println("Enter account number of account to delete");
					number = scan.nextInt();
					BankAccount tmpacc = accManager.findAccount(number);
					if (tmpacc == null) {
						System.out.println("Account dosen't exist");
						break;
					}
					accManager.deleteAccount(number);
				}

				if (options.equalsIgnoreCase("Account")) {
					System.out.println(
							"Enter one of these operations:" + "/n" + " DEPOSIT, WITHDRAW, BALANCE, MENU or QUIT. ");
					operation = scan.next();
					if (operation.equalsIgnoreCase("BALANCE")) {
						System.out.println("Enter Account Number");
						number = scan.nextInt();
						BankAccount tmpacc = accManager.findAccount(number);
						if (tmpacc == null) {
							System.out.println("Account dosen't exist");
							break;
						}
						System.out.println("Balance is: " + tmpacc.getBalance());
					}

					if (operation.equalsIgnoreCase("DEPOSIT")) {
						System.out.println("Enter Account number");
						number = scan.nextInt();
						BankAccount tmpacc = accManager.findAccount(number);
						if (tmpacc == null) {
							System.out.println("Account dosen't exist");
							break;
						} else {
							System.out.println("Enter an amount to deposit");
						}
						double depositAmount = scan.nextDouble();
						tmpacc.depositMoney(depositAmount);
					}
					if (operation.equalsIgnoreCase("WITHDRAW")) {
						System.out.println("Enter Account number");
						number = scan.nextInt();
						BankAccount tmpacc = accManager.findAccount(number);
						if (tmpacc == null) {
							System.out.println("Account dosen't exist");
							break;
						} else {
							System.out.println("Enter an amount to withdraw");
						}
						double withdrawAmount = scan.nextDouble();
						boolean success = tmpacc.withdrawMoney(withdrawAmount);
						if (success == false) {
							System.out.println("Unable to complete transaction"
									+ " because of insufficient funds or reached" + " withdraw limit. ");
						} else {
							System.out.println("Withdraw succeded");
						}

					}
					if (operation.equalsIgnoreCase("quit")) {
						endProgram = true;
						mainmenu = false;
					}
					if (!operation.equalsIgnoreCase("BALANCE") && !operation.equalsIgnoreCase("DEPOSIT")
							&& !operation.equalsIgnoreCase("WITHDRAW") && !operation.equalsIgnoreCase("QUIT")
							&& !operation.equalsIgnoreCase("MAXIMUM") && !operation.equalsIgnoreCase("MINIMUM")
							&& !operation.equalsIgnoreCase("AVERAGE") && !operation.equalsIgnoreCase("DELETE")) {
						System.out.println("Invalid Command, please try again");
					}
				}
			}
		}
		
		if (scan != null)
			scan.close();
		
		try {
			accManager.saveAccounts(accManager);
		} catch (Exception e) {
			System.out.println("Error writing to file");
		}
		finally {
			if (scan != null)
				scan.close();
		}
	}
}
