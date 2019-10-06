package algorithm.sa.main;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import algorithm.sa.base.Particle;
import main.Configuration;

public class Annealing extends Evaluatable
{
    public double coolingRate;
    public double temp;

    public Particle currentSolution;
    public Particle bestSolution;

    public Annealing()
    {
        Configuration.instance.mutationType = Configuration.MutationType.BITFLIP;

        this.temp = Configuration.instance.initialTemp;
        this.coolingRate = Configuration.instance.coolingRate;

        currentSolution = new Particle();
        bestSolution = new Particle(currentSolution.rep);
    }

    /**
     * @param bns: temp, cooling rate
     */
    @Override
    public void setHyperparams(Hyperparameter... bns)
    {
        assert bns.length == 2;

        this.temp = bns[0].value;
        this.coolingRate = bns[1].value;
    }

    public boolean accept(double currentEnergy, double newEnergy)
    {
        double energyDelta = newEnergy - currentEnergy;

        if (energyDelta <= 0)
            return true;
        else
            return Math.exp(-energyDelta / this.temp) > Configuration.instance.randomGenerator.nextDouble();
    }

    public Particle randomNeighbour()
    {
        Particle nextSolution = currentSolution.move();
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
        Particle nextSolution = randomNeighbour();

        int currentFitness = currentSolution.getValue();
        int nextFitness = nextSolution.getValue();

        // TODO what is the best way to minimize these?
        if (accept(1 / (double) currentFitness, 1 / (double) nextFitness))
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
        {
//            System.out.println("Annealing running for: " + gen++);
            step(gen);
        }

        return bestSolution.getValue();
    }


}
