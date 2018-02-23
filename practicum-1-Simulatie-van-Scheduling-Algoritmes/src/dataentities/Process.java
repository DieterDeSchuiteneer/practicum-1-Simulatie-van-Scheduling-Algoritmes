package dataentities;

public class Process {
    private int pid;
    private int arrivalTime;
    private int serviceTime;
    private int startTime;
    private int endTime;
    private int waitTime;

    public int getPid() {
        return pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    private int tat;
    private int ntat;

    public Process(int pid, int arrivalTime, int serviceTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        //TODO: Set TAT NTAT and WAITTIME.
    }

    public int getWaitTime() {
        return waitTime;
    }

    @Override
    public String toString() {
        return pid+ ": "+ arrivalTime + "; " + serviceTime ;
    }
}
