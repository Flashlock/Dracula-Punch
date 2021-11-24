package dracula_punch.Camera;

import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.DraculaPunchGame;
import dracula_punch.Networking.Client;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Camera extends GameObject {
  public float zoomFactor = 1.0f;
  public Vector isometric = new Vector(0,0);
  public boolean moveUp, moveDown, moveLeft, moveRight, zoomIn, zoomOut;
  private float previousZoom = 1.0f;
  private float currentZoom = 0.5f;     // changed from 1.0 to 0.5f for our new tiledmap!
  private final float MINIMUM_ZOOM = 0.1f;  // changed from 0.5 to 0.2f for our new tiledmap!
  private final float MAXIMUM_ZOOM = 0.5f;
  private final float ZOOM_INCREMENT_SIZE = 0.05f;
  public Coordinate currentTile = new Coordinate();
  private Coordinate previousTile = new Coordinate();
  private final float TOTAL_MOVE_TIME = 200;
  private float movingTime = 99; // one less than total to trigger calculation once on startup
  private float percentMoveDone;
  private DPTiledMap map;

  public Camera(DPTiledMap map) {
    super(0,0);
    this.map = map;
    currentTile.setEqual(map.playerSpawnCoordinate);
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) { }

  public void centerOn(ArrayList<CharacterController> players) {
    // 800 x 600 Screen
    // 256 x 128 Tiles
    float highTileX, lowTileX, highTileY, lowTileY, highestX, lowestX, highestY, lowestY;
    highTileX = highTileY = highestX = highestY = Float.MIN_VALUE;
    lowTileX = lowTileY = lowestX = lowestY = Float.MAX_VALUE;
    for (CharacterController player : players) {
      if (player.currentTile.x > highTileX) { highTileX = player.currentTile.x; }
      if (player.currentTile.x < lowTileX) { lowTileX = player.currentTile.x; }
      if (player.currentTile.y > highTileY) { highTileY = player.currentTile.y; }
      if (player.currentTile.y < lowTileY) { lowTileY = player.currentTile.y; }
      if (player.getX() > highestX) { highestX = player.getX(); }
      if (player.getX() < lowestX) { lowestX = player.getX(); }
      if (player.getY() > highestY) { highestY = player.getY(); }
      if (player.getY() < lowestY) { lowestY = player.getY(); }
    }
    float displayWidth = DraculaPunchGame.SCREEN_WIDTH / currentZoom;
    float displayHeight = DraculaPunchGame.SCREEN_HEIGHT / currentZoom;
    float pixelsFromEdgesOfScreenToInitiateZoomOut = 50;
    float pixelsFromEdgesOfScreenToInitiateZoomIn = 200;
    float zoomOutBuffer = pixelsFromEdgesOfScreenToInitiateZoomOut / currentZoom;
    float zoomInBuffer = pixelsFromEdgesOfScreenToInitiateZoomIn / currentZoom;
    zoomOut =
        highestX > displayWidth - zoomOutBuffer
            || highestY > displayHeight - zoomOutBuffer
            || lowestX < zoomOutBuffer
            || lowestY < zoomOutBuffer;
    zoomIn =
        highestX < displayWidth - zoomInBuffer
            && highestY < displayHeight - zoomInBuffer
            && lowestX > zoomInBuffer
            && lowestY > zoomInBuffer;
    float centerTileX = (highTileX - lowTileX) / 2 + lowTileX;
    float centerTileY = (highTileY - lowTileY) / 2 + lowTileY;
    moveLeft = currentTile.x > (int)centerTileX;
    moveRight = currentTile.x < (int)centerTileX;
    moveUp = currentTile.y > (int)centerTileY;
    moveDown = currentTile.y < (int)centerTileY;
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
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
    //if ((dx!=0 || dy!=0 || dz!=0) && map.isPassable[(int)currentTile.x + dx][(int)currentTile.y + dy]) {
    if (dx!=0 || dy!=0 || dz!=0) {
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

  public Vector getScreenPositionFromTile(Coordinate tile) {
    Vector isometric = tile.getIsometricFromTile(map);
    float x = getCamPosition().getX() - isometric.getX();
    float y = getCamPosition().getY() - isometric.getY();
    return new Vector(x,y);
  }

  public Vector getCamPosition(){
    Vector screenOffset = getScreenOffset();
    return new Vector(
        isometric.getX() + screenOffset.getX(),
        isometric.getY() + screenOffset.getY()
    );
  }

  public Vector getScreenOffset(){
    return new Vector(
        DraculaPunchGame.SCREEN_WIDTH / zoomFactor / 2,
        DraculaPunchGame.SCREEN_HEIGHT / zoomFactor / 2
    );
  }
  public boolean isInScreenRange(Coordinate currentTile){
    return true;
  }
}
