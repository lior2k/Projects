package tests;

import api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyNodeTest {
    MyNode n = new MyNode(0, new GeoLocationImpl(1,2,3));

    @Test
    void addEdge() {
        MyEdge E = new MyEdge(0,2, 0);
        n.addEdge(E);
        assertEquals(E, n.getEdges().get(new MyPair(0,2)));
    }

    @Test
    void removeEdge() {
        MyEdge E = new MyEdge(0,2, 0);
        n.addEdge(E);
        assertEquals(E, n.removeEdge(new MyPair(0,2)));
    }

    @Test
    void copy() {
        MyNode v = n.copy();
        assertNotEquals(v, n);
        MyNode u = v.copy();
        assertNotEquals(u, v);
    }

    @Test
    void testEquals() {
        MyNode v = new MyNode(0, new GeoLocationImpl(1,2,3));
        assertFalse(v.equals(n));
    }
}