package api;
import java.util.HashMap;
import java.util.Iterator;

public class DirectedWeightedGraphImpl implements DirectedWeightedGraph{

    private HashMap<Integer, NodeData> graph;
    private HashMap<MyPair, EdgeData> all_edges;
    private int MC;

    public DirectedWeightedGraphImpl(){
        this.graph = new HashMap<>();
        this.all_edges = new HashMap<>();
        this.MC =0;
    }

    @Override
    public NodeData getNode(int key) {
        return graph.get(key);
    }

    @Override
    public EdgeData getEdge(int src, int dest) {
        return all_edges.get(new MyPair(src,dest));
    }

    @Override
    public void addNode(NodeData n) {
        graph.put(n.getKey(), n);
        MC++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (graph.get(src) == null || graph.get(dest) == null) {
            return;
        }
        MyEdge E = new MyEdge(src, dest, w);
        MyNode srcnode = (MyNode) graph.get(src);
        MyNode destnode = (MyNode) graph.get(dest);
        srcnode.addEdge(E);
        destnode.addEdge(E);
        all_edges.put(E.getPair(), E);
        MC++;
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return new MyNodeIterator(this, this.MC);
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return new MyEdgeIterator(this, this.MC);
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return new MyEdgeIterator(this, this.MC, node_id);
    }

    @Override
    public NodeData removeNode(int key) {
        if (graph.get(key) == null) {
            return null;
        }
        MyNode temp = (MyNode) graph.get(key);
        for (EdgeData E : temp.getEdges().values()) {
            if (E.getSrc() == key) {
                MyNode src_node = (MyNode) graph.get(E.getDest());
                src_node.removeEdge(((MyEdge) E).getPair());
            }
            if (E.getDest() == key) {
                MyNode dest_node = (MyNode) graph.get(E.getSrc());
                dest_node.removeEdge(((MyEdge) E).getPair());
            }
            all_edges.remove(((MyEdge) E).getPair());
        }
        graph.remove(key);
        MC++;
        return temp;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if (graph.get(src) == null || graph.get(dest) == null) {
            return null;
        }
        MyPair p = new MyPair(src,dest);
        all_edges.remove(p);
        MyNode src_node = (MyNode) graph.get(src);
        MyNode dest_node = (MyNode) graph.get(dest);
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

    public HashMap<Integer, NodeData> get_map() {
        return this.graph;
    }

    public HashMap<MyPair, EdgeData> getAll_edges() {
        return this.all_edges;
    }

    public String toString(){
        return "Node size: "+graph.size()+" edge size: "+edgeSize()+this.graph;
    }

    public double max_x() {
        double max = -1;
        Iterator<NodeData> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = (MyNode) node_iter.next();
            if (n.getLocation().x() > max) {
                max = n.getLocation().x();
            }
        }
        return max;
    }

    public double max_y() {
        double max = -1;
        Iterator<NodeData> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = (MyNode) node_iter.next();
            if (n.getLocation().y() > max) {
                max = n.getLocation().y();
            }
        }
        return max;
    }

    public double min_x() {
        double min = Double.MAX_VALUE;
        Iterator<NodeData> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = (MyNode) node_iter.next();
            if (n.getLocation().x() < min) {
                min = n.getLocation().x();
            }
        }
        return min;
    }

    public double min_y() {
        double min = Double.MAX_VALUE;
        Iterator<NodeData> node_iter = nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = (MyNode) node_iter.next();
            if (n.getLocation().y() < min) {
                min = n.getLocation().y();
            }
        }
        return min;
    }

}
