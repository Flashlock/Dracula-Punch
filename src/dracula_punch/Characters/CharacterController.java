package dracula_punch.Characters;

import dracula_punch.Actions.Action;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Damage_System.IDamageable;
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
public abstract class CharacterController extends GameObject implements IDamageable {
  public boolean moveUp, moveDown, moveLeft, moveRight;
  protected Animation curAnim;
  protected float scaleFactor;
  protected int xRenderOffset, yRenderOffset;
  protected LevelState curLevelState;
  protected Vector facingDir;

  protected Image idleImage;
  protected Coordinate previousTile = new Coordinate();
  public Coordinate currentTilePlusPartial = new Coordinate();
  protected float TOTAL_MOVE_TIME = 100;
  protected float movingTime = 99; // one less than total to trigger calculation once on startup
  protected float percentMoveDone;

  protected Action finishMeleeAction;

  private boolean animLock;
  public boolean getAnimLock(){ return animLock; }

  protected int maxHealth;
  @Override
  public int getMaxHealth(){ return maxHealth; }
  protected int currentHealth;
  @Override
  public int getCurrentHealth(){ return currentHealth; }

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

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics){
    Camera cam = curLevelState.camera;
    setPosition(cam.getScreenPositionFromTile(currentTilePlusPartial));
    if(cam.isInScreenRange(currentTile)) {
      render(graphics);
    }
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    /*
    Animation unlocks when attacks are completed
    Melee attacks deal damage at the end of the animation
     */
    if(animLock){
      animLock = !curAnim.isStopped();
      if(!animLock && finishMeleeAction != null){
        finishMeleeAction.Execute();
      }
    }
  }

  //region Damage System
  @Override
  public void takeDamage(int damage) {
    System.out.println("Taking Damage");
    currentHealth -= damage;
    if(currentHealth <= 0){
      curLevelState.deadObjects.add(this);
    }
  }

  @Override
  public void heal(int health) {
    currentHealth += health;
    if(currentHealth > maxHealth){
      currentHealth = maxHealth;
    }
  }
  //endregion

  /**
   * Helper function for determining sprite sheet from facing direction
   * @param up Upwards sprite sheet
   * @param down Downwards sprite sheet
   * @param left Leftwards sprite sheet
   * @param right Rightwards sprite sheet
   * @return The given sheet correlating to the facing direction
   */
  protected String getSheetHelper(String up, String down, String left, String right){
    int x = (int) facingDir.getX();
    int y = (int) facingDir.getY();

    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = right;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = left;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = up;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = down;
    }
    else if(x == 0 && y == 0){
      // stop - do nothing for now. No idle pose/anim
    }
    else{
      System.out.println("Invalid Direction: Unable to Animate");
    }
    return sheet;
  }

  /**
   * Determine sprite sheet for new facing direction
   * @param x The x direction to face
   * @param y The y direction to face
   * @return The given sheet correlating to the facing direction
   */
  protected String getSheetHelper(String up, String down, String left, String right, int x, int y){
    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = right;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = left;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = up;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = down;
    }
    else if(x == 0 && y == 0){
      // stop - do nothing for now. No idle pose/anim
    }
    else{
      System.out.println("Invalid Direction: Unable to Animate");
    }
    return sheet;
  }


  /**
   * Animate the controller's movement
   * @param direction The direction to move
   */
  public void animateMove(Vector direction){
    String sheet = getRunSheet((int) direction.getX(), (int) direction.getY());

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
   * Animate the controller's attack
   * @param sheet Sprite sheet for animation
   * @param width Width of each sprite
   * @param height Height of each sprite
   */
  public void animateAttack(String sheet, int width, int height){
    // Put a lock on animation to wait until attack completes
    animLock = true;

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

  /**
   * @return Idle image in the facing direction
   */
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
   * @param x The x direction we wish to face
   * @param y The y direction we wish to face
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
