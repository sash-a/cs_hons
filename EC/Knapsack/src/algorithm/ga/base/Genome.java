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

        if (Configuration.instance.mutationType == Configuration.MutationType.BITFLIP)
            mutator = new BitFlip();
        else if (Configuration.instance.mutationType == Configuration.MutationType.DISPLACEMENT)
            mutator = new Displacement();
        else if (Configuration.instance.mutationType == Configuration.MutationType.EXCHANGE)
            mutator = new Exchange();
        else if (Configuration.instance.mutationType == Configuration.MutationType.INSERTION)
            mutator = new Insertion();
        else if (Configuration.instance.mutationType == Configuration.MutationType.INVERSION)
            mutator = new Inversion();
        else
            System.out.println("Error invalid mutation type!");
    }

    public Genome mutate() { return new Genome(mutator.mutate(new LinkedList<>(rep))); }

    public Genome crossover(Representation other) { return new Genome(crossover.crossover(this.rep, other.rep)); }
}
