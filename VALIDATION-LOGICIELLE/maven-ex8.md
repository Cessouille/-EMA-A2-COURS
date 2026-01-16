# Exercice 8

## Define account variable

```java
private BankAccount account;

@BeforeEach
void setUp() {
    account = new BankAccount();
}
```

## `depositMoney`

### Deposit money (Happy path)

```java
@Test
void shouldDepositMoney() {
    account.depositMoney(100);

    assertEquals(100, account.getBalance());
}
```

### Deposit negative money (Edge case)

```java
@Test
void shouldNotDepositMoney() {
    account.depositMoney(-100);

    assertEquals(0, account.getBalance());
}
```

## `withdrawMoney`

### Withdraw money (Happy path)

```java
@Test
void shouldWithdrawMoney() {
    account.setWithdrawLimit(500);
    account.depositMoney(100);
    account.withdrawMoney(50);

    assertEquals(50, account.getBalance());
}
```

### Withdraw more money than present on the account (Edge case)

```java
@Test
void shouldNotWithdrawMoney() {
    account.setWithdrawLimit(500);
    account.depositMoney(100);
    account.withdrawMoney(150);

    assertEquals(100, account.getBalance());
}
```
