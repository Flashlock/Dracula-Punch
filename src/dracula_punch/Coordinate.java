package dracula_punch;

public class Coordinate {
  public int x;
  public int y;

  public Coordinate() {
    this.x = 0;
    this.y = 0;
  }

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public boolean isEqual(Coordinate toCompare) { return x == toCompare.x && y == toCompare.y; }


  public boolean isEqual(int x, int y) {
    return x == this.x && y == this.y;
  }
}
