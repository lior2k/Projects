package api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class MyNodeIterator implements Iterator<MyNode> {
    private final DirectedWeightedGraphImpl graph;
    private final int MC;
    private final ArrayList<MyNode> list;
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
    public MyNode next() {
        if (graph.getMC() == this.MC) {
            MyNode n = list.get(i);
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
    public void forEachRemaining(Consumer<? super MyNode> action) {
        Iterator.super.forEachRemaining(action);
    }
}
