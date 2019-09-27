package algorithm.ga.evolution.mutation;

import algorithm.ga.base.Genome;
import main.Configuration;

import java.util.LinkedList;
import java.util.List;

public class Displacement extends Mutator
{
    @Override
    public List<Boolean> mutate(List<Boolean> rep)
    {
        System.out.println("Displacement mutation");
        int startMove = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int endMove = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        if (endMove < startMove)
        {
            int temp = startMove;
            startMove = endMove;
            endMove = temp;
        }

        List<Boolean> toMove = new LinkedList<>(rep.subList(startMove, endMove));
        rep.subList(startMove, endMove).clear();
        System.out.println("Moving: " + toMove);

        int startPlace = Configuration.instance.randomGenerator.nextInt(rep.size());
        rep.addAll(startPlace, toMove);

        return rep;
    }
}