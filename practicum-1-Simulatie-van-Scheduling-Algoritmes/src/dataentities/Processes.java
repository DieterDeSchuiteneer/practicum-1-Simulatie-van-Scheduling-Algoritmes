package dataentities;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Processes implements java.io.Serializable{
    List<Process> processList;
    public double averageTat;
    public double averageNtat;
    public double averageWaitTime;

    public Processes() {
        this.processList = new LinkedList<>();
    }
    public Processes(List processList) {
        this.processList = processList;
    }

    public List<Process> getSortedByArrivalProcessList() {
        Collections.sort(processList, Comparator.comparing(Process::getArrivalTime));
        return processList;
    }

    public List<Process> getSortedByWaitTimeProcessList() {
        Collections.sort(processList, Comparator.comparing(Process::getWaitTime));
        return processList;
    }

    public List<Process> getSortedByServiceTimeProcessList() {
        Collections.sort(processList, Comparator.comparing(Process::getServiceTime));
        return processList;
    }


    public void addProcess(Process process) {
        if (processList != null) {
            processList.add(process);
        }
    }

    @Override
    public String toString() {
        return "Average wait time: " + averageWaitTime + "\t Average TAT: " + averageTat +"\t Average NTAT: " + averageNtat;
    }
}
