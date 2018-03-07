package graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;

public class FreeChartGraph extends JFrame {

    public FreeChartGraph(XYDataset dataset, JPanel graph1JPanelArg)
    {
        MakeGraph(dataset,graph1JPanelArg);
    }


    private void MakeGraph(XYDataset dataset, JPanel graph1JPanelArg)
    {

        JPanel p = new JPanel();
        JFreeChart fcfsWaittime= ChartFactory.createXYLineChart("Test","FCFS", "Wait time",dataset, PlotOrientation.VERTICAL,false,false,false);
        ChartPanel chPanel = new ChartPanel(fcfsWaittime);
        graph1JPanelArg.setBackground(Color.red);

        graph1JPanelArg.setLayout(new BorderLayout());
        graph1JPanelArg.add(chPanel, BorderLayout.NORTH);

        JFrame frame = new JFrame();
        frame.add(graph1JPanelArg);
        frame.pack();
        frame.setVisible(true);
        frame.setFocusable(true);
    }

}
