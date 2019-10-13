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
    private int size;
    private int elite;
    private double mutationChance;

    // Tournament selection constructor
    public Population(int k)
    {
        elite = Configuration.instance.elite;
        size = Configuration.instance.populationSize;
        mutationChance = Configuration.instance.mutationChance;

        this.genomes = new LinkedList<>();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome());

        selector = new Tournament(k);
    }

    // Roulette wheel constructor
    public Population()
    {
        elite = Configuration.instance.elite;
        size = Configuration.instance.populationSize;
        mutationChance = Configuration.instance.mutationChance;

        this.genomes = new LinkedList<>();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome());

        selector = new RouletteWheel();
    }

    public Population(double[] hyperparameters)
    {
        assert hyperparameters.length == 6;

        size = (int) hyperparameters[0];
        elite = (int) hyperparameters[1];
        mutationChance = hyperparameters[5];

        if (hyperparameters[2] < 2)
            selector = new RouletteWheel();
        else
            selector = new Tournament((int) hyperparameters[2]);

        Configuration.instance.crossoverPoints = (int) hyperparameters[3];
        Configuration.instance.mutationType = Configuration.MutationType.values()[(int) hyperparameters[4]];

        genomes = new LinkedList<>();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome());
    }

    /**
     * @param hyperparameters: pop size
     *                         num elite,
     *                         tournament size (0, 1 = rouletteWheel),
     *                         crossover (1 or 2),
     *                         mutator (1-5),
     *                         mutation chance
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

        Configuration.instance.crossoverPoints = (int) hyperparameters[3].value;
        Configuration.instance.mutationType = Configuration.MutationType.values()[(int) hyperparameters[4].value];

        this.genomes.clear();
        for (int i = 0; i < size; i++)
            genomes.add(new Genome());
    }

    public int run()
    {
        Genome bestGenome = genomes.get(0);

        // Running evolution
        for (int i = 0; i < Configuration.instance.numGAGens; i++)
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
            boolean valid = mutant.toKnapsack().isValid();

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
        for (int i = 0; i < Configuration.instance.validAttempts; i++) // todo fix
        {
            child = parents.get(0).crossover(parents.get(1));

            valid = child.isValid();
            if (valid) break;
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
