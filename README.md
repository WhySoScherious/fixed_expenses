# Fixed Expenses
Takes a person's credit report, parses it, and returns JSON describing both the parsed contents of the credit report and some derived facts.


## Usage
```shell
java -jar <jar name> [-f report path]
```


## Build executable
mvn package


## Examples
```shell
java -jar target/fixed_expenses-0.0.1-SNAPSHOT.jar -f testdata_given1.txt
```
```shell
cat testdata_given2.txt | java -jar target/fixed_expenses-0.0.1-SNAPSHOT.jar
```


## Notes
* I used Maven to create a jar executable and to manage test dependencies.
* No external/3rd party libraries were used in the main application solution.
* Java has no built in JSON API implementation :'‑(
