package algorithm.pso.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import main.Configuration;

public class Swarm extends Evaluatable
{
    public PSOParticle[] particles;
    public PSOParticle gbestParticle;
    private int generations;

    public Swarm()
    {
        this.generations = Configuration.instance.numPSOGens;
        particles = new PSOParticle[Configuration.instance.numParticles];
        for (int i = 0; i < Configuration.instance.numParticles; i++)
            particles[i] = new PSOParticle();

        gbestParticle = new PSOParticle();
    }

    public Swarm(int generations)
    {
        this();
        this.generations = generations;
    }

    /**
     * @param hyperparameters: num particles, inertia, cognitive factor, social factor
     */
    public Swarm(double[] hyperparameters)
    {
        assert hyperparameters.length == 4;

        int numParticles = (int) hyperparameters[0];
        double inertia = hyperparameters[1];
        double cog = hyperparameters[2];
        double social = hyperparameters[3];

        particles = new PSOParticle[numParticles];
        gbestParticle = new PSOParticle(inertia, cog, social);
        for (int i = 0; i < numParticles; i++)
            particles[i] = new PSOParticle(inertia, cog, social);
    }

    /**
     * Swarm that optimizes meta heuristics which inherit from {@link Evaluatable}
     *
     * @param generations     number of generations to optimize for
     * @param numParticles    number of particles the swarm has
     * @param evaluatable     the meta heuristic to optimize, must be of type {@link Evaluatable}
     * @param hyperparameters {@link Hyperparameter} of the meta heuristic
     */
    public Swarm(int generations, int numParticles, Evaluatable evaluatable, Hyperparameter... hyperparameters)
    {
        this.generations = generations;
        particles = new RecommenderPSOParticle[numParticles];

        // inertia, cognitive factor, social factor
        double in = 1.2;
        double lf = 1.1;
        double gf = 1.3;

        for (int i = 0; i < numParticles; i++)
            particles[i] = new RecommenderPSOParticle(in, lf, gf, hyperparameters, evaluatable);

        gbestParticle = new RecommenderPSOParticle(in, lf, gf, hyperparameters, evaluatable);
    }

    /**
     * @param hyperparameters: num particles, inertia, cognitive factor, social factor
     */
    @Override
    public void setHyperparams(Hyperparameter... hyperparameters)
    {
        assert hyperparameters.length == 4;

        int numParticles = (int) hyperparameters[0].value;
        double inertia = hyperparameters[1].value;
        double cog = hyperparameters[2].value;
        double social = hyperparameters[3].value;

        particles = new PSOParticle[numParticles];
        gbestParticle = new PSOParticle(inertia, cog, social);
        for (int i = 0; i < numParticles; i++)
            particles[i] = new PSOParticle(inertia, cog, social);
    }

    public void step(int gen)
    {
        if (particles[0] instanceof RecommenderPSOParticle)
        {
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Optimizer generation " + gen);
            System.out.println("Current best configuration from " + gen + " generations\n" +
                    gbestParticle +
                    "Achieved value of: " + gbestParticle.bestFitness);
            System.out.println("------------------------------------------------------------------------");
        }

        for (PSOParticle p : particles) p.calcFitness();

        for (PSOParticle p : particles)
        {
            if (p.bestFitness > gbestParticle.bestFitness)
            {
                System.out.println("Generation " + gen + ". New best " + gbestParticle.bestFitness + " -> " + p.bestFitness);

                if (p instanceof RecommenderPSOParticle)
                {
                    gbestParticle = new RecommenderPSOParticle((RecommenderPSOParticle) p);
                    System.out.println("Hyper params:\n" + gbestParticle);
                }
                else
                    gbestParticle = new PSOParticle(p);
            }
        }

        for (PSOParticle p : particles) p.move(gbestParticle.pos);
    }

    public int run()
    {
        for (int i = 0; i < generations; i++)
            step(i);

        if (gbestParticle instanceof RecommenderPSOParticle)
            System.out.println("\n\nBest value found through PSO optimization: " + gbestParticle.bestFitness);
        else
            System.out.println("\n\nFinal best -> value: " + gbestParticle.bestFitness + " weight: " + gbestParticle.getWeight());
        return gbestParticle.bestFitness;
    }
}
