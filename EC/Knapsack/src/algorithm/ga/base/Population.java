package algorithm.ga.base;

import algorithm.ga.evolution.selection.RouletteWheel;
import algorithm.ga.evolution.selection.Selector;
import algorithm.ga.evolution.selection.Tournament;
import main.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Population {
    public List<Genome> genomes;
    public List<Gene> allGenes;
    public Selector selector;


    public Population(List<Genome> genomes, List<Gene> genes, int k) {
        this.genomes = genomes;
        this.allGenes = genes;
        selector = new Tournament(k);
    }

    public Population(List<Genome> genomes, List<Gene> genes) {
        this.genomes = genomes;
        this.allGenes = genes;
        selector = new RouletteWheel();
    }

    public void step() {
        for (int i = 0; i < Configuration.instance.newChildren; i++) {
            List<Genome> parents = selectParents();
            Genome child = createChild(parents);
            mutateChild(child);
        }

    }

    private Genome mutateChild(Genome child) {
        if (Configuration.instance.randomGenerator.nextDouble() < Configuration.instance.mutationChance)
            return child.mutate();

        return child;
    }

    private Genome createChild(List<Genome> parents) {
        return parents.get(0).crossover(parents.get(1));
    }

    private List<Genome> selectParents() {
        selector.beforeSelection(genomes, allGenes);

        List<Genome> parents = new ArrayList<>();
        for (int i = 0; i < 2; i++)  // TODO config option parents to crossover
        {
            parents.add(selector.select());
            parents.add(selector.select());
        }

        return parents;
    }
}
