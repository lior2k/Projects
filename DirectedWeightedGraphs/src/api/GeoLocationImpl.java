package api;

public class GeoLocationImpl implements GeoLocation{
    private final double x;
    private final double y;
    private final double z;

    public GeoLocationImpl(double x,double y,double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    public GeoLocationImpl copy(){
        return new GeoLocationImpl(this.x,this.y,this.z);
    }

    @Override
    public double distance(GeoLocation g) {
        double dx = g.x() - this.x;
        double dy = g.y() - this.y;
        double dz = g.z() - this.z;
        return Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
    }

    public String toString() {
        return x+","+y+","+z;
    }
}
