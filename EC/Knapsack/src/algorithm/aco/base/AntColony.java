package algorithm.aco.base;

import algorithm.base.Evaluatable;
import algorithm.base.Hyperparameter;
import main.Configuration;

public class AntColony extends Evaluatable
{
    public double[][] pheromones;
    public Ant[] ants;
    public Ant globalBestAnt;

    private int numAnts;
    private double evaporationRate;

    private void init(int numAnts, double alpha, double beta, double evapRate, double initialPheromoneValue)
    {
        this.numAnts = numAnts;
        this.evaporationRate = evapRate;

        pheromones = new double[Configuration.instance.numberOfItems][Configuration.instance.numberOfItems];

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            for (int j = 0; j < Configuration.instance.numberOfItems; j++)
                pheromones[i][j] = initialPheromoneValue;

        ants = new Ant[numAnts];
        for (int i = 0; i < numAnts; i++)
            ants[i] = new Ant(alpha, beta);

        globalBestAnt = new Ant(alpha, beta);
    }

    public AntColony()
    {
        init(Configuration.instance.numAnts, Configuration.instance.alpha, Configuration.instance.beta, Configuration.instance.evaporationRate, Configuration.instance.initialPheromoneValue);
    }

    /**
     * @param hyperparameters: num ants, alpha, beta, evaporation rate
     */
    public AntColony(double[] hyperparameters)
    {
        assert hyperparameters.length == 5;
        init((int) hyperparameters[0], hyperparameters[1], hyperparameters[2], hyperparameters[3], hyperparameters[4]);
    }

    /**
     * @param hyperparameters: num ants, alpha, beta, evaporation rate
     */
    @Override
    public void setHyperparams(Hyperparameter... hyperparameters)
    {
        assert hyperparameters.length == 5;
        init((int) hyperparameters[0].value, hyperparameters[1].value, hyperparameters[2].value, hyperparameters[3].value, hyperparameters[4].value);
    }

    public void decay()
    {
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            for (int j = 0; j < Configuration.instance.numberOfItems; j++)
                pheromones[i][j] *= evaporationRate;
    }

    public void step(int gen)
    {
        Ant bestAnt = ants[0];
        int bestValue = -1;

        for (int i = 0; i < numAnts; i++)
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

        if (bestAnt.knapsackValue > globalBestAnt.knapsackValue)
        {
            globalBestAnt = new Ant(bestAnt);
            System.out.println("New best value: " + globalBestAnt.knapsackValue + " | Generation: " + gen);
        }
    }

    public int run()
    {
        for (int i = 0; i < Configuration.instance.numACOGens; i++)
            step(i);

        System.out.println("Final best knapsack from ACO: " + globalBestAnt);
        return globalBestAnt.knapsackValue;
    }
}
