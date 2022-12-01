package api;
import java.util.HashMap;
import java.util.Iterator;

public class DirectedWeightedGraphImpl implements DirectedWeightedGraph{

    private final HashMap<Integer, MyNode> graph;
    private final HashMap<MyPair, MyEdge> all_edges;
    private int MC;

    public DirectedWeightedGraphImpl(){
        this.graph = new HashMap<>();
        this.all_edges = new HashMap<>();
        this.MC =0;
    }

    @Override
    public MyNode getNode(int key) {
        return graph.get(key);
    }

    @Override
    public MyEdge getEdge(int src, int dest) {
        return all_edges.get(new MyPair(src,dest));
    }

    @Override
    public void addNode(MyNode n) {
        graph.put(n.getKey(), n);
        MC++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (graph.get(src) == null || graph.get(dest) == null) {
            return;
        }
        MyEdge E = new MyEdge(src, dest, w);
        MyNode srcnode = graph.get(src);
        MyNode destnode = graph.get(dest);
        srcnode.addEdge(E);
        destnode.addEdge(E);
        all_edges.put(E.getPair(), E);
        MC++;
    }

    @Override
    public Iterator<MyNode> nodeIter() {
        return new MyNodeIterator(this, this.MC);
    }

    @Override
    public Iterator<MyEdge> edgeIter() {
        return new MyEdgeIterator(this, this.MC);
    }

    @Override
    public Iterator<MyEdge> edgeIter(int node_id) {
        return new MyEdgeIterator(this, this.MC, node_id);
    }

    @Override
    public MyNode removeNode(int key) {
        if (graph.get(key) == null) {
            return null;
        }
        MyNode temp = graph.get(key);
        for (MyEdge E : temp.getEdges().values()) {
            if (E.getSrc() == key) {
                MyNode src_node = graph.get(E.getDest());
                src_node.removeEdge(E.getPair());
            }
            if (E.getDest() == key) {
                MyNode dest_node = graph.get(E.getSrc());
                dest_node.removeEdge(E.getPair());
            }
            all_edges.remove(E.getPair());
        }
        graph.remove(key);
        MC++;
        return temp;
    }

    @Override
    public MyEdge removeEdge(int src, int dest) {
        if (graph.get(src) == null || graph.get(dest) == null) {
            return null;
        }
        MyPair p = new MyPair(src,dest);
        all_edges.remove(p);
        MyNode src_node = graph.get(src);
        MyNode dest_node = graph.get(dest);
        MyEdge E = src_node.removeEdge(p);
        dest_node.removeEdge(p);
        MC++;
        return E;
    }

    @Override
    public int nodeSize() {
        return this.graph.size();
    }

    @Override
    public int edgeSize() {
        return all_edges.size();
    }

    @Override
    public int getMC() {
        return this.MC;
    }

    public HashMap<Integer, MyNode> get_map() {
        return this.graph;
    }

    public HashMap<MyPair, MyEdge> getAll_edges() {
        return this.all_edges;
    }

    public String toString(){
        return "Node size: "+graph.size()+" edge size: "+edgeSize()+this.graph;
    }

    public double max_x() {
        double max = -1;
        Iterator<MyNode> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            if (n.getLocation().x() > max) {
                max = n.getLocation().x();
            }
        }
        return max;
    }

    public double max_y() {
        double max = -1;
        Iterator<MyNode> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            if (n.getLocation().y() > max) {
                max = n.getLocation().y();
            }
        }
        return max;
    }

    public double min_x() {
        double min = Double.MAX_VALUE;
        Iterator<MyNode> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            if (n.getLocation().x() < min) {
                min = n.getLocation().x();
            }
        }
        return min;
    }

    public double min_y() {
        double min = Double.MAX_VALUE;
        Iterator<MyNode> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            if (n.getLocation().y() < min) {
                min = n.getLocation().y();
            }
        }
        return min;
    }

}
