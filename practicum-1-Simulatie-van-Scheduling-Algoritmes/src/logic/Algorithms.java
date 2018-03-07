package logic;

import dataentities.Process;
import dataentities.Processes;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Algorithms {


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

//    // q = 2 or q = 8
//    public Processes executeRR(List<Process> processList, int q) {
//
//        int clock = 0;
//        Process temp;
//        List<Process> completedProcessList = new LinkedList<>();
//        int numberOfProcesses = processList.size();
//        int remainingTime;
//        clock = processList.get(0).getArrivalTime();
//        int pid = 1;
//        Queue<Process> RR_Queue = new LinkedList<Process>();
//        RR_Queue.add(processList.get(0));
//        processList.get(0).setRemainingTime(processList.get(0).getServiceTime());
//
//        while (!RR_Queue.isEmpty()) {
//            temp = RR_Queue.remove();
//            remainingTime = temp.getRemainingTime();
//            for (int i = 0; i < q; i++) {
//                clock = clock + 1;
//                remainingTime = remainingTime - 1;
//                temp.setRemainingTime(remainingTime);
//                if (remainingTime == 0) {
//                    i = i + q;
//                    temp.setEndTime(clock);
//                    completedProcessList.add(temp);
//                }
//            }
//            if (pid < numberOfProcesses) {
//                if (processList.get(pid).getArrivalTime() <= clock) {
//                    RR_Queue.add(processList.get(pid));
//                    processList.get(pid).setRemainingTime(processList.get(pid).getServiceTime());
//                    pid = pid + 1;
//                }
//            }
//            if (remainingTime > 0) {
//                RR_Queue.add(temp);
//            }
//            if (RR_Queue.isEmpty() && pid < numberOfProcesses) {
//                RR_Queue.add(processList.get(pid));
//                processList.get(pid).setRemainingTime(processList.get(pid).getServiceTime());
//                clock = processList.get(pid).getArrivalTime();
//                pid = pid + 1;
//            }
//        }
//        System.out.println("RR completed");
//        return new Processes(completedProcessList);
//    }

    public Processes executeRR(List<Process> processList, int timeSlice) {
        Queue<Process> RRProcessQueue = new LinkedList<>();
        List<Process> completedProcessList = new LinkedList<>();
        Process currentProcess = null;

        RRProcessQueue.add(processList.get(0));
        int clock = processList.get(0).getArrivalTime();
        processList.remove(0);

        while (!processList.isEmpty() || !RRProcessQueue.isEmpty()) {
            if (!RRProcessQueue.isEmpty()) currentProcess = RRProcessQueue.remove();

            else {
                if (processList.isEmpty()) break;
                else {
                    RRProcessQueue.add(processList.get(0));
                    clock = processList.get(0).getArrivalTime();
                    processList.remove(0);
                }
            }

            currentProcess.setStartTime(clock);

            if (currentProcess.getRemainingTime() <= timeSlice) {
                clock += currentProcess.getRemainingTime();
                currentProcess.setEndTime(clock);
                completedProcessList.add(currentProcess);
                int finalClock = clock;
                if (!processList.isEmpty()) {
                    List temp = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
                    RRProcessQueue.addAll(temp);
                    processList.removeAll(temp);
                }

            } else {
                clock += timeSlice;
                int finalClock = clock;
                if (!processList.isEmpty()) {
                    List temp = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
                    RRProcessQueue.addAll(temp);
                    processList.removeAll(temp);
                }
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);
                RRProcessQueue.add(currentProcess);
            }
        }

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
        Process currentProcess = null;
        int counter = 0;

        while (!arrivedProcessesList.isEmpty() || !processList.isEmpty()) {
            if (counter == processList.get(0).getArrivalTime()) {
                //add to arrived Lists
                arrivedProcessesList.add(processList.get(0));
                processList.remove(0);
                currentProcess = evaluate(currentProcess, arrivedProcessesList);
            }

            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setEndTime(counter);
                completed.add(currentProcess);
                currentProcess = evaluate(null, arrivedProcessesList);
            }


            counter++;
        }
        System.out.println("SRTF completed" + completed.size());
        return new Processes(completed);
    }

    private Process evaluate(Process currenProcess, List<Process> arrivedProcesList) {
        if (currenProcess != null) {
            arrivedProcesList.add(currenProcess);
        }
        currenProcess = arrivedProcesList.stream().sorted(Comparator.comparing(Process::getRemainingTime)).findFirst().get();
        arrivedProcesList.remove(currenProcess);
        return currenProcess;
    }

