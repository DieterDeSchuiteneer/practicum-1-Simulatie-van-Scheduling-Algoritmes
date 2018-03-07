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
        int previousEndTime = 0;

        for (Process process : processList) {
            if (previousEndTime < process.getArrivalTime())
                process.setEndTime(process.getArrivalTime() + process.getServiceTime());
            else
                process.setEndTime(previousEndTime + process.getServiceTime());

            previousEndTime = process.getEndTime();
        }
        System.out.println("FCFS completed");
        return new Processes(processList);
    }


    public Processes executeSJF(List<Process> processList) {
        List<Process> completed = new LinkedList<>();
        List<Process> arrivedProcessesList = new LinkedList<>();


        arrivedProcessesList.add(processList.get(0));
        processList.remove(0);
        arrivedProcessesList.get(0).setEndTime(arrivedProcessesList.get(0).getArrivalTime() + arrivedProcessesList.get(0).getServiceTime());
        int time = 0;
        time = arrivedProcessesList.get(0).getArrivalTime();


        while (processList.size() != 0 || arrivedProcessesList.size() != 0) {
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
            time = time + shortestProcess.getServiceTime();
            shortestProcess.setEndTime(time);
            completed.add(shortestProcess);
        }
        System.out.println("SJF completed");
        return new Processes(completed);

    }

    // q = 2 or q = 8
    public Processes executeRR(List<Process> processList, int q) {

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
            for (int i = 0; i < q; i++) {
                clock = clock + 1;
                remainingTime = remainingTime - 1;
                temp.setRemainingTime(remainingTime);
                if (remainingTime == 0) {
                    i = i + q;
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
        System.out.println("RR completed");
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

        while ((currentProcess == null && arrivedProcessesList.size() == 0 && processList.size() == 0)) {
            if (counter == processList.get(0).getArrivalTime()) {
                //add to arrived Lists
                arrivedProcessesList.add(processList.get(0));
                processList.remove(0);
                evaluate(currentProcess, arrivedProcessesList);

            }

            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setEndTime(counter);
                completed.add(currentProcess);
                evaluate(null, arrivedProcessesList);
            }

            counter++;
        }
        System.out.println("SRTF completed");
        return new Processes(completed);
    }

    private Process evaluate(Process currenProcess, List<Process> arrivedProcesList) {
        if (currenProcess != null)
            arrivedProcesList.add(currenProcess);

        currenProcess = arrivedProcesList.stream().sorted(Comparator.comparing(Process::getRemainingTime)).findFirst().get();
        arrivedProcesList.remove(currenProcess);
        return currenProcess;
    }

    public Processes executeMLFM(List<Process> processList, int[] queueLength) {


        //used variables
        int clock;
        Process currentProcess;
        List<Process> completedProcessList = new LinkedList<>();
        int processAmount = processList.size();
        int q1 = queueLength[0];
        int q2 = queueLength[1];
        int q3 = queueLength[2];
        int q, remainingTime; //q holds size of time-slice of chosen queue, remainingtime holds " from a process
        int pidNextArriving = 1;

        //Process queues
        Queue<Process> pq1 = new LinkedList<>();
        Queue<Process> pq2 = new LinkedList<>();
        Queue<Process> pq3 = new LinkedList<>();

        //processes are sorted by arrivaltime
        //INITIALISATION
        //initialize clock at arrivaltime of first process
        clock = processList.get(0).getArrivalTime();
        processList.get(0).setRemainingTime(processList.get(pidNextArriving - 1).getServiceTime());
        pq1.add(processList.get(0));
        pidNextArriving++;

        while (!pq1.isEmpty() || !pq2.isEmpty() || !pq3.isEmpty()) {

            //SELECT next process to execute, pick from highest priority queue
            if (!pq1.isEmpty()) {
                currentProcess = pq1.poll();
                q = q1;
            } else if (!pq2.isEmpty()) {
                currentProcess = pq2.poll();
                q = q2;
            } else {
                currentProcess = pq3.poll();
                q = q3;
            }

            remainingTime = currentProcess.getRemainingTime();

            //chosen process can execute until: time-slice is consumed or terminated
            int stopTime;
            if (remainingTime < q) stopTime = clock + remainingTime;    //TERMINATED before time-slice ended
            else stopTime = clock + q;                                                    //TIME-SLICE is consumed
            //EXECUTE process, check arrival of processes during execution (unless nextArriving doesn't exist)
            Process temp = null;
            while (clock <= stopTime && pidNextArriving <= processAmount) {

                do { //several processes can arrive at same moment, only continue clock if next process won't arrive now
                    temp = processList.get(pidNextArriving - 1); //pid 1 is second element in list (index starts from 0)
                    if (temp.getArrivalTime() == clock) {
                        //not executed yet: remaining time = service time
                        temp.setRemainingTime(temp.getServiceTime());
                        pq1.add(temp);
                        pidNextArriving++;
                    }
                }
                while (pidNextArriving <= processAmount && processList.get(pidNextArriving - 1).getArrivalTime() == clock);
                clock++;
            }

            clock = stopTime; //last process wont execute loop (to check arriving processes), so clock must be set correctly

            //STOPPED: if terminated: calculate other values, else put process in appropriate queue
            if (remainingTime <= q) {    //TERMINATED, does not come back in a queue
                remainingTime = 0;
                currentProcess.setRemainingTime(remainingTime);
                currentProcess.setEndTime(clock);
                completedProcessList.add(currentProcess);


                //ATTENTION: iddle time of processor and no process arrived during execution => queues empty!
                //FAST FORWARD CLOCK if all queues are empty
                //process 2 has index 1 in list, next process (2) has current pid as index (1)
                if (pidNextArriving < processAmount && pq1.isEmpty() && pq2.isEmpty() && pq3.isEmpty()) { //there are still processes expected
                    temp = processList.get(pidNextArriving - 1);

                    if (clock < temp.getArrivalTime()) {    // arrival of next process is in the future
                        temp.setRemainingTime(temp.getServiceTime());
                        pq1.add(temp);
                        clock = temp.getArrivalTime(); //fast forward clock during idle time
                        pidNextArriving++;                //update next one expected
                    }
                }
            } else { //PAUSED, executed "q" from remaining time
                remainingTime = remainingTime - q;
                currentProcess.setRemainingTime(remainingTime);    //put process in appropriate queue
                if (q == q1)
                    pq2.add(currentProcess);
                else if (q == q2)
                    pq3.add(currentProcess);
                else
                    pq3.add(currentProcess);

            }
            //end cycle, select next process
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
            clock = clock + temp_Highest_NTAT.getServiceTime();


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
