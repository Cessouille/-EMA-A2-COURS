# Exercice 7

## `mvn clean`

### Which phases of the Maven lifecycle are executed?

Only the Clean phase.

### What new files or folders appear in `target/`?

Create no new files or folders and delete all content in `target/`.

## `mvn test`

### Which phases of the Maven lifecycle are executed?

The Clean, Validate, Compile and Test phases.

### What new files or folders appear in `target/`?

Create the classes, test classes, sources and maven status in `target/`.

## `mvn package`

### Which phases of the Maven lifecycle are executed?

The Clean, Validate, Compile, Test and Package phases.

### What new files or folders appear in `target/`?

Create the maven archiver and .jar of the application in `target/`.

## Run `mvn verify` and write down your hypothesis: how is verify different from test and package?

In my opinion, `mvn verify` is the same as doing `mvn test` and `mvn package`.
