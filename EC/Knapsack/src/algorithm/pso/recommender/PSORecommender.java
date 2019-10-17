package algorithm.pso.recommender;

import algorithm.base.Hyperparameter;
import algorithm.base.Recommender;
import algorithm.pso.base.RecommenderPSOParticle;
import algorithm.pso.base.Swarm;
import main.Configuration;

public class PSORecommender extends Recommender
{
    public void recommend()
    {
        System.out.println("Running PSO recommender");
        // Only generation parameter matters here, rest will be overwritten
        Swarm swarm = new Swarm(5000, 0, 0, 0, 0);
        Swarm optimizer = new Swarm(300, 10, swarm,
                new Hyperparameter("num_particles", 10, 5, 20, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("inertia", 1, 0.5, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("cognitive", 1, 0.5, 2, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("social", 1, 0.5, 2, Hyperparameter.Type.DOUBLE)
        );

        optimizer.run();
        System.out.println("Best hyperparameters for swarm optimization:\n" + optimizer.gbestParticle);
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "pso_best.xml";
        writeConfig(path, "pso", ((RecommenderPSOParticle) optimizer.gbestParticle));
    }
}
