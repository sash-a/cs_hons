package algorithm.ga.base;

import algorithm.base.Representation;
import algorithm.ga.evolution.crossover.Crossover;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.TwoPointCrossover;
import algorithm.ga.evolution.mutation.*;
import main.Configuration;

import java.util.LinkedList;
import java.util.List;

public class Genome extends Representation implements Comparable<Genome>
{
    private Crossover crossover;
    private Mutator mutator;

    public Genome(int crossoverPoints, Configuration.MutationType mt)
    {
        super();
        setMutationAndCrossover(crossoverPoints, mt);
    }

    public Genome(Mutator m, Crossover c, List<Boolean> rep)
    {
        super(rep);
        mutator = m;
        crossover = c;
    }

    private void setMutationAndCrossover(int crossoverPoints, Configuration.MutationType mt)
    {
        assert crossoverPoints == 1 || crossoverPoints == 2;
        if (crossoverPoints == 1)
            crossover = new OnePointCrossover();
        else
            crossover = new TwoPointCrossover();

        if (mt == Configuration.MutationType.BITFLIP)
            mutator = new BitFlip();
        else if (mt == Configuration.MutationType.DISPLACEMENT)
            mutator = new Displacement();
        else if (mt == Configuration.MutationType.EXCHANGE)
            mutator = new Exchange();
        else if (mt == Configuration.MutationType.INSERTION)
            mutator = new Insertion();
        else if (mt == Configuration.MutationType.INVERSION)
            mutator = new Inversion();
        else
            System.out.println("Error invalid mutation type!");
    }

    public Genome mutate() { return new Genome(mutator, crossover, mutator.mutate(new LinkedList<>(rep))); }

    public Genome crossover(Representation other) { return new Genome(mutator, crossover, crossover.crossover(this.rep, other.rep)); }

    @Override
    public int compareTo(Genome other) { return Integer.compare(this.getValue(), other.getValue()); }
}
