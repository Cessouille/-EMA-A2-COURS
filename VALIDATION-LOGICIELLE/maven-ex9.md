# Exercice 9

## Before

![Code coverage before adding tests](images/coverage-before.png)

## After

![Code coverage after adding tests](images/coverage-after.png)

## Added test

```java
@Test
void shouldInitBankAccountWithBalance() {
    Person person = new Person();
    BankAccount bankAccount = new BankAccount(100, 1000, "date", person);

    assertEquals(100, bankAccount.getBalance());
}
```
