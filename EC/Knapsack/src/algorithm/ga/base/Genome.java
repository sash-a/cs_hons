package algorithm.ga.base;

import main.Configuration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Genome implements Comparable<Genome>
{
    public List<Gene> genes;
    public Set<Integer> gene_ids;

    public Genome(List<Gene> all_genes)
    {
        this.gene_ids = new HashSet<>();
        this.genes = new LinkedList<>();

        int weight = 0;
        while (weight < Configuration.instance.maximumCapacity)
        {
            double rand = Configuration.instance.randomGenerator.nextDouble(true, true);
            int idx = (int) (rand * Configuration.instance.numberOfItems);
            if (this.gene_ids.contains(idx)) // Assuming the genes ID == its position in the list
                continue;

            Gene new_gene = all_genes.get(idx);
            this.gene_ids.add(idx);
            this.genes.add(new_gene);
            weight += all_genes.get(idx).weight;
        }

        this.genes.remove(this.genes.size() - 1);
    }

    public boolean isValid()
    {
        return genes.stream().map(g -> g.weight).reduce(0, Integer::sum) < Configuration.instance.maximumCapacity;
    }

    public int getFitness()
    {
        return genes.stream().map(g -> g.value).reduce(0, Integer::sum);
    }

    public Genome mutate()
    {
        return this;
    }

    public Genome crossover(Genome other)
    {
        return this;
    }

    @Override
    public String toString()
    {
        return genes.toString();
    }

    @Override
    public int compareTo(Genome other)
    {
        return Integer.compare(this.getFitness(), other.getFitness());
    }
}
