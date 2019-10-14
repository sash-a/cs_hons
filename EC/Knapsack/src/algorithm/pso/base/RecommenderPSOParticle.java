package algorithm.pso.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.base.Utils;
import main.Configuration;

import java.util.Arrays;

public class RecommenderPSOParticle extends PSOParticle
{
    public Hyperparameter[] hyperparameters;
    public Evaluatable evaluatable;

    public RecommenderPSOParticle(double inertia, double localForce, double globalForce, Hyperparameter[] hps, Evaluatable evaluatable)
    {
        super.inertia = inertia;
        super.localForce = localForce;
        super.globalForce = globalForce;

        this.hyperparameters = new Hyperparameter[hps.length];
        for (int i = 0; i < hps.length; i++)
        {
            double r = Configuration.instance.randomGenerator.nextDouble() * (hps[i].max - hps[i].min) + hps[i].min;
            System.out.println("rng: " + r);
            hyperparameters[i] = hps[i].change(r);
            System.out.println("Initial hp: " + hyperparameters[i]);
        }

        this.pos = Arrays.stream(hyperparameters).map(x -> x.value).mapToDouble(x -> x).toArray();
        this.velocity = new double[hps.length];

        for (int i = 0; i < hps.length; i++)
        {
            velocity[i] = Configuration.instance.randomGenerator.nextDouble() * (hps[i].max - hps[i].min) + hps[i].min;

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

            double t1 = velocity[i] * super.inertia;
            double t2 = super.localForce * r1 * (bestPosition[i] - pos[i]);
            double t3 = super.globalForce * r2 * (globalBest[i] - pos[i]);

            double boundary = hyperparameters[i].max / 4;
            // Forcing velocity to be at max 1/2 of the max value of any hyperparameter
//            velocity[i] = Utils.sig(t1 + t2 + t3, min, max, min / 4, max / 4);
            velocity[i] = Utils.clamp(t1 + t2 + t3, -boundary, boundary);
            hyperparameters[i] = hyperparameters[i].change(pos[i] + velocity[i]);
        }
        this.pos = Arrays.stream(hyperparameters).map(x -> x.value).mapToDouble(x -> x).toArray();
    }

    @Override
    public int getValue()
    {
        double totalFitness = 0;

        System.out.println("\n\nEvaluating " + Configuration.instance.repeats + " times, with hyperparameters:\n" + toString());

        for (int i = 0; i < Configuration.instance.repeats; i++)
        {
            evaluatable.setHyperparams(hyperparameters);
            totalFitness += evaluatable.run();
        }

        return (int) (totalFitness / (double) Configuration.instance.repeats);
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
