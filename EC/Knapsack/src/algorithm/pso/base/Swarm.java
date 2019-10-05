package algorithm.pso.base;

import main.Configuration;

public class Swarm
{
    public Particle[] particles;
    public Particle gbestParticle;

    public Swarm()
    {
        particles = new Particle[Configuration.instance.numParticles];
        for (int i = 0; i < Configuration.instance.numParticles; i++)
            particles[i] = new Particle();

        gbestParticle = new Particle();
    }

    public void step(int gen)
    {
        for (Particle p : particles)
            p.calcFitness();

        for (Particle p : particles)
            if (p.bestFitness > gbestParticle.bestFitness)
            {
                System.out.println("New best: " + p.bestFitness + " at generation " + gen);
                gbestParticle = new Particle(p);
            }

        for (Particle p : particles)
            p.move(gbestParticle.pos);
    }

    public void run()
    {
        for (int i = 0; i < Configuration.instance.generations; i++)
            step(i);

        System.out.println("Final best: " + gbestParticle.bestFitness);
    }
}
