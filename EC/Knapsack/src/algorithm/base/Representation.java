package algorithm.base;

import algorithm.ga.evolution.crossover.Crossover;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.TwoPointCrossover;
import algorithm.ga.evolution.mutation.*;
import main.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
    Genome in the case of a GA
*/
public class Representation
{
    public List<Boolean> rep;
    private Crossover crossover;
    private Mutator mutator;

    public Representation()
    {
        rep = new ArrayList<>();
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            rep.add(Configuration.instance.randomGenerator.nextBoolean());

        boolean valid = new Knapsack(rep).isValid();
        while (!valid)
        {
            // Remove random genes until weight is acceptable:
            // Pick a random position in list and random direction (forward or back) and traverse until you find a gene
            // then remove it to save weight.
            int pos = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
            int dir = 1;
            boolean reverse = Configuration.instance.randomGenerator.nextBoolean();
            if (reverse)
                dir = -1;

            for (int i = pos; i < Configuration.instance.numberOfItems && i >= 0; i += dir)
            {
                if (rep.get(i))
                {
                    rep.set(i, false);
                    break;
                }
            }

            valid = new Knapsack(rep).isValid();
        }

        setMuatationAndCrossover();
    }

    public Representation(List<Boolean> rep)
    {
        setMuatationAndCrossover();
        this.rep = rep;
    }

    private void setMuatationAndCrossover()
    {
        assert Configuration.instance.crossoverPoints == 1 || Configuration.instance.crossoverPoints == 2;
        if (Configuration.instance.crossoverPoints == 1)
            crossover = new OnePointCrossover();
        else
            crossover = new TwoPointCrossover();

        switch (Configuration.instance.mutationType)
        {
            case BITFLIP:
                mutator = new BitFlip();
            case DISPLACEMENT:
                mutator = new Displacement();
            case EXCHANGE:
                mutator = new Exchange();
            case INSERTION:
                mutator = new Insertion();
            case INVERRSION:
                mutator = new Inversion();
            default:
                mutator = new BitFlip();
        }
    }

    public Representation mutate()
    {
        return new Representation(mutator.mutate(new LinkedList<>(rep)));
    }

    public Representation crossover(Representation other)
    {
        return new Representation(crossover.crossover(this.rep, other.rep));
    }

    public Knapsack toKnapsack()
    {
        return new Knapsack(this);
    }

    public int getValue() { return new Knapsack(this).getFitness(); }

    public int getWeight() { return new Knapsack(this).getWeight(); }

    public boolean isValid() { return new Knapsack(this).isValid(); }

    @Override
    public String toString()
    {
        return this.rep.toString();
    }

}
