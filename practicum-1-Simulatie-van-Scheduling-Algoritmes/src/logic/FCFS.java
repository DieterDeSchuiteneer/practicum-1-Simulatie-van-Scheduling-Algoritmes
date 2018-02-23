package logic;

import dataentities.Process;
import dataentities.Processes;

import java.util.*;

public class FCFS {
    public Queue<Process> processesTodo;


    public void executeFCFS(List<Process> processList){
        int previousEndTime = 0;

        for (Process process: processList) {
            if(previousEndTime < process.getArrivalTime())
                process.setEndTime(process.getArrivalTime() + process.getServiceTime());
            else
                process.setEndTime(previousEndTime + process.getServiceTime());
            previousEndTime = process.getEndTime();
        }

    }

}
