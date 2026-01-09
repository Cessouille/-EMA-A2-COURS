# Exercice 3

LOC = Lines of code\
WMC = Weighted Methods per Class\
CBO = Coupling Between Objects\
LCOM = Lack of Cohesion of Methods

| Class | LOC | WMC | CBO | LCOM | Quick notes |
| ----- | --- | --- | --- | ---- | ----------- |
| `Bank` | 412 | 14 | 3 | 0 | - |
| `BankAccount` | 469 | 21 | 2 | 46 | - |
| `Person` | 324 | 23 | 0 | 79 | - |

## Which class has the highest WMC?

The `Person` class has the highest WMC with a value of 23.

## Which class has the highest CBO?

The `BankAccount` class has the highest CBO with a value of 3.

## Looking at WMC + CBO + LCOM together, which class would you worry about most for future maintenance, and why?

The `Person` class, because it has the highest WMC and LCOM. This means it is complex and lacks cohesion, so it will be harder to maintain.
