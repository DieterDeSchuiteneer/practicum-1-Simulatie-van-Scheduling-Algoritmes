package dataentities;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Processes {
    List<Process> processList;
    public double averageTat;
    public double averageNtat;
    public double averageWaitTime;

    public Processes() {
        this.processList = new LinkedList<>();
    }

    public List<Process> getSortedByArrivalProcessList() {
        Collections.sort(processList, (a, b) -> a.getArrivalTime() < b.getArrivalTime() ? -1 : a.getArrivalTime() == b.getArrivalTime() ? 0 : 1);
        return processList;
    }

    public List<Process> getSortedByWaitTimeProcessList() {
        Collections.sort(processList, (a, b) -> a.getWaitTime() < b.getWaitTime() ? -1 : a.getWaitTime() == b.getWaitTime() ? 0 : 1);
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
