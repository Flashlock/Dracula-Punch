package dracula_punch.Characters;

import dracula_punch.Actions.Damage_System.AttackAction;
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
  protected Vector facingDir;
  protected Image idleImage;
  protected LevelState curLevelState;

  protected AttackAction attackAction;
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
    currentTile = new Coordinate(curLevelState.map.playerSpawnCoordinate);
    TOTAL_MOVE_TIME = 100;
    movingTime = 99; // one less than total to trigger calculation once on startup

    this.curLevelState = curLevelState;

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
    if(animLock){
      // is this the animation frame where the action is triggered?
      int frame = curAnim.getFrame();
      if(attackAction != null && frame == attackAction.getFrameActionIndex() && !attackAction.actionTriggered){
        attackAction.Execute();
      }

      // if the animation is over, remove lock and reset action trigger
      animLock = frame != curAnim.getFrameCount() - 1;
      if(!animLock && attackAction != null){
        attackAction.actionTriggered = false;
      }
    }
  }

  //region Damage System
  @Override
  public void takeDamage(int damage) {
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
              sheet, DraculaPunchGame.SPRITE_SIZE, DraculaPunchGame.SPRITE_SIZE
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
   */
  public void animateAttack(String sheet){
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
              sheet, DraculaPunchGame.SPRITE_SIZE, DraculaPunchGame.SPRITE_SIZE
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
          .getSpriteSheet(getIdleSheet(), DraculaPunchGame.SPRITE_SIZE, DraculaPunchGame.SPRITE_SIZE)
          .getSprite(0, 0);
    }
    else if(y == -1 && x == 0){
      // 180
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), DraculaPunchGame.SPRITE_SIZE, DraculaPunchGame.SPRITE_SIZE)
          .getSprite(2, 0);
    }
    else if(y == 0 && x == -1){
      // 90
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), DraculaPunchGame.SPRITE_SIZE, DraculaPunchGame.SPRITE_SIZE)
          .getSprite(1, 0);
    }
    else if(y == 0 && x == 1){
      // 270
      return ResourceManager
          .getSpriteSheet(getIdleSheet(), DraculaPunchGame.SPRITE_SIZE, DraculaPunchGame.SPRITE_SIZE)
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
   * @return The character's Idle Sprite Sheet - currently no animation
   */
  public abstract String getIdleSheet();

  /**
   * @return Sprite sheet for melee attack
   */
  public abstract String getMeleeSheet();

  /**
   * @return Sprite sheet for ranged attack
   */
  public abstract String getRangedSheet();

}
