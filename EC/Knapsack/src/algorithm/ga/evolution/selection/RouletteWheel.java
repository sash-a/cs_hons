package algorithm.ga.evolution.selection;

import algorithm.ga.base.Genome;
import main.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RouletteWheel extends Selector
{
    List<Double> selectionChances;

    public RouletteWheel()
    {
        super();
        this.selectionChances = new ArrayList<>();
    }

    public Genome select()
    {
        double probability = Configuration.instance.randomGenerator.nextDouble();
        for (int i = 0; i < selectionChances.size(); i++)
        {
            if (probability <= selectionChances.get(i))
                return individuals.get(i);
        }

        System.out.println("Error: could not find an appropriate genome given the probabilities");
        return null;
    }

    public void beforeSelection(List<Genome> individuals)
    {
        super.beforeSelection(individuals);

        // Getting the selection probabilities
        double totalFitness = getTotalFitness(individuals);
        double fitnessSum = 0;
        List<Double> newSelectionChances = new ArrayList<>();

        for (Genome g : individuals)
        {
            fitnessSum += g.getFitness() / totalFitness;
            newSelectionChances.add(fitnessSum);
        }
        this.selectionChances = newSelectionChances.stream().sorted().collect(Collectors.toList());
    }

    private int getTotalFitness(List<Genome> genomes) {return genomes.stream().map(Genome::getFitness).reduce(0, Integer::sum);}
}
