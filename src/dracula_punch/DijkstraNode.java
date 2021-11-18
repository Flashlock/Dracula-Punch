package dracula_punch;

import dracula_punch.Camera.Coordinate;

public class DijkstraNode implements Comparable<DijkstraNode>{
  int x;
  int y;
  Coordinate coord;
  int distance = Integer.MAX_VALUE;
  DijkstraNode nextInPath = null;
  boolean visited;
  boolean isPassable;

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
