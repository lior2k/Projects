package tests;

import api.MyPair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyPairTest {
    MyPair p = new MyPair(10,20);

    @Test
    void getLeft() {
        int ans = p.getLeft();
        assertEquals(10, ans);
    }

    @Test
    void getRight() {
        int ans = p.getRight();
        assertEquals(20, ans);
    }

    @Test
    void testEquals() {
        MyPair p2 = new MyPair(10,20);
        assertEquals(p ,p2);
    }

    @Test
    void testHashCode() {
        int ans = p.hashCode();
        assertEquals(30, ans);
    }
}