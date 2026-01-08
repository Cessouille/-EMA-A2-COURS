/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankAccountApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author jay
 */
public class BankAccount {

	private Person accountHolder;
	private double balance = 0;
	private String dateCreated;
	private double withdrawLimit = 0;
	private boolean success;
	private double initMoneyAmount = 0;
	private int accountNumber = 0;
	private double amountWithdrawn = 0;

	public BankAccount() {
		// Person Person = new Person();
		initMoneyAmount = 0;
		this.withdrawLimit = 0;
		this.balance = 0;
		this.dateCreated = "";
		this.accountHolder = null;
		this.accountNumber = 0;
	}

	/**
	 * @return the accountHolder
	 */
	public BankAccount(double newInitMoneyAmount, double newWithdrawlimit, String newDateCreated, Person initHolder) {

		initMoneyAmount = newInitMoneyAmount;
		balance = newInitMoneyAmount;
		withdrawLimit = newWithdrawlimit;
		dateCreated = newDateCreated;
		accountNumber = 0;
		accountHolder = initHolder;
	}

	public BankAccount(int accountNumber, double balance, double withdrawLimit, String dateCreated,
			String accountHolder) throws Exception {
		Person Person = new Person(accountHolder);
		initMoneyAmount = 0;
		this.withdrawLimit = withdrawLimit;
		this.balance = balance;
		this.dateCreated = dateCreated;
		this.accountHolder = Person;
		this.accountNumber = accountNumber;
	}

	public Person getAccountHolder() {
		// accountHolder = acc.getAccountHolder(acc);

		return accountHolder;
	}

	/**
	 * @param accountHolder the accountHolder to set
	 */
	public void setAccountHolder(Person accountHolder) {

		if (accountHolder != null) {
			this.accountHolder = accountHolder;
		}
		// accountHolder = Person.getName() ;
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	private void setBalance(double balance) {

		this.balance = balance;
	}

	/**
	 * @return the dateCreated
	 */
	public String getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated the dateCreated to set
	 */
	private void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the withdrawLimit
	 */
	public double getWithdrawLimit() {
		return withdrawLimit;
	}

	/**
	 * @param withdrawLimit the withdrawLimit to set
	 */
	public void setWithdrawLimit(double withdrawLimit) {
		this.withdrawLimit = withdrawLimit;
	}

	public double getAmountWithdrawn() {
		return amountWithdrawn;
	}

	public void depositMoney(double depositAmount) {
		if (depositAmount >= 0) {
			balance = balance + depositAmount;
		}
	}

	// public boolean withdrawMoney(double withdrawAmount) {
	// if (withdrawAmount >= 0 && balance >= withdrawAmount && withdrawAmount <
	// withdrawLimit
	// && withdrawAmount + amountWithdrawn <= withdrawLimit) {
	// balance = balance - withdrawAmount;
	// success = true;
	// amountWithdrawn += withdrawAmount;
	// } else {
	// success = false;
	// }
	// return success;
	// }

	public boolean withdrawMoney(double withdrawAmount) {
		if (checkWithdrawConditions(withdrawAmount)) { // decision point (1 condition)
			balance = balance - withdrawAmount;
			success = true;
			amountWithdrawn += withdrawAmount;
		} else { // decision point (1 condition)
			success = false;
		}
		return success;
	}

	private boolean checkWithdrawConditions(double withdrawAmount) {
		return withdrawAmount >= 0 &&
				balance >= withdrawAmount &&
				withdrawAmount < withdrawLimit &&
				withdrawAmount + amountWithdrawn <= withdrawLimit;
	}

	public void setAccountNumber(int accNumber) {
		accountNumber = accNumber;

	}

	public int getAccountNumber() {

		return accountNumber;
	}

	public double getInitMoneyAmount() {
		return initMoneyAmount;
	}

	public int loadFromText(String text) {
		int accountsLoaded = 0;
		Bank accManager = new Bank();
		FileInputStream fis = null;
		Scanner fileScanner = null;
		try {
			while (fileScanner.hasNextLine()) {
				fis = new FileInputStream(text);
				fileScanner = new Scanner(fis);
				BankAccount tmpAccount = new BankAccount();
				tmpAccount.setAccountNumber(fileScanner.nextInt());
				tmpAccount.setBalance(fileScanner.nextDouble());
				tmpAccount.setWithdrawLimit(fileScanner.nextDouble());
				tmpAccount.setDateCreated(fileScanner.next());
				String newName = fileScanner.next();
				char gender = fileScanner.next().charAt(0);
				int newAge = fileScanner.nextInt();
				float newWeight = fileScanner.nextFloat();
				float newHeight = fileScanner.nextFloat();
				String newHairColor = fileScanner.next();
				String newEyeColor = fileScanner.next();
				String newEmail = fileScanner.next();
				String accountHolder1 = newName + Person.DELIM + gender + Person.DELIM + newAge + Person.DELIM
						+ newHeight + Person.DELIM + newWeight + Person.DELIM + newHairColor + Person.DELIM
						+ newEyeColor + Person.DELIM + newEmail;
				Person accountHolderManager = new Person(accountHolder1);
				tmpAccount.setAccountHolder(accountHolderManager);
				accountsLoaded = accManager.addAccount(tmpAccount, 1);
			}
			fis.close();

		} catch (Exception e) {
			System.out.println("Error reading file");
		} finally {
			if (fis != null) {
				try {
					fis.close();

				} catch (IOException ex) {
					// ignore
				}
			}
			if (fileScanner != null) {
				fileScanner.close();
			}
		}
		return accountsLoaded;
	}

	public String toString() {

		String bankInfo = "Your Account number is " + accountNumber + " " + "Your Balance is: " + balance + " "
				+ "Date account created is: " + dateCreated + " " + "Withdraw limit is: " + withdrawLimit + " "
				+ "Your account holder info is: " + accountHolder;

		return bankInfo;
	}

	public String convertToText(BankAccount tmp) {

		String AccountsInfo = tmp.getAccountNumber() + Person.DELIM + tmp.getBalance() + Person.DELIM
				+ tmp.getWithdrawLimit() + Person.DELIM + tmp.getDateCreated() + Person.DELIM + tmp.getAccountHolder();
		// allAccountInfo += AccountsInfo;

		return AccountsInfo;
	}
}
