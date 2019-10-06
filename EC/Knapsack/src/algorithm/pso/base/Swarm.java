package algorithm.pso.base;

import algorithm.base.Hyperparameter;
import algorithm.base.Evaluatable;
import main.Configuration;

public class Swarm
{
    public Particle[] particles;
    public Particle gbestParticle;

    private boolean recommenderRun;

    public Swarm()
    {
        particles = new Particle[Configuration.instance.numParticles];
        for (int i = 0; i < Configuration.instance.numParticles; i++)
            particles[i] = new Particle();

        gbestParticle = new Particle();

        recommenderRun = false;
    }

    public Swarm(Evaluatable evaluatable, Hyperparameter... bns)
    {
        particles = new RecommenderParticle[Configuration.instance.numParticles];

        for (int i = 0; i < Configuration.instance.numParticles; i++)
            particles[i] = new RecommenderParticle(bns, evaluatable);

        gbestParticle = new RecommenderParticle(bns, evaluatable);

        recommenderRun = true;
    }

    public void step(int gen)
    {
        System.out.println("Optim generation " + gen);
        for (Particle p : particles) p.calcFitness();

        for (Particle p : particles)
        {
            if (p.bestFitness > gbestParticle.bestFitness)
            {
                System.out.println("New best: " + p.bestFitness + " at generation " + gen);

                if (p instanceof RecommenderParticle)
                {
                    gbestParticle = new RecommenderParticle((RecommenderParticle) p);
                    System.out.println("Hyper params:\n" + gbestParticle);
                }
                else
                    gbestParticle = new Particle(p);
            }
        }

        for (Particle p : particles) p.move(gbestParticle.pos);
    }

    public void run()
    {
        for (int i = 0; i < Configuration.instance.numGenerations; i++)
            step(i);

        System.out.println("Final best - value: " + gbestParticle.bestFitness + " weight: " + gbestParticle.getWeight());
    }
}
