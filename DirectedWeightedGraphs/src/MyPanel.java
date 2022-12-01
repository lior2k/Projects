import api.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class MyPanel extends JPanel {
    DirectedWeightedGraphAlgoImpl Algo;
    DirectedWeightedGraphImpl graph;
    Dimension dim;
    private final int window_height;
    private final int window_wide;

    public MyPanel(DirectedWeightedGraphAlgoImpl algo, Dimension dim) {
        this.Algo = algo;
        this.graph = Algo.getGraph();
        this.dim = dim;
        this.window_height = dim.height/3;
        this.window_wide = dim.width/3;
        this.setPreferredSize(new Dimension(dim.width*2/4, dim.height*2/4));
        this.setSize(window_wide, window_height);
    }


    @Override
    public void paint(Graphics g) {

        Graphics2D g2D = (Graphics2D) g;
        double max_x = graph.max_x();
        double max_y = graph.max_y();
        double min_x = graph.min_x();
        double min_y = graph.min_y();
        double widefactor = window_wide/(max_x-min_x);
        double heightfactor = window_height/(max_y-min_y);
        Iterator<MyNode> node_iter = graph.nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            String nkey = ""+n.getKey();//+" location:"+n.getLocation()
            GeoLocationImpl location = (GeoLocationImpl) n.getLocation();
            double x = (location.x()-min_x)*widefactor+100;
            double y = (location.y()-min_y)*heightfactor+100;
            g2D.setColor(new Color(n.getTag()));
            g2D.fillOval((int) x, (int) y,10,10);
            g2D.drawString(nkey, (int) x-3, (int)y-3);

            Iterator<MyEdge> edge_iter = graph.edgeIter(n.getKey());
            while (edge_iter.hasNext()) {
                MyEdge edge = edge_iter.next();
                if (edge.getSrc() == n.getKey()) {
                    MyNode v = graph.getNode(edge.getDest());
                    double x2 = (v.getLocation().x()-min_x)*widefactor+100;
                    double y2 = (v.getLocation().y()-min_y)*heightfactor+100;
                    g2D.setColor(new Color(edge.getTag()));
                    g2D.drawLine((int) x+5, (int) y+5, (int) x2+5, (int) y2+5);
                    drawArrowHead(g,(int) x+5, (int) y+5, (int) x2+5, (int) y2+5);
                }
            }
        }
        resetColors();
    }

    //Draw a triangle shaped polygon at the end of each line to show the direction of the edge,
    //this specific function is from stack overflow.
    private void drawArrowHead(Graphics g, int x1, int y1, int x2, int y2) {
        int arrow_width = 15;
        int arrow_height = 2;
        int diff_x = x2 - x1, diff_y = y2 - y1;
        double D = Math.sqrt(diff_x*diff_x + diff_y*diff_y);
        double xm = D - arrow_width, xn = xm, ym = arrow_height, yn = -arrow_height, x;
        double sin = diff_y / D;
        double cos = diff_x / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] x_points = {x2, (int) xm, (int) xn};
        int[] y_points = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(x_points, y_points, 3);
    }

    public void addnode(int key, double x, double y) {
        double max_x = graph.max_x();
        double max_y = graph.max_y();
        double min_x = graph.min_x();
        double min_y= graph.min_y();
        double widefactor = window_wide/(max_x-min_x);
        double heightfactor = window_height/(max_y-min_y);
        x = min_x+x/widefactor;
        y = min_y+y/heightfactor;
        MyNode v = new MyNode(key, new GeoLocationImpl(x,y,0));
        graph.addNode(v);
    }

    public void resetColors() {
        Iterator<MyNode> node_iter = graph.nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            n.setTag(0xFFFFFF);
        }
        Iterator<MyEdge> edge_iter = graph.edgeIter();
        while (edge_iter.hasNext()) {
            MyEdge edge = edge_iter.next();
            edge.setTag(0x8E23A4);
        }
    }
}
//0xFF0000