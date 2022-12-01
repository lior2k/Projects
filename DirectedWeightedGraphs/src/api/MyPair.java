package api;

import java.util.Objects;

public class MyPair {
    private int left;
    private int right;

    public MyPair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft(){
        return this.left;
    }

    public int getRight(){
        return this.right;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MyPair) {
            return ((MyPair) o).left == this.left && ((MyPair) o).right == this.right;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(this.left) + Objects.hashCode(this.right);
    }

    public String toString() {
        return "("+this.left+","+this.right+")";
    }

}
