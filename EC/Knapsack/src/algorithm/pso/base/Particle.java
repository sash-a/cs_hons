package algorithm.pso.base;

import algorithm.base.Knapsack;
import algorithm.base.Utils;
import main.Configuration;

import java.util.Arrays;

public class Particle implements Comparable<Particle>
{
    public double[] pos;
    public double[] velocity;
    public double[] bestPosition;
    public int bestFitness;
    public int fitness;

    public Particle()
    {
        pos = new double[Configuration.instance.numberOfItems];
        velocity = new double[Configuration.instance.numberOfItems];
        bestPosition = new double[Configuration.instance.numberOfItems];

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            pos[i] = Configuration.instance.randomGenerator.nextInt(2);

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            velocity[i] = Configuration.instance.randomGenerator.nextDouble() *
                    (Configuration.instance.vmax - Configuration.instance.vmin) + Configuration.instance.vmin;

        while (!isValid())
        {
            // Remove random genes until weight is acceptable:
            // Pick a random position in list and random direction (forward or back) and traverse until you find a gene
            // then remove it to save weight.
            int startPos = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
            int dir = 1;
            boolean reverse = Configuration.instance.randomGenerator.nextBoolean();
            if (reverse)
                dir = -1;

            for (int i = startPos; i < Configuration.instance.numberOfItems && i >= 0; i += dir)
            {
                if (pos[i] == 1)
                {
                    pos[i] = 0;
                    break;
                }
            }
        }

        bestPosition = Arrays.copyOf(pos, pos.length);
        bestFitness = 0;
    }


    public Particle(Particle other)
    {
        pos = Arrays.copyOf(other.pos, other.pos.length);
        bestPosition = Arrays.copyOf(other.bestPosition, other.bestPosition.length);
        bestFitness = other.bestFitness;
        velocity = Arrays.copyOf(other.velocity, other.velocity.length);
        fitness = other.fitness;
    }

    public void move(double[] globalBest)
    {
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
        {
            double r1 = Configuration.instance.randomGenerator.nextDouble();
            double r2 = Configuration.instance.randomGenerator.nextDouble();

            double t1 = velocity[i] * Configuration.instance.inertia;
            double t2 = Configuration.instance.localForce * r1 * (bestPosition[i] - pos[i]);
            double t3 = Configuration.instance.globalForce * r2 * (globalBest[i] - pos[i]);

            velocity[i] = Utils.clamp(t1 + t2 + t3, Configuration.instance.vmin, Configuration.instance.vmax);
            pos[i] = Utils.sig(pos[i] + velocity[i]) > Configuration.instance.randomGenerator.nextDouble() ? 1 : 0;
        }
    }

    public void calcFitness()
    {
        fitness = getValue();
        if (getWeight() > Configuration.instance.maximumCapacity)
            fitness = 0;

        if (fitness > bestFitness)
        {
            bestFitness = fitness;
            bestPosition = Arrays.copyOf(pos, pos.length);
        }
    }

    public boolean isValid() { return getWeight() < Configuration.instance.maximumCapacity; }

    public int getWeight()
    {
        int weight = 0;
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            if (pos[i] == 1)
                weight += Knapsack.allItems[i].weight;

        return weight;
    }


    public int getValue()
    {
        int value = 0;
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            if (pos[i] == 1)
                value += Knapsack.allItems[i].value;

        return value;
    }

    @Override
    public int compareTo(Particle other) { return Integer.compare(this.getValue(), other.getValue()); }
}
