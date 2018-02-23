package logic;


import dataentities.Processes;
import io.XMLReader;

//General logic to coordinate the GUI and the rest of the functionality
public class Logic {

    private XMLReader io;
    private String file;
    private Processes processes;
    public Logic() {
        this.file = "";
        io = new XMLReader();
    }

    //runs the test based on the testId
    public void runTest(int testId) {

    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void execute(String type) {
        this.processes = io.readXMLFile(this.file);
        System.out.println("File loaded");
    }
}
