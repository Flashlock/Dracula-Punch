package dracula_punch.Camera;

import org.newdawn.slick.tiled.TiledMap;

public class Camera {
  public float zoomFactor = 1.0f;
  public Coordinate isometric = new Coordinate();
  public boolean moveUp, moveDown, moveLeft, moveRight, zoomIn, zoomOut;
  private float previousZoom = 1.0f;
  private float currentZoom = 1.0f;
  private Coordinate currentTile = new Coordinate();
  private Coordinate previousTile = new Coordinate();
  private float totalMoveTime = 100;
  private float movingTime = 99; // one less than total to trigger calculation once on startup
  private float percentMoveDone;
  private TiledMap map;

  public Camera(TiledMap map) {
    this.map = map;
  }

  public void update(int delta) {
    if (movingTime == totalMoveTime) { initializeCameraMovement(); }
    else { calculateCameraPosition(delta); }
  }

  private void initializeCameraMovement() {
    int dx = 0;
    int dy = 0;
    float dz = 0f;
    if (moveLeft && currentTile.x > 0) { dx = -1; }
    if (moveRight && currentTile.x < map.getWidth() - 1) { dx = 1; }
    if (moveUp && currentTile.y > 0) { dy = -1; }
    if (moveDown && currentTile.y < map.getHeight() - 1) { dy = 1; }
    if (zoomOut) { dz = -0.2f; }
    if (zoomIn) { dz = 0.2f; }
    if (dx!=0 || dy!=0 || dz!=0) { moveCamera(dx, dy, dz); }
  }

  private void moveCamera(int dx, int dy, float dz) {
    movingTime = 0;
    previousTile.setEqual(currentTile);
    previousZoom = currentZoom;
    currentTile.add(dx, dy);
    currentZoom += dz;
    // System.out.println(currentTile.x + " " + currentTile.y + " " + currentZoom);
  }

  private void calculateCameraPosition(int delta) {
    movingTime += delta;
    if(movingTime > totalMoveTime) { movingTime = totalMoveTime; }
    percentMoveDone = (totalMoveTime - movingTime) / totalMoveTime;
    float partialX = 0, partialY = 0, partialZ = 0;
    if (previousTile.x > currentTile.x) { partialX = percentMoveDone; }
    else if (previousTile.x < currentTile.x) { partialX = -percentMoveDone; }
    if (previousTile.y > currentTile.y) { partialY = percentMoveDone; }
    else if (previousTile.y < currentTile.y) { partialY = -percentMoveDone; }
    if (previousZoom > currentZoom) { partialZ = 0.2f * percentMoveDone; }
    else if (previousZoom < currentZoom) { partialZ = 0.2f * -percentMoveDone; }
    Coordinate currentTilePlusPartial = new Coordinate(currentTile);
    currentTilePlusPartial.add(partialX, partialY);
    isometric = currentTilePlusPartial.getIsometricFromTile(map);
    zoomFactor = currentZoom + partialZ;
  }
}
