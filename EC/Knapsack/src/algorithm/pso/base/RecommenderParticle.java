package algorithm.pso.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.base.Utils;
import main.Configuration;

import java.util.Arrays;

public class RecommenderParticle extends Particle
{
    public Hyperparameter[] hyperparameters;
    public Evaluatable evaluatable;

    public RecommenderParticle(Hyperparameter[] initialPositions, Evaluatable evaluatable)
    {
        this.hyperparameters = new Hyperparameter[initialPositions.length];
        for (int i = 0; i < initialPositions.length; i++)
        {
            System.out.println("Attempting to create value in range: " + initialPositions[i].min + " - " + initialPositions[i].max);
            hyperparameters[i] = initialPositions[i].change(Configuration.instance.randomGenerator.nextDouble() *
                    (initialPositions[i].max - initialPositions[i].min) + initialPositions[i].min);
        }

        this.pos = Arrays.stream(hyperparameters).map(x -> x.value).mapToDouble(x -> x).toArray();
        this.velocity = new double[initialPositions.length];

        for (int i = 0; i < initialPositions.length; i++)
            velocity[i] = Configuration.instance.randomGenerator.nextDouble() *
                    (initialPositions[i].max - initialPositions[i].min) / 10 + initialPositions[i].min;

        System.out.println(evaluatable);
        this.evaluatable = evaluatable;

        System.out.println("Evaluating with hyperparameters:\n" + toString());
    }

    public RecommenderParticle(RecommenderParticle p)
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
            hyperparameters[i].change(pos[i] + velocity[i]);
        }
        this.pos = Arrays.stream(hyperparameters).map(x -> x.value).mapToDouble(x -> x).toArray();
    }

    @Override
    public int getValue()
    {
        evaluatable.setHyperparams(hyperparameters);
        return evaluatable.run();
    }

    @Override
    public int getWeight() { return 0; }

    public String toString()
    {
        String out = "";
        for (Hyperparameter bn : hyperparameters)
            out += bn.toString() + "\n";

        return out;
    }
}
