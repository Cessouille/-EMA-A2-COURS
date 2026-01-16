# Exercice 2

## 1. Chosen method

The method `withdrawMoney` has a cyclomatic complexity value of 5.

```java
public boolean withdrawMoney(double withdrawAmount) {
        if (                                                    // decision point (4 conditions)
            withdrawAmount >= 0 && 
            balance >= withdrawAmount && 
            withdrawAmount < withdrawLimit && 
            withdrawAmount + amountWithdrawn <= withdrawLimit 
        ) {
            balance = balance - withdrawAmount;
            success = true;
            amountWithdrawn += withdrawAmount;
        } else {                                                // decision point (1 condition)
            success = false;
        }
        return success;
    }
```

## 2. Proposed refactoring

I would extract the if with 4 conditions into a helper method, it would greatly decrease the cyclomatic complexity.

I would name the helper method `checkWithdrawConditions`.

## 3. Bonus

```java
public boolean withdrawMoney(double withdrawAmount) {
    if (checkWithdrawConditions(withdrawAmount)) {          // decision point (1 condition)
        balance = balance - withdrawAmount;
        success = true;
        amountWithdrawn += withdrawAmount;
    } else {                                                // decision point (1 condition)
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
```

The method `withdrawMoney` now has a cyclomatic complexity value of 2.
