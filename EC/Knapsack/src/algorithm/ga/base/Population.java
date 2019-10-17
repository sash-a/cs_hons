package algorithm.ga.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Selector;
import algorithm.ga.evolution.selection.Tournament;
import main.Configuration;

import java.util.*;
import java.util.stream.Collectors;

public class Population extends Evaluatable
{
    public List<Genome> genomes;
    private Selector selector;
    private int size;
    private int elite;
    private int generations;
    private double mutationChance;

    public Population(int generations, int popSize, int elite, double mutationChance, Configuration.MutationType mutationType, int crossoverPoints, int tournamentSize)
    {
        this.generations = generations;
        this.elite = elite;
        this.size = popSize;
        this.mutationChance = mutationChance;

        this.genomes = new LinkedList<>();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome(crossoverPoints, mutationType));

        if (tournamentSize < 2)
            selector = new RouletteWheel();
        else
            selector = new Tournament(tournamentSize);
    }

    /**
     * Must be passed in the correct order
     *
     * @param hyperparameters: pop size, num elite, tournament size (0, 1 = rouletteWheel),
     *                         crossover (1 or 2), mutator (1-5), mutation chance
     */
    public Population(int generations, double... hyperparameters)
    {
        assert hyperparameters.length == 6;

        this.generations = generations;
        size = (int) hyperparameters[0];
        elite = (int) hyperparameters[1];
        mutationChance = hyperparameters[5];

        if (hyperparameters[2] < 2)
            selector = new RouletteWheel();
        else
            selector = new Tournament((int) hyperparameters[2]);

        int crossoverPoints = (int) hyperparameters[3];
        Configuration.MutationType mutationType = Configuration.MutationType.values()[(int) hyperparameters[4]];

        genomes = new LinkedList<>();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome(crossoverPoints, mutationType));
    }

    /**
     * Must be passed in the correct order
     *
     * @param hyperparameters: pop size, num elite, tournament size (0, 1 = rouletteWheel),
     *                         crossover (1 or 2), mutator (1-5), mutation chance
     */
    public void setHyperparams(Hyperparameter... hyperparameters)
    {
        assert hyperparameters.length == 6;
        genomes.clear();

        size = (int) hyperparameters[0].value;
        elite = (int) hyperparameters[1].value;
        mutationChance = hyperparameters[5].value;

        if (hyperparameters[2].value < 2)
            selector = new RouletteWheel();
        else
            selector = new Tournament((int) hyperparameters[2].value);

        int crossoverPoints = (int) hyperparameters[3].value;
        Configuration.MutationType mutationType = Configuration.MutationType.values()[(int) hyperparameters[4].value];

        this.genomes.clear();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome(crossoverPoints, mutationType));
    }

    public int run()
    {
        Genome bestGenome = genomes.get(0);

        // Running evolution
        for (int i = 0; i < generations; i++)
        {
            Genome fittest = step();
            if (fittest.getValue() > bestGenome.getValue())
            {
                System.out.println("Generation " + i + ". New best fitness: " + bestGenome.getValue() + " -> " + fittest.getValue());
                bestGenome = fittest;
            }
        }

        // Printing out final best individual
        int bestValue = bestGenome.getValue();
        System.out.println("GA final fittest individual" +
                "\nFitness:" + bestValue +
                "\nWeight: " + bestGenome.getWeight() +
                /*"\nIndividual" + bestGenome +*/ "\n\n");

        return bestValue;
    }

    public Genome step()
    {
        // Creating children
        List<Genome> children = new LinkedList<>();
        for (int i = 0; i < size - elite; i++)
        {
            List<Genome> parents = selectParents();
            Genome child = createChild(parents);
            children.add(mutateChild(child));
        }

        // Sorting to find the elite individuals
        List<Genome> sortedGenomes = genomes.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toCollection(LinkedList::new));

        // Adding all children to the next generation
        genomes.clear();
        genomes.addAll(sortedGenomes.subList(0, elite));
        genomes.addAll(children);

        return sortedGenomes.get(0);  // Returning the best elite individual
    }

    private Genome mutateChild(Genome child)
    {
        if (Configuration.instance.randomGenerator.nextDouble() < mutationChance)
        {
            Genome mutant = child.mutate();
            boolean valid = mutant.isValid();

            for (int i = 0; i < Configuration.instance.validAttempts; i++)
            {
                mutant = child.mutate();

                valid = mutant.isValid();
                if (valid) break;
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
        for (int i = 0; i < Configuration.instance.validAttempts; i++)
        {
            child = parents.get(0).crossover(parents.get(1));

            valid = child.isValid();
            if (valid) break;
        }

        if (valid)
            return child;

        // Never found a valid combination of parents so return fittest parent
        return parents.stream().max(Comparator.comparingInt(Genome::getValue)).get();
    }

    private List<Genome> selectParents()
    {
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
