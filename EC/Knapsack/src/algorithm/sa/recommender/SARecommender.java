package algorithm.sa.recommender;

import algorithm.base.Hyperparameter;
import algorithm.base.Recommender;
import algorithm.pso.base.RecommenderPSOParticle;
import algorithm.pso.base.Swarm;
import algorithm.sa.main.Annealing;
import main.Configuration;

public class SARecommender extends Recommender
{
    public void recommend()
    {
        System.out.println("Running SA recommender");
        // None of these values matter, will be overwritten during optimization
        Annealing annealing = new Annealing(0, 0, 0, 0);

        Swarm optimizer = new Swarm(1000, 15, annealing,
                new Hyperparameter("initial_temp", 1E6, 1E6, 1E7, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("min_temp", 1E-5, 1E-20, 1E-10, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("cooling_rate", 0.9999, 0.9999, 1, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("reset_chance", 0, 0, 0.05, Hyperparameter.Type.DOUBLE));

        System.out.println("Evaluating simulated annealing...");
        optimizer.run();

        System.out.println("Optimization finished, found best hyperparameters:\n" + optimizer.gbestParticle);
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "sa_best.xml";
        writeConfig(path, "sa", ((RecommenderPSOParticle) optimizer.gbestParticle));
    }
}