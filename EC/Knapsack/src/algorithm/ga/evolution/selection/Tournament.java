package algorithm.ga.evolution.selection;

import algorithm.base.Representation;
import algorithm.base.Knapsack;
import algorithm.ga.base.Genome;
import main.Configuration;

import java.util.*;

public class Tournament extends Selector
{
    public int tournamentSize;

    public Tournament(int tournamentSize)
    {
        super();

        assert tournamentSize >= 2;
        this.tournamentSize = tournamentSize;
    }

    public Genome select()
    {
        List<Genome> tournament = new LinkedList<>();
        HashSet<Integer> selected = new HashSet<>();

        int attempts = Configuration.instance.validAttempts;
        for (int i = 0; i < tournamentSize; i++)
        {
            int idx = (int) (Configuration.instance.randomGenerator.nextDouble() * genomes.size());
            if (selected.contains(idx)) // making sure all tournament entrants are distinct
            {
                i--;
                continue;
            }

            selected.add(idx);
            tournament.add(genomes.get(idx));

            // Protects against the case where number of genes in the genome < tournament size
            attempts--;
            if (attempts == 0)
                break;
        }

        return (tournament.stream()
                .max(Comparator.comparingInt(Representation::getValue))
                .get());
    }
}
