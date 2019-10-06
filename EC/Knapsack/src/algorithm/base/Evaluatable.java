package algorithm.base;

public abstract class Evaluatable
{
    public abstract void setHyperparams(Hyperparameter... bns);

    public abstract int run();
}
