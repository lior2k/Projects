import api.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MyFrame extends JFrame implements ActionListener {
    MyPanel panel;
    DirectedWeightedGraphAlgoImpl Algo;
    DirectedWeightedGraphImpl graph;

    public MyFrame(DirectedWeightedGraphAlgoImpl algo) {
        super();
        this.Algo = algo;
        this.graph = (DirectedWeightedGraphImpl) Algo.getGraph();
        this.setTitle("Directed Weighted Graph GUI");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int height = dim.height;
        int width = dim.width;
        panel = new MyPanel(Algo, dim);
        this.add(panel);
        init_menu();
        this.setSize(width, height);
        this.setVisible(true);
        this.pack();
        this.setBackground(new Color(0x2FA8A8));
    }

    public void init_menu() {
        JMenuBar bar = new JMenuBar();

        JMenu file_menu = new JMenu("File");
        JMenuItem save = new JMenuItem("save");
        JMenuItem load = new JMenuItem("load");
        file_menu.add(save);
        file_menu.add(load);
        save.addActionListener(this);
        load.addActionListener(this);
        JMenu graph = new JMenu("Graph");
        JMenuItem getNode = new JMenuItem("getNode");
        JMenuItem getEdge = new JMenuItem("getEdge");
        JMenuItem addNode = new JMenuItem("addNode");
        JMenuItem connect = new JMenuItem("connect");
        JMenuItem removeNode = new JMenuItem("removeNode");

        JMenuItem removeEdge = new JMenuItem("removeEdge");
        graph.add(getNode);
        graph.add(getEdge);
        graph.add(addNode);
        graph.add(connect);
        graph.add(removeNode);
        graph.add(removeEdge);
        getNode.addActionListener(this);
        getEdge.addActionListener(this);
        addNode.addActionListener(this);
        connect.addActionListener(this);
        removeNode.addActionListener(this);
        removeEdge.addActionListener(this);
        JMenu algorithms = new JMenu("Algorithms");
        JMenuItem isConnected = new JMenuItem("isConnected");
        JMenuItem tsp = new JMenuItem("tsp");
        JMenuItem shortestPath = new JMenuItem("shortestPath");
        JMenuItem shortestPathDist = new JMenuItem("shortestPathDist");
        JMenuItem center = new JMenuItem("center");
        algorithms.add(isConnected);
        algorithms.add(tsp);
        algorithms.add(shortestPath);
        algorithms.add(shortestPathDist);
        algorithms.add(center);
        isConnected.addActionListener(this);
        tsp.addActionListener(this);
        shortestPath.addActionListener(this);
        shortestPathDist.addActionListener(this);
        center.addActionListener(this);

        bar.add(file_menu);
        bar.add(graph);
        bar.add(algorithms);
        this.setJMenuBar(bar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //repaint();
        String st = e.getActionCommand();
        if (Objects.equals(st, "removeNode")) {
                String node_id_str = JOptionPane.showInputDialog("insert node id");
                int node_id = Integer.parseInt(node_id_str);
                graph.removeNode(node_id);
                repaint();
        } else if (Objects.equals(st,"addNode")) {
            String node_id_str = JOptionPane.showInputDialog("insert node id");
            String node_location_x_str = JOptionPane.showInputDialog("insert x value");
            String node_location_y_str = JOptionPane.showInputDialog("insert y value");
            int node_id = Integer.parseInt(node_id_str);
            double node_location_x = Double.parseDouble(node_location_x_str);
            double node_location_y = Double.parseDouble(node_location_y_str);
            this.panel.addnode(node_id, node_location_x, node_location_y);
            repaint();
        } else if (Objects.equals(st,"load")) {
            String load_filename_str = JOptionPane.showInputDialog("insert json file including location");
            Algo.load(load_filename_str);
            graph = (DirectedWeightedGraphImpl) Algo.getGraph();
            panel.Algo = Algo;
            panel.graph = (DirectedWeightedGraphImpl) Algo.getGraph();
            repaint();
        } else if (Objects.equals(st, "save")) {
            String save_filename_str = JOptionPane.showInputDialog("insert filename");
            Algo.save(save_filename_str);
        } else if (Objects.equals(st, "removeEdge")) {
            String remove_edge_src_str = JOptionPane.showInputDialog("insert src node key");
            String remove_edge_dest_str = JOptionPane.showInputDialog("insert dest node key");
            int src_node_index = Integer.parseInt(remove_edge_src_str);
            int dest_node_index = Integer.parseInt(remove_edge_dest_str);
            graph.removeEdge(src_node_index, dest_node_index);
            repaint();
        } else if (Objects.equals(st, "connect")) {
            String src_node_index_str = JOptionPane.showInputDialog("insert src node key");
            String dest_node_index_str = JOptionPane.showInputDialog("insert dest node key");
            String weight_str = JOptionPane.showInputDialog("insert edge weight");
            int src_node_index = Integer.parseInt(src_node_index_str);
            int dest_node_index = Integer.parseInt(dest_node_index_str);
            double weight = Double.parseDouble(weight_str);
            graph.connect(src_node_index, dest_node_index, weight);
            repaint();
        } else if (Objects.equals(st, "isConnected")) {
            boolean ans = Algo.isConnected();
            JOptionPane.showMessageDialog(null, ans);
            repaint();
        } else if (Objects.equals(st, "shortestPathDist")) {
            String src_node_str = JOptionPane.showInputDialog("insert src node key");
            String dest_node_str = JOptionPane.showInputDialog("insert dest node key");
            int src_node_index = Integer.parseInt(src_node_str);
            int dest_node_index = Integer.parseInt(dest_node_str);
            double ans = Algo.shortestPathDist(src_node_index, dest_node_index);
            JOptionPane.showMessageDialog(null, ans);
        } else if (Objects.equals(st, "shortestPath")) {
            String src_node_str = JOptionPane.showInputDialog("insert src node key");
            String dest_node_str = JOptionPane.showInputDialog("insert dest node key");
            int src_node_index = Integer.parseInt(src_node_str);
            int dest_node_index = Integer.parseInt(dest_node_str);
            List<NodeData> ans = Algo.shortestPath(src_node_index, dest_node_index);
            String ans_str = "";
            for (NodeData an : ans) {
                MyNode n = (MyNode) an;
                ans_str = ans_str + n.getKey() + " ";
                n.setTag(0x00FF04);
            }
            JOptionPane.showMessageDialog(null, ans_str);
            repaint();
        } else if (Objects.equals(st, "center")) {
            MyNode n = (MyNode) Algo.center();
            int ans = n.getKey();
            n.setTag(0x00FF04);
            repaint();
            JOptionPane.showMessageDialog(null, ans);
        } else if (Objects.equals(st, "tsp")) {
            String cities_size_str = JOptionPane.showInputDialog("insert how many cities you want to visit");
            int cities_size = Integer.parseInt(cities_size_str);
            List<NodeData> cities = new LinkedList<>();
            for (int i=1; i<cities_size+1; i++) {
                String city_str = JOptionPane.showInputDialog("insert the "+i+"th city");
                int city = Integer.parseInt(city_str);
                cities.add(graph.getNode(city));
            }
            List<NodeData> ans = Algo.tsp(cities);
            List<Integer> ans_int = new LinkedList<>();
            for (NodeData city : ans) {
                city.setTag(0x00FF04);
                ans_int.add(city.getKey());
            }
            repaint();
            String ans_str = "";
            for (int i : ans_int) {
                 ans_str = ans_str+i+" ";
            }
            JOptionPane.showMessageDialog(null, ans_str);
        } else if (Objects.equals(st, "getNode")) {
            String str = JOptionPane.showInputDialog("insert node key");
            int node_key = Integer.parseInt(str);
            graph.getNode(node_key).setTag(0xFF0000);
            repaint();
        } else if (Objects.equals(st, "getEdge")) {
            String src = JOptionPane.showInputDialog("insert edge source");
            String dest = JOptionPane.showInputDialog("insert edge dest");
            int src_key = Integer.parseInt(src);
            int dest_key = Integer.parseInt(dest);
            graph.getEdge(src_key, dest_key).setTag(0xFF0000);
            repaint();
        }
    }
}
