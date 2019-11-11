package fr.insa.colisvif.view;

public class CanvasNode {

    public double x;

    public double y;

    private long nodeId;

    public CanvasNode(long nodeId, double x, double y) {
        this.nodeId = nodeId;
        this.x = x;
        this.y = y;
    }

    public long getNodeId() {
        return this.nodeId;
    }

    public void setCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean intersects(double x, double y) {
        double squaredDistance = (x - this.x) + (y - this.y);
        return squaredDistance <= CanvasConstants.DELIVERY_NODE_SQUARED_RADIUS;
    }

    @Override
    public String toString() {
        return "CanvasNode{" + "nodeId=" + nodeId + ", x=" + x + ", y=" + y + '}';
    }
}
