package dracula_punch.Camera;

import jig.Vector;
import org.newdawn.slick.tiled.TiledMap;

import java.io.Serializable;

public class Coordinate implements Serializable {
  public float x;
  public float y;

  public Coordinate() {
    this.x = 0;
    this.y = 0;
  }

  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Coordinate(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Coordinate(Coordinate in) {
    this.x = in.x;
    this.y = in.y;
  }

  public boolean isEqual(Coordinate toCompare) { return x == toCompare.x && y == toCompare.y; }

  public boolean isEqual(int x, int y) {
    return x == this.x && y == this.y;
  }

  public void setEqual(Coordinate toMatch) {
    this.x = toMatch.x;
    this.y = toMatch.y;
  }

  public void add(float x, float y) {
    this.x += x;
    this.y += y;
  }

  public Vector getIsometricFromTile(TiledMap map) {
    Coordinate cartesian = new Coordinate();
    cartesian.x = -x * map.getTileWidth() / 2;
    cartesian.y = -y * map.getTileHeight();
    float isometricX = cartesian.x - cartesian.y - map.getTileWidth() / 2f;
    float isometricY = (cartesian.x + cartesian.y) / 2 - map.getTileHeight() / 2f;
    return new Vector(isometricX, isometricY);
  }

}

