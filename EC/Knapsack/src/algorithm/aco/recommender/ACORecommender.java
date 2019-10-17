package algorithm.aco.recommender;

import algorithm.aco.base.AntColony;
import algorithm.base.Hyperparameter;
import algorithm.base.Recommender;
import algorithm.pso.base.RecommenderPSOParticle;
import algorithm.pso.base.Swarm;
import main.Configuration;

public class ACORecommender extends Recommender
{
    public void recommend()
    {
        // No values here matter except for generations because they will be overwritten immediately by the optimizer
        AntColony ac = new AntColony(2500, 0, 0, 0, 0, 0);

        Swarm optimizer = new Swarm(100, 10, ac,
                new Hyperparameter("num_ants", 10, 5, 20, Hyperparameter.Type.INT),
                new Hyperparameter("alpha", 1, 0, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("beta", 1, 0, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("evaporation_rate", 0.7, 0, 1, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("initial_pheromone", 1, 0, 20, Hyperparameter.Type.DOUBLE)

        );

        optimizer.run();

        System.out.println("ACO optimization finished, best parameters:\n" + optimizer.gbestParticle);
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "aco_best.xml";
        writeConfig(path, "aco", ((RecommenderPSOParticle) optimizer.gbestParticle));
    }
}
