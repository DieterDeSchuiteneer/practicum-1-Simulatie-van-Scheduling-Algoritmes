package logic;

import dataentities.Processes;
import io.XMLReader;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AlgorithmsTest {
    private Processes processes;

    @Before
    public void before() {
        XMLReader reader = new XMLReader();
        processes = reader.readXMLFile("testfile.xml");
    }

    @Test
    public void executeFCFS() throws Exception {
    }

    @Test
    public void executeSJF() throws Exception {
    }

    @Test
    public void newRR() throws Exception {
    }

    @Test
    public void executeSRTF() throws Exception {
    }

    @Test
    public void executeMLFB() throws Exception {
    }

    @Test
    public void executeHRRN() throws Exception {
        System.out.println("ran");
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Algorithms.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
