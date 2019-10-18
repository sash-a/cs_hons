# Meta heuristic knapsack solver

This was made according to the specification.pdf doc.  
It contains 4 algorithms for solving knapsack and 1 optimization algorithm  
All algorithms have a default and best config file in data/  
The data file is also situated in data/

## How to run

This is built to run in intellij.  
Command line options:
```-algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration```

## Optimization

Algorithm can be automatically optimized with the ```-search_best_configuration option``` this runs PSO on top of the given algorithm. In main/Configuration.java there is an option ```repeats``` this controls how many times to rerun the same hyperparameters of the meta-heuristic, during optimization, to consolidate accuracy.

## Algorithms
GA - genetic algorithm  
SA - simulated annealing  
ACO - ant colony optimization  
PSO - particle swarm optimization

