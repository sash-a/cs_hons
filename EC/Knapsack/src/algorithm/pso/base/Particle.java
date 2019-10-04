package algorithm.pso.base;

import algorithm.base.Representation;
import main.Configuration;

import java.util.List;

public class Particle extends Representation implements Comparable<Particle>
{
    public Vector pos;
    public Vector velocity;
    public Vector bestPosition;
    public int bestValue;

    public Particle()
    {
        super();
        pos = new Vector(rep);
        pos.wrap();
        bestPosition = new Vector(pos);
        bestValue = 0;
        velocity = new Vector(10, 10, 10);
    }

    public Particle(List<Boolean> rep)
    {
        super(rep);
        pos = new Vector(rep);
        bestPosition = new Vector(pos);
        bestValue = 0;
        velocity = new Vector(100, 100, 100);
    }

    public Particle(Particle other)
    {
        rep = other.rep;
        pos = other.pos;
        bestPosition = other.bestPosition;
        bestValue = other.bestValue;
        velocity = other.velocity;
    }

    public void move(Vector globalBest)
    {
        boolean valid = false;

        Vector possiblePos = new Vector(pos);
        // TODO test
        while (!valid)
        {
            Vector inertia = new Vector(velocity).mul(Configuration.instance.inertia);
            Vector local = new Vector(bestPosition)
                    .sub(pos)
//                    .normalize() // TODO should this be done? Would it be better to mutate a bigger magnitude?
                    .mul(Configuration.instance.localForce)
                    .mul(Configuration.instance.randomGenerator.nextDouble());

            Vector global = new Vector(globalBest)
                    .sub(pos)
//                    .normalize() // TODO should this be done? Would it be better to mutate a bigger magnitude?
                    .mul(Configuration.instance.globalForce)
                    .mul(Configuration.instance.randomGenerator.nextDouble());

            velocity = inertia.add(local).add(global);
            possiblePos = new Vector(pos).add(velocity);

            if (Configuration.instance.useClamp)
                possiblePos.clamp();
            else
                possiblePos.wrap();

            valid = new Representation(possiblePos.toBoolList()).isValid();
        }

        pos = possiblePos;
        update();

        int currVal = getValue();
        if (currVal > bestValue)
        {
            bestValue = currVal;
            bestPosition = new Vector(pos);
        }
    }

    // So that all the methods from super class work
    public void update() { rep = pos.toBoolList(); }

    @Override
    public int compareTo(Particle other)
    {
        return Integer.compare(this.getValue(), other.getValue());
    }
}
