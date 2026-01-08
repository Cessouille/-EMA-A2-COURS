package bankAccountApp;

public interface ACHService {
	/**
	 * Register account to transfer funds to another bank/account
	 * @param fromAccountNumber
	 * @param fromRoutingNumber
	 * @param destinationBank
	 * @param toAccountNumber
	 * @return
	 */
	public boolean registerAccount(int fromAccountNumber, int fromRoutingNumber, int destinationBank, int toAccountNumber);
	
	/**
	 * Transfer funds to bank/account
	 * @param fromAccountNumber
	 * @param fromRoutingNumber
	 * @param destinationBank
	 * @param toAccountNumber
	 * @param amount
	 * @return
	 */
	public boolean transferAmount(int fromAccountNumber,int fromRoutingNumber,int destinationBank, int toAccountNumber, float amount);

}
