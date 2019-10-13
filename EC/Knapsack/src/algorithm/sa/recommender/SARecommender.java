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
        Annealing annealing = new Annealing();

        Swarm optimizer = new Swarm(1, annealing,
                new Hyperparameter("initial_temp", Configuration.instance.initialTemp, 1E6, 1E7, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("min_temp", 1E-5, 1E-20, 1E-10, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("cooling_rate", Configuration.instance.coolingRate, 0.9999, 1, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("reset_chance", Configuration.instance.resetToGlobalChance, 0, 0.05, Hyperparameter.Type.DOUBLE));

        System.out.println("Evaluating simulated annealing...");
        optimizer.run();

        System.out.println(optimizer.gbestParticle);
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "sa_best.xml";
        writeConfig(path, "ga", ((RecommenderPSOParticle) optimizer.gbestParticle));
    }
}
/*Evaluating 3 times, with hyperparameters:
Initial temp: 5.624332624408891E9
Cooling rate: 0.9999696016797301
Reset chance: 0.002512621745218885*/