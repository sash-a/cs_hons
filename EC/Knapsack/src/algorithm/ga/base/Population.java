package algorithm.ga.base;

import algorithm.base.Representation;
import algorithm.base.Knapsack;
import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Selector;
import algorithm.ga.evolution.selection.Tournament;
import main.Configuration;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Population
{
    public List<Representation> genomes;
    public Selector selector;

    // Tournament selection constructor
    public Population(List<Representation> genomes, int k)
    {
        this.genomes = genomes;
        selector = new Tournament(k);
    }

    // Roulette wheel constructor
    public Population(List<Representation> genomes)
    {
        this.genomes = genomes;
        selector = new RouletteWheel();
    }

    public void step()
    {
        List<Representation> children = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.populationSize - Configuration.instance.elite; i++)
        {
            List<Representation> parents = selectParents();
            Representation child = createChild(parents);
            children.add(mutateChild(child));
        }

        // Sorting to find the elite individuals
        List<Knapsack> phenotypes = genomes.stream()
                .map(Representation::toKnapsack)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        genomes = phenotypes.subList(0, Configuration.instance.elite)
                .stream()
                .map(phenotype -> new Representation(phenotype.getRepresentation()))
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println("Fittest individual " + phenotypes.get(0).getFitness());

        // Adding all children to the next generation
        genomes.addAll(children);
    }

    private Representation mutateChild(Representation child)
    {
        if (Configuration.instance.randomGenerator.nextDouble() < Configuration.instance.mutationChance)
        {
            Representation mutant = child.mutate();
            boolean valid = mutant.toKnapsack().isValid();

            for (int i = 0; i < Configuration.instance.validAttempts && !valid; i++)
            {
                mutant = child.mutate();
                valid = mutant.toKnapsack().isValid();
            }

            if (valid)
                return mutant;
        }

        return child;
    }

    private Representation createChild(List<Representation> parents)
    {
        Representation child = parents.get(0).crossover(parents.get(1));
        boolean valid = child.toKnapsack().isValid();
        for (int i = 0; i < Configuration.instance.validAttempts && !valid; i++)
        {
            child = parents.get(0).crossover(parents.get(1));
            valid = child.toKnapsack().isValid();
        }

        if (valid)
            return child;

        // Never found a valid combination of parents so return fittest parent
        List<Integer> fitnesses = parents.stream().map(Representation::toKnapsack).map(Knapsack::getFitness).collect(Collectors.toList());
        if (fitnesses.get(0) > fitnesses.get(1))
            return parents.get(0);
        else
            return parents.get(1);
    }

    private List<Representation> selectParents()
    {
//        System.out.println("Selecting parents");
        selector.beforeSelection(genomes);

        List<Representation> parents = new LinkedList<>();

        parents.add(selector.select());
        parents.add(selector.select());


        return parents;
    }

    @Override
    public String toString()
    {
        StringBuilder joined = new StringBuilder();
        for (Representation g : genomes)
            joined.append(g.toString()).append("\n");

        return "Population:\n" + joined.toString();
    }
}
