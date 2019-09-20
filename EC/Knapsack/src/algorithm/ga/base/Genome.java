package algorithm.ga.base;

import java.util.LinkedList;

public class Genome
{
    LinkedList<Gene> genes;

    public Genome()
    {
        genes = new LinkedList<>();
    }

    public int getFitness()
    {
        int fitness = 0;
        for (Gene gene : this.genes) fitness += gene.value;

        return fitness;
    }

    public Genome mutate()
    {
        // TODO
        return this;
    }

}
