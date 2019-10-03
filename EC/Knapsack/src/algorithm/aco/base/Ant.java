package algorithm.aco.base;

import algorithm.base.Knapsack;
import algorithm.base.Representation;
import main.Configuration;

import java.util.*;

public class Ant implements Comparable<Ant>
{
    public Representation representation;
    public List<Integer> path;
    public List<Integer> notVisited;  // Index of items that have not yet been considered

    public int knapsackValue;

    public Ant() { reset(); }

    public Ant(Ant other)
    {
        this.path = new ArrayList<>(other.path);
        this.representation = new Representation(other.representation.rep);
        this.knapsackValue = other.knapsackValue;
    }

    public void reset()
    {
        path = new ArrayList<>();

        List<Boolean> rep = new ArrayList<>(Arrays.asList(new Boolean[Configuration.instance.numberOfItems]));

        Collections.fill(rep, Boolean.FALSE);
        this.representation = new Representation(rep);

        notVisited = new LinkedList<>();
        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
            notVisited.add(i);

        Collections.shuffle(notVisited); // So that no item is biased. TODO is this correct?
    }

    public int search(double[][] pheromones)
    {
        // initial city
        // can take first item in not visited list because it is random

        int currentItem = notVisited.get(0);
        representation.rep.set(currentItem, true);
        path.add(currentItem);
        notVisited.remove(0);


        for (int i = 0; i < Configuration.instance.numberOfItems; i++)
        {
            // Calculating the denominator for the probability function
            double totalPheromonesRatio = 0.0;
            for (Integer visitableItem : notVisited)
                totalPheromonesRatio += Math.pow(pheromones[currentItem][visitableItem], Configuration.instance.alpha) /
                        Math.pow(Knapsack.allItems[visitableItem].value, Configuration.instance.beta);

            double probability = 0.0;
            double rand = Configuration.instance.randomGenerator.nextDouble();

            // Trying to find next item in the path
            Iterator<Integer> itr = notVisited.iterator();
            while (itr.hasNext())
            {
                int possibleItem = itr.next();
                // Probability of choosing this item
                probability += Math.pow(pheromones[currentItem][possibleItem], Configuration.instance.alpha) /
                        Math.pow(Knapsack.allItems[possibleItem].value, Configuration.instance.beta) /
                        totalPheromonesRatio;

                if (rand < probability)
                {
                    representation.rep.set(possibleItem, true);
                    itr.remove(); // Either item is used or makes knapsack to heavy, therefore remove it

                    // Only add if item is valid
                    if (!representation.isValid())
                    {
                        representation.rep.set(possibleItem, false);
                        continue;
                    }

                    currentItem = possibleItem;
                    path.add(currentItem);
                    break;
                }
            }

            // If at max capacity no use in trying anymore
            if (representation.getWeight() == Configuration.instance.maximumCapacity) // TODO: this call may be expensive in large lists
                break;
        }

        knapsackValue = representation.getValue();
        return knapsackValue;
    }

    public void placePheromones(double[][] pheromones)
    {
        // TODO make it bidirectional?
        for (int i = 0; i < path.size() - 1; i++)
        {
            pheromones[path.get(i)][path.get(i + 1)] += knapsackValue;
            pheromones[path.get(i + 1)][path.get(i)] += knapsackValue;
        }
        pheromones[path.get(path.size() - 1)][path.get(0)] += knapsackValue;
        pheromones[path.get(0)][path.get(path.size() - 1)] += knapsackValue;
    }

    @Override
    public int compareTo(Ant other)
    {
        return Integer.compare(this.knapsackValue, other.knapsackValue);
    }
}
