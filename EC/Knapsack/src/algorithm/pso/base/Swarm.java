package algorithm.pso.base;

import main.Configuration;

import java.util.Arrays;

public class Swarm
{
    public Particle[] particles;
    public Particle gbestParticle;

    public Swarm()
    {
        particles = new Particle[Configuration.instance.numParticles];
        for (int i = 0; i < Configuration.instance.numParticles; i++)
            particles[i] = new Particle();

        gbestParticle = particles[0];
    }

    public void step()
    {
        Particle best = Arrays.stream(particles).max(Particle::compareTo).get();
//        System.out.println("best " + best.bestValue + " " + best.getValue());
        if (best.getValue() > gbestParticle.bestValue)
        {
            gbestParticle = new Particle(best);
            System.out.println("New best value: " + gbestParticle.bestValue);
        }

        for (Particle p : particles)
        {
            p.move(gbestParticle.pos);
//            System.out.println(p.pos);
        }

//        System.out.println("done step");
    }

    public void run()
    {
        for (int i = 0; i < Configuration.instance.generations; i++)
            step();

        System.out.println("Final best: " + gbestParticle.bestValue);
    }
}
