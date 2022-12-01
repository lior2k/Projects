package tests;

import api.*;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphImplTest {
    DirectedWeightedGraphImpl graph;
    MyNode n0 = new MyNode(0, new GeoLocationImpl(1,2,3));
    MyNode n1 = new MyNode(1, new GeoLocationImpl(1,2,3));
    MyNode n2 = new MyNode(2, new GeoLocationImpl(1,2,3));

    @Test
    void getNode() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        MyNode v = graph.getNode(0);
        assertEquals(v, n0);
    }

    @Test
    void getEdge() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.connect(0,1, 0.5);
        MyPair p = new MyPair(0,1);
        assertEquals(n0.getEdges().get(p), n1.getEdges().get(p));
    }

    @Test
    void addNode() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        assertEquals(n0, graph.getNode(0));
    }

    @Test
    void nodeIter() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(new MyNode(0, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(1, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(2, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(3, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(4, new GeoLocationImpl(0,1,2)));
        Iterator<MyNode> node_iter = graph.nodeIter();
        try {
            while (node_iter.hasNext()) {
                MyNode n = node_iter.next();
                if (n.getKey() == 3) {
                    graph.removeNode(0);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void edgeIter() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(new MyNode(0, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(1, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(2, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(3, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(4, new GeoLocationImpl(0,1,2)));
        graph.connect(0,1,0);
        graph.connect(1,2,0);
        graph.connect(2,3,0);
        graph.connect(3,4,0);
        graph.connect(4,0,0);
        graph.connect(0,2,0);
        graph.connect(3,1,0);
        try {
            Iterator<MyEdge> edge_iter = graph.edgeIter();
            while (edge_iter.hasNext()) {
                MyEdge e = edge_iter.next();
                if (e.getSrc() == 4) {
                    graph.removeEdge(3,4);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEdgeIter() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(new MyNode(0, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(1, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(2, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(3, new GeoLocationImpl(0,1,2)));
        graph.addNode(new MyNode(4, new GeoLocationImpl(0,1,2)));
        graph.connect(0,1,0);
        graph.connect(1,2,0);
        graph.connect(2,3,0);
        graph.connect(3,4,0);
        graph.connect(4,0,0);
        graph.connect(0,2,0);
        graph.connect(3,1,0);
        graph.connect(1,0,0);
        try {
            Iterator<MyEdge> edge_iter = graph.edgeIter(0);
            while (edge_iter.hasNext()) {
                MyEdge e = edge_iter.next();
                if (e.getSrc() == 0) {
                    graph.removeEdge(3,4);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    void removeNode() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        MyNode v = graph.removeNode(0);
        assertEquals(v, n0);
        assertNull(graph.getNode(0));
    }

    @Test
    void removeEdge() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.connect(0,1,0.5);
        graph.connect(1,2,1);
        MyEdge edge= graph.getEdge(0,1);
        MyEdge removed_edge = graph.removeEdge(0,1);
        assertEquals(edge, removed_edge);
        assertNull(graph.getEdge(0,1));
        assertNull(graph.removeEdge(0,1));

    }

    @Test
    void nodeSize() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        assertEquals(3, graph.nodeSize());
    }

    @Test
    void edgeSize() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.connect(0,1,0.5);
        graph.connect(1,2,1);
        assertEquals(2, graph.edgeSize());
    }

    @Test
    void getMC() {
        graph = new DirectedWeightedGraphImpl();
        graph.addNode(n0);
        graph.addNode(n1);
        graph.addNode(n2);
        graph.connect(0,1,0.5);
        graph.connect(1,2,1);
        assertEquals(5, graph.getMC());
    }
}