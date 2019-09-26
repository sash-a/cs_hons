package algorithm.ga.evolution.crossover;

import main.Configuration;

import java.util.List;

public class OnePointCrossover extends Crossover{
    public OnePointCrossover(){
        points = 1;
    }

    @Override
    public List<Boolean> crossover(List<Boolean> self, List<Boolean> other) {
        // TODO should this return return two children? i.e self x other and other x self

        int point = (int)(Configuration.instance.randomGenerator.nextDouble() * Configuration.instance.maximumCapacity);
        System.out.println(point);
        List<Boolean> child = self.subList(0, point);
        child.addAll(other.subList(point, Configuration.instance.maximumCapacity));

        return child;
    }
}
