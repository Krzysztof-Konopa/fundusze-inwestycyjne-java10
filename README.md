### FUNDUSZE INVESTYCYJNE  ###

### ABOUT ###
Application for calculating investment base on computing.
Project "Fundusze Inwestycyjne" migrated from java8 to java10

### REQUIREMENTS ###

Java 10 installed (or higher)
Maven 3 (3.5.3 used in project)

### BUILD ###
Run from project root directory:

mvn clean install

### RUN ###
Run from project root directory:

java -jar investment-service/target/investment-service-1.0-SNAPSHOT.jar

navigate to localhost:8080/ in your browser

### HOW TO USE ###

1. Fill Cash field
2. Pick strategy from dropdown list
3. Fill table like in task examples
    - only type column is mandatory to fill
    - [Add] button adds row into table
    - [-] button removes row
4. Hit [Compute] button to compute result.
5. As a result you should see table with computed results below [Compute] button.

