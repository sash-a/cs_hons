package main;

import random.MersenneTwisterFast;

public enum Configuration
{
    instance;

    public String fileSeparator = System.getProperty("file.separator");
    public String userDirectory = System.getProperty("user.dir");
    public String dataDirectory = userDirectory + fileSeparator + "data" + fileSeparator;
    public String dataFilePath = dataDirectory + "knapsack_instance.csv";
    public String dataRDirectory = userDirectory;

    public MersenneTwisterFast randomGenerator = new MersenneTwisterFast(System.nanoTime());

    public int numberOfItems = 150;
    public int maximumCapacity = 822;
    public int bestKnownOptimum = 1013;
    public int numGenerations = 1000;

    // ------------------------ GA config options ------------------------

    public enum MutationType
    {
        BITFLIP,
        DISPLACEMENT,
        EXCHANGE,
        INSERTION,
        INVERSION
    }

    public double mutationChance = 0.1;
    public int populationSize = 50;
    public int elite = 1;
    public int validAttempts = 1000;

    public int crossoverPoints = 2;
    public MutationType mutationType = MutationType.INVERSION;

    // ------------------------ SA config options ------------------------

    public double initialTemp = 1E10;
    public double minTemp = 1.0;
    public double coolingRate = 0.999997;

    // ------------------------ ACO config options ------------------------

    public double alpha = 1.2;
    public double beta = 1;
    public double evaporationRate = 0.7;

    public final double initialPheromoneValue = 2;
    public final int numAnts = 10;

    // ------------------------ PSO config options ------------------------

    public double inertia = 1;
    public double localForce = 1; // Cognitive factor
    public double globalForce = 1; // Social factor

    public int numParticles = 20;

    public double vmax = 5;
    public double vmin = -5;
}