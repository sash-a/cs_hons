package algorithm.ga.evolution.selection;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;

import java.util.ArrayList;
import java.util.List;

public abstract class Selector {
    List<Genome> genomes;
    List<Gene> allGenes;

    public Selector() {
        this.genomes = new ArrayList<>();
    }

    public void beforeSelection(List<Genome> genomes, List<Gene> allGenes) {
        this.genomes = genomes;
        this.allGenes = allGenes;
    }

    public abstract Genome select();
}
