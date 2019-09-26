package algorithm.ga.evolution.selection;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;
import algorithm.ga.base.Phenotype;
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
                return genomes.get(i);
        }

        System.out.println("Error: could not find an appropriate genome given the probabilities");
        return null;
    }

    public void beforeSelection(List<Genome> genomes, List<Gene> allGenes)
    {
        super.beforeSelection(genomes, allGenes);
        List<Phenotype> phenotypes = genomes.stream().map(g -> new Phenotype(g, allGenes)).collect(Collectors.toList());

        // Getting the selection probabilities
        double totalFitness = getTotalFitness(phenotypes);
        double fitnessSum = 0;
        List<Double> newSelectionChances = new ArrayList<>();

        for (Phenotype p : phenotypes)
        {
            fitnessSum += p.getFitness() / totalFitness;
            newSelectionChances.add(fitnessSum);
        }
        this.selectionChances = newSelectionChances.stream().sorted().collect(Collectors.toList());
    }

    private int getTotalFitness(List<Phenotype> phenotypes) {return phenotypes.stream().map(Phenotype::getFitness).reduce(0, Integer::sum);}
}
