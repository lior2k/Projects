package api;
import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DirectedWeightedGraphAlgoImpl implements DirectedWeightedGraphAlgorithms{

    private DirectedWeightedGraphImpl graph;
    int Grey = 153;
    int Black = 0x000000;
    int White = 0xFFFFFF;
    private int Time = 0;

    public DirectedWeightedGraphAlgoImpl() {
        this.graph = new DirectedWeightedGraphImpl();
    }

    @Override
    public void init(DirectedWeightedGraphImpl g) {
        this.graph = g;
    }

    @Override
    public DirectedWeightedGraphImpl getGraph() {
        return this.graph;
    }

    //for each node in our graph we run a node copy method which returns a deep copy of the node
    //and the node's objects (edges, geolocations)
    @Override
    public DirectedWeightedGraphImpl copy() {
        DirectedWeightedGraphImpl g = new DirectedWeightedGraphImpl();
        Iterator<MyNode> iter = this.graph.nodeIter();
        while(iter.hasNext()){
            MyNode n = iter.next();
            g.addNode(n.copy());
        }
        return g;
    }

    // reverse all edges of the graph. used to check graph connectivity.
    public void reverseEdges() {
        Iterator<MyEdge> iter = graph.edgeIter();
        while (iter.hasNext()) {
            iter.next().reverse();
        }
    }

    //Run BFS algorithm on an arbitrary node  n, if after performing the BFS we find a node
    //with infinite distance it means the node was not reachable from n as well as any of n's
    //neighbours hence return false, otherwise return true.
    @Override
    public boolean isConnected() {
        Iterator<MyNode> iter = graph.nodeIter();
        MyNode n = iter.next();
        BFS(n);
        while (iter.hasNext()) {
            MyNode v = iter.next();
            if (v.getDistance() == Integer.MAX_VALUE) {
                return false;
            }
        }
        this.reverseEdges();
        this.BFS(n);
        this.reverseEdges();
        iter = graph.nodeIter();
        while (iter.hasNext()) {
            MyNode v = iter.next();
            if (v.getDistance() == Integer.MAX_VALUE) {
                return false;
            }
        }

        return true;
    }

    //run Dijkstra algorithm on source node, if the destination's node distance value is still
    //set to infinity, return -1, otherwise return destination's node distance
    @Override
    public double shortestPathDist(int src, int dest) {
        Dijkstra(graph.getNode(src));
        double shortest_dis = (graph.getNode(dest)).getDistance();
        if(shortest_dis == Integer.MAX_VALUE){
            return -1;
        }
        return shortest_dis;
    }


    //Run Dijkstra algorithm on source node, add all nodes to a list beginning with dest > dest.prev > ... > source
    //then reverse the list and return list
    @Override
    public List<MyNode> shortestPath(int src, int dest) {
        List<MyNode> list = new LinkedList<>();
        Dijkstra(graph.getNode(src));
        MyNode temp = graph.getNode(dest);
        if(temp.getDistance() == Integer.MAX_VALUE){
            return null;
        }
        //add nodes destnode -> destnodeprev -> ... -> src
        while(temp != null){
            list.add(temp);
            temp = temp.getPrev();
        }
        //reverse the list
        List<MyNode> ans = new LinkedList<>();
        int size = list.size()-1;
        while (size >= 0){
            ans.add(list.get(size));
            size--;
        }
        return ans;
    }

    @Override
    public MyNode center() {
        if (!isConnected()) {
            return null;
        }
        double shortest_dist = Double.MAX_VALUE;
        MyNode ans = null;
        Iterator<MyNode> iter = graph.nodeIter();
        while (iter.hasNext()) {
            MyNode n = iter.next();
            Dijkstra(n);
            Iterator<MyNode> iter2 = graph.nodeIter();
            double max_dist = -1;
            while (iter2.hasNext()) {
                MyNode v = iter2.next();
                if (v.getDistance() > max_dist) {
                    max_dist = v.getDistance();
                }
            }
            if (max_dist < shortest_dist) {
                shortest_dist = max_dist;
                ans = n;
            }
        }
        return ans;
    }

    @Override
    public List<MyNode> tsp(List<MyNode> cities) {
        Iterator<MyNode> node_iter = graph.nodeIter();
        while (node_iter.hasNext()) {
            node_iter.next().setTag(White);
        }
        List<MyNode> ans = new LinkedList<>();
        List<MyNode> citiescopy = new LinkedList<>(cities);
        while (!citiescopy.isEmpty()) {
            if (citiescopy.size() == 1) {
                ans.add(citiescopy.get(0));
                break;
            }
            citiescopy.removeIf(intersection -> intersection.getTag() == Black);
            MyNode n1 = citiescopy.remove(0);
            if (citiescopy.size() == 0) {
                break;
            }
            MyNode n2 = citiescopy.get(0);
            List<MyNode> path = shortestPath(n1.getKey(), n2.getKey());
            for (MyNode intersection : path) {
                if (ans.contains(intersection) && intersection != n2) {
                    intersection.setTag(Black);
                }
                if (intersection != n2) {
                    ans.add(intersection);
                }
            }
        }
        return ans;
    }

    @Override
    public boolean save(String file) {
        JsonObject JsonGraph = new JsonObject();
        JsonArray nodesArr = new JsonArray();
        JsonArray edgesArr = new JsonArray();
        Iterator<MyNode> iter = graph.nodeIter();
        while (iter.hasNext()) {
            MyNode n = iter.next();
            int key = n.getKey();
            JsonObject JsonNode = new JsonObject();
            JsonNode.addProperty("pos", n.getLocation().x()+","+n.getLocation().y()+","+n.getLocation().z());
            JsonNode.addProperty("id", key);
            nodesArr.add(JsonNode);
            Iterator<MyEdge> edgeIter = graph.edgeIter(key);
            while (edgeIter.hasNext()) {
                MyEdge e = edgeIter.next();
                if (e.getSrc() == key) {
                    JsonObject JsonEdge = new JsonObject();
                    JsonEdge.addProperty("src", e.getSrc());
                    JsonEdge.addProperty("w", e.getWeight());
                    JsonEdge.addProperty("dest", e.getDest());
                    edgesArr.add(JsonEdge);
                }
            }
        }
        JsonGraph.add("Edges", edgesArr);
        JsonGraph.add("Nodes", nodesArr);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(String.valueOf(JsonGraph));
            fw.flush();
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        try {
            FileReader fr = new FileReader(file);
            JsonElement file_elem = JsonParser.parseReader(fr);
            JsonObject object = file_elem.getAsJsonObject();
            JsonArray nodes = object.getAsJsonArray("Nodes");
            JsonArray edges = object.getAsJsonArray("Edges");
            DirectedWeightedGraphImpl G = new DirectedWeightedGraphImpl();
            for (JsonElement elem : nodes) {
                JsonObject obj = elem.getAsJsonObject();
                String st = obj.get("pos").getAsString();
                String[] split = st.split(",");
                GeoLocation location = new GeoLocationImpl(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                MyNode n = new MyNode(obj.get("id").getAsInt(), location);
                G.addNode(n);
            }
            for (JsonElement elem : edges) {
                JsonObject obj = elem.getAsJsonObject();
                int src,dest;
                double w;
                src = obj.get("src").getAsInt();
                dest = obj.get("dest").getAsInt();
                w = obj.get("w").getAsDouble();
                G.connect(src, dest, w);
            }
            init(G);
        } catch (FileNotFoundException E) {
            E.printStackTrace();
            return false;
        }
        return true;
    }

    private void Dijkstra(MyNode src_node) {
        List<MyNode> queue = new LinkedList<>();
        Iterator<MyNode> iter = graph.nodeIter();
        while(iter.hasNext()){
            MyNode n = iter.next();
            n.setPrev(null);
            n.setDistance(Integer.MAX_VALUE);
            queue.add(n);
        }
        src_node.setDistance(0);
        while (queue.size() != 0) {
            int key = getMinDistNodeKey(queue);
            if(key == -1) {
                break;
            }
            MyNode u = graph.getNode(key);
            queue.remove(u);
            Iterator<MyEdge> EdgeIter = graph.edgeIter(u.getKey());
            while (EdgeIter.hasNext()) {
                MyEdge e = EdgeIter.next();
                if (e.getSrc() == u.getKey()) {
                    MyNode v = graph.getNode(e.getDest());
                    double dis_from_src = u.getDistance() + e.getWeight();
                    if(dis_from_src < v.getDistance()){
                      v.setDistance(dis_from_src);
                      v.setPrev(u);
                    }
                }
            }
        }
    }

    private int getMinDistNodeKey(List<MyNode> L) {
        int ans = -1;
        double dist = Integer.MAX_VALUE;
        for (MyNode nodeData : L) {
            if (nodeData.getDistance() < dist) {
                dist = nodeData.getDistance();
                ans = nodeData.getKey();
            }
        }
        return ans;
    }

    private void BFS(MyNode s) {
        Iterator<MyNode> node_iter = graph.nodeIter();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            n.setDistance(Integer.MAX_VALUE);
            n.setTag(White);
        }
        s.setTag(Grey);
        s.setDistance(0);
        List<NodeData> queue = new LinkedList<>();
        queue.add(s);
        while (queue.size() > 0) {
            MyNode u = (MyNode) queue.remove(0);
            Iterator<MyEdge> edge_iter = graph.edgeIter(u.getKey());
            while (edge_iter.hasNext()) {
                MyEdge e = edge_iter.next();
                if (e.getSrc() == u.getKey()) {
                    MyNode v = graph.getNode(e.getDest());
                    if (v.getTag() == White) {
                        v.setTag(Grey);
                        v.setDistance(u.getDistance()+1);
                        queue.add(v);
                    }
                }
            }
            u.setTag(Black);
        }
    }

    public void DFS() {
        MyNode u;
        this.Time = 0;
        Iterator<MyNode> node_iter = this.graph.nodeIter();
        while (node_iter.hasNext()) {
            u = node_iter.next();
            u.setPrev(null);
            u.setTag(White);
        }
        node_iter = this.graph.nodeIter();
        while (node_iter.hasNext()) {
            u = node_iter.next();
            if (u.getTag() == White) {
                DFS_Visit(u);
            }
        }
    }

    private void DFS_Visit(MyNode u) {
        u.setTag(Grey);
        Time += 1;
        u.setDiscovery_time(Time);
        for(MyEdge E: u.getEdges().values()) {
            MyNode v = (E.getSrc() == u.getKey()) ? graph.getNode(E.getDest()) : graph.getNode(E.getSrc());
            if (v.getTag() == White) {
                v.setPrev(u);
                DFS_Visit(v);
            }
        }
        u.setTag(Black);
        Time += 1;
        u.setFinish_time(Time);
    }

    public void init_random_graph(int nodes_amount) {
        DirectedWeightedGraphImpl G = new DirectedWeightedGraphImpl();
        for (int i=0; i<nodes_amount; i++) {
            G.addNode(new MyNode(i, new GeoLocationImpl(0,0,0)));
        }
        Iterator<MyNode> node_iter = G.get_map().values().iterator();
        while (node_iter.hasNext()) {
            MyNode n = node_iter.next();
            for (int i=0; i<10; i++) {
                G.connect(n.getKey(), (int) (Math.random()*nodes_amount),0);
            }
        }
        this.graph = G;
    }
}
