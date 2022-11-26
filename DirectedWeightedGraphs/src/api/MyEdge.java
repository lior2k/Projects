package api;

public class MyEdge implements EdgeData {

    private final double weight;
    private String info;
    private int tag;
    private final MyPair pair;

    public MyEdge(int src, int dest, double w){
        this.pair = new MyPair(src,dest);
        this.weight = w;
        this.tag = 0x8E23A4;
        this.info = "from: "+src+" to: "+dest+" weight: "+w+" tag: "+tag;
    }
    public MyPair getPair(){
        return this.pair;
    }

    @Override
    public int getSrc() {
        return this.pair.getLeft();
    }

    @Override
    public int getDest() {
        return this.pair.getRight();
    }

    @Override
    public double getWeight() {
        return this.weight;
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

    public MyEdge copy(){
        return new MyEdge(this.getSrc(),this.getDest(),this.weight);
    }

    public String toString(){
        return "src: " +this.getSrc()+ " dest: " +this.getDest()+ " weight: " +this.weight;
    }
}
