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
    public void run()
    {
        System.out.println("Running GA " + Configuration.instance.dataFilePath);
        Population.allGenes = new ArrayList<>(Configuration.instance.numberOfItems);
        List<Genome> genomes = new LinkedList<>();

        // Collecting genes
        try
        {
            BufferedReader f = new BufferedReader(new FileReader(Configuration.instance.dataFilePath));
            f.readLine(); // Skipping first line
            String line = f.readLine();
            while (line != null)
            {
                int[] attributes = Arrays.stream(line.split(";")).map(Integer::parseInt).mapToInt(x -> x).toArray();
                Population.allGenes.add(new Gene(attributes[0], attributes[1], attributes[2]));
                line = f.readLine();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        // Creating genomes
        for (int i = 0; i < Configuration.instance.populationSize; i++)
            genomes.add(new Genome());

        Population pop = new Population(genomes);

        // Running evolution
        for (int i = 0; i < Configuration.instance.generations; i++)
        {
            System.out.println("gen" + i);
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
