package bankAccountApp;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;

import bankAccountApp.BankAccount;
import org.junit.Test;

public class BankAccountTest {
	String name = "John";
	char gender = 'm';
	int age = 22;
	int weight = 190;
	float height = 172;
	String hairColor = "brown";
	String eyeColor = "green";
	String email = "jufm@gmail.com";

	int assignAccountNumber = 0;
	int accountNumberExists = 1;
	int initMoneyAmount = 5000;
	int withdrawLimit = 700;
	String dateCreated = "05/21/2019";
	BankAccount bankAccount = null;
	String text = "C:\\Users\\jay4k\\Desktop\\stuff\\Bankaccountinfo\\BankAccountinfotext.text";
	// String text = "/Users/markkelly/BankAccountinfotext.text";
	Bank bank = null;
	Person accountHolder = null;
	String serializedPerson = null;

	@Before
	public void setup() {
		// Create Person
		try {
			accountHolder = new Person(name, gender, age, weight, height, hairColor, eyeColor, email);
			serializedPerson = name + Person.DELIM + gender + Person.DELIM + age + Person.DELIM + height + Person.DELIM
					+ weight + Person.DELIM + hairColor + Person.DELIM + eyeColor + Person.DELIM + email;
		} catch (Exception e) {
			System.out.print("Unexpected failure during test setup creating accountHolder");
			e.printStackTrace();
		}
		bank = new Bank();
		bankAccount = new BankAccount(initMoneyAmount, withdrawLimit, dateCreated, accountHolder);
	}

	@Test
	public void test_create_vert1_and_gets() throws Exception {
		BankAccount acc1 = new BankAccount();
		bank.addAccount(acc1, assignAccountNumber);

		// Then
		assertEquals("balance error", 0, acc1.getBalance(), 0f);
		assertEquals("withdrawal error", 0, acc1.getWithdrawLimit(), 0f);
		assertEquals("amount withdrawn error", 0, acc1.getAmountWithdrawn(), 0f);
		assertEquals("date created error", "", acc1.getDateCreated());
		assertEquals("account holder error", null, acc1.getAccountHolder());
		assertEquals("account error", 1, acc1.getAccountNumber());
	}

	@Test
	public void test_create_vert2_and_gets() throws Exception {
		// Given
		BankAccount acc1 = new BankAccount(initMoneyAmount, withdrawLimit, dateCreated, accountHolder);
		bank.addAccount(acc1, assignAccountNumber);

		// Then
		assertEquals(5000, acc1.getBalance(), 0f);
		assertEquals(700, acc1.getWithdrawLimit(), 0f);
		assertEquals("05/21/2019", acc1.getDateCreated());
		assertEquals(accountHolder, acc1.getAccountHolder());
		assertSame(accountHolder, acc1.getAccountHolder());
		assertEquals(1, acc1.getAccountNumber());
	}

	@Test
	public void test_create_vert3_and_gets() throws Exception {
		// Given
		BankAccount acc1 = new BankAccount(1000, initMoneyAmount, withdrawLimit, dateCreated, serializedPerson);
		bank.addAccount(acc1, accountNumberExists);

		// Then
		assertEquals(5000, acc1.getBalance(), 0f);
		assertEquals(700, acc1.getWithdrawLimit(), 0f);
		assertEquals("05/21/2019", acc1.getDateCreated());
		assertNotNull(acc1.getAccountHolder());
		assertEquals(0, acc1.getInitMoneyAmount(), 0f);
		assertEquals(700, acc1.getWithdrawLimit(), 0f);
		Person person = acc1.getAccountHolder();
		assertEquals(name, person.getName());
		assertEquals(gender, person.getGender());
		assertEquals(age, person.getAge());
		assertEquals(height, person.getHeight(), 0);
		assertEquals(weight, person.getWeight(), 0);
		assertEquals(hairColor, person.getHairColor());
		assertEquals(eyeColor, person.getEyeColor());
		assertEquals(email, person.getEmail());
		assertEquals(1000, acc1.getAccountNumber());
	}

