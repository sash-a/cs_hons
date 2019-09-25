package algorithm.ga.evolution.selection;

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
        List<Genome> tournament = new ArrayList<>();
        HashSet<Integer> selected = new HashSet<>();

        for (int i = 0; i < tournamentSize; i++)
        {
            int idx = (int) (Configuration.instance.randomGenerator.nextDouble() * Configuration.instance.numberOfItems);
            if (selected.contains(idx)) // making sure all tournament entrants are distinct
            {
                i--;
                continue;
            }

            selected.add(idx);
            tournament.add(individuals.get(idx));
        }

        return Collections.max(tournament);
    }
}
