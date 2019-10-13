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

    private void init(int numAnts, double alpha, double beta, double evapRate)
    {
        this.numAnts = numAnts;
        this.evaporationRate = evapRate;

        pheromones = new double[Configuration.instance.numberOfItems][Configuration.instance.numberOfItems];

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            for (int j = 0; j < Configuration.instance.numberOfItems; j++)
                pheromones[i][j] = Configuration.instance.initialPheromoneValue;

        ants = new Ant[numAnts];
        for (int i = 0; i < numAnts; i++)
            ants[i] = new Ant(alpha, beta);

        globalBestAnt = new Ant(alpha, beta);
    }

    public AntColony()
    {
        init(Configuration.instance.numAnts, Configuration.instance.alpha, Configuration.instance.beta, Configuration.instance.evaporationRate);
    }

    public AntColony(double[] hyperparameters)
    {
        init((int) hyperparameters[0], hyperparameters[1], hyperparameters[2], hyperparameters[3]);
    }

    /**
     * @param hyperparameters: num ants, alpha, beta, evaporation rate
     */
    @Override
    public void setHyperparams(Hyperparameter... hyperparameters)
    {
        init((int) hyperparameters[0].value, hyperparameters[1].value, hyperparameters[2].value, hyperparameters[3].value);
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

        if (gen % 250 == 0)
            System.out.println(System.currentTimeMillis());
    }

    public int run()
    {
        for (int i = 0; i < Configuration.instance.numACOGens; i++)
            step(i);

        return globalBestAnt.knapsackValue;
    }
}
