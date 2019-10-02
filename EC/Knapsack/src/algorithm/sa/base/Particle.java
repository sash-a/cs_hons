package algorithm.sa.base;

import algorithm.base.Representation;
import algorithm.ga.evolution.mutation.BitFlip;

import java.util.ArrayList;
import java.util.List;

public class Particle extends Representation
{
    BitFlip mover;

    public Particle()
    {
        super();
        mover = new BitFlip();
    }

    public Particle(List<Boolean> rep)
    {
        super(rep);
        mover = new BitFlip();
    }

    public Particle move() { return new Particle(mover.mutate(new ArrayList<>(rep))); }
}
