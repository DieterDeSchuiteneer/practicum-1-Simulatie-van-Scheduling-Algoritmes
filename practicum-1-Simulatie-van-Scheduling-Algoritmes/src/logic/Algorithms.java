package logic;

import dataentities.Process;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

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
        List<Process> completed = new LinkedList<>();
        List<Process> arrivedProcessesList = new LinkedList<>();


        arrivedProcessesList.add(processList.get(0));
        processList.remove(0);
        arrivedProcessesList.get(0).setEndTime(arrivedProcessesList.get(0).getArrivalTime() + arrivedProcessesList.get(0).getServiceTime());
        int time = 0;
        time = arrivedProcessesList.get(0).getArrivalTime();


        while (processList.size() != 0 && arrivedProcessesList.size() != 0) {

            //find the new processes²
            int finalTime = time;
            List<Process> newProcessList = processList.stream().filter(p -> p.getArrivalTime() <= finalTime).collect(Collectors.toList());
            if (newProcessList.size() == 0 && arrivedProcessesList.size() == 0) {
                newProcessList.add(processList.get(0));
                time = processList.get(0).getArrivalTime();
            }

            arrivedProcessesList.addAll(newProcessList);
            processList.removeAll(newProcessList);


            //find Shortest process
            Process shortestProcess = arrivedProcessesList.stream().sorted(Comparator.comparing(Process::getServiceTime)).findFirst().get();
            arrivedProcessesList.remove(shortestProcess);

            //process the shorted process
            time = time + time + shortestProcess.getServiceTime();
            shortestProcess.setEndTime(time);
            completed.add(shortestProcess);
        }

        processList = (List) Logic.deepClone(completed);
    }


    public static void executeRR(List<Process> processList, int queue) {

        int clock = 0;
        Process temp;
        List<Process> completedProcessList = new LinkedList<>();
        int numberOfProcesses = processList.size();
        int remainingTime;
        clock = processList.get(0).getArrivalTime();
        int pid = 1;
        Queue<Process> RR_Queue = new LinkedList<Process>();
        RR_Queue.add(processList.get(0));
        processList.get(0).setRemainingTime(processList.get(0).getServiceTime());

        while (!RR_Queue.isEmpty()) {
            temp = RR_Queue.remove();
            remainingTime = temp.getRemainingTime();
            for (int i = 0; i < queue; i++) {
                clock = clock + 1;
                remainingTime = remainingTime - 1;
                temp.setRemainingTime(remainingTime);
                if (remainingTime == 0) {
                    i = i + queue;
                    temp.setEndTime(clock);
                    completedProcessList.add(temp);
                }
            }
            if (pid < numberOfProcesses) {
                if (processList.get(pid).getArrivalTime() <= clock) {
                    RR_Queue.add(processList.get(pid));
                    processList.get(pid).setRemainingTime(processList.get(pid).getServiceTime());
                    pid = pid + 1;
                }
            }
            if (remainingTime > 0) {
                RR_Queue.add(temp);
            }
            if (RR_Queue.isEmpty() && pid < numberOfProcesses) {
                RR_Queue.add(processList.get(pid));
                processList.get(pid).setRemainingTime(processList.get(pid).getServiceTime());
                clock = processList.get(pid).getArrivalTime();
                pid = pid + 1;
            }
        }

        processList = (List<Process>) Logic.deepClone(completedProcessList);
    }
}
