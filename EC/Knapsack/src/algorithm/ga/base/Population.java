package algorithm.ga.base;

import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Selector;
import algorithm.ga.evolution.selection.Tournament;
import algorithm.ga.main.GARunner;
import main.Configuration;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Population
{
    public List<Genome> genomes;
    public Selector selector;

    // Tournament selection constructor
    public Population(List<Genome> genomes, int k)
    {
        this.genomes = genomes;
        selector = new Tournament(k);
    }

    // Roulette wheel constructor
    public Population(List<Genome> genomes)
    {
        this.genomes = genomes;
        selector = new RouletteWheel();
    }

    public void step()
    {
        List<Genome> children = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.populationSize - Configuration.instance.elite; i++)
        {
            List<Genome> parents = selectParents();
            Genome child = createChild(parents);
            children.add(mutateChild(child));
        }

        // Sorting to find the elite individuals
        List<Phenotype> phenotypes = genomes.stream()
                .map(Genome::toPheno)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        genomes = phenotypes.subList(0, Configuration.instance.elite)
                .stream()
                .map(phenotype -> new Genome(phenotype.getRepresentation()))
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println("Fittest individual " + phenotypes.get(0).getFitness());

        // Adding all children to the next generation
        genomes.addAll(children);
    }

    private Genome mutateChild(Genome child)
    {
        if (Configuration.instance.randomGenerator.nextDouble() < Configuration.instance.mutationChance)
        {
            Genome mutant = child.mutate();
            boolean valid = mutant.toPheno().isValid();

            for (int i = 0; i < Configuration.instance.validAttempts && !valid; i++)
            {
                mutant = child.mutate();
                valid = mutant.toPheno().isValid();
            }

            if (valid)
                return mutant;
        }

        return child;
    }

    private Genome createChild(List<Genome> parents)
    {
        Genome child = parents.get(0).crossover(parents.get(1));
        boolean valid = child.toPheno().isValid();
        for (int i = 0; i < Configuration.instance.validAttempts && !valid; i++)
        {
            child = parents.get(0).crossover(parents.get(1));
            valid = child.toPheno().isValid();
        }

        if (valid)
            return child;

        // Never found a valid combination of parents so return fittest parent
        List<Integer> fitnesses = parents.stream().map(Genome::toPheno).map(Phenotype::getFitness).collect(Collectors.toList());
        if (fitnesses.get(0) > fitnesses.get(1))
            return parents.get(0);
        else
            return parents.get(1);
    }

    private List<Genome> selectParents()
    {
//        System.out.println("Selecting parents");
        selector.beforeSelection(genomes);

        List<Genome> parents = new LinkedList<>();

        parents.add(selector.select());
        parents.add(selector.select());


        return parents;
    }

    @Override
    public String toString()
    {
        StringBuilder joined = new StringBuilder();
        for (Genome g : genomes)
            joined.append(g.toString()).append("\n");

        return "Population:\n" + joined.toString();
    }
}
