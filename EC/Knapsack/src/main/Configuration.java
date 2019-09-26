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

    public int numberOfItems = 4; // 150;
    public int maximumCapacity = 9; //822;
    public int bestKnownOptimum = 1013;

    // ------------------------ GA config options ------------------------

    public double mutationChance = 0.8; // TODO
    public int crossoverPoints = 1;
    public int populationSize = 10;
    public int elite = 1;

    // ------------------------ GA config options ------------------------
}