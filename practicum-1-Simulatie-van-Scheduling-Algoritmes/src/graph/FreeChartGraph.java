package graph;

import java.awt.Color;
import java.awt.Paint;

import javax.swing.JFrame;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.plot.DefaultDrawingSupplier;

import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class FreeChartGraph extends JFrame {

    private String title;
    private XYDataset dataset;
    private javax.swing.JPanel graph1JPanel;


    public FreeChartGraph(String titleArg, XYDataset datasetArg, javax.swing.JPanel graph1JPanelArg) {
        title = titleArg;
        dataset = datasetArg;
        graph1JPanel = graph1JPanelArg;
    }

    public void Make(int boundry) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, "Gem. Servicetime", "Gem. " + title, dataset, PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();

        //override colors for series to better visible colors
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[]{Color.red,
                        Color.green,
                        Color.magenta,
                        Color.black,
                        Color.cyan,
                        Color.orange,
                        Color.yellow,
                        Color.black},
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
        ));

        NumberAxis xAxis = new LogarithmicAxis("Percentile");
        plot.setDomainAxis(xAxis);

        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setRange(0, boundry);

        ChartPanel chartPanel = new ChartPanel(chart);

        try {
            ChartUtilities.saveChartAsPNG(new File(title+".png"), chart, 1000, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }


        graph1JPanel.removeAll();
        Dimension panelSize = graph1JPanel.getSize();
        graph1JPanel.add(chartPanel,BorderLayout.CENTER);
        chartPanel.setSize(panelSize);

    }

}
