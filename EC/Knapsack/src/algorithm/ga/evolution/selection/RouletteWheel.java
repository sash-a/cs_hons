package algorithm.ga.evolution.selection;

import algorithm.base.Representation;
import algorithm.base.Knapsack;
import main.Configuration;

import java.util.*;
import java.util.stream.Collectors;

public class RouletteWheel extends Selector
{
    List<Double> selectionChances;

    public RouletteWheel()
    {
        super();
        this.selectionChances = new LinkedList<>();
    }

    public Representation select()
    {
        double probability = Configuration.instance.randomGenerator.nextDouble();

        for (int i = 0; i < selectionChances.size(); i++)
        {
            if (probability <= selectionChances.get(i))
            {
                return genomes.get(i);
            }
        }

        System.out.println("Error: could not find an appropriate genome given the probabilities");
        return null;
    }

    public void beforeSelection(List<Representation> genomes)
    {
        super.beforeSelection(genomes);
        selectionChances.clear();
        List<Knapsack> phenotypes = genomes.stream().map(Representation::toKnapsack).sorted(Collections.reverseOrder()).collect(Collectors.toList());

        // Getting the selection probabilities
        double totalFitness = getTotalFitness(phenotypes);
        double fitnessSum = 0;

        for (Knapsack p : phenotypes)
        {
            fitnessSum += p.getFitness() / totalFitness;
            selectionChances.add(fitnessSum);
        }
        // Ordering selection chances and genomes
        Comparator<Representation> genomeComparator = Comparator.comparing(Representation::toKnapsack, Collections.reverseOrder());
        this.genomes.sort(genomeComparator);
    }

    private int getTotalFitness(List<Knapsack> phenotypes)
    { return phenotypes.stream().map(Knapsack::getFitness).reduce(0, Integer::sum); }
}
