package algorithm.pso.base;

import algorithm.base.Representation;
import main.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vector
{
    private List<Double> velocity;
    private double vmax, vmin;

    public Vector(Vector other)
    {
        this.vmax = other.vmax;
        this.vmin = other.vmin;
        this.velocity = new ArrayList<>(other.velocity);
    }

    public Vector(List<Boolean> rep, double vmax, double vmin)
    {
        this.vmax = vmax;
        this.vmin = vmin;

        this.velocity = new ArrayList<>();
        for (int i = 0; i < rep.size(); i++) velocity.add(rep.get(i) ? 1.0 : 0.0);
    }

    public Vector(double vmax, double vmin)
    {
        this.vmax = vmax;
        this.vmin = vmin;

        this.velocity = new ArrayList<>();
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            velocity.add(
                    (Configuration.instance.randomGenerator.nextDouble() * (vmax + Math.abs(vmin)) + vmin));
    }

    public Vector clamp()
    {
        for (int i = 0; i < velocity.size(); i++)
            velocity.set(i, Math.min(Math.max(velocity.get(i), vmin), vmax));

        return this;
    }


    public Vector add(Vector other) throws IndexOutOfBoundsException
    {
        if (other.velocity.size() != this.velocity.size())
            throw new IndexOutOfBoundsException("Velocity vectors not the same size");

        for (int i = 0; i < this.velocity.size(); i++)
            this.velocity.set(i, this.velocity.get(i) + other.velocity.get(i));

        return this;
    }

    public Vector sub(Vector other) throws IndexOutOfBoundsException
    {
        if (other.size() != this.size())
            throw new IndexOutOfBoundsException("Velocity vectors not the same size");

        for (int i = 0; i < this.velocity.size(); i++)
            this.velocity.set(i, this.velocity.get(i) - other.velocity.get(i));

        return this;
    }

    public Vector mul(double n)
    {
        for (int i = 0; i < this.size(); i++)
            this.velocity.set(i, this.velocity.get(i) * n);

        return this;
    }

    public double mag()
    {
        double sum = 0;
        for (double v : velocity)
            sum += v * v;

        return Math.sqrt(sum);
    }

    public Vector normalize()
    {
        double mag = mag();

        for (int i = 0; i < this.velocity.size(); i++)
            this.velocity.set(i, this.velocity.get(i) / mag);

        return this;
    }

    public int size() { return velocity.size(); }

    public List<Double> getVelocity()
    {
        return velocity;
    }

    @Override
    public String toString()
    {
        return velocity.toString();
    }
}
