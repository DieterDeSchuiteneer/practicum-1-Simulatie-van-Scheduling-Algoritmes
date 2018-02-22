package dataentities;

public class Process {
    private int pid;
    private int arrivalTime;
    private int serviceTime;
    private int startTime;
    private int endTime;
    private int waitTime;
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
        //Hier moet nog TAT NTAT en WAITTIME geset worden.
    }
}
