package dracula_punch.Pathfinding;

import dracula_punch.Camera.Coordinate;

public class DijkstraNode {
    public final boolean isPassable;
    public final int x, y;
    public final Coordinate coordinate;

    int distance = Integer.MAX_VALUE;
    DijkstraNode prevNode;

    public DijkstraNode(int x, int y, boolean isPassable){
        this.isPassable = isPassable;
        this.x = x;
        this.y = y;
        coordinate = new Coordinate(x, y);
    }

    /**
     * Reset node values
     */
    public void clearNode(){
        distance = Integer.MAX_VALUE;
        prevNode = null;
    }

    /**
     * @param node The node to compare myself to
     * @return True if node is my new previous node
     */
    public boolean compareTo(DijkstraNode node){
        int nextDistance = node.distance + 1;
        if(isPassable && nextDistance < distance){
            distance = nextDistance;
            prevNode = node;
            return true;
        }
        return false;
    }
}
