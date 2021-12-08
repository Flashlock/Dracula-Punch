package dracula_punch.Pathfinding;

import dracula_punch.Camera.Coordinate;

public class DijkstraNode implements Comparable<DijkstraNode>{
  public int x;
  public int y;
  public Coordinate coord;
  public int distance = Integer.MAX_VALUE;
  public DijkstraNode nextInPath = null;
  public boolean visited;
  public boolean isPassable;

  public DijkstraNode(int x, int y) {
    this.x = x;
    this.y = y;
    this.coord = new Coordinate(x, y);
  }

  public DijkstraNode(Coordinate coord) {
    this.x = (int)coord.x;
    this.y = (int)coord.y;
    this.coord = coord;
  }

  @Override
  public int compareTo(DijkstraNode o) {
    if(o.distance < distance) { return 1; }
    else return -1;
  }
}
