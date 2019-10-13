package algorithm.sa.main;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.sa.base.SAParticle;
import main.Configuration;

public class Annealing extends Evaluatable
{
    public double coolingRate;
    public double temp;
    private double minTemp;
    private double resetChance;

    public SAParticle currentSolution;
    public SAParticle bestSolution;

    public Annealing()
    {
        this.temp = Configuration.instance.initialTemp;
        this.minTemp = Configuration.instance.minTemp;
        this.coolingRate = Configuration.instance.coolingRate;
        this.resetChance = Configuration.instance.resetToGlobalChance;

        currentSolution = new SAParticle();
        bestSolution = new SAParticle(currentSolution);
    }

    /**
     * @param hyperparameters: temp, cooling rate, reset to global chance
     */
    @Override
    public void setHyperparams(Hyperparameter... hyperparameters)
    {
        assert hyperparameters.length == 4;

        this.temp = hyperparameters[0].value;
        this.minTemp = hyperparameters[1].value;
        this.coolingRate = hyperparameters[2].value;
        this.resetChance = hyperparameters[3].value;

        currentSolution = new SAParticle();
        bestSolution = new SAParticle(currentSolution);
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
        else if (Configuration.instance.randomGenerator.nextDouble() < resetChance)
            currentSolution = new SAParticle(bestSolution.rep);  // small chance to reset current particle to best

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
        while (temp > minTemp)
            step(gen++);

        System.out.println("Ran for " + gen + " generations");
        System.out.println("Best solution " + bestSolution);
        return bestSolution.getValue();
    }


}
