package algorithm.ga.evolution.crossover;

import main.Configuration;

import java.util.LinkedList;
import java.util.List;

public class OnePointCrossover extends Crossover
{
    public OnePointCrossover()
    {
        points = 1;
    }

    @Override
    public List<Boolean> crossover(List<Boolean> self, List<Boolean> other)
    {
        // TODO should this return return two children? i.e self x other and other x self

        int point = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        List<Boolean> child = new LinkedList<>(self.subList(0, point));
        child.addAll(new LinkedList<>(other.subList(point, Configuration.instance.numberOfItems)));

        return child;
    }
}
