package algorithm.pso.recommender;

import algorithm.base.Hyperparameter;
import algorithm.pso.base.Swarm;

public class PSORecommender
{
    public void recommend()
    {
        Swarm swarm = new Swarm(10000);
        Swarm optimizer = new Swarm(1000, swarm,
                new Hyperparameter("num particles", 10, 5, 20, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("inertia", 1, 0.5, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("cognitive", 1, 0.5, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("social", 1, 0.5, 2, Hyperparameter.Type.DOUBLE)
        );

        optimizer.run();
        System.out.println("Best hyperparameters for swarm optimization:\n" + optimizer.gbestParticle);
    }
}
