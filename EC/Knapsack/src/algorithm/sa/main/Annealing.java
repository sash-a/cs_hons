package algorithm.sa.main;

import algorithm.base.Representation;
import main.Configuration;

public class Annealing
{
    public double coolingRate;
    public double temp;

    public Representation currentSolution;
    public Representation bestSolution;

    public Annealing()
    {
        Configuration.instance.mutationType = Configuration.MutationType.BITFLIP;

        this.temp = Configuration.instance.initialTemp;
        this.coolingRate = Configuration.instance.coolingRate;

        currentSolution = new Representation();
        bestSolution = new Representation(currentSolution.rep);

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

    public Representation randomNeighbour()
    {
        Representation nextSolution = currentSolution.mutate();
        for (int i = 0; i < Configuration.instance.validAttempts; i++)
        {
            if (nextSolution.isValid())
                return nextSolution;

            nextSolution = currentSolution.mutate();
        }

        System.out.println("Could not find valid solution mutation");
        return currentSolution;
    }

    public void step()
    {
        Representation nextSolution = randomNeighbour();

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
