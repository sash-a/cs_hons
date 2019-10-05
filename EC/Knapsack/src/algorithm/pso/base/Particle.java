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
        pos = new Vector(rep, 1, 0);
        bestPosition = new Vector(pos);
        bestValue = 0;
        velocity = new Vector(Configuration.instance.vmax, Configuration.instance.vmin);
        System.out.println("Starting velocity: " + velocity);
        System.out.println("Starting pos: " + pos);
    }

    public Particle(List<Boolean> rep)
    {
        super(rep);
        pos = new Vector(rep, 1, 0);
        bestPosition = new Vector(pos);
        bestValue = 0;
        velocity = new Vector(Configuration.instance.vmax, Configuration.instance.vmin);
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
            Vector inertia = new Vector(velocity).mul(Configuration.instance.inertia).clamp();
            Vector local = new Vector(bestPosition)
                    .sub(pos)
//                    .normalize() // TODO should this be done? Would it be better to mutate a bigger magnitude?
                    .mul(Configuration.instance.localForce)
                    .mul(Configuration.instance.randomGenerator.nextDouble())
                    .clamp();

            Vector global = new Vector(globalBest)
                    .sub(pos)
//                    .normalize() // TODO should this be done? Would it be better to mutate a bigger magnitude?
                    .mul(Configuration.instance.globalForce)
                    .mul(Configuration.instance.randomGenerator.nextDouble())
                    .clamp();
            System.out.println("i: " + inertia + "\nl: " + local + "\ng: " + global);

            velocity = inertia.add(local).add(global);
            System.out.println("v: " + velocity);
            possiblePos = new Vector(pos).add(velocity);


            double rand = Configuration.instance.randomGenerator.nextDouble();
            for (int i = 0; i < velocity.size(); i++)
                rep.set(i, rand < sig(velocity.getVelocity().get(i)));

            valid = isValid();
        }

        pos = possiblePos;

        int currVal = getValue();
        if (currVal > bestValue)
        {
            bestValue = currVal;
            bestPosition = new Vector(pos);
        }
    }

    public static double sig(double n)
    {
        return 1 / Math.exp(-n);
    }

    @Override
    public int compareTo(Particle other)
    {
        return Integer.compare(this.getValue(), other.getValue());
    }
}
