package algorithm.ga.evolution.crossover;

import java.util.List;

public abstract class Crossover {
    public int points;
    public abstract List<Boolean> crossover(List<Boolean> self, List<Boolean> other);
}