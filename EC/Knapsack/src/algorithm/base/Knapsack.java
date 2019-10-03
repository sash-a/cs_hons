package algorithm.base;

import main.Configuration;

import java.util.*;

/*
    Phenotype in the case of a GA
 */
public class Knapsack implements Comparable<Knapsack>
{
    public static Item [] allItems;
    public List<Item> items;

    private Knapsack()
    {
        this.items = new LinkedList<>();
    }

    public Knapsack(List<Boolean> representation)
    {
        this();
        for (int i = 0; i < representation.size() - 1; i++)
            if (representation.get(i))
                items.add(allItems[i]);
    }

    public Knapsack(Representation representation)
    {
        this(representation.rep);
    }

    public boolean isValid() { return getWeight() <= Configuration.instance.maximumCapacity; }

    public int getFitness() { return items.stream().map(g -> g.value).reduce(0, Integer::sum); }

    public int getWeight() { return items.stream().map(g -> g.weight).reduce(0, Integer::sum); }

    public List<Boolean> getRepresentation()
    {
        List<Boolean> rep = new LinkedList<>();
        Comparator<Item> cmp = Comparator.comparingInt(o -> o.id);
        items.sort(cmp);

        int prevID = 0;
        for (Item g : items)
        {
            for (int i = prevID; i < g.id - 1; i++)
            {
                rep.add(false);
            }
            rep.add(true);
            prevID = g.id;
        }

        for (int i = prevID; i < Configuration.instance.numberOfItems; i++)
        {
            rep.add(false);
        }
        return rep;
    }

    @Override
    public String toString()
    {
        return items.toString();
    }

    @Override
    public int compareTo(Knapsack other)
    {
        return Integer.compare(this.getFitness(), other.getFitness());
    }
}
