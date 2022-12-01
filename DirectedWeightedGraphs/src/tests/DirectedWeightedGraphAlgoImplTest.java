package tests;

import api.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectedWeightedGraphAlgoImplTest {
    DirectedWeightedGraphAlgoImpl Algo = new DirectedWeightedGraphAlgoImpl();
    String nodes1000 = "1000Nodes.json"; //center = node num 362
    String nodes10000 = "10000Nodes.json"; //center = node num 3846
    String nodes100000 = "100000Nodes.json";
    String G3 = "out/artifacts/Ex2_jar/G1.json";
    String G2 = "out/artifacts/Ex2_jar/G1.json";
    String G1 = "out/artifacts/Ex2_jar/G1.json";

    @org.junit.jupiter.api.Test
    void init() {
//        Algo.load(G3);
//        DirectedWeightedGraphAlgoImpl Algo2 = new DirectedWeightedGraphAlgoImpl();
//        Algo2.init(Algo.getGraph());
//        assertEquals(Algo.getGraph(), Algo2.getGraph());

    }

    @org.junit.jupiter.api.Test
    void getGraph() {
//        DirectedWeightedGraphImpl G1 = new DirectedWeightedGraphImpl();
//        Algo.init(G1);
//        assertEquals(G1, Algo.getGraph());
    }

    @org.junit.jupiter.api.Test
    void copy() {
//        DirectedWeightedGraphImpl G1 = new DirectedWeightedGraphImpl();
//        MyNode n = new MyNode(0, new GeoLocationImpl(1,2,3));
//        MyNode v = new MyNode(1, new GeoLocationImpl(1,2,4));
//        G1.addNode(n);
//        G1.addNode(v);
//        Algo.init(G1);
//        DirectedWeightedGraphImpl G2 = (DirectedWeightedGraphImpl) Algo.copy();
//        assertNotEquals(G1, G2);
//        assertNotEquals(G1.getNode(0), G2.getNode(0));
//        assertNotEquals(G1.getNode(1), G2.getNode(1));
//        assertEquals(0, G2.getNode(0).getKey());
//        assertEquals(1, G2.getNode(1).getKey());
    }

    @org.junit.jupiter.api.Test
    void isConnected() {
//        Algo.load(G1);
//        assertTrue(Algo.isConnected());
//        Algo.load(G2);
//        assertTrue(Algo.isConnected());
//        Algo.load(G3);
//        assertTrue(Algo.isConnected());
//        Algo.load(nodes1000);
//        assertTrue(Algo.isConnected());
//        Algo.load(nodes10000);
//        assertTrue(Algo.isConnected());
//        Algo.load(nodes100000);
//        assertTrue(Algo.isConnected());
        Algo.isConnected();
    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {
       double ans;
//        Algo.load(G1);
//        ans = Algo.shortestPathDist(6,10);
//        assertEquals(5.4283296635770935, ans);
//        Algo.load(nodes1000);
//        ans = Algo.shortestPathDist(362,384);
//        assertEquals(605.0049493760619, ans);
//        Algo.load(nodes10000);
//        ans = Algo.shortestPathDist(362,3840);
//        assertEquals(ans, 1093.542322166061);
//        Algo.load(nodes100000);
//        ans = Algo.shortestPathDist(52,93);
//        assertEquals(ans, 1262.608122320432);
        Algo.shortestPathDist(50, 49384);
    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
//        List<NodeData> ans;
//        Algo.load(G3);
//        ans = Algo.shortestPath(7,31);
//        List<NodeData> eq = new LinkedList<>();
//        eq.add(Algo.getGraph().getNode(7));
//        eq.add(Algo.getGraph().getNode(11));
//        eq.add(Algo.getGraph().getNode(13));
//        eq.add(Algo.getGraph().getNode(14));
//        eq.add(Algo.getGraph().getNode(29));
//        eq.add(Algo.getGraph().getNode(30));
//        eq.add(Algo.getGraph().getNode(31));
//        for (int i=0; i<ans.size(); i++) {
//            assertEquals(ans.get(i), eq.get(i));
//        }
//        Algo.load(nodes10000);
//        ans = Algo.shortestPath(362, 3840);
//        eq = new LinkedList<>();
//        eq.add(Algo.getGraph().getNode(362));
//        eq.add(Algo.getGraph().getNode(5676));
//        eq.add(Algo.getGraph().getNode(2869));
//        eq.add(Algo.getGraph().getNode(6643));
//        eq.add(Algo.getGraph().getNode(3840));
//        for (int i=0; i<ans.size(); i++) {
//            assertEquals(ans.get(i), eq.get(i));
//        }
    }

    @org.junit.jupiter.api.Test
    void center() {
        NodeData n;
        NodeData v;
//        Algo.load(G1);
//        n = Algo.getGraph().getNode(8);
//        v = Algo.center();
//        assertEquals(n, v);
//        Algo.load(G2);
//        n = Algo.getGraph().getNode(0);
//        v = Algo.center();
//        assertEquals(n, v);
//        Algo.load(G3);
//        n = Algo.getGraph().getNode(40);
//        v = Algo.center();
//        assertEquals(n, v);
//        Algo.load(nodes1000);
//        n = Algo.getGraph().getNode(362);
//        v = Algo.center();
//        assertEquals(n, v);
    }

    @org.junit.jupiter.api.Test
    void tsp() {
        List<MyNode> cities;
//        Algo.load(G3);
//        cities = new LinkedList<>();
//        cities.add(Algo.getGraph().getNode(0));
//        cities.add(Algo.getGraph().getNode(3));
//        cities.add(Algo.getGraph().getNode(7));
//        cities.add(Algo.getGraph().getNode(21));
//        cities.add(Algo.getGraph().getNode(44));
//        List<NodeData> ans = Algo.tsp(cities);
        cities = new LinkedList<>();
        cities.add(Algo.getGraph().getNode(0));
        cities.add(Algo.getGraph().getNode(3));
        cities.add(Algo.getGraph().getNode(7));
        Algo.tsp(cities);

//        assertEquals(0,ans.get(0).getKey());
//        assertEquals(2,ans.get(1).getKey());
//        assertEquals(3,ans.get(2).getKey());
//        assertEquals(13,ans.get(3).getKey());
//        assertEquals(11,ans.get(4).getKey());
//        assertEquals(7,ans.get(5).getKey());
//        assertEquals(11,ans.get(6).getKey());
//        assertEquals(13,ans.get(7).getKey());
//        assertEquals(14,ans.get(8).getKey());
//        assertEquals(29,ans.get(9).getKey());
//        assertEquals(30,ans.get(10).getKey());
//        assertEquals(31,ans.get(11).getKey());
//        assertEquals(32,ans.get(12).getKey());
//        assertEquals(21,ans.get(13).getKey());
//        assertEquals(32,ans.get(14).getKey());
//        assertEquals(31,ans.get(15).getKey());
//        assertEquals(36,ans.get(16).getKey());
//        assertEquals(37,ans.get(17).getKey());
//        assertEquals(38,ans.get(18).getKey());
//        assertEquals(39,ans.get(19).getKey());
//        assertEquals(40,ans.get(20).getKey());
//        assertEquals(41,ans.get(21).getKey());
//        assertEquals(42,ans.get(22).getKey());
//        assertEquals(43,ans.get(23).getKey());
//        assertEquals(44,ans.get(24).getKey());
    }

    @org.junit.jupiter.api.Test
    void save() {
        DirectedWeightedGraphImpl graph = new DirectedWeightedGraphImpl();
        graph.addNode(new MyNode(1,new GeoLocationImpl(0,0,0)));
        graph.addNode(new MyNode(2,new GeoLocationImpl(0,0,0)));
        graph.addNode(new MyNode(3,new GeoLocationImpl(0,0,0)));
        graph.addNode(new MyNode(4,new GeoLocationImpl(0,0,0)));
        Algo.init(graph);
        boolean flag = Algo.save("output.json");
        assertTrue(flag);
    }

    @org.junit.jupiter.api.Test
    void load() {
        DirectedWeightedGraph graph = new DirectedWeightedGraphImpl();
        graph.addNode(new MyNode(1,new GeoLocationImpl(0,0,0)));
        graph.addNode(new MyNode(2,new GeoLocationImpl(0,0,0)));
        graph.addNode(new MyNode(3,new GeoLocationImpl(0,0,0)));
        graph.addNode(new MyNode(4,new GeoLocationImpl(0,0,0)));
        boolean flag = Algo.load("output.json");
        assertTrue(flag);

    }
}