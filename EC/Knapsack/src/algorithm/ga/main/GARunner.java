package algorithm.ga.main;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;
import algorithm.ga.base.Phenotype;
import algorithm.ga.base.Population;
import main.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GARunner
{
    public static void run()
    {
        System.out.println("Running GA " + Configuration.instance.dataFilePath);
        List<Genome> genomes = new LinkedList<>();

        // Creating genomes
        for (int i = 0; i < Configuration.instance.populationSize; i++)
            genomes.add(new Genome());

        Population pop = new Population(genomes);

        // Running evolution
        for (int i = 0; i < Configuration.instance.generations; i++)
        {
            System.out.println("Generation " + i);
            pop.step();
        }

        // Printing out best individual
        Phenotype best = (pop.genomes.stream()
                .map(Genome::toPheno)
                .max(Comparator.comparingInt(Phenotype::getFitness)).get());

        System.out.println("Final fittest individual" +
                "\nFitness:" + best.getFitness() +
                "\nWeight: " + best.getWeight() +
                "\nIndividual" + best);
    }
}
