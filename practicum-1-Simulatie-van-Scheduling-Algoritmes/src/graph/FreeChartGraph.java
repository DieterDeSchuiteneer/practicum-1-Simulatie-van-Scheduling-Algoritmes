package graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;

import javax.swing.JFrame;

import org.jfree.chart.plot.DefaultDrawingSupplier;

import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import javafx.geometry.Orientation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;
import java.awt.*;

public class FreeChartGraph extends JFrame {

    private String title;
    private XYDataset dataset;
    private javax.swing.JPanel graph1JPanel;


    public FreeChartGraph(String titleArg, XYDataset datasetArg, javax.swing.JPanel graph1JPanelArg) {
        title = titleArg;
        dataset = datasetArg;
        graph1JPanel = graph1JPanelArg;
    }

    public void Make() {
        JFreeChart chart = ChartFactory.createXYLineChart(title, "Gem. Servicetime", "Gem. " + title, dataset, PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();

        //override colors for series to better visible colors
        plot.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[]{Color.red,
                        Color.green,
                        Color.blue,
                        Color.black,
                        Color.cyan,
                        Color.gray,
                        Color.yellow,
                        Color.magenta},
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
        ));

        NumberAxis xAxis = new LogarithmicAxis("Percentile");
        xAxis.setAutoRangeIncludesZero(true);
        plot.setDomainAxis(xAxis);
        //NumberAxis yAs = new LogarithmicAxis("NOG INVULLEN "+ title);
        //plot.setRangeAxis(yAs);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.zoomOutBoth(300, 300);
        graph1JPanel.removeAll();
        graph1JPanel.add(chartPanel);
        graph1JPanel.validate();
    }

}