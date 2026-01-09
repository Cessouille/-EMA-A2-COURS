# Exercice 4

## List 3 issues that you consider important

>Define a constant instead of duplicating this literal "Account doesn't exist" 4 times.\
BankAccountApp.java:139

This issue has a high impact on maintainability because if the value has to be updated, we would have to change it 4 times. The use of a constant would allow us to modify its value only once.

---
>Refactor this method to reduce its Cognitive Complexity from 125 to the 15 allowed.\
BankAccountApp.java:20

This issue has a high impact on maintainability because it has a high Cognitive Complexity meaning that this method is hard to read, understand, test and modify.

---
>Change this "try" to a try-with-resources.\
Bank.java:144

This issue has a high impact on maintainability because it is not sure that the resource will be closed which can lead to memory leaks.

## Fix at least 2 of these issues

### First issue

Before

```java
System.out.println("Account dosen't exist");
```

After

```java
private static final String ACCOUNT_NOT_EXIST = "Account dosen't exist";
...
System.out.println(ACCOUNT_NOT_EXIST);
```

### Second issue

Before

```java
System.out.println(ACCOUNT_NOT_EXIST);
```

After

```java
private static final Logger logger = Logger.getLogger(BankAccountApp.class.getName());
...
logger.info(ACCOUNT_NOT_EXIST);
```

## Re-run SonarLint on the same scope

Before fixing the issues

>Found 108 issues in 10 files

After fixing the issues

>Found 106 issues in 10 files

## Do SonarLint issues appear more often in the classes with higher WMC / CBO you saw earlier, or not really?

Not really, the `Person` class does not have that many issues (only 9) even though it has the highest WMC and LCOM.
