package api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class MyNodeIterator implements Iterator<NodeData> {
    private DirectedWeightedGraphImpl graph;
    private final int MC;
    private ArrayList<NodeData> list;
    private int i = 0;

    public MyNodeIterator(DirectedWeightedGraphImpl graph, int MC) {
        this.graph = graph;
        this.MC = MC;
        list = new ArrayList<>();
        list.addAll(graph.get_map().values());
    }

    @Override
    public boolean hasNext() {
        return i < list.size();
    }

    @Override
    public NodeData next() {
        if (graph.getMC() == this.MC) {
            NodeData n = list.get(i);
            i++;
            return n;
        } else {
            throw new RuntimeException("Graph was changed, iteration stopped!");
        }
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super NodeData> action) {
        Iterator.super.forEachRemaining(action);
    }
}
