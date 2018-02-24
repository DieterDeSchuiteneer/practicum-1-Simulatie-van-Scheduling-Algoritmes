package logic;

import dataentities.Process;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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
        int time =0;
        time = arrivedProcessesList.get(0).getArrivalTime();


        while(processList.size() != 0 && arrivedProcessesList.size() != 0) {

            //find the new processes
            int finalTime = time;
            List<Process> newProcessList = processList.stream().filter(p-> p.getArrivalTime() <= finalTime).collect(Collectors.toList());
            if(newProcessList.size() == 0 && arrivedProcessesList.size() ==0) {
                newProcessList.add(processList.get(0));
                time = processList.get(0).getArrivalTime();
            }

            arrivedProcessesList.addAll(newProcessList);
            processList.removeAll(newProcessList);


            //find Shortest process
            Process shortestProcess = arrivedProcessesList.stream().sorted(Comparator.comparing(Process::getServiceTime)).findFirst().get();
            arrivedProcessesList.remove(shortestProcess);

            //process the shorted process
            time = time + time+shortestProcess.getServiceTime();
            shortestProcess.setEndTime(time);
            completed.add( shortestProcess);
        }


        processList = (List)Logic.deepClone(completed);


    }
}
