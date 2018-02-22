package dataentities;

import java.util.LinkedList;
import java.util.List;

public class Processes {
    List<Process> processList;
    int  averageTat;
    int  averageNtat;
    int averageWaitTime;

    public Processes() {
        this.processList = new LinkedList<>();
    }

    public void addProcess(Process process) {
        if(processList != null) {
            processList.add(process);
        }
    }
}
