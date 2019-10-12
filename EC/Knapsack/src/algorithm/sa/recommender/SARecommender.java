package algorithm.sa.recommender;

import algorithm.base.Hyperparameter;
import algorithm.pso.base.Swarm;
import algorithm.sa.main.Annealing;
import main.Configuration;

public class SARecommender
{
    public void recommend()
    {
        Annealing annealing = new Annealing();

        Swarm swarm = new Swarm(1000, annealing,
                new Hyperparameter("Initial temp", Configuration.instance.initialTemp, Configuration.instance.minTemp, 10E10, Hyperparameter.Type.DOUBLE),
                new Hyperparameter("Cooling rate", Configuration.instance.coolingRate, 0.9, 0.99999999999999, Hyperparameter.Type.DOUBLE));

        System.out.println("Evaluating simulated annealing...");
        swarm.run();
        System.out.println(swarm.gbestParticle);
    }
}
