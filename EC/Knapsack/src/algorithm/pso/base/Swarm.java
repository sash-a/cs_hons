package algorithm.pso.base;

import main.Configuration;

import java.util.Arrays;

public class Swarm
{
    public Particle[] particles;
    public Particle bestParticle;

    public Swarm()
    {
        particles = new Particle[Configuration.instance.numParticles];
        for (int i = 0; i < Configuration.instance.numParticles; i++)
        {
            particles[i] = new Particle();
            particles[i].update();
        }

        bestParticle = particles[0];
    }

    public void step()
    {
        Particle best = Arrays.stream(particles).max(Particle::compareTo).get();
        if (best.getValue() > bestParticle.bestValue)
        {
            bestParticle = new Particle(best);
            System.out.println("New best value: " + bestParticle.bestValue);
        }

        for (Particle p : particles)
            p.move(bestParticle.pos);

//        System.out.println("done step");
    }

    public void run()
    {
        for (int i = 0; i < Configuration.instance.generations; i++)
            step();

        System.out.println("Final best: " + bestParticle.bestValue);
    }
}
