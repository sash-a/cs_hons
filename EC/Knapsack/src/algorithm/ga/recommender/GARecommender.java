package algorithm.ga.recommender;

import algorithm.base.Hyperparameter;
import algorithm.ga.base.Population;
import algorithm.ga.evolution.mutation.Mutator;
import algorithm.pso.base.Swarm;

public class GARecommender
{
    public void recommend()
    {
        Population pop = new Population();
        Swarm GAOptimizer = new Swarm(pop,
                new Hyperparameter("Pop size", 50, 20, 200, Hyperparameter.Type.INT),
                new Hyperparameter("Num elite", 1, 0, 5, Hyperparameter.Type.INT),
                new Hyperparameter("Tournament size", 0, 0, 30, Hyperparameter.Type.INT),
                new Hyperparameter("Crossover", 1, 1, 2, Hyperparameter.Type.INT),
                new Hyperparameter("Mutator", 1, 1, 5, Hyperparameter.Type.INT),
                new Hyperparameter("Mutation chance", 0.1, 0, 1, Hyperparameter.Type.DOUBLE));

        GAOptimizer.run();
    }
}
