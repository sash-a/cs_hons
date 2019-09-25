package main;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;

import java.util.Arrays;

public class Application
{
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
        System.out.println("Running");
        Gene g1 = new Gene(0, 1, 2);
        Gene g2 = new Gene(0, 2, 3);
        Gene g3 = new Gene(0, 3, 1);
        Gene g4 = new Gene(0, 4, 4);

        Genome gen = new Genome(Arrays.asList(g1, g2, g3, g4));
        System.out.println(gen.getFitness());
    }
}