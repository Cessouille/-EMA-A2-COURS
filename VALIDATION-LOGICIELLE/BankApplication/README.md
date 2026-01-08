# BankApplication
This is a Java console application to simulate banking activity. The application is quite simple but it provides a way to show examples on how to use JUnit, Hamcrest and Mockito.

  **Application Java Classes**
  
  - Bank - institution owning accounts
  - BankAccount - account held by bank on behalf of a customer
  - Person - customer of bank
  - BankAccountApp - simple console interface to test classes
  
## Following Banking transactions are simulated
  1. Add Account
  2. Delete Account
  3. Edit Account
  4. Deposit
  5. Withdrawal
  6. Banking Account Aggregate Metrics
  
## Automated Testing

    ** Test Java Classes using JUnit, Hamcrest and Mockito **
  
  - BankTest - test Bank class
  - BankAccountTest - test BankAccount class
  - PersonTest - test Person class
  - ACHServiceTest - simple use of Mochito
  - AllTests - runs all test

## Running the Project

 1. Install Ecplise
 2. Setup a Maven Project to load the BankApplication
 3. Invoke BankAccountApp Main to launch console interface
 4. Run test classes as JUnit tests using Ecplise Run as Junit interface.
