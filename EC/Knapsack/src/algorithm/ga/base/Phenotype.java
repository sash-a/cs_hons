package algorithm.ga.base;

import algorithm.ga.evolution.crossover.Crossover;
import algorithm.ga.evolution.crossover.OnePointCrossover;
import algorithm.ga.evolution.crossover.TwoPointCrossover;
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

    public Phenotype(List<Gene> all_genes)
    {
        this();

        int weight = 0;
        while (weight < Configuration.instance.maximumCapacity)
        {
            double rand = Configuration.instance.randomGenerator.nextDouble(true, true);
            int idx = (int) (rand * Configuration.instance.numberOfItems);
            Gene new_gene = all_genes.get(idx);

            // Making sure gene not yet in genome
            if (this.gene_ids.contains(new_gene.id))
                continue;

            this.gene_ids.add(idx);
            this.genes.add(new_gene);
            weight += all_genes.get(idx).weight;
        }

        this.genes.remove(this.genes.size() - 1);
    }

    public Phenotype(List<Boolean> representation, List<Gene> all_genes)
    {
        this();

        for (int i = 0; i < representation.size(); i++)
        {
            if (representation.get(i))
            {
                gene_ids.add(i);
                genes.add(all_genes.get(i));
            }
        }
    }

    public Phenotype(Genome genome, List<Gene> all_genes)
    {
        this(genome.rep, all_genes);
    }


    public boolean isValid()
    {
        return genes.stream().map(g -> g.weight).reduce(0, Integer::sum) < Configuration.instance.maximumCapacity;
    }

    public int getFitness()
    {
        return genes.stream().map(g -> g.value).reduce(0, Integer::sum);
    }

    public List<Boolean> getRepresentation()
    {
        List<Boolean> rep = new ArrayList<>();
        Comparator<Gene> cmp = Comparator.comparingInt(o -> o.id);
        genes.sort(cmp);
        System.out.println(genes);

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

        for (int i = prevID; i < Configuration.instance.maximumCapacity; i++)
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
