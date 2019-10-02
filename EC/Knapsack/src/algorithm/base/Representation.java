package algorithm.base;

import main.Configuration;

import java.util.ArrayList;
import java.util.List;

public class Representation
{
    public List<Boolean> rep;

    public Representation()
    {
        rep = new ArrayList<>();
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            rep.add(Configuration.instance.randomGenerator.nextBoolean());

        boolean valid = new Knapsack(rep).isValid();
        while (!valid)
        {
            // Remove random genes until weight is acceptable:
            // Pick a random position in list and random direction (forward or back) and traverse until you find a gene
            // then remove it to save weight.
            int pos = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
            int dir = 1;
            boolean reverse = Configuration.instance.randomGenerator.nextBoolean();
            if (reverse)
                dir = -1;

            for (int i = pos; i < Configuration.instance.numberOfItems && i >= 0; i += dir)
            {
                if (rep.get(i))
                {
                    rep.set(i, false);
                    break;
                }
            }

            valid = new Knapsack(rep).isValid();
        }
    }

    public Representation(List<Boolean> rep) { this.rep = rep; }

    public Knapsack toKnapsack()
    {
        return new Knapsack(this);
    }

    public int getValue() { return new Knapsack(this).getFitness(); }

    public int getWeight() { return new Knapsack(this).getWeight(); }

    public boolean isValid() { return new Knapsack(this).isValid(); }

    @Override
    public String toString()
    {
        return this.rep.toString();
    }

}
