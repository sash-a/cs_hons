package main;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Phenotype;
import algorithm.ga.main.GARunner;
import algorithm.sa.main.Annealing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Application
{
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
        readGenes();
//        GARunner.run();
        new Annealing().run();
    }

    public static void readGenes()
    {
        Phenotype.allGenes = new ArrayList<>(Configuration.instance.numberOfItems);

        try
        {
            BufferedReader f = new BufferedReader(new FileReader(Configuration.instance.dataFilePath));
            f.readLine(); // Skipping first line
            String line = f.readLine();
            while (line != null)
            {
                int[] attributes = Arrays.stream(line.split(";")).map(Integer::parseInt).mapToInt(x -> x).toArray();
                Phenotype.allGenes.add(new Gene(attributes[0], attributes[1], attributes[2]));
                line = f.readLine();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}