package api;
import java.util.HashMap;
import java.util.Map;

public class MyNode implements NodeData{

    private final int key;
    private GeoLocationImpl location;
    private double weight;
    private String info;
    private int tag;
    private HashMap<MyPair,EdgeData> edges;
    private int discovery_time;
    private int finish_time;
    private double distance;
    private MyNode prev;

    public MyNode(int id, GeoLocation loc) {
        this.key = id;
        this.tag = 0xFFFFFF;
        this.location = (GeoLocationImpl) loc;
        this.weight = 0;
        this.edges = new HashMap<>();
        this.discovery_time = 0;
        this.finish_time = 0;
        this.distance = 0;
        this.prev = null;
        this.info = "key: "+this.key+" location: "+this.location;
    }

    public int getDiscovery_time(){
        return this.discovery_time;
    }

    public int getFinish_time(){
        return this.finish_time;
    }

    public void setDiscovery_time(int s){
        this.discovery_time = s;
    }

    public void setFinish_time(int f){
        this.finish_time = f;
    }

    public double getDistance(){
        return this.distance;
    }

    public MyNode getPrev(){
        return this.prev;
    }

    public void setDistance(double d){
        this.distance = d;
    }

    public void setPrev(MyNode n){
        this.prev = n;
    }

    public HashMap<MyPair, EdgeData> getEdges() {
        return this.edges;
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public GeoLocation getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(GeoLocation p) {
        this.location = (GeoLocationImpl) p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public MyNode copy(){
        MyNode n = new MyNode(this.key, this.location.copy());
        n.tag = this.tag;
        n.weight = this.weight;
        n.info = this.info;
        n.discovery_time = this.discovery_time;
        n.finish_time = this.finish_time;
        n.prev = this.prev;
        n.distance = this.distance;
        for (EdgeData E : this.edges.values()){
            MyEdge edge = ((MyEdge) E).copy();
            n.edges.put(edge.getPair(), edge);
        }
        return n;
    }

    public void addEdge(MyEdge e){
        edges.put(e.getPair(), e);
    }

    public MyEdge removeEdge(MyPair p) {
        return (MyEdge) this.edges.remove(p);
    }

    public String toString() {
        return "key: "+this.key;
    }
}
