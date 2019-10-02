package algorithm.ga.evolution.selection;

import algorithm.base.Representation;
import algorithm.ga.base.Genome;

import java.util.LinkedList;
import java.util.List;

public abstract class Selector
{
    List<Genome> genomes;

    public Selector() { this.genomes = new LinkedList<>(); }

    public void beforeSelection(List<Genome> genomes)
    {
        this.genomes = genomes;
    }

    public abstract Genome select();
}