//    public Processes newSRTF(List<Process> processList){
//        List<Process> compledProcessList = new LinkedList<>();
//        List<Process> arrivedProcessList = new LinkedList<>();
//        Process currentProcess = null;
//
//        currentProcess = processList.get(0);
//        processList.remove(currentProcess);
//        currentProcess.setStartTime(currentProcess.getArrivalTime());
//        int clock = currentProcess.getArrivalTime();
//        int nextArrival = processList.get(0).getArrivalTime();
//
//        while(!processList.isEmpty() || !arrivedProcessList.isEmpty()) {
//            if(nextArrival)
//        }
//
//
//        return new Processes(compledProcessList);
//    }

    public Processes executeMLFB(List<Process> processList, int[] queueLength, int timeSlice) {
        //used variables
        int clock;
        Process currentProcess = null;
        List<Process> completedProcessList = new LinkedList<>();
        int q1 = queueLength[0];
        int q2 = queueLength[1];
        int q3 = queueLength[2];

        //Process queues
        Queue<Process> pq1 = new LinkedList<>();
        Queue<Process> pq2 = new LinkedList<>();
        Queue<Process> pq3 = new LinkedList<>();
        Queue<Process> nextQueue = null;
        //processes are sorted by arrivaltime
        //INITIALISATION
        //initialize clock at arrivaltime of first process
        clock = processList.get(0).getArrivalTime();
        pq1.add(processList.get(0));
        while (!pq1.isEmpty() || !pq2.isEmpty() || !pq3.isEmpty() || !processList.isEmpty() || currentProcess != null) {


            //Select the next process to execute
            if (!pq1.isEmpty() && currentProcess == null) {
                currentProcess = pq1.remove();
                nextQueue = pq2;
            } else if (!pq2.isEmpty() && currentProcess == null) {
                currentProcess = pq2.remove();
                nextQueue = pq2;
            } else if (!pq2.isEmpty() && currentProcess == null) {
                currentProcess = pq3.remove();
                nextQueue = pq3;
            } else if (currentProcess == null) {
                if (!processList.isEmpty()) {
                    currentProcess = processList.get(0);
                    clock = currentProcess.getArrivalTime();
                }
            }
            currentProcess.setStartTime(clock); //Selected process gets startTime


            //chosen process can execute until: time-slice is consumed or terminated
            if (currentProcess.getRemainingTime() <= timeSlice) { //process finishes
                int finalClock = clock;
                currentProcess.setRemainingTime(-1);
                currentProcess.setEndTime(clock);

                clock += currentProcess.getRemainingTime();
                List nextArrivedProcesses = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
                processList.removeAll(nextArrivedProcesses);
                pq1.addAll(nextArrivedProcesses);
                completedProcessList.add(currentProcess);
                currentProcess = null;

            } else {
                clock += timeSlice;
                int finalClock = clock;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - timeSlice);
                List nextArrivedProcesses = processList.stream().filter(p -> p.getArrivalTime() <= finalClock).collect(Collectors.toList());
                processList.removeAll(nextArrivedProcesses);
                pq1.addAll(nextArrivedProcesses);
            }
        }

        System.out.println("MLFM Completed");
        return new Processes(completedProcessList);
    }


    public Processes executeHRRN(List<Process> processList) {
        int clock = 0;
        Process temp;
        List<Process> completedProcessList = new LinkedList<>();

        int numberOfProcesses = processList.size();
        int pid = 1;
        clock = processList.get(0).getArrivalTime();
        Queue<Process> HRRN_Queue = new LinkedList<Process>();
        HRRN_Queue.add(processList.get(0));
        double highest_NTAT;
        Process temp_Highest_NTAT;
        for (int i = 0; i < numberOfProcesses - 1; i++) {
            highest_NTAT = Double.MIN_VALUE;
            temp_Highest_NTAT = null;
            int n = HRRN_Queue.size();
            for (int j = 0; j < n; j++) {
                temp = HRRN_Queue.remove();
                temp.setEndTime(clock + temp.getServiceTime());
                completedProcessList.add(temp);

                if (highest_NTAT < temp.getNtat()) {
                    highest_NTAT = temp.getNtat();
                    if (temp_Highest_NTAT != null) {
                        HRRN_Queue.add(temp_Highest_NTAT);
                        temp_Highest_NTAT = temp;
                    } else {
                        temp_Highest_NTAT = temp;
                    }
                } else {
                    HRRN_Queue.add(temp);
                }
            }
            if (temp_Highest_NTAT != null) clock += temp_Highest_NTAT.getServiceTime();


            while (processList.get(pid).getArrivalTime() <= clock && pid < numberOfProcesses - 1) {
                HRRN_Queue.add(processList.get(pid));
                pid++;
            }
            if (HRRN_Queue.isEmpty() && pid < numberOfProcesses - 1) {
                temp = processList.get(pid);
                pid++;
                HRRN_Queue.add(temp);
                clock = temp.getArrivalTime();
            }
        }
        System.out.println("HRRN completed");
        return new Processes(completedProcessList);
    }
}
