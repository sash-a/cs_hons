package algorithm.ga.evolution.mutation;

import main.Configuration;

import java.util.List;

public class Exchange extends Mutator {
    @Override
    public List<Boolean> mutate(List<Boolean> rep)
    {
        System.out.println("Exchange mutation");
        int pos1 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int pos2 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);

        boolean temp = rep.get(pos1);
        rep.set(pos1, rep.get(pos2));
        rep.set(pos2, temp);

        return rep;
    }
}