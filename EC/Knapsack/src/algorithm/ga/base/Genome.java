package algorithm.ga.base;

import algorithm.ga.evolution.crossover.Crossover;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.TwoPointCrossover;
import algorithm.ga.evolution.mutation.Mutator;
import main.Configuration;

import java.util.LinkedList;
import java.util.List;

public class Genome {
    public List<Boolean> rep;
    private Crossover crossover;
    private Mutator mutator;

    public Genome()
    {
        rep = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.maximumCapacity; i++)
            rep.add(Configuration.instance.randomGenerator.nextBoolean());
    }

    public Genome(int crossoverPoints)
    {
        this();
        assert crossoverPoints == 1 || crossoverPoints == 2;
        if (crossoverPoints == 1)
            crossover = new OnePointCrossover();
        else
            crossover = new TwoPointCrossover();
    }

    public Genome(int crossoverPoints, List<Boolean> rep)
    {
        this(crossoverPoints);
        this.rep = rep;
    }

    public Genome mutate()
    {
        return new Genome(crossover.points, mutator.mutate(rep));
    }

    public Genome crossover(Genome other)
    {
        return new Genome(crossover.points, crossover.crossover(this.rep, other.rep));
    }

    @Override
    public String toString()
    {
        return this.rep.toString();
    }

}
