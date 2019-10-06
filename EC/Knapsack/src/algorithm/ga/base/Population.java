package algorithm.ga.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.base.Representation;
import algorithm.base.Knapsack;
import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Selector;
import algorithm.ga.evolution.selection.Tournament;
import main.Configuration;

import java.util.*;
import java.util.stream.Collectors;

public class Population extends Evaluatable
{
    public List<Genome> genomes;
    public Selector selector;

    // Tournament selection constructor
    public Population(int k)
    {
        this.genomes = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.populationSize; i++)
            genomes.add(new Genome());
        selector = new Tournament(k);
    }

    // Roulette wheel constructor
    public Population()
    {
        this.genomes = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.populationSize; i++)
            genomes.add(new Genome());


        selector = new RouletteWheel();
    }

    /**
     * @param bns: pop size, tournament size (0 = rouletteWheel), crossover (1 or 2), mutator (1-5)
     */
    public void setHyperparams(Hyperparameter... bns)
    {
        assert bns.length == 4;
        genomes.clear();
        Configuration.instance.populationSize = (int) bns[0].value;

        if (bns[1].value == 0.0)
            selector = new RouletteWheel();
        else
            selector = new Tournament((int) bns[1].value);

        Configuration.instance.crossoverPoints = (int) bns[2].value;
        Configuration.instance.mutationType = Configuration.MutationType.values()[(int) bns[3].value];

        this.genomes.clear();
        for (int i = 0; i < Configuration.instance.populationSize; i++)
            genomes.add(new Genome());
    }

    public int run()
    {
        Genome bestGenome = genomes.get(0);

        // Running evolution
        for (int i = 0; i < Configuration.instance.numGenerations; i++)
        {
            Genome fittest = step();
            if (fittest.getValue() > bestGenome.getValue())
            {
                System.out.println("Generation " + i + ". New best fitness: " + fittest.getValue() + ", improved on: " + bestGenome.getValue());
                bestGenome = fittest;
            }
        }

        // Printing out final best individual
        Genome best = genomes.stream()
                .max(Comparator.comparingInt(Representation::getValue)).get();

        System.out.println("Final fittest individual" +
                "\nFitness:" + best.getValue() +
                "\nWeight: " + best.getWeight() +
                "\nIndividual" + best);

        return best.getValue();
    }

    public Genome step()
    {
        List<Genome> children = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.populationSize - Configuration.instance.elite; i++)
        {
            List<Genome> parents = selectParents();
            Genome child = createChild(parents);
            children.add(mutateChild(child));
        }

        // Sorting to find the elite individuals
        List<Knapsack> phenotypes = genomes.stream()
                .map(Genome::toKnapsack)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());

        // Only taking the top n from the sorted list in genome form
        genomes = phenotypes.subList(0, Configuration.instance.elite)
                .stream()
                .map(phenotype -> new Genome(phenotype.getRepresentation()))
                .collect(Collectors.toCollection(LinkedList::new));

        // Adding all children to the next generation
        genomes.addAll(children);


        return genomes.get(0);
    }

    private Genome mutateChild(Genome child)
    {
        if (Configuration.instance.randomGenerator.nextDouble() < Configuration.instance.mutationChance)
        {
            Genome mutant = child.mutate();
            boolean valid = mutant.toKnapsack().isValid();

            for (int i = 0; i < Configuration.instance.validAttempts && !valid; i++)
            {
                mutant = child.mutate();
                valid = mutant.isValid();
            }

            if (valid)
                return mutant;
        }

        return child;
    }

    private Genome createChild(List<Genome> parents)
    {
        Genome child = parents.get(0).crossover(parents.get(1));
        boolean valid = child.toKnapsack().isValid();
        for (int i = 0; i < Configuration.instance.validAttempts && !valid; i++) // todo fix
        {
            child = parents.get(0).crossover(parents.get(1));
            valid = child.isValid();
        }

        if (valid)
            return child;

        // Never found a valid combination of parents so return fittest parent
        List<Integer> fitnesses = parents.stream().map(Genome::toKnapsack).map(Knapsack::getFitness).collect(Collectors.toList());
        if (fitnesses.get(0) > fitnesses.get(1))
            return parents.get(0);
        else
            return parents.get(1);
    }

    private List<Genome> selectParents()
    {
//        System.out.println("Selecting parents");
        selector.beforeSelection(genomes);

        List<Genome> parents = new ArrayList<>();

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
