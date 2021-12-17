package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.GameObject;
import dracula_punch.Damage_System.IDamageable;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

public abstract class Projectile extends GameObject {
  protected float moveSpeed;
  protected final LevelState curLevelState;

  protected final Vector direction;

  public Projectile(float x, float y, Coordinate curTile, LevelState curLevelState, Vector direction) {
    super(x, y);
    currentTile.setEqual(curTile);
    previousTile.setEqual(curTile);
    this.direction = direction;
    this.curLevelState = curLevelState;
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    move();
    smoothlyCatchUpToCurrentTile(delta);
    checkCollision();
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
    Camera cam = curLevelState.camera;
    setPosition(cam.getScreenPositionFromTile(currentTilePlusPartial));
    if(cam.isInScreenRange(getPosition())) {
      render(graphics);
    }
  }

  private void move() {
    if (movingTime == TOTAL_MOVE_TIME) {
      changeCurrentTile((int)direction.getX(), (int)-direction.getY());
    }
  }

  private void checkCollision(){
    if(currentTile.x < 0 || currentTile.y < 0){
      curLevelState.deadObjects.add(this);
      return;
    }
    // collision with map
    if(!curLevelState.map.isPassable[(int) currentTile.x][(int) currentTile.y]){
      curLevelState.deadObjects.add(this);
      return;
    }

    // collision with other objects
    ArrayList<GameObject> gameObjects = curLevelState.getObjectsFromTile(currentTile);
    if(!gameObjects.isEmpty()){
      collide(gameObjects);
    }
  }

  /**
   * There was a collision on the current tile with these objects.
   * @param collisions The objects collided with.
   */
  protected abstract void collide(ArrayList<GameObject> collisions);

  /**
   * Animate the projectile with the given sheet
   * @param sheet The Sprite Sheet to animate from
   */
  protected void animate(String sheet){
    Animation animation = new Animation(
        ResourceManager.getSpriteSheet(
            sheet,
            DraculaPunchGame.SPRITE_SIZE,
            DraculaPunchGame.SPRITE_SIZE
        ),
        DraculaPunchGame.ANIMATION_DURATION
    );
    animation.setLooping(true);
    addAnimation(animation);
  }
}
