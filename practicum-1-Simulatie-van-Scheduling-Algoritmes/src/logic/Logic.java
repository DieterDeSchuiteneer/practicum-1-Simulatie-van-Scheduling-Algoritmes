package logic;


import dataentities.Process;
import dataentities.Processes;
import io.XMLReader;
import org.jfree.data.xy.XYSeries;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

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

        //FCFS algorithm
        Algorithms algoritmhs = new Algorithms();
        algoritmhs.executeFCFS(processes.getSortedByArrivalProcessList());
        calculateAverages(processes);

        //DEBUGPRINT
        processes.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        System.out.println(processes.toString());
    }


    public void calculateAverages(Processes processes) {
        processes.averageWaitTime = processes.getSortedByArrivalProcessList().stream().map(Process::getWaitTime).mapToInt(i -> i).sum() / processes.getSortedByArrivalProcessList().size();
        processes.averageTat = processes.getSortedByArrivalProcessList().stream().map(Process::getTat).mapToDouble(i -> i).sum() / processes.getSortedByArrivalProcessList().size();
        processes.averageNtat = processes.getSortedByArrivalProcessList().stream().map(Process::getNtat).mapToDouble(i -> i).sum() / processes.getSortedByArrivalProcessList().size();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creats a XYSerie set for usage in the graphs
     *
     * @param processes the processes on witch is used
     * @param algorithm the algorithm which is used
     * @param waitTime  if true it returns waitTime els it returns serviceTime
     * @return XYSerie used in the graph
     */
    private XYSeries waitTimePercentile(Processes processes, String algorithm, boolean waitTime) {
        //add average waitTime per percentile to a XYdataset
        List processList = processes.getSortedByServiceTimeProcessList();
        List<List<Process>> partitionedList = percentilePartitionList(processList);
        XYSeries serie = new XYSeries("WaitTime " + algorithm);

        for (List<Process> percentile : partitionedList) {
            int averageWaitTime = percentile.stream().map(Process::getWaitTime).mapToInt(i -> i).sum() / percentile.size();
            int averageTime = 0;

            if (waitTime)
                averageTime = percentile.stream().map(Process::getServiceTime).mapToInt(i -> i).sum() / percentile.size();
            else
                averageTime = percentile.stream().map(Process::getServiceTime).mapToInt(i -> i).sum() / percentile.size();

            serie.add(averageWaitTime, averageTime);
        }

        return serie;
    }

    /**
     * Devides a list in sublist used for percentile calculation
     *
     * @param processList The list which needs to be divided
     * @return the divided list
     */
    private List<List<Process>> percentilePartitionList(List<Process> processList) {
        int partitionSize = processList.size() / 100;
        List<List<Process>> partitions = new LinkedList<List<Process>>();
        for (int i = 0; i < processList.size(); i += partitionSize) {
            partitions.add(processList.subList(i,
                    Math.min(i + partitionSize, processList.size())));
        }

        return partitions;

    }


    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
