package main;

import algorithm.aco.base.AntColony;
import algorithm.base.Item;
import algorithm.base.Knapsack;
import algorithm.base.Representation;
import algorithm.ga.main.GARunner;
import algorithm.pso.base.Swarm;
import algorithm.pso.base.Vector;
import algorithm.sa.main.Annealing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Application
{
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
//        List<Boolean> b = new ArrayList<>();
//        b.add(true);
//        b.add(true);
//        b.add(true);
//
//        b.add(true);
//        b.add(false);
//        b.add(false);
//        b.add(true);
//
//
//        b.add(true);
//        b.add(true);
//        b.add(false);
//        b.add(true);
//
//
//        Vector v = new Vector(b);
//
//        System.out.println(b);
//        System.out.println(v.toBoolList());
//        System.out.println(Vector.intToBoolList(v.getX()));
//        System.out.println(Vector.intToBoolList(v.getY()));
//        System.out.println(Vector.intToBoolList(v.getZ()));

        readItems();
        double time = System.currentTimeMillis();
//        GARunner.run();
//        new Annealing().run();
//        new AntColony().run();
        new Swarm().run();
        System.out.println("took: " + ((System.currentTimeMillis() - time) / 1000) + "s");

    }

    public static void readItems()
    {
        Knapsack.allItems = new Item[Configuration.instance.numberOfItems];
        int i = 0;

        try
        {
            BufferedReader f = new BufferedReader(new FileReader(Configuration.instance.dataFilePath));
            f.readLine(); // Skipping first line
            String line = f.readLine();
            while (line != null && i < Configuration.instance.numberOfItems)
            {
                int[] attributes = Arrays.stream(line.split(";")).map(Integer::parseInt).mapToInt(x -> x).toArray();
                Knapsack.allItems[i++] = new Item(attributes[0], attributes[1], attributes[2]);
                line = f.readLine();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}