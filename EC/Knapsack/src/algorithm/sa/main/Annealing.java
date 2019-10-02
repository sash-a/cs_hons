package algorithm.sa.main;

import algorithm.sa.base.Particle;
import main.Configuration;

public class Annealing
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

    public boolean accept(int currentFitness, int newFitness)
    {
        if (newFitness < currentFitness)
            return true;
        else
        {
            int delta = currentFitness - newFitness;
            return Math.exp(delta / this.temp) > Configuration.instance.randomGenerator.nextDouble();
        }
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

        System.out.println("Could not find valid solution mutation");
        return currentSolution;
    }

    public void step()
    {
        Particle nextSolution = randomNeighbour();

        int currentFitness = currentSolution.getValue();
        int nextFitness = nextSolution.getValue();

        if (accept(currentFitness, nextFitness))
            currentSolution = nextSolution;

        if (nextFitness > bestSolution.getValue())
            bestSolution = nextSolution;

        temp *= coolingRate;
    }

    public void run()
    {
        int gen = 0;
        while (temp > Configuration.instance.minTemp && gen < Configuration.instance.generations)
        {
            gen++;
            System.out.println("\nGeneration: " + gen +
                    "\nTemperature: " + temp +
                    "\nCurrent: " + currentSolution.getValue() +
                    "\nBest: " + bestSolution.getValue());
            step();
        }
    }


}
