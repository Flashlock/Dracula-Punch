package dracula_punch.Camera;

import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.DraculaPunchGame;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public class Camera extends GameObject {
  public float zoomFactor = 1.0f;
  private Vector isometricPosition = new Vector(0,0);
  private boolean zoomIn, zoomOut;
  private float previousZoom = 0.5f;
  private float currentZoom = 0.5f;
  private final float MINIMUM_ZOOM = 0.1f;
  private final float MAXIMUM_ZOOM = 0.5f;
  private float zoomIncrementSize;
  private final float TOTAL_ZOOM_TIME = 100;
  private float zoomingTime = 99; // one less than total to trigger calculation once on startup
  private DPTiledMap map;
  private ArrayList<CharacterController> playerObjects;

  public Camera(DPTiledMap map, ArrayList<CharacterController> playerObjects) {
    super(0,0);
    this.map = map;
    this.playerObjects = playerObjects;
  }

  public boolean isInScreenRange(Coordinate currentTile){
    return true;
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
        isometricPosition.getX() + screenOffset.getX(),
        isometricPosition.getY() + screenOffset.getY()
    );
  }

  private Vector getScreenOffset(){
    return new Vector(
        DraculaPunchGame.SCREEN_WIDTH / zoomFactor / 2,
        DraculaPunchGame.SCREEN_HEIGHT / zoomFactor / 2
    );
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) { }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    centerCameraOnPlayers();
    zoomCamera(delta);
  }

  private void centerCameraOnPlayers() {
    float highTileX, lowTileX, highTileY, lowTileY;
    highTileX = highTileY = Float.MIN_VALUE;
    lowTileX = lowTileY = Float.MAX_VALUE;
    for (CharacterController player : playerObjects) {
      if (player.currentTilePlusPartial.x > highTileX) { highTileX = player.currentTilePlusPartial.x; }
      if (player.currentTilePlusPartial.x < lowTileX) { lowTileX = player.currentTilePlusPartial.x; }
      if (player.currentTilePlusPartial.y > highTileY) { highTileY = player.currentTilePlusPartial.y; }
      if (player.currentTilePlusPartial.y < lowTileY) { lowTileY = player.currentTilePlusPartial.y; }
    }
    float centerTileX = (highTileX - lowTileX) / 2 + lowTileX;
    float centerTileY = (highTileY - lowTileY) / 2 + lowTileY;
    Coordinate centerTile = new Coordinate(centerTileX,centerTileY);
    isometricPosition = centerTile.getIsometricFromTile(map);
  }

  private void zoomCamera(int delta) {
    determineZoomTarget();
    if (zoomingTime == TOTAL_ZOOM_TIME) { initializeZoomMovement(); }
    smoothlyAdjustZoom(delta);
  }

  private void determineZoomTarget() {
    float highestX, lowestX, highestY, lowestY;
    highestX = highestY = Float.MIN_VALUE;
    lowestX = lowestY = Float.MAX_VALUE;
    for (CharacterController player : playerObjects) {
      if (player.getX() > highestX) { highestX = player.getX(); }
      if (player.getX() < lowestX) { lowestX = player.getX(); }
      if (player.getY() > highestY) { highestY = player.getY(); }
      if (player.getY() < lowestY) { lowestY = player.getY(); }
    }
    float displayWidth = DraculaPunchGame.SCREEN_WIDTH / currentZoom;
    float displayHeight = DraculaPunchGame.SCREEN_HEIGHT / currentZoom;
    float pixelsFromEdgesOfScreenToInitiateZoomOut = 100;
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
  }

  private void initializeZoomMovement() {
    if (currentZoom < 0.25f) { zoomIncrementSize =  currentZoom / 40; }
    if (currentZoom > 0.25f) { zoomIncrementSize =  currentZoom / 20; }
    float dz = 0f;
    if (zoomOut) { dz = -zoomIncrementSize; }
    if (zoomIn) { dz = zoomIncrementSize; }
    if (dz!=0) {
      zoomingTime = 0;
      previousZoom = currentZoom;
      if ((currentZoom > MINIMUM_ZOOM && dz < 0) || (currentZoom < MAXIMUM_ZOOM && dz > 0)) { currentZoom += dz; }
      if (currentZoom < MINIMUM_ZOOM) { currentZoom = MINIMUM_ZOOM; }
      if (currentZoom > MAXIMUM_ZOOM) { currentZoom = MAXIMUM_ZOOM; }
    }
  }

  private void smoothlyAdjustZoom(int delta) {
    zoomingTime += delta;
    if(zoomingTime > TOTAL_ZOOM_TIME) { zoomingTime = TOTAL_ZOOM_TIME; }
    float percentZoomDone = (TOTAL_ZOOM_TIME - zoomingTime) / TOTAL_ZOOM_TIME;
    float partialZ = 0;
    if (previousZoom > currentZoom) { partialZ = zoomIncrementSize * percentZoomDone; }
    else if (previousZoom < currentZoom) { partialZ = zoomIncrementSize * -percentZoomDone; }
    zoomFactor = currentZoom + partialZ;
  }
}
