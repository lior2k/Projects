package api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class MyEdgeIterator implements Iterator<MyEdge> {
    private final DirectedWeightedGraphImpl graph;
    private final int MC;
    private final ArrayList<MyEdge> list;
    private int i = 0;

    public MyEdgeIterator(DirectedWeightedGraphImpl graph, int MC) {
        this.graph = graph;
        this.MC = MC;
        list = new ArrayList<>();
        list.addAll(graph.getAll_edges().values());
    }

    public MyEdgeIterator(DirectedWeightedGraphImpl graph, int MC, int key) {
        this.graph = graph;
        this.MC = MC;
        list = new ArrayList<>();
        MyNode n = graph.getNode(key);
        list.addAll(n.getEdges().values());
    }

    @Override
    public boolean hasNext() {
        return i < list.size();
    }

    @Override
    public MyEdge next() {
        if (graph.getMC() == this.MC) {
            MyEdge edge = list.get(i);
            i++;
            return edge;
        } else {
            throw new RuntimeException("Graph was changed, iteration stopped!");
        }
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super MyEdge> action) {
        Iterator.super.forEachRemaining(action);
    }
}
