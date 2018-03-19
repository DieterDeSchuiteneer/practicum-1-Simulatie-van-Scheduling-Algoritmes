package logic;

import dataentities.Process;
import dataentities.Processes;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Algorithms {

    /**
     * FCFS algorithm
     * @param processList sorted by arrival time
     * @return
     */
    public Processes executeFCFS(List<Process> processList) {
        int clock = processList.get(0).getArrivalTime();
        List<Process>  compProcesses = new LinkedList<>();
        for (Process process : processList) {
            process.setStartTime(clock);
            if (clock < process.getArrivalTime()) {
                process.setStartTime(process.getArrivalTime());
                process.setEndTime(process.getArrivalTime() + process.getServiceTime());
            }else {
                process.setEndTime(clock + process.getServiceTime());
            }
            compProcesses.add(process);
            clock = process.getEndTime();
        }
        System.out.println("FCFS completed");
        return new Processes(compProcesses);
    }

    /**
     * SJF algorithm
     * @param processList sorted by arrival time
     * @return
     */
    public Processes executeSJF(List<Process> processList) {
        List<Process> completed = new LinkedList<>();
        List<Process> arrivedProcessesList = new LinkedList<>();


        arrivedProcessesList.add(processList.get(0));
        processList.remove(0);
        arrivedProcessesList.get(0).setEndTime(arrivedProcessesList.get(0).getArrivalTime() + arrivedProcessesList.get(0).getServiceTime());
        int clock = arrivedProcessesList.get(0).getArrivalTime();


        while (processList.size() != 0 || arrivedProcessesList.size() != 0) {
            //find the new processes²
            int finalTime = clock;
            List<Process> newProcessList = processList.stream().filter(p -> p.getArrivalTime() <= finalTime).collect(Collectors.toList());
            if (newProcessList.size() == 0 && arrivedProcessesList.size() == 0) {
                newProcessList.add(processList.get(0));
                clock = processList.get(0).getArrivalTime();
            }

            arrivedProcessesList.addAll(newProcessList);
            processList.removeAll(newProcessList);


            //find Shortest process
            Process shortestProcess = arrivedProcessesList.stream().sorted(Comparator.comparing(Process::getServiceTime)).findFirst().get();
            arrivedProcessesList.remove(shortestProcess);
            shortestProcess.setStartTime(clock);

            //process the shorted process
            clock = clock + shortestProcess.getServiceTime();
            shortestProcess.setEndTime(clock);
            completed.add(shortestProcess);
        }
        System.out.println("SJF completed");
        return new Processes(completed);
    }


    /**
     *
     * @param processList sorted by arrival time
     * @param timeSlice the used time slice for round robin
     * @return
     */
    public Processes executeRR(List<Process> processList, int timeSlice) {
        Queue<Process> RRProcessQueue = new LinkedList<>();
        List<Process> completedProcessList = new LinkedList<>();
        Process currentProcess = null;

        RRProcessQueue.add(processList.get(0));
        int clock = processList.get(0).getArrivalTime();
        processList.remove(0);

        while (!processList.isEmpty() || !RRProcessQueue.isEmpty()) {
            if(processList.isEmpty() && RRProcessQueue.isEmpty()) break;
            if(!RRProcessQueue.isEmpty()) currentProcess = RRProcessQueue.poll();
            if(!processList.isEmpty() && RRProcessQueue.isEmpty()){
                clock = processList.get(0).getArrivalTime();
                RRProcessQueue.add(processList.remove(0));
            }
            currentProcess.setStartTime(clock);

            if (currentProcess.getRemainingTime() <= timeSlice) {
                clock += currentProcess.getRemainingTime();
                currentProcess.setEndTime(clock);
                completedProcessList.add(currentProcess);
                if (!processList.isEmpty()) {
                    int finalClock = clock;
                    List<Process> temp = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
                    RRProcessQueue.addAll(temp);
                    processList.removeAll(temp);
                }
            } else {
                clock += timeSlice;
                int finalClock = clock;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);
                if (!processList.isEmpty()) {
                    List<Process> temp = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
                    RRProcessQueue.addAll(temp);
                    processList.removeAll(temp);
                }
                RRProcessQueue.add(currentProcess);
            }
        }
        System.out.println("RR Completed");
        return new Processes(completedProcessList);
    }

    /**
     * the process with the smallest amount of time remaining until completion is selected to execute
     *
     * @param processList
     * @return
     */
    public Processes executeSRTF(List<Process> processList) {
        List<Process> completed = new LinkedList<>();
        List<Process> arrivedProcessesList = new LinkedList<>();
        Process currentProcess = processList.remove(0);
        int counter = currentProcess.getArrivalTime();

        while (!arrivedProcessesList.isEmpty() || !processList.isEmpty()) {
            //System.out.println(counter);
            if(arrivedProcessesList.isEmpty() && processList.isEmpty() ) break;
            if(!processList.isEmpty()) {
                if (counter == processList.get(0).getArrivalTime()) {
                    //add to arrived Lists
                    arrivedProcessesList.add(processList.remove(0));
                    currentProcess = evaluate(currentProcess, arrivedProcessesList);
                    currentProcess.setStartTime(counter);
                }
            }
            if(arrivedProcessesList.isEmpty()&& !processList.isEmpty() && currentProcess == null){
                currentProcess = processList.remove(0);
                counter = currentProcess.getArrivalTime();
                currentProcess.setStartTime(counter);
            }

            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);

            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setEndTime(counter);
                completed.add(currentProcess);
                currentProcess = evaluate(null, arrivedProcessesList);
                if(currentProcess != null) currentProcess.setStartTime(counter);
            }


            counter++;
        }
        System.out.println("SRTF completed");
        return new Processes(completed);
    }

    private Process evaluate(Process currentProcess, List<Process> arrivedProcessList) {
        if (currentProcess != null) {
            arrivedProcessList.add(currentProcess);
        }
        currentProcess = arrivedProcessList.stream().min(Comparator.comparing(Process::getRemainingTime)).orElse(null);
        if(currentProcess != null) arrivedProcessList.remove(currentProcess);
        return currentProcess;
    }


    public Processes executeMLFB(List<Process> processList, int[] queueLength) {
        //used variables
        int clock;
        Process currentProcess = null;
        List<Process> completedProcessList = new LinkedList<>();
        int q1ts = queueLength[0];
        int q2ts = queueLength[1];
        int q3ts = queueLength[2];
        int timeSlice = q1ts;

        //Process queues
        Queue<Process> pq1 = new LinkedList<>();
        Queue<Process> pq2 = new LinkedList<>();
        Queue<Process> pq3 = new LinkedList<>();
        Queue<Process> nextQueue = null;
        //processes are sorted by arrivaltime
        //INITIALISATION
        //initialize clock at arrivaltime of first process
        clock = processList.get(0).getArrivalTime();
        pq1.add(processList.remove(0));

        while (!pq1.isEmpty() || !pq2.isEmpty() || !pq3.isEmpty() || !processList.isEmpty() || currentProcess != null) {
            //Select the next process to execute
            if (!pq1.isEmpty() && currentProcess == null) {
                currentProcess = pq1.remove();
                timeSlice = q1ts;
                nextQueue = pq2;
            } else if (!pq2.isEmpty() && currentProcess == null) {
                currentProcess = pq2.remove();
                timeSlice = q2ts;
                nextQueue = pq3;
            } else if (!pq3.isEmpty() && currentProcess == null) {
                currentProcess = pq3.remove();
                timeSlice = q3ts;
                nextQueue = pq3;
            } else if (currentProcess == null) {
                if (!processList.isEmpty()) {
                    currentProcess = processList.get(0);
                    clock = currentProcess.getArrivalTime();
                    nextQueue = pq2;
                    timeSlice = q1ts;
                } else break;
            }
            currentProcess.setStartTime(clock); //Selected process gets startTime

            //chosen process can execute until: time-slice is consumed or terminated
            if (currentProcess.getRemainingTime() <= timeSlice) { //process finishes
                clock += currentProcess.getRemainingTime();
                currentProcess.setRemainingTime(-1);
                currentProcess.setEndTime(clock);
            } else {
                clock += timeSlice;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);
                nextQueue.add(currentProcess);
            }

            int finalClock = clock;
            List nextArrivedProcesses = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
            processList.removeAll(nextArrivedProcesses);
            pq1.addAll(nextArrivedProcesses);
            completedProcessList.add(currentProcess);
            currentProcess = null;

        }

        System.out.println("MLFB Completed");
        return new Processes(completedProcessList);
    }


    public Processes executeHRRN(List<Process> processList) {
        List<Process> completedProcessList = new LinkedList<>();
        List<Process> arrivedProcessList = new LinkedList<>();

        Process currenProcess = null;
        int clock = processList.get(0).getArrivalTime();
        arrivedProcessList.add(processList.remove(0));

        while(currenProcess != null || !processList.isEmpty() || !arrivedProcessList.isEmpty()) {
            //select process with higest NTAT from list
            if(arrivedProcessList.isEmpty()){
                if(processList.size() == 0 && arrivedProcessList.isEmpty()) break;
                currenProcess = processList.remove(0);
                clock = currenProcess.getArrivalTime();
                currenProcess.setStartTime(clock);
            } else {
                currenProcess = arrivedProcessList.stream().max(Comparator.comparing(Process::getNtat)).get();
                currenProcess.setStartTime(clock);
            }
            arrivedProcessList.remove(currenProcess);

            //Process the current process.
            clock += currenProcess.getRemainingTime();
            int finalClock = clock;
            currenProcess.setEndTime(finalClock);
            completedProcessList.add(currenProcess);

            //find all the newly arrived processes.
            List<Process> newProcessList = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
            arrivedProcessList.addAll(newProcessList);
            processList.removeAll(arrivedProcessList);

            //Recalculate NTAT.
            arrivedProcessList.forEach(p-> p.forceStartTime(finalClock));
            arrivedProcessList.forEach(p-> p.setEndTime(finalClock + p.getServiceTime()));
        }

        System.out.println("HRRN completed");
        return new Processes(completedProcessList);
    }


    /////////////////////////////////Helpers///////////////////////////////////

}
