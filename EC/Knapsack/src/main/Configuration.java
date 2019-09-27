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

    // ------------------------ GA config options ------------------------

    public enum MutationType
    {
        BITFLIP,
        DISPLACEMENT,
        EXCHANGE,
        INSERTION,
        INVERRSION
    }

    public double mutationChance = 0.1;
    public int populationSize = 50;
    public int elite = 1;
    public int generations = 100000;
    public int validAttempts = 100;

    public int crossoverPoints = 1;
    public MutationType mutationType = MutationType.BITFLIP;

    // ------------------------ GA config options ------------------------
}