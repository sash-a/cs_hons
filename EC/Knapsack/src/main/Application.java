package main;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;
import algorithm.ga.base.Phenotype;
import algorithm.ga.base.Population;

import java.util.Arrays;
import java.util.List;

public class Application {
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args) {
        System.out.println("Running");
        Gene g1 = new Gene(0, 1, 2);
        Gene g2 = new Gene(1, 2, 3);
        Gene g3 = new Gene(2, 3, 1);
        Gene g4 = new Gene(3, 4, 4);

        List<Gene> allGenes = Arrays.asList(g1, g2, g3, g4);
        Genome gen1 = new Genome(Configuration.instance.crossoverPoints);
        Genome gen2 = new Genome(Configuration.instance.crossoverPoints);

        Population pop = new Population(Arrays.asList(gen1, gen2), allGenes);
        pop.step();

        System.out.println(gen1);
        System.out.println(gen2);

        System.out.println(gen1.crossover(gen2));
    }
}