package logic;


import dataentities.Process;
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
        //Static FCFS algorithm
        FCFS fcfs = new FCFS();
        fcfs.executeFCFS(processes.getSortedByArrivalProcessList());
        calculateAverages(processes);

        //DEBUGPRINT
        processes.getSortedByArrivalProcessList().forEach(p-> System.out.println(p.toString()));
        System.out.println(processes.toString());
    }


    public void calculateAverages(Processes preocesses) {
        preocesses.averageWaitTime = preocesses.getSortedByArrivalProcessList().stream().map(Process::getWaitTime).mapToInt(i -> i).sum()/preocesses.getSortedByArrivalProcessList().size();
        preocesses.averageTat = preocesses.getSortedByArrivalProcessList().stream().map(Process::getTat).mapToDouble(i -> i).sum()/preocesses.getSortedByArrivalProcessList().size();
        preocesses.averageNtat = preocesses.getSortedByArrivalProcessList().stream().map(Process::getNtat).mapToDouble(i -> i).sum()/preocesses.getSortedByArrivalProcessList().size();
    }
}
