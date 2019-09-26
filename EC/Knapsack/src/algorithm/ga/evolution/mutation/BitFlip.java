package algorithm.ga.evolution.mutation;

import main.Configuration;

import java.util.List;

public class BitFlip extends Mutator
{
    @Override
    public List<Boolean> mutate(List<Boolean> rep)
    {
        int pos = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        rep.set(pos, !rep.get(pos));
        return rep;
    }
}