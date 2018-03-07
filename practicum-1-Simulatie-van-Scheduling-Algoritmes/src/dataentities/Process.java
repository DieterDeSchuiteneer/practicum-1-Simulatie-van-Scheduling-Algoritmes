package dataentities;

public class Process implements java.io.Serializable {
    private int pid;
    private int arrivalTime;
    private int serviceTime;
    private int startTime;
    private int remainingTime;
    private int endTime;
    private int waitTime;
    private double tat;
    private double ntat;

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }
    public void setStartTime(int startTime) { this.startTime = startTime; }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public Process(int pid, int arrivalTime, int serviceTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingTime = serviceTime;
    }

    public double getTat() {
        return tat;
    }

    public double getNtat() {
        return ntat;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        this.waitTime = this.startTime - this.arrivalTime;
        this.tat = this.serviceTime + this.waitTime;
        this.ntat = this.tat / this.serviceTime;
    }


    public int getWaitTime() {
        return waitTime;
    }

    public int getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "<PID>" + pid + ":\t Arrival: " + arrivalTime + "\t Start: " + startTime + "\t endTime: " + endTime + "\t serviceTime: " + serviceTime;
    }
}
