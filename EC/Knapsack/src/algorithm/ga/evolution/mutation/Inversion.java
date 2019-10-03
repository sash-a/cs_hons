package algorithm.ga.evolution.mutation;

import main.Configuration;

import java.util.List;

public class Inversion extends Mutator
{
    @Override
    public List<Boolean> mutate(List<Boolean> rep)
    {
        int pos1 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int pos2 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);

        if (pos1 > pos2)
        {
            int temp = pos1;
            pos1 = pos2;
            pos2 = temp;
        }

        for (int i = pos1; i < pos2; i++)
            rep.set(i, !rep.get(i));

        return rep;
    }
}