package algorithm.ga.evolution.selection;

import algorithm.base.Representation;

import java.util.LinkedList;
import java.util.List;

public abstract class Selector
{
    List<Representation> genomes;

    public Selector() { this.genomes = new LinkedList<>(); }

    public void beforeSelection(List<Representation> genomes)
    {
        this.genomes = genomes;
    }

    public abstract Representation select();
}
