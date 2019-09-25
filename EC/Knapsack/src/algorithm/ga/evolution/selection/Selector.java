package algorithm.ga.evolution.selection;

import algorithm.ga.base.Gene;
import algorithm.ga.base.Genome;

import java.util.ArrayList;
import java.util.List;

public abstract class Selector
{
    List<Genome> individuals;

    public Selector() { this.individuals = new ArrayList<>(); }

    public void beforeSelection(List<Genome> individuals) { this.individuals = individuals; }

    public abstract Genome select();
}
