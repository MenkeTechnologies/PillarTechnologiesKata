# Pillar Technology Kata
https://github.com/PillarTechnology/kata-babysitter

Dependencies are Git, Maven and Java

Instructions for Compiling, Running Tests and Running Main

```
git clone https://github.com/MenkeTechnologies/Pillar-Technology-Kata.git
cd Pillar-Technology-Kata
mvn clean install
java -cp target/com.jakobmenke.pillarkata-1.0.jar BabysitterCalculator.Main
```

To Run All Tests
```
cd Pillar-Technology-Kata
mvn test
    ```
To Run just the InvalidHoursStarting test (replace InvalidHoursStarting with another method name to run any other single test)
    ```
    mvn '-Dtest=BabysitterTest#InvalidHoursStarting test'
    ```


    Interaction type can be changed from interactive (read from command line) to noninteractive (data from getDataNonInteractively in Utilies.java) in BabysitterCalculator.Main
    ```
    //interactionType = "noninteractive";
    ```


# created by Jacob Menke