	@Test
	public void test_convert_to_text() {
		// Given
		BankAccount acc1 = new BankAccount(initMoneyAmount, withdrawLimit, dateCreated, accountHolder);
		bank.addAccount(acc1, assignAccountNumber);
		String serializedAccount = acc1.convertToText(acc1);

		// Then
		String[] tokens = serializedAccount.split(Person.DELIM);
		assertThat(tokens.length, equalTo(12)); 
		assertThat(Integer.valueOf(tokens[0]), equalTo(acc1.getAccountNumber()));
		assertThat(Double.valueOf(tokens[1]), equalTo(acc1.getBalance()));
		assertThat(Double.valueOf(tokens[2]), equalTo(acc1.getWithdrawLimit()));
		assertThat(tokens[3], equalTo(acc1.getDateCreated()));
		assertThat(tokens[4], equalTo(accountHolder.getName()));
		assertThat("gender error", tokens[5].charAt(0), equalTo((accountHolder.getGender())));
		assertThat("age error", Integer.valueOf(tokens[6]), equalTo(accountHolder.getAge()));
		assertThat("height error", Float.valueOf(tokens[7]), equalTo(accountHolder.getHeight()));
		assertThat("weight error", Float.valueOf(tokens[8]), equalTo(accountHolder.getWeight()));
		assertThat("hair color error", tokens[9], equalTo(accountHolder.getHairColor()));
		assertThat("eye color error", tokens[10], equalTo(accountHolder.getEyeColor()));
		assertThat("email error", tokens[11], equalTo(accountHolder.getEmail()));
	}

	@Test
	public void test_create_and_deposit_money() {
		// Given
		int depositamount = 200;
		bank.addAccount(bankAccount, assignAccountNumber);

		// Then
		assertEquals(5000, bankAccount.getBalance(), 0f);
		bankAccount.depositMoney(depositamount);
		assertEquals(5200, bankAccount.getBalance(), 0f);
	}

	@Test
	public void test_create_and_withdraw_money() throws Exception {
		// Given
		int withdrawamount = 200;
		bank.addAccount(bankAccount, assignAccountNumber);

		// Then
		assertEquals(5000, bankAccount.getBalance(), 0f);
		bankAccount.withdrawMoney(withdrawamount);
		assertEquals(200, bankAccount.getAmountWithdrawn(), 0f);
		assertEquals(4800, bankAccount.getBalance(), 0f);

	}

	@Test
	public void test_create_and_setWithdrawLimit_failure1() {
		// Given
		int withdrawamount = 800;
		bank.addAccount(bankAccount, assignAccountNumber);

		// Then
		bankAccount.withdrawMoney(withdrawamount);
		assertEquals(5000, bankAccount.getBalance(), 0f);

		assertEquals(700, bankAccount.getWithdrawLimit(), 0f);
		bankAccount.setWithdrawLimit(820);

		assertEquals(5000, bankAccount.getBalance(), 0f);
		// Exceed balance and check for failure (return = false)
		assertEquals(false, bankAccount.withdrawMoney(10000.00));
		assertEquals(5000, bankAccount.getBalance(), 0f);
	}

	@Test
	public void test_create_and_setWithdrawLimit_failure2() {
		// Given
		int withdrawamount = 800;
		bank.addAccount(bankAccount, assignAccountNumber);

		// Then
		assertEquals(700, bankAccount.getWithdrawLimit(), 0f);
		bankAccount.setWithdrawLimit(820);
		assertEquals(5000, bankAccount.getBalance(), 0f);
		bankAccount.withdrawMoney(withdrawamount);
		assertEquals(4200, bankAccount.getBalance(), 0f);
		// Next withdrawal exceeds daily withdrawal limit
		assertEquals(false, bankAccount.withdrawMoney(withdrawamount));
		assertEquals(4200, bankAccount.getBalance(), 0f);
	}

	@Test
	public void test_toString() {

		String actualBankAccountValue = bankAccount.toString();
		assertThat("account string part 1 incorrect", actualBankAccountValue,
				containsString("Your Account number is 0 Your Balance is: 5000.0"));
		assertThat("account string part 2 incorrect", actualBankAccountValue, containsString(
				"Date account created is: 05/21/2019 Withdraw limit is: 700.0 Your account holder info is: John"));
		assertThat("account string part 3 incorrect", actualBankAccountValue, containsString("190.0"));
		assertThat("account string part 4 incorrect", actualBankAccountValue, containsString("172.0"));
		assertThat("account string part 5 incorrect", actualBankAccountValue, containsString("brown"));
		assertThat("account string part 6 incorrect", actualBankAccountValue, containsString("green"));
		assertThat("account string part 7 incorrect", actualBankAccountValue, containsString("jufm@gmail.com"));
	}

}
