package api;

public class MyEdge implements EdgeData {

    private final double weight;
    private int tag;
    private final MyPair pair;

    public MyEdge(int src, int dest, double w){
        this.pair = new MyPair(src,dest);
        this.weight = w;
        this.tag = 0x8E23A4;
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

    public void reverse() {
        int temp = this.pair.getLeft();
        this.pair.setLeft(this.pair.getRight());
        this.pair.setRight(temp);
    }
}
