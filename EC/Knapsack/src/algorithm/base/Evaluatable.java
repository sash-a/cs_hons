package algorithm.base;

public abstract class Evaluatable
{
    /**
     * Resets this, using the hyperparameters provided. Hyperparameters must be passed in the correct order
     */
    public abstract void setHyperparams(Hyperparameter... hyperparameters);

    public abstract int run();
}
