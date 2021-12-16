package dracula_punch.Pathfinding;

import dracula_punch.Camera.Coordinate;
import dracula_punch.TiledMap.DPTiledMap;

import java.util.ArrayList;
import java.util.Collections;

public class DijkstraGraph {

    private final DijkstraNode[][] graph;
    public int getGraphWidth(){ return graph.length; }
    public int getGraphHeight(){ return graph[0].length; }

    public DijkstraGraph(DPTiledMap tiledMap){
        graph = new DijkstraNode[tiledMap.isPassable.length][tiledMap.isPassable[0].length];
        for(int i = 0; i < graph.length; i++){
            for(int j = 0; j < graph[i].length; j++){
                graph[i][j] = new DijkstraNode(i, j, tiledMap.isPassable[i][j]);
            }
        }
    }

    /**
     * @param startX Starting x value
     * @param startY Starting y value
     * @param targetX Target's x value
     * @param targetY Target's y value
     * @return The shortest path from start to target
     */
    public ArrayList<DijkstraNode> findPath(int startX, int startY, int targetX, int targetY) {
        runDijkstra(startX, startY);

        ArrayList<DijkstraNode> path = new ArrayList<>();
        DijkstraNode curNode = graph[targetX][targetY];

        while(curNode != null) {
            path.add(curNode);
            curNode = curNode.prevNode;
        }
        Collections.reverse(path);

        clearGraph();
        return path;
    }

    /**
     * @param startTile Starting tile
     * @param targetTile Target tile
     * @return The shortest path from start to target
     */
    public ArrayList<DijkstraNode> findPath(Coordinate startTile, Coordinate targetTile){
        return findPath(
                (int) startTile.x,
                (int) startTile.y,
                (int) targetTile.x,
                (int) targetTile.y
        );
    }

    /**
     * Run Dijkstra's from the starting position
     * @param x starting x value
     * @param y starting y value
     */
    private void runDijkstra(int x, int y){
        DijkstraNode startNode = graph[x][y];
        startNode.distance = 0;
        runDijkstraHelper(startNode);
    }

    /**
     * Recurse through all nodes generating the shortest paths
     * @param curNode Current node to examine
     */
    private void runDijkstraHelper(DijkstraNode curNode){
        int posX = curNode.x + 1;
        int negX = curNode.x - 1;
        int posY = curNode.y + 1;
        int negY = curNode.y - 1;

        DijkstraNode node;
        if(posX < graph[0].length){
            node = graph[posX][curNode.y];
            if(node.compareTo(curNode)){
                runDijkstraHelper(node);
            }
        }
        if(negX > -1){
            node = graph[negX][curNode.y];
            if(node.compareTo(curNode)){
                runDijkstraHelper(node);
            }
        }
        if(posY < graph.length){
            node = graph[curNode.x][posY];
            if(node.compareTo(curNode)){
                runDijkstraHelper(node);
            }
        }
        if(negY > -1){
            node = graph[curNode.x][negY];
            if(node.compareTo(curNode)){
                runDijkstraHelper(node);
            }
        }
    }

    /**
     * Reset the graph
     */
    public void clearGraph(){
        for(DijkstraNode[] nodes : graph){
            for(DijkstraNode node : nodes){
                node.clearNode();
            }
        }
    }
}
