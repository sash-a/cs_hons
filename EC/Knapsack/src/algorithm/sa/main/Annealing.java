package algorithm.sa.main;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.sa.base.SAParticle;
import main.Configuration;

public class Annealing extends Evaluatable
{
    public double coolingRate;
    public double temp;

    public SAParticle currentSolution;
    public SAParticle bestSolution;

    public Annealing()
    {
        Configuration.instance.mutationType = Configuration.MutationType.BITFLIP;

        this.temp = Configuration.instance.initialTemp;
        this.coolingRate = Configuration.instance.coolingRate;

        currentSolution = new SAParticle();
        bestSolution = new SAParticle(currentSolution.rep);
    }

    /**
     * @param hyperparameters: temp, cooling rate
     */
    @Override
    public void setHyperparams(Hyperparameter... hyperparameters)
    {
        assert hyperparameters.length == 2;

        this.temp = hyperparameters[0].value;
        this.coolingRate = hyperparameters[1].value;
    }

    public boolean accept(double currentEnergy, double newEnergy)
    {
        double energyDelta = newEnergy - currentEnergy;

        if (energyDelta <= 0)
            return true;
        else
            return Math.exp(-energyDelta / this.temp) > Configuration.instance.randomGenerator.nextDouble();
    }

    public SAParticle randomNeighbour()
    {
        SAParticle nextSolution = currentSolution.move();
        for (int i = 0; i < Configuration.instance.validAttempts; i++)
        {
            if (nextSolution.isValid())
                return nextSolution;

            nextSolution = currentSolution.move();
        }

        System.out.println("Could not find valid neighbour solution");
        return currentSolution;
    }

    public void step(int gen)
    {
        SAParticle nextSolution = randomNeighbour();

        int currentFitness = currentSolution.getValue();
        int nextFitness = nextSolution.getValue();

        if (accept(-(double) currentFitness, -(double) nextFitness))
            currentSolution = nextSolution;

        if (nextFitness > bestSolution.getValue())
        {
            System.out.println("New best solution: " + nextFitness + ", compared to old fitness: " + bestSolution.getValue() + " (" + gen + ")");
            bestSolution = nextSolution;
        }

        temp *= coolingRate;
    }

    public int run()
    {
        int gen = 0;
        while (temp > Configuration.instance.minTemp)
            step(gen++);

        System.out.println("Ran for " + gen + " generations");
        System.out.println("Best solution " + bestSolution);
        return bestSolution.getValue();
    }


}
