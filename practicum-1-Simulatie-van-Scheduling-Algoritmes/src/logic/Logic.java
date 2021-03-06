package logic;


import dataentities.Process;
import dataentities.Processes;
import graph.FreeChartGraph;
import io.XMLReader;
import javafx.concurrent.Task;
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
        int[] queueLengthMlfb = new int[]{10, 50, 100};
        //WaitTime
        XYSeries[] FCFS = getXYSeriesWaitTimeFCFS( (Processes) deepClone(processes)); //WERKT
        XYSeries[] SJF = getXYSeriesWaitTimeSJF((Processes) deepClone(processes)); //WERKT
        XYSeries[] RR2 = getXYSeriesWaitTimeRRTs2((Processes) deepClone(processes));//WERKT
        XYSeries[] RR8 = getXYSeriesWaitTimeRRTs8((Processes) deepClone(processes));//WERKT
        XYSeries[] SRTF = getXYSeriesWaitTimeSRTF((Processes) deepClone(processes)); //NOPE
        XYSeries[] MLFB = getXYSeriesWaitTimeMLFB((Processes) deepClone(processes),queueLengthMlfb);//WERKT?
        XYSeries[] HRRN = getXYSeriesWaitTimeHRRN((Processes) deepClone(processes)); //WERKT
/*
        Task FCFSTask = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeFCFS( (Processes) deepClone(processes));}
        };

        Task SJFTask = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeSJF( (Processes) deepClone(processes));}
        };

        Task RR2Task = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeRRTs2( (Processes) deepClone(processes));}
        };

        Task RR8Task = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeRRTs8( (Processes) deepClone(processes));}
        };

        Task SRTFTask = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeSRTF( (Processes) deepClone(processes));}
        };
        Task MLFBTask = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeMLFB((Processes) deepClone(processes),queueLengthMlfb);}
        };
        Task HRRNTask = new Task() {
            @Override
            protected Object call() throws Exception {return getXYSeriesWaitTimeHRRN( (Processes) deepClone(processes));}
        };

        FCFSTask.run();
        SJFTask.run();
        RR2Task.run();
        RR8Task.run();
        SRTFTask.run();
        MLFBTask.run();
        HRRNTask.run();

        //Waittime
        dataset = new XYSeriesCollection();
        dataset.addSeries(((XYSeries[])FCFSTask.getValue())[0]); //WERKT
        dataset.addSeries(((XYSeries[])SJFTask.getValue())[0]); //WERKT
        dataset.addSeries(((XYSeries[])RR2Task.getValue())[0]); //WERKT
        dataset.addSeries(((XYSeries[])RR8Task.getValue())[0]); //WERKT
        dataset.addSeries(((XYSeries[])SRTFTask.getValue())[0]); //WERKT
        dataset.addSeries(((XYSeries[])MLFBTask.getValue())[0]); //WERKT
        dataset.addSeries(((XYSeries[])HRRNTask.getValue())[0]); //WERKT

        //ServiceTime
        dataset2 = new XYSeriesCollection();
        dataset2.addSeries(((XYSeries[])FCFSTask.getValue())[1]); //WERKT
        dataset2.addSeries(((XYSeries[])SJFTask.getValue())[1]); //WERKT
        dataset2.addSeries(((XYSeries[])RR2Task.getValue())[1]);//WERKT
        dataset2.addSeries(((XYSeries[])RR8Task.getValue())[1]);//WERKT
        dataset2.addSeries(((XYSeries[])SRTFTask.getValue())[1]); //WERKT
        dataset2.addSeries(((XYSeries[])MLFBTask.getValue())[1]); //WERKT
        dataset2.addSeries(((XYSeries[])HRRNTask.getValue())[1]); //WERKT
*/
        dataset = new XYSeriesCollection();
        dataset.addSeries(FCFS[0]); //WERKT
        dataset.addSeries(SJF[0]); //WERKT
        dataset.addSeries(RR2[0]); //WERKT
        dataset.addSeries(RR8[0]); //WERKT
        dataset.addSeries(SRTF[0]); //WERKT?
        dataset.addSeries(MLFB[0]); //WERKT
        dataset.addSeries(HRRN[0]); //WERKT

        //ServiceTime
        dataset2 = new XYSeriesCollection();
        dataset2.addSeries(FCFS[1]); //WERKT
        dataset2.addSeries(SJF[1]); //WERKT
        dataset2.addSeries(RR2[1]);//WERKT
        dataset2.addSeries(RR8[1]);//WERKT
        dataset2.addSeries(SRTF[1]); //WERKT?
        dataset2.addSeries(MLFB[1]); //WERKT
        dataset2.addSeries(HRRN[1]); //WERKT

        //calculateAverages(processes);

        //Make line graph with XYdataset...
        FreeChartGraph globalWaittime= new FreeChartGraph( "Waittime",dataset,graph1JPanel);
        globalWaittime.Make(processes.getSortedByArrivalProcessList().size()/10);

        //Make line graph with XYdataset...
        FreeChartGraph globalServiceTime= new FreeChartGraph( "NTAT",dataset2,graph2JPanel);
        globalServiceTime.Make(processes.getSortedByArrivalProcessList().size()/10);

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
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeRRTs2(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.newRR(processes.getSortedByWaitTimeProcessList(), 2);
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "RR ts=2", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "RR ts=2", "ServiceTime");
//        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        return arrayXYSerie;
    }

    public XYSeries[] getXYSeriesWaitTimeRRTs8(Processes processes) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.newRR(processes.getSortedByWaitTimeProcessList(), 8);
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


    public XYSeries[] getXYSeriesWaitTimeMLFB(Processes processes, int[] queueLength) {
        XYSeries[] arrayXYSerie = new XYSeries[2];
        Processes executedProcesses = algorithms.executeMLFB(processes.getSortedByWaitTimeProcessList(), queueLength);
        arrayXYSerie[0] = waitTimePercentile(executedProcesses, "MLFM", "WaitTime");
        arrayXYSerie[1] = waitTimePercentile(executedProcesses, "MLFM", "ServiceTime");
        executedProcesses.getSortedByArrivalProcessList().forEach(p -> System.out.println(p.toString()));
        System.out.println(executedProcesses.getSortedByArrivalProcessList().size());

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
        int counter = 0;
        for (List<Process> percentile : partitionedList) {
            double averageServiceTime = percentile.stream().map(Process::getServiceTime).mapToInt(i -> i).sum() / percentile.size();
            double averageTime = 0;

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
