package logic;


import dataentities.Processes;
import io.XMLReader;

//General logic to coordinate the GUI and the rest of the functionality
public class Logic {

    private XMLReader io;

    private String file;

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
        Processes processes = io.readXMLFile(this.file);
        FCFS fcfs = new FCFS();
        fcfs.executeFCFS(processes.getSortedByArrivalProcessList());
        processes.getSortedByArrivalProcessList().forEach(p-> System.out.println(p.toString()));
        System.out.println("File loaded");
    }
}
