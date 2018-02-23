package dataentities;

import java.util.*;

public class Processes {
    List<Process> processList;
    int averageTat;
    int averageNtat;
    int averageWaitTime;

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
}
