package algorithm.pso.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import main.Configuration;

import java.util.Arrays;

public class RecommenderPSOParticle extends PSOParticle
{
    public Hyperparameter[] hyperparameters;
    public Evaluatable evaluatable;

    public RecommenderPSOParticle(Hyperparameter[] hps, Evaluatable evaluatable)
    {
        this.hyperparameters = new Hyperparameter[hps.length];
        for (int i = 0; i < hps.length; i++)
        {
            hyperparameters[i] = hps[i].change(Configuration.instance.randomGenerator.nextDouble() *
                    (hps[i].max - hps[i].min) + hps[i].min);
        }

        this.pos = Arrays.stream(hyperparameters).map(x -> x.value).mapToDouble(x -> x).toArray();
        this.velocity = new double[hps.length];

        for (int i = 0; i < hps.length; i++)
        {
            velocity[i] = Configuration.instance.randomGenerator.nextDouble() *
                    (hps[i].max - hps[i].min) + hps[i].min;

            if (Configuration.instance.randomGenerator.nextBoolean())
                velocity[i] *= -1;
        }

        this.evaluatable = evaluatable;
    }

    public RecommenderPSOParticle(RecommenderPSOParticle p)
    {
        super(p);
        hyperparameters = new Hyperparameter[p.hyperparameters.length];
        for (int i = 0; i < p.pos.length; i++)
            hyperparameters[i] = p.hyperparameters[i].change(p.hyperparameters[i].value);
    }

    @Override
    public void move(double[] globalBest)
    {
        for (int i = 0; i < velocity.length; i++)
        {
            double r1 = Configuration.instance.randomGenerator.nextDouble();
            double r2 = Configuration.instance.randomGenerator.nextDouble();

            double t1 = velocity[i] * Configuration.instance.inertia;
            double t2 = Configuration.instance.localForce * r1 * (bestPosition[i] - pos[i]);
            double t3 = Configuration.instance.globalForce * r2 * (globalBest[i] - pos[i]);

            velocity[i] = t1 + t2 + t3;
            hyperparameters[i] = hyperparameters[i].change(pos[i] + velocity[i]);

        }
        this.pos = Arrays.stream(hyperparameters).map(x -> x.value).mapToDouble(x -> x).toArray();
    }

    @Override
    public int getValue()
    {
        double totalFitness = 0;

        System.out.println("\n\nEvaluating " + Configuration.instance.repeats + " times, with hyperparameters:\n" + toString() + "\n");

        for (int i = 0; i < Configuration.instance.repeats; i++)
        {
            evaluatable.setHyperparams(hyperparameters);
            totalFitness += evaluatable.run();
        }

        int avg = (int) (totalFitness / (double) Configuration.instance.repeats);
        System.out.println("Final averaged fitness: " + avg);
        return avg;
    }

    @Override
    public int getWeight() { return 0; }

    public String toString()
    {
        StringBuilder out = new StringBuilder();
        for (Hyperparameter bn : hyperparameters)
            out.append(bn.toString()).append("\n");

        return out.toString();
    }
}
