package main;

import algorithm.aco.recommender.ACORecommender;
import algorithm.base.Item;
import algorithm.base.Knapsack;
import algorithm.base.Representation;
import algorithm.ga.base.Population;
import algorithm.ga.main.GARunner;
import algorithm.ga.recommender.GARecommender;
import algorithm.pso.base.Swarm;
import algorithm.pso.recommender.PSORecommender;
import algorithm.sa.main.Annealing;
import algorithm.aco.base.AntColony;
import algorithm.sa.recommender.SARecommender;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Application
{
    // --- command line ---
    // -algorithm [ga | sa | aco | pso | best-algorithm] -configuration [default | best] -search_best_configuration
    public static void main(String... args)
    {
        loadConfig(false);
        String path = Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "ga_default.xml";
        readItems();

        double time = System.currentTimeMillis();
        new Population().run();
//        new Annealing().run();
//        new AntColony().run();
//        new Swarm().run();

//        new GARecommender().recommend();
        new SARecommender().recommend();
//        new ACORecommender().recommend();
//        new PSORecommender().recommend();
        System.out.println("Finished in: " + ((System.currentTimeMillis() - time) / 1000) + "s");
    }

    public static void loadConfig(boolean best)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            File file = new File(Configuration.instance.dataDirectory + Configuration.instance.fileSeparator + "ga_default.xml");
            Document doc = builder.parse(file);
//            Element e = doc.getElementById("ga");
            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();
            int pop_size = Integer.parseInt(doc.getElementsByTagName("pop_size").item(0).getTextContent());
            System.out.println("ps " + pop_size);
            // Do something with the document here.
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
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