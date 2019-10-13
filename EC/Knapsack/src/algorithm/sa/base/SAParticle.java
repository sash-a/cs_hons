package algorithm.sa.base;

import algorithm.base.Representation;
import algorithm.ga.evolution.mutation.BitFlip;
import algorithm.ga.evolution.mutation.Mutator;

import java.util.ArrayList;
import java.util.List;

public class SAParticle extends Representation
{
    Mutator mover;
    int value;

    public SAParticle()
    {
        super();
        mover = new BitFlip();
        value = -1;
    }

    public SAParticle(List<Boolean> rep)
    {
        super(rep);
        mover = new BitFlip();
        value = -1;
    }

    public SAParticle(SAParticle other)
    {
        super(other.rep);
        mover = new BitFlip();
        this.value = other.value;
    }

    public SAParticle move() { return new SAParticle(mover.mutate(new ArrayList<>(rep))); }

    @Override
    public int getValue()
    {
        if (value == -1)
            value = super.getValue();

        return value;
    }

    @Override
    public String toString()
    {
        return "Fitness: " + getValue() + "\nWeight: " + getWeight();
    }
}
