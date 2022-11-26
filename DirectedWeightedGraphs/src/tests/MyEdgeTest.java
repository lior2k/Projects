package tests;

import api.MyEdge;
import api.MyPair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyEdgeTest {
    MyEdge E = new MyEdge(0,2, 0);
    MyPair p = new MyPair(0,2);

    @Test
    void getPair() {
        assertEquals(p, E.getPair());
    }

    @Test
    void copy() {
        MyEdge copy = E.copy();
        assertNotEquals(copy, E);
    }
}