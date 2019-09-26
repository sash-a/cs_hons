package algorithm.ga.evolution.crossover;

import main.Configuration;

import java.util.List;

public class TwoPointCrossover extends Crossover {
    public TwoPointCrossover()
    {
        points = 2;
    }

    @Override
    public List<Boolean> crossover(List<Boolean> self, List<Boolean> other) {
        int point1 = (int)(Configuration.instance.randomGenerator.nextDouble() * Configuration.instance.maximumCapacity);
        int point2 = (int)(Configuration.instance.randomGenerator.nextDouble() * Configuration.instance.maximumCapacity);
        List<Boolean> child = self.subList(0, point1);
        child.addAll(other.subList(point1, point2));
        child.addAll(self.subList(point2, Configuration.instance.maximumCapacity));

        return child;
    }
}
