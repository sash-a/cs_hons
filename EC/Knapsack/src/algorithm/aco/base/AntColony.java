package algorithm.aco.base;

import main.Configuration;

public class AntColony
{
    public double[][] pheromones;
    public Ant[] ants;
    public Ant globalBestAnt;

    public AntColony()
    {
        pheromones = new double[Configuration.instance.numberOfItems][Configuration.instance.numberOfItems];

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            for (int j = 0; j < Configuration.instance.numberOfItems; j++)
                pheromones[i][j] = Configuration.instance.initialPheromoneValue;

        ants = new Ant[Configuration.instance.numAnts];
        for (int i = 0; i < Configuration.instance.numAnts; i++)
            ants[i] = new Ant();
    }

    public void decay()
    {
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            for (int j = 0; j < Configuration.instance.numberOfItems; j++)
                pheromones[i][j] *= Configuration.instance.evaporationRate;
    }

    public void step()
    {
        Ant bestAnt = ants[0];
        int bestValue = -1;
        for (int i = 0; i < Configuration.instance.numAnts; i++)
        {
            ants[i].reset();
            int value = ants[i].search(pheromones);
            if (value > bestValue)
            {
                bestAnt = ants[i];
                bestValue = value;
            }
        }

        bestAnt.placePheromones(pheromones);
        decay();

        if (globalBestAnt == null || bestAnt.knapsackValue > globalBestAnt.knapsackValue)
        {
            globalBestAnt = new Ant(bestAnt);
            System.out.println("New best value: " + globalBestAnt.knapsackValue);
        }
    }

    public void run()
    {
        for (int i = 0; i < Configuration.instance.numGenerations; i++)
            step();
    }
}
