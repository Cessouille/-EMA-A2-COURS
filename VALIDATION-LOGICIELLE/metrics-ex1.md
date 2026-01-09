# Exercice 1

LOC = Lines of code\
NOM = Number of lines

| Class | LOC | NOM | Responsability |
| ----- | --- | --- | -------------- |
| `BankAccount` | 469 | 20 | Get and set information about bank account |
| `Person` | 324 | 23 | Get, set and validate information about person |
| `BankAccountApp` | 482 | 2 | Initialize bank account |
| `Bank` | 412 | 14 | Get information about bank and its accounts, CRUD on accounts |

## Do you feel its size roughly matches its responsibility?

Not necessarily, for example, BankAccount has a lot of LOC and 20 methods but most of them are getters/setters. On the other hand, BankAccountApp has 482 LOC but only 2 methods, one being the main method with a cyclomatic complexity of 40. This method could benefit from refactoring.
