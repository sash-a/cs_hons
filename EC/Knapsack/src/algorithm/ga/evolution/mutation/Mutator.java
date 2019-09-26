package algorithm.ga.evolution.mutation;

import java.util.List;

public abstract class Mutator {
    public abstract List<Boolean> mutate(List<Boolean> rep);
}
