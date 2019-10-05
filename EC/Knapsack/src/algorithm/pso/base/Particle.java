package algorithm.pso.base;

import algorithm.base.Knapsack;
import main.Configuration;

import java.util.Arrays;

public class Particle implements Comparable<Particle>
{
    public int[] pos;
    public double[] velocity;
    public int[] bestPosition;
    public int bestFitness;
    public int fitness;

    public Particle()
    {
        pos = new int[Configuration.instance.numberOfItems];
        velocity = new double[Configuration.instance.numberOfItems];
        bestPosition = new int[Configuration.instance.numberOfItems];

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            pos[i] = Configuration.instance.randomGenerator.nextInt(2);

        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            velocity[i] = Configuration.instance.randomGenerator.nextDouble() * 10 - 5;

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
        bestFitness = getValue();
    }

    public Particle(Particle other)
    {
        pos = Arrays.copyOf(other.pos, other.pos.length);
        bestPosition = Arrays.copyOf(other.bestPosition, other.bestPosition.length);
        bestFitness = other.bestFitness;
        velocity = Arrays.copyOf(other.velocity, other.velocity.length);
    }

    public void move(int[] globalBest)
    {
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
        {
            double r1 = Configuration.instance.randomGenerator.nextDouble();
            double r2 = Configuration.instance.randomGenerator.nextDouble();

            double t1 = velocity[i] * Configuration.instance.inertia;
            double t2 = Configuration.instance.localForce * r1 * (bestPosition[i] - pos[i]);
            double t3 = Configuration.instance.globalForce * r2 * (globalBest[i] - pos[i]);

            velocity[i] = t1 + t2 + t3;
            pos[i] = sig(pos[i] + velocity[i]) > Configuration.instance.randomGenerator.nextDouble() ? 1 : 0;
        }
    }

    public int calcFitness()
    {
        fitness = getValue();
        if (getWeight() > Configuration.instance.maximumCapacity)
            fitness = 0;

        if (fitness > bestFitness)
        {
            bestFitness = fitness;
            bestPosition = Arrays.copyOf(pos, pos.length);
        }

        return fitness;
    }

    public int getWeight()
    {
        int weight = 0;
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            if (pos[i] == 1)
                weight += Knapsack.allItems[i].weight;

        return weight;
    }

    public boolean isValid() { return getWeight() < Configuration.instance.maximumCapacity; }

    public int getValue()
    {
        int value = 0;
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            if (pos[i] == 1)
                value += Knapsack.allItems[i].value;

        return value;
    }

    public static double sig(double n) { return 1 / Math.exp(-n); }

    @Override
    public int compareTo(Particle other) { return Integer.compare(this.getValue(), other.getValue()); }
}
