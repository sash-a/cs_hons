package algorithm.ga.base;

import algorithm.base.Representation;
import algorithm.ga.evolution.crossover.Crossover;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.TwoPointCrossover;
import algorithm.ga.evolution.mutation.*;
import main.Configuration;

import java.util.LinkedList;
import java.util.List;

public class Genome extends Representation
{
    private Crossover crossover;
    private Mutator mutator;

    public Genome()
    {
        super();
        setMutationAndCrossover();
    }

    public Genome(List<Boolean> rep)
    {
        super(rep);
        setMutationAndCrossover();
    }


    private void setMutationAndCrossover()
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

    public Genome mutate() { return new Genome(mutator.mutate(new LinkedList<>(rep))); }

    public Genome crossover(Representation other) { return new Genome(crossover.crossover(this.rep, other.rep)); }
}
