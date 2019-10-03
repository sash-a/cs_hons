package algorithm.ga.evolution.crossover;

import main.Configuration;

import javax.crypto.spec.PSource;
import java.util.LinkedList;
import java.util.List;

public class TwoPointCrossover extends Crossover
{
    public TwoPointCrossover()
    {
        points = 2;
    }

    @Override
    public List<Boolean> crossover(List<Boolean> self, List<Boolean> other)
    {
        // TODO should this return return two children? i.e self x other and other x self

        int point1 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        int point2 = Configuration.instance.randomGenerator.nextInt(Configuration.instance.numberOfItems);
        if (point1 > point2)
        {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        List<Boolean> child = new LinkedList<>(self.subList(0, point1));
        child.addAll(new LinkedList<>(other.subList(point1, point2)));
        child.addAll(new LinkedList<>(self.subList(point2, Configuration.instance.numberOfItems)));

        return child;
    }
}
