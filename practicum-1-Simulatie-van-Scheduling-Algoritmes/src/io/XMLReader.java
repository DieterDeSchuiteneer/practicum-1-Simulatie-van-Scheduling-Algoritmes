package io;

import dataentities.Process;
import dataentities.Processes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLReader {
    public Processes readXMLFile(String path) {
        Processes processes = new Processes();
        System.out.println(path);
        try {

            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("process");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    int pid = Integer.parseInt(getTagValue("pid", eElement));
                    int arrivaltime = Integer.parseInt(getTagValue("arrivaltime", eElement));
                    int servicetime = Integer.parseInt(getTagValue("servicetime", eElement));
                    Process tempProcess = new Process(pid, arrivaltime, servicetime);
                    processes.addProcess(tempProcess);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return processes;
    }


    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
