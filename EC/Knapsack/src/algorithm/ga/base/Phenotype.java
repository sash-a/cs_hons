package algorithm.ga.base;

import main.Configuration;

import java.util.*;

public class Phenotype implements Comparable<Phenotype>
{
    public List<Gene> genes;
    public Set<Integer> gene_ids;

    private Phenotype()
    {
        this.gene_ids = new HashSet<>();
        this.genes = new LinkedList<>();
    }

    public Phenotype(List<Boolean> representation)
    {
        this();
        for (int i = 0; i < representation.size() - 1; i++)
        {
            if (representation.get(i))
            {
                gene_ids.add(i);
                genes.add(Population.allGenes.get(i));
            }
        }
    }

    public Phenotype(Genome genome)
    {
        this(genome.rep);
    }


    public boolean isValid() { return getWeight() <= Configuration.instance.maximumCapacity; }

    public int getFitness() { return genes.stream().map(g -> g.value).reduce(0, Integer::sum); }

    public int getWeight() { return genes.stream().map(g -> g.weight).reduce(0, Integer::sum); }

    public List<Boolean> getRepresentation()
    {
        List<Boolean> rep = new LinkedList<>();
        Comparator<Gene> cmp = Comparator.comparingInt(o -> o.id);
        genes.sort(cmp);

        int prevID = 0;
        for (Gene g : genes)
        {
            for (int i = prevID; i < g.id - 1; i++)
            {
                rep.add(false);
            }
            rep.add(true);
            prevID = g.id;
        }

        for (int i = prevID; i < Configuration.instance.numberOfItems; i++)
        {
            rep.add(false);
        }
        return rep;
    }

    @Override
    public String toString()
    {
        return genes.toString();
    }

    @Override
    public int compareTo(Phenotype other)
    {
        return Integer.compare(this.getFitness(), other.getFitness());
    }
}
