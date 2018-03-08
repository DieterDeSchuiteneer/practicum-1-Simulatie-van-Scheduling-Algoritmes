package logic;


import dataentities.Process;
import dataentities.Processes;
import graph.FreeChartGraph;
import io.XMLReader;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

//General logic to coordinate the GUI and the rest of the functionality
public class Logic {

    private XMLReader io;
    private String file;
    private JPanel graph1JPanel;
    private JPanel graph2JPanel;

    private FreeChartGraph freeChartGraph;
    private Algorithms algorithms;

    public Logic(JPanel graph1JPanelArg,JPanel graph2JPanelArg) {
        algorithms = new Algorithms();
        graph1JPanel = graph1JPanelArg;
        graph2JPanel = graph2JPanelArg;
        this.file = "";
        io = new XMLReader();
    }


    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void execute(String type) {
        Processes processes = io.readXMLFile(this.file);
        XYSeriesCollection dataset;
        XYSeriesCollection dataset2;


        //Global
        //Waittime
        Algorithms algorithms = new Algorithms();
        int[] queueLengthMlfb = new int[]{1, 2, 4, 8};
        int[] queueLengthMlfb2 = new int[]{1, 2, 3, 4};
        //WaitTime
        XYSeries[] FCFS = getXYSeriesWaitTimeFCFS( (Processes) deepClone(processes)); //WERKT
        XYSeries[] SJF = getXYSeriesWaitTimeSJF((Processes) deepClone(processes)); //WERKT
        XYSeries[] RR2 = getXYSeriesWaitTimeRRTs2((Processes) deepClone(processes));//WERKT
        XYSeries[] RR8 = getXYSeriesWaitTimeRRTs8((Processes) deepClone(processes));//WERKT
        XYSeries[] SRTF = getXYSeriesWaitTimeSRTF((Processes) deepClone(processes)); //NOPE
        XYSeries[] MLFM = getXYSeriesWaitTimeMLFM((Processes) deepClone(processes),queueLengthMlfb);//WERKT
        XYSeries[] HRRN = getXYSeriesWaitTimeHRRN((Processes) deepClone(processes)); //WERKT

        //Waittime
        dataset = new XYSeriesCollection();
        dataset.addSeries(FCFS[0]); //WERKT
        dataset.addSeries(SJF[0]); //WERKT
        dataset.addSeries(RR2[0]); //WERKT
        dataset.addSeries(RR8[0]); //WERKT
        dataset.addSeries(SRTF[0]); //WERKT?
        dataset.addSeries(MLFM[0]); //WERKT
        dataset.addSeries(HRRN[0]); //WERKT

        //ServiceTime
        dataset2 = new XYSeriesCollection();
        dataset2.addSeries(FCFS[1]); //WERKT
        dataset2.addSeries(SJF[1]); //WERKT
        dataset2.addSeries(RR2[1]);//WERKT
        dataset2.addSeries(RR8[1]);//WERKT
        dataset2.addSeries(SRTF[1]); //WERKT?
        dataset2.addSeries(MLFM[1]); //WERKT
        dataset2.addSeries(HRRN[1]); //WERKT

        //calculateAverages(processes);

        //Make line graph with XYdataset...
        FreeChartGraph globalWaittime= new FreeChartGraph( "Waittime",dataset,graph1JPanel);
        globalWaittime.Make();

        //Make line graph with XYdataset...
        FreeChartGraph globalServiceTime= new FreeChartGraph( "NTAT",dataset2,graph2JPanel);
        globalServiceTime.Make();

    }


    public void calculateAverages(Processes processes) {
        processes.averageWaitTime = processes.getSortedByArrivalProcessList().stream().map(Process::getWaitTime).mapToInt(i -> i).sum() / processes.getSortedByArrivalProcessList().size();
        processes.averageTat = processes.getSortedByArrivalProcessList().stream().map(Process::getTat).mapToDouble(i -> i).sum() / processes.getSortedByArrivalProcessList().size();
        processes.averageNtat = processes.getSortedByArrivalProcessList().stream().map(Process::getNtat).mapToDouble(i -> i).sum() / processes.getSortedByArrivalProcessList().size();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public XYSeries[] getXYSeriesWaitTimeFCFS(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeFCFS(processes.getSortedByWaitTimeProcessList());
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "FCFS", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "FCFS", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeSJF(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeSJF(processes.getSortedByWaitTimeProcessList());
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "SJF", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "SJF", "ServiceTime");
        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeRRTs2(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeRR(processes.getSortedByWaitTimeProcessList(), 2);
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "RR ts=2", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "RR ts=2", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeRRTs8(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeRR(processes.getSortedByWaitTimeProcessList(), 8);
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "RR ts=8", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "RR ts=8", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeSRTF(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeSRTF(processes.getSortedByWaitTimeProcessList());
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "SRTF", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "SRTF", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }


    public XYSeries[] getXYSeriesWaitTimeMLFM(Processes processes,int[] queueLength) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeMLFB(processes.getSortedByWaitTimeProcessList(), queueLength, 8);
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "MLFM", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "MLFM", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeHRRN(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeHRRN(processes.getSortedByWaitTimeProcessList());
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "HRRN", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "HRRN", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    /**
     * Creats a XYSerie set for usage in the graphs
     *
     * @param processes the processes on witch is used
     * @param algorithm the algorithm which is used
     * @param time  String waitTime for waitTime els it returns serviceTime
     * @return XYSerie used in the graph
     */
    private XYSeries waitTimePercentile(Processes processes, String algorithm, String time) {
        //add average waitTime per percentile to a XYdataset
        List processList = processes.getSortedByServiceTimeProcessList();
        List<List<Process>> partitionedList = percentilePartitionList(processList);
        XYSeries serie = new XYSeries(time + " " + algorithm);

        for (List<Process> percentile : partitionedList) {
            int averageServiceTime = percentile.stream().map(Process::getServiceTime).mapToInt(i -> i).sum() / percentile.size();
            int averageTime = 0;

            if (time.equals("WaitTime"))
                averageTime = percentile.stream().map(Process::getWaitTime).mapToInt(i -> i).sum() / percentile.size();
            else
                averageTime = (int) percentile.stream().map(Process::getNtat).mapToDouble(i -> i).sum() / percentile.size();
            serie.add(averageServiceTime, averageTime);
        }

        return serie;
    }

    /**
     * Devides a list in sublist used for percentile calculation
     *
     * @param processList The list which needs to be divided
     * @return the divided list
     */
    private List<List<Process>> percentilePartitionList(List<Process> processList) {
        int partitionSize = processList.size() / 100;
        List<List<Process>> partitions = new LinkedList<List<Process>>();
        for (int i = 0; i < processList.size(); i += partitionSize) {
            partitions.add(processList.subList(i,
                    Math.min(i + partitionSize, processList.size())));
        }

        return partitions;
    }


    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
