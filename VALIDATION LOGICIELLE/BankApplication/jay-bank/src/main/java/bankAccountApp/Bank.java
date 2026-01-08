/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankAccountApp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 *
 * @author jay
 */
public class Bank {

	private static ArrayList<BankAccount> Accounts;
	private final int initialSize = 1000;
	private boolean success;
	// private double maximumBalance;
	private double averageBalance;
	// private double minumumBalance;
	private int accNumber = 1;
	private int accountsLoaded = 0;
	// Bank accManeger = new Bank();

	public Bank() {

		Accounts = new ArrayList<BankAccount>(initialSize);

	}

	public int addAccount(BankAccount acc, int isLoadAccount) {
		if (isLoadAccount == 0) {
			Accounts.add(acc);
			acc.setAccountNumber(accNumber);
			accountsLoaded++;
			accNumber++;
		}
		if (isLoadAccount == 1) {
			Accounts.add(acc);
			accountsLoaded++;
			return accountsLoaded;
		}
		if (isLoadAccount == 2) {
			int accnumber1 = accountsLoaded + 1;
			Accounts.add(acc);
			acc.setAccountNumber(accnumber1);
			accountsLoaded++;
		}
		return acc.getAccountNumber();
	}

	protected void setAccountsLoaded(int accountsLoaded) {
		this.accountsLoaded = accountsLoaded;
	}

	protected int getAccountsLoaded() {
		return accountsLoaded;
	}

	public BankAccount findAccount(int accountNumber) { // FIX THIS NOW have account creater start at 0 instead of 1
		int compareAccountNumber = 0;
		BankAccount correctAccount = null;
		for (int i = 0; i < Accounts.size(); i++) {
			BankAccount acc = Accounts.get(i);
			compareAccountNumber = acc.getAccountNumber();

			if (accountNumber == compareAccountNumber) {
				success = true;
				correctAccount = acc;
				break;
			}

		}
		if (success == true) {
			return correctAccount;
		} else {
			return null;
		}

	}

	public void deleteAccount(int accountNumber) {
		for (int i = 0; i < Accounts.size(); i++) {
			BankAccount acc = Accounts.get(i);
			int compareAccountNumber = acc.getAccountNumber();

			if (accountNumber == compareAccountNumber) {
				Accounts.remove(acc);
			}

		}
	}

	public double getAverageBalance() {
		double total = 0.0;

		for (int i = 0; i < Accounts.size(); i++) {
			BankAccount tmpAcc = Accounts.get(i);

			total += tmpAcc.getBalance();
		}
		averageBalance = total / Accounts.size();

		return averageBalance;
	}

	public double getMaximumBalance() {
		Double maximumBalance = 0.0;

		for (int i = 0; i < Accounts.size(); i++) {
			BankAccount tmpAcc = Accounts.get(i);

			if (tmpAcc.getBalance() > maximumBalance) {
				maximumBalance = tmpAcc.getBalance();
			}
		}

		return maximumBalance;
	}

	public double getMinimumBalance() { // if account 0 dosen't exist minimum is always 0
		Double minimumBalance = 0.0;

		for (int i = 0; i < Accounts.size(); i++) {

			BankAccount tmpAcc = Accounts.get(i);
			if (i == 0) {
				minimumBalance = tmpAcc.getBalance();
			}
			if (tmpAcc.getBalance() < minimumBalance) {
				minimumBalance = tmpAcc.getBalance();
			}
		}
		return minimumBalance;
	}

	public void saveAccounts(Bank accManager) {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			fos = new FileOutputStream("C:\\Users\\jay4k\\Desktop\\stuff\\Bankaccountinfo\\BankAccountinfotext.text");
			osw = new OutputStreamWriter(fos);
			for (int i = 0; i < Accounts.size(); i++) {
				BankAccount tmp = Accounts.get(i);

				osw.write(tmp.convertToText(tmp));

			}
		} catch (IOException e) {
			System.out.println("Error writing to file");
		} finally {
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					// no action
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// no action
				}
			}
		}

	}

	public String convertToText() {
		String allAccountInfo = "";
		for (int i = 0; i < Accounts.size(); i++) {
			BankAccount acc = Accounts.get(i);
			String AccountsInfo = acc.getAccountNumber() + Person.DELIM + acc.getBalance() + Person.DELIM
					+ acc.getWithdrawLimit() + Person.DELIM + acc.getDateCreated() + Person.DELIM
					+ acc.getAccountHolder();
			allAccountInfo += AccountsInfo;
		}
		return allAccountInfo;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<BankAccount> getAccounts() {
		return (ArrayList<BankAccount>) Accounts.clone();
	}

	public boolean registerAccount(int fromAccountNumber, int fromRoutingNumber, int destinationBank,
			int toAccountNumber) {
		return true;
	};

	public boolean transferAmount(int fromAccountNumber, int fromRoutingNumber, int destinationBank,
			int toAccountNumber, float amount) {
		ACHServiceImpl service = new ACHServiceImpl();
		service.transferAmount(fromAccountNumber, fromRoutingNumber, destinationBank, toAccountNumber, amount);
		return true;
	};

}
