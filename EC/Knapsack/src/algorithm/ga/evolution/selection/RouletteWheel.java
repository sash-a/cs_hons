package algorithm.ga.evolution.selection;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;
import algorithm.ga.base.Phenotype;
import algorithm.ga.main.GARunner;
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

    public Genome select()
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

    public void beforeSelection(List<Genome> genomes)
    {
        super.beforeSelection(genomes);
        selectionChances.clear();
        List<Phenotype> phenotypes = genomes.stream().map(Genome::toPheno).sorted(Collections.reverseOrder()).collect(Collectors.toList());

        // Getting the selection probabilities
        double totalFitness = getTotalFitness(phenotypes);
        double fitnessSum = 0;

        for (Phenotype p : phenotypes)
        {
            fitnessSum += p.getFitness() / totalFitness;
            selectionChances.add(fitnessSum);
        }
        // Ordering selection chances and genomes
        Comparator<Genome> genomeComparator = Comparator.comparing(Genome::toPheno, Collections.reverseOrder());
        this.genomes.sort(genomeComparator);
    }

    private int getTotalFitness(List<Phenotype> phenotypes)
    { return phenotypes.stream().map(Phenotype::getFitness).reduce(0, Integer::sum); }
}
