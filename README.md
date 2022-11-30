# CCN-practical-1
For just one sample run Practical.java with the desired population size, mutation rate and if there is crossover(N for no, Y for yes). (default 100, 0.01 and 1000 respectively) <br/>
```
java Practical populationSize mutationRate isCrossover
javac Practical.java
java Practical 100 0.01 Y
```
---
For multiple samples run Tester.java with the desired population size, mutation rate, if there is crossover(N for no, Y for yes) and total amount of samples. (default 100, 0.01 and 1000 respectively) <br/>
```
java Tester populationSize mutationRate isCrossover sampleSize
javac Tester.java
java Tester 100 0.01 Y 1000
```
creates a text file called
```
"test" + "_p" + popSize + "_m" + mutationRate + "_c" + "Y" or "N" "_ss" + sampleSize + ".txt"
"test_p100_m0.01_cN_ss1000.txt"
```
