package dracula_punch.Characters;

import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

/**
 * All Characters - including enemies - will inherit from this class
 */
public abstract class CharacterController extends GameObject {
  public boolean moveUp, moveDown, moveLeft, moveRight, isLocallyControlled;
  protected Animation curAnim;
  protected float scaleFactor;
  protected int xRenderOffset, yRenderOffset;
  protected LevelState curLevelState;
  protected Vector facingDir;

  private Image idleImage;
  protected Coordinate previousTile = new Coordinate();
  public Coordinate currentTilePlusPartial = new Coordinate();
  protected float TOTAL_MOVE_TIME = 100;
  protected float movingTime = 99; // one less than total to trigger calculation once on startup
  protected float percentMoveDone;

  private boolean inputLock;
  public boolean getInputLock(){ return inputLock; }

  public CharacterController(final float x, final float y, LevelState curLevelState){
    super(x, y);
    this.curLevelState = curLevelState;
    currentTile = new Coordinate(curLevelState.map.playerSpawnCoordinate);

    // Generate a random idle sprite
    int dirX, dirY;
    double randX = Math.random() * 1.5f;
    // X direction
    if(randX < .5f){
      dirX = -1;
    }
    else if(randX < 1){
      dirX = 0;
    }
    else{
      dirX = 1;
    }
    // Y direction
    if(dirX != 0){
      dirY = 0;
    }
    else{
      dirY = Math.random() < .5f ? -1 : 1;
    }

    facingDir = new Vector(dirX, dirY);
    idleImage = getIdleSprite();
    addImage(idleImage);
  }
  public CharacterController(final float x, final float y, LevelState curLevelState, boolean lameWorkaround){
    super(x, y);
    this.curLevelState = curLevelState;
    currentTile = new Coordinate(curLevelState.map.playerSpawnCoordinate);

  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics){
//        Vector screenOffset = curLevelState.getScreenOffset();
//        float x = screenOffset.getX() - 5 + xRenderOffset;
//        // If idle pose, no current animation
//        float y = curAnim == null ?
//                screenOffset.getY() - 5 - getIdleHeight() / 2f * scaleFactor + yRenderOffset
//                : screenOffset.getY() - 5 - curAnim.getHeight() / 2f * scaleFactor + yRenderOffset;

    Camera cam = curLevelState.camera;
    setPosition(cam.getScreenPositionFromTile(currentTilePlusPartial));
    if(cam.isInScreenRange(currentTile)) {
      render(graphics);
    }
  }


  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    if (movingTime == TOTAL_MOVE_TIME) {
      moveByPlayerControl();
    } else {
      smoothlyCatchUpToNewPosition(delta);
    }
    if(inputLock){
      inputLock = !curAnim.isStopped();
    }
  }

  private void moveByPlayerControl() {
    int dx = 0;
    int dy = 0;
    if (moveLeft && currentTile.x > 0) {
      dx = -1;
    }
    if (moveRight && currentTile.x < curLevelState.map.getWidth() - 1) {
      dx = 1;
    }
    if (moveUp && currentTile.y > 0) {
      dy = -1;
    }
    if (moveDown && currentTile.y <  curLevelState.map.getHeight() - 1) {
      dy = 1;
    }
    boolean nextTileIsChosen = dx!=0 || dy!=0;
    boolean nextTileIsPassable = curLevelState.map.isPassable[(int)currentTile.x + dx][(int)currentTile.y + dy];
    if (nextTileIsChosen && nextTileIsPassable) {
      movingTime = 0;
      previousTile.setEqual(currentTile);
      currentTile.add(dx, dy);
      //System.out.println("Player Moved " + currentTile.x + " " + currentTile.y);
    }
  }

  private void smoothlyCatchUpToNewPosition(int delta) {
    movingTime += delta;
    if(movingTime > TOTAL_MOVE_TIME) {
      movingTime = TOTAL_MOVE_TIME;
    }

    percentMoveDone = (TOTAL_MOVE_TIME - movingTime) / TOTAL_MOVE_TIME;
    float partialX = 0, partialY = 0;
    if (previousTile.x > currentTile.x) {
      partialX = percentMoveDone;
    }
    else if (previousTile.x < currentTile.x) {
      partialX = -percentMoveDone;
    }
    if (previousTile.y > currentTile.y) {
      partialY = percentMoveDone;
    }
    else if (previousTile.y < currentTile.y) {
      partialY = -percentMoveDone;
    }
    currentTilePlusPartial.setEqual(currentTile);
    currentTilePlusPartial.add(partialX, partialY);
  }

  /**
   * Animate the controller's movement
   * @param direction The direction to move
   */
  public void animateMove(Vector direction){
    int x = (int) direction.getX();
    int y = (int) direction.getY();

    String sheet = getRunSheet(x, y);

    if(sheet != null){
      if(curAnim == null){
        // If there's no animation, then we have an idle image to remove
        removeImage(idleImage);
      }
      else{
        removeAnimation(curAnim);
      }

      // Set the new animation
      curAnim = new Animation(
          ResourceManager.getSpriteSheet(
              sheet, getRunWidth(), getRunHeight()
          ),
          DraculaPunchGame.ANIMATION_DURATION
      );
      curAnim.setLooping(true);
      addAnimation(curAnim);

      facingDir = direction;
    }
    else if(curAnim != null){
      removeAnimation(curAnim);
      curAnim = null;

      idleImage = getIdleSprite();
      addImage(idleImage);
    }
  }

  /**
   * Use only for player characters
   * */
  public void animateAttack(){
    String sheet;
    int width, height;
    inputLock = true;
    if(this instanceof AustinController){
      sheet = getMeleeSheet();
      width = getMeleeWidth();
      height = getMeleeHeight();
    }
    else{
      sheet = getRangedSheet();
      width = getRangedWidth();
      height = getRangedHeight();
    }

    if(sheet != null){
      if(curAnim == null){
        // If there's no animation, then we have an idle image to remove
        removeImage(idleImage);
      }
      else{
        removeAnimation(curAnim);
      }

      // Set the new animation
      curAnim = new Animation(
              ResourceManager.getSpriteSheet(
                      sheet, width, height
              ),
              DraculaPunchGame.ANIMATION_DURATION
      );
      curAnim.setLooping(false);
      addAnimation(curAnim);
    }
    else if(curAnim != null){
      removeAnimation(curAnim);
      curAnim = null;

      idleImage = getIdleSprite();
      addImage(idleImage);
    }
  }

  private Image getIdleSprite(){
    int x = (int) facingDir.getX();
    int y = (int) facingDir.getY();

    if(y == 1 && x == 0){
      // 0
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
          .getSprite(0, 0);
    }
    else if(y == -1 && x == 0){
      // 180
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
          .getSprite(2, 0);
    }
    else if(y == 0 && x == -1){
      // 90
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
          .getSprite(1, 0);
    }
    else if(y == 0 && x == 1){
      // 270
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), getIdleWidth(), getIdleHeight())
          .getSprite(3, 0);
    }
    else{
      System.out.println("Unknown Facing Direction: " + x + ", " + y);
      return null;
    }
  }

  /**
   * @return The character's Run Sprite Sheet for the given direction
   * @param x facing direction x
   * @param y facing direction y
   */
  public abstract String getRunSheet(int x, int y);

  /**
   * @return The width of each sprite in the character's run animation
   */
  public abstract int getRunWidth();

  /**
   * @return The height of each sprite in the character's run animation
   */
  public abstract int getRunHeight();

  /**
   * @return The character's Idle Sprite Sheet - currently no animation
   */
  public abstract String getIdleSheet();

  /**
   * @return The width of each sprite in the character's idle animation
   */
  public abstract int getIdleWidth();

  /**
   * @return The height of each sprite in the character's idle animation
   */
  public abstract int getIdleHeight();

  /**
   * @return Sprite sheet for melee attack
   */
  public abstract String getMeleeSheet();

  /**
   * @return Width of each sprite in the melee sprite sheet
   */
  public abstract int getMeleeWidth();

  /**
   * @return Height of each sprite in the melee sprite sheet
   */
  public abstract int getMeleeHeight();

  /**
   * @return Sprite sheet for ranged attack
   */
  public abstract String getRangedSheet();

  /**
   * @return Width of each sprite in the ranged sprite sheet
   */
  public abstract int getRangedWidth();

  /**
   * @return Height of each sprite in the ranged sprite sheet
   */
  public abstract int getRangedHeight();
}
