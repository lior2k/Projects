package tests;

import api.GeoLocationImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoLocationImplTest {
    GeoLocationImpl g = new GeoLocationImpl(1,2,3);

    @Test
    void x() {
        assertEquals(1, g.x());
    }

    @Test
    void y() {
        assertEquals(2, g.y());
    }

    @Test
    void z() {
        assertEquals(3, g.z());
    }

    @Test
    void distance() {
        GeoLocationImpl g1 = new GeoLocationImpl(2,3,4);
        double ans = g.distance(g1);
        assertEquals(1.7320508075688772, ans);
    }

    @Test
    void copy() {
        GeoLocationImpl g1 = g.copy();
        assertNotEquals(g1, g);
    }
}