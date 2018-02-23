package logic;

import dataentities.Process;
import dataentities.Processes;

import java.util.*;

public class FCFS {
    public Queue<Process> processesTodo;

    public void SetProcesses(Processes processes){

        this.processesTodo.addAll( processes.getSortedByArrivalProcessList());
    }

    public void executeFCFS(List processes){

    }

}
