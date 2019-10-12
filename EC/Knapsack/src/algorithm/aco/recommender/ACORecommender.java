package algorithm.aco.recommender;

import algorithm.aco.base.AntColony;
import algorithm.base.Hyperparameter;
import algorithm.pso.base.Swarm;

public class ACORecommender
{
    public void recommend()
    {
        AntColony ac = new AntColony();

        Swarm optimizer = new Swarm(1000, ac,
                new Hyperparameter("Num ants", 10, 5, 20, Hyperparameter.Type.INT),
                new Hyperparameter("Alpha", 1, 0.25, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("Beta", 1, 0.25, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("Evaporation rate", 0.7, 0.1, 0.9999, Hyperparameter.Type.DOUBLE)
        );

        optimizer.run();
    }
}
