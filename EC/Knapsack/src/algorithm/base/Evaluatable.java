package algorithm.base;

public abstract class Evaluatable
{
    public abstract void setHyperparams(Hyperparameter... hyperparameters);

    public abstract int run();
}
