package main;

import algorithm.aco.recommender.ACORecommender;
import algorithm.base.Item;
import algorithm.base.Knapsack;
import algorithm.ga.base.Population;
import algorithm.ga.recommender.GARecommender;
import algorithm.pso.base.Swarm;
import algorithm.pso.recommender.PSORecommender;
import algorithm.sa.main.Annealing;
import algorithm.aco.base.AntColony;
import algorithm.sa.recommender.SARecommender;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class Application
{
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator;
        String suffix = "_default.xml";
        String algorithm = "";
        boolean paramSeach = false;
        int maxGens = 10000;

        readItems();

        // Parsing CLI
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-algorithm"))
            {
                i++;
                algorithm = args[i];
            }

            if (args[i].equals("-configuration"))
            {
                i++;
                if (args[i].equals("best"))
                    suffix = "_best.xml";
            }

            if (args[i].equals("-search_best_configuration"))
                paramSeach = true;
        }

        if (paramSeach) suffix = "_best.xml";
        double[] hyperparameters = loadConfig(path + algorithm + suffix);
        System.out.println("Chosen algorithm: " + algorithm);

        double time = System.currentTimeMillis();
        // Running correct algorithm according to CLI
        switch (algorithm)
        {
            case "ga":
                if (paramSeach)
                    new GARecommender().recommend();
                else
                    new Population(maxGens, hyperparameters).run();
                break;
            case "sa":
            case "best-algorithm":
                if (paramSeach)
                    new SARecommender().recommend();
                else
                    new Annealing(hyperparameters).run();
                break;
            case "aco":
                if (paramSeach)
                    new ACORecommender().recommend();
                else
                    new AntColony(maxGens, hyperparameters).run();
                break;
            case "pso":
                if (paramSeach)
                    new PSORecommender().recommend();
                else
                    new Swarm(maxGens * 10, hyperparameters).run(); // 10x max gens because PSO is does less per gen and is much faster
                break;
            default:
                System.out.println("Could not find algorithm with name " + algorithm + ". Options are: ga, sa, aco, pso");
                break;
        }
        System.out.println("Finished in: " + ((System.currentTimeMillis() - time) / 1000) + "s");
    }

    public static double[] loadConfig(String path)
    {
        try
        {
            System.out.println("Reading config from: " + path);
            System.out.println("Collected hyperparameters:");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(path));

            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            NodeList children = root.getChildNodes();

            double[] hyperparameters = new double[children.getLength()];
            for (int i = 0; i < children.getLength(); i++)
            {
                System.out.printf("%s:%.15f\n", children.item(i).getNodeName(), Double.parseDouble(children.item(i).getTextContent()));
                hyperparameters[i] = Double.parseDouble(children.item(i).getTextContent());
            }

            return hyperparameters;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        System.out.println();
        return new double[0];
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