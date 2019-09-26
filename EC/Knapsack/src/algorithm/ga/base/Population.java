package algorithm.ga.base;

import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Selector;
import algorithm.ga.evolution.selection.Tournament;
import main.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Population
{
    public List<Genome> genomes;
    public List<Gene> allGenes;
    public Selector selector;


    // Tournament selection constructor
    public Population(List<Genome> genomes, List<Gene> genes, int k)
    {
        this.genomes = genomes;
        this.allGenes = genes;
        selector = new Tournament(k);
    }

    // Roulette wheel constructor
    public Population(List<Genome> genomes, List<Gene> genes)
    {
        this.genomes = genomes;
        this.allGenes = genes;
        selector = new RouletteWheel();
    }

    public void step()
    {
        List<Genome> children = new ArrayList<>();
        for (int i = 0; i < Configuration.instance.populationSize - Configuration.instance.elite; i++)
        {
            List<Genome> parents = selectParents();
            Genome child = createChild(parents);
            children.add(mutateChild(child));
        }

        // Setting next generations genomes to the elite Genomes
        genomes = genomes.stream()
                .map(g -> g.toPheno(allGenes))
                .sorted()
                .collect(Collectors.toList())
                .subList(0, Configuration.instance.elite)
                .stream()
                .map(phenotype -> new Genome(phenotype.getRepresentation()))
                .collect(Collectors.toList());

        // Adding all children to the next generation
        genomes.addAll(children);
    }

    private Genome mutateChild(Genome child)
    {
        if (Configuration.instance.randomGenerator.nextDouble() < Configuration.instance.mutationChance)
            return child.mutate();

        return child;
    }

    private Genome createChild(List<Genome> parents)
    {
        return parents.get(0).crossover(parents.get(1));
    }

    private List<Genome> selectParents()
    {
        selector.beforeSelection(genomes, allGenes);

        List<Genome> parents = new ArrayList<>();
        for (int i = 0; i < 2; i++)  // TODO config option parents to crossover
        {
            parents.add(selector.select());
            parents.add(selector.select());
        }

        return parents;
    }
}
