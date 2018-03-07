package io;

import dataentities.Process;
import dataentities.Processes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;

public class XMLReader {
    public Processes readXMLFile(String path) {
        System.out.println("Reading...");
        Processes processes = new Processes();
        System.out.println(path);
        try {

            File fXmlFile = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            //Speeding up the xml parser (No DTD in XML files)
            builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            Document doc = builder.parse(fXmlFile);

            //optional, but recommended
            //doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("process");


            System.out.println("Entering painfully long for-loop..");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Element eElement = (Element) nList.item(temp);

                int pid = Integer.parseInt(eElement.getElementsByTagName("pid").item(0).getChildNodes().item(0).getNodeValue());
                int arrivalTime = Integer.parseInt(eElement.getElementsByTagName("arrivaltime").item(0).getChildNodes().item(0).getNodeValue());
                int serviceTime = Integer.parseInt(eElement.getElementsByTagName("servicetime").item(0).getChildNodes().item(0).getNodeValue());

                processes.addProcess(new Process(pid, arrivalTime, serviceTime));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done!");
        return processes;
    }
}
