package algorithm.ga.evolution.mutation;

import main.Configuration;

import java.util.List;

public class Inversion extends Mutator
{
    @Override
    public List<Boolean> mutate(List<Boolean> rep)
    {
        System.out.println("Inversion mutation");
        int pos1 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int pos2 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);

        int first, last;
        if (pos1 > pos2)
        {
            first = pos2;
            last = pos1;
        } else
        {
            first = pos1;
            last = pos2;
        }

        System.out.println("first pos" + first + " last pos " + last);
        for (int i = first; i < last; i++)
            rep.set(i, !rep.get(i));

        return rep;
    }
}