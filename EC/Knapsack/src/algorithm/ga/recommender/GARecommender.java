package algorithm.ga.recommender;

import algorithm.base.Hyperparameter;
import algorithm.base.Recommender;
import algorithm.ga.base.Population;
import algorithm.ga.evolution.mutation.Mutator;
import algorithm.pso.base.RecommenderPSOParticle;
import algorithm.pso.base.Swarm;
import main.Application;
import main.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GARecommender extends Recommender
{
    public void recommend()
    {
        Population pop = new Population();
        Swarm GAOptimizer = new Swarm(100, pop,
                new Hyperparameter("Pop size", 50, 20, 200, Hyperparameter.Type.INT),
                new Hyperparameter("Num elite", 1, 0, 5, Hyperparameter.Type.INT),
                new Hyperparameter("Tournament size", 0, 0, 30, Hyperparameter.Type.INT),
                new Hyperparameter("Crossover", 1, 1, 2, Hyperparameter.Type.INT),
                new Hyperparameter("Mutator", 1, 1, 5, Hyperparameter.Type.INT),
                new Hyperparameter("Mutation chance", 0.1, 0, 1, Hyperparameter.Type.DOUBLE));

        GAOptimizer.run();
//        List<String> paramNames = Arrays.stream(((RecommenderPSOParticle) GAOptimizer.gbestParticle).hyperparameters).map(hp -> hp.name).collect(Collectors.toList());
//        List<String> paramValues = Arrays.stream(((RecommenderPSOParticle) GAOptimizer.gbestParticle).hyperparameters).map(hp -> "" + hp.value).collect(Collectors.toList());
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "ga_best.xml";
//
//        Application.writeConfig(path, "ga", paramNames, paramValues);
        writeConfig(path, "ga", ((RecommenderPSOParticle) GAOptimizer.gbestParticle));
    }
}
