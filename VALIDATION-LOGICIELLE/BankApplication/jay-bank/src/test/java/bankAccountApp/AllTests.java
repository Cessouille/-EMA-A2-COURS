package bankAccountApp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ACHServiceTest.class, BankAccountTest.class, BankTest.class, PersonTest.class })
public class AllTests {

}
