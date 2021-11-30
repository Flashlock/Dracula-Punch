package dracula_punch.TiledMap;

import dracula_punch.Camera.*;
import jig.Vector;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class DPTiledMap extends TiledMap {

  public final boolean[][] isPassable;
  public final Coordinate playerSpawnCoordinate;
  public int tilesInWindowX;
  public int tilesInWindowY;

  public DPTiledMap(String ref) throws SlickException {
    super(ref);
    isPassable = new boolean[getWidth()][getHeight()];
    playerSpawnCoordinate = new Coordinate();
    tilesInWindowX = getWidth();
    tilesInWindowY = getHeight();
    extractMapData();
  }

  private void extractMapData() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        if (getTileId(x, y, getLayerIndex("PlayerSpawn")) != 0) {
          playerSpawnCoordinate.x = x;
          playerSpawnCoordinate.y = y;
        }
        if (getTileId(x, y, getLayerIndex("Collision")) == 0) {
          isPassable[x][y] = true;
        }
      }
    }
  }

  public void renderLayersBehindObjects(Vector cameraPosition) {
    int camX = (int) cameraPosition.getX();
    int camY = (int) cameraPosition.getY();
    render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 0, true);  // render floor
  }

  public void renderLayersAboveObjects(Vector cameraPosition) {
    int camX = (int) cameraPosition.getX();
    int camY = (int) cameraPosition.getY();
    render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 1, true);
    render(camX, camY, 0, 0, tilesInWindowX, tilesInWindowY, 2, true);
  }

  /**
   * Use this function to render characters
   * @param visualY I'm not sure yet, currently it's always 0.
   *                Maybe this will change when the screen begins scrolling.
   * @param mapY y coordinate of the row being rendered.
   * @param layer the layer being rendered.
   */
  @Override
  protected void renderedLine(int visualY, int mapY, int layer) {
    super.renderedLine(visualY, mapY, layer);
  }
}
