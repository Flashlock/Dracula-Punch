package dracula_punch.Camera;

import dracula_punch.TiledMap.DPTiledMap;

public class Camera {
  public float zoomFactor = 1.0f;
  public Coordinate isometric = new Coordinate();
  public boolean moveUp, moveDown, moveLeft, moveRight, zoomIn, zoomOut;
  private float previousZoom = 1.0f;
  private float currentZoom = 0.5f;     // changed from 1.0 to 0.5f for our new tiledmap!
  private final float MINIMUM_ZOOM = 0.2f;  // changed from 0.5 to 0.2f for our new tiledmap!
  private final float MAXIMUM_ZOOM = 3f;
  private final float ZOOM_INCREMENT_SIZE = 0.2f;
  private Coordinate currentTile = new Coordinate();
  private Coordinate previousTile = new Coordinate();
  private final float TOTAL_MOVE_TIME = 100;
  private float movingTime = 99; // one less than total to trigger calculation once on startup
  private float percentMoveDone;
  private DPTiledMap map;

  public Camera(DPTiledMap map) {
    this.map = map;
    currentTile.setEqual(map.playerSpawnCoordinate);
  }

  public void update(int delta) {
    if (movingTime == TOTAL_MOVE_TIME) { initializeCameraMovement(); }
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
    if (zoomOut) { dz = -ZOOM_INCREMENT_SIZE; }
    if (zoomIn) { dz = ZOOM_INCREMENT_SIZE; }
    if ((dx!=0 || dy!=0 || dz!=0) && map.isPassable[(int)currentTile.x + dx][(int)currentTile.y + dy]) {
      moveCamera(dx, dy, dz);
    }
  }

  private void moveCamera(int dx, int dy, float dz) {
    movingTime = 0;
    previousTile.setEqual(currentTile);
    previousZoom = currentZoom;
    currentTile.add(dx, dy);
    if ((currentZoom > MINIMUM_ZOOM && dz < 0) || (currentZoom < MAXIMUM_ZOOM && dz > 0)) { currentZoom += dz; }
    if (currentZoom < MINIMUM_ZOOM) { currentZoom = MINIMUM_ZOOM; }
    if (currentZoom > MAXIMUM_ZOOM) { currentZoom = MAXIMUM_ZOOM; }
    //System.out.println(currentTile.x + " " + currentTile.y + " " + currentZoom);
  }

  private void calculateCameraPosition(int delta) {
    movingTime += delta;
    if(movingTime > TOTAL_MOVE_TIME) { movingTime = TOTAL_MOVE_TIME; }
    percentMoveDone = (TOTAL_MOVE_TIME - movingTime) / TOTAL_MOVE_TIME;
    float partialX = 0, partialY = 0, partialZ = 0;
    if (previousTile.x > currentTile.x) { partialX = percentMoveDone; }
    else if (previousTile.x < currentTile.x) { partialX = -percentMoveDone; }
    if (previousTile.y > currentTile.y) { partialY = percentMoveDone; }
    else if (previousTile.y < currentTile.y) { partialY = -percentMoveDone; }
    if (previousZoom > currentZoom) { partialZ = ZOOM_INCREMENT_SIZE * percentMoveDone; }
    else if (previousZoom < currentZoom) { partialZ = ZOOM_INCREMENT_SIZE * -percentMoveDone; }
    Coordinate currentTilePlusPartial = new Coordinate(currentTile);
    currentTilePlusPartial.add(partialX, partialY);
    isometric = currentTilePlusPartial.getIsometricFromTile(map);
    zoomFactor = currentZoom + partialZ;
  }
}
