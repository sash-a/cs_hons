package algorithm.ga.evolution.mutation;

import main.Configuration;

import java.util.List;

public class Insertion extends Mutator
{
    @Override
    public List<Boolean> mutate(List<Boolean> rep)
    {
        // Move value at position 2 to position 1
        int pos1 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int pos2 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);

        rep.add(pos1, rep.get(pos2));
        rep.remove(pos2 + 1);

        return rep;
    }
}