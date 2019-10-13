package algorithm.base;

import algorithm.pso.base.RecommenderPSOParticle;
import main.Application;
import main.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Recommender
{

    public void writeConfig(String path, String rootName, List<String> optionNames, List<String> optionValues)
    {
        try
        {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // root element
            Element root = document.createElement(rootName);
            document.appendChild(root);


            for (int i = 0; i < optionNames.size(); i++)
            {
                Element option = document.createElement(optionNames.get(i));
                option.appendChild(document.createTextNode(optionValues.get(i)));
                root.appendChild(option);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(path));

            transformer.transform(domSource, streamResult);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void writeConfig(String path, String rootName, RecommenderPSOParticle gbestParticle)
    {
        List<String> paramNames = Arrays.stream(gbestParticle.hyperparameters).map(hp -> hp.name).collect(Collectors.toList());
        List<String> paramValues = Arrays.stream(gbestParticle.hyperparameters).map(hp -> "" + hp.value).collect(Collectors.toList());
        System.out.println(paramNames);
        System.out.println(paramValues);
        writeConfig(path, rootName, paramNames, paramValues);
    }
}
