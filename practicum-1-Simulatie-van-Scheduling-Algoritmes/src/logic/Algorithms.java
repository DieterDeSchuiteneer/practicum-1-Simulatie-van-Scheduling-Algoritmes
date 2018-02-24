package logic;

import dataentities.Process;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Algorithms {

    public void executeFCFS(List<Process> processList) {
        int previousEndTime = 0;

        for (Process process : processList) {
            if (previousEndTime < process.getArrivalTime())
                process.setEndTime(process.getArrivalTime() + process.getServiceTime());
            else
                process.setEndTime(previousEndTime + process.getServiceTime());

            previousEndTime = process.getEndTime();
        }
    }


    public void executeSJF(List<Process> processList) {
        int currentTime = 0;
        Queue processQueue = new LinkedList();

    }
}
