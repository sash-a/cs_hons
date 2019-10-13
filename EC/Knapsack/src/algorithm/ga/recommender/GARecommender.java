package algorithm.ga.recommender;

import algorithm.base.Hyperparameter;
import algorithm.base.Recommender;
import algorithm.ga.base.Population;
import algorithm.pso.base.RecommenderPSOParticle;
import algorithm.pso.base.Swarm;
import main.Configuration;

public class GARecommender extends Recommender
{
    public void recommend()
    {
        System.out.println("Running GA recommender");

        Population pop = new Population();
        Swarm GAOptimizer = new Swarm(100, pop,
                new Hyperparameter("pop_size", 20, 20, 100, Hyperparameter.Type.INT),
                new Hyperparameter("num_elite", 1, 0, 5, Hyperparameter.Type.INT),
                new Hyperparameter("tournament_size", 0, 0, 30, Hyperparameter.Type.INT),
                new Hyperparameter("crossover", 1, 1, 2, Hyperparameter.Type.INT),
                new Hyperparameter("mutator", 1, 1, 5, Hyperparameter.Type.INT),
                new Hyperparameter("mutation_chance", 0.1, 0, 1, Hyperparameter.Type.DOUBLE));

        GAOptimizer.run();

        System.out.println("GA optimization finished, best parameters:\n" + GAOptimizer.gbestParticle);
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "ga_best.xml";
        writeConfig(path, "ga", ((RecommenderPSOParticle) GAOptimizer.gbestParticle));
    }
}
