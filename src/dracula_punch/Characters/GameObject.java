package dracula_punch.Characters;

import dracula_punch.Camera.Coordinate;
import dracula_punch.States.LevelState;
import jig.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Inherit from this instead of Entity.
 * This way we can allow our entities to update and render themselves.
 */
public abstract class GameObject extends Entity {
  public Coordinate currentTile = new Coordinate();
  protected Coordinate previousTile = new Coordinate();
  public Coordinate currentTilePlusPartial = new Coordinate();
  protected float TOTAL_MOVE_TIME;
  protected float movingTime;

  public GameObject(final float x, final float y){
    super(x, y);
  }

  /**
   * Update the controller each frame
   * @param gameContainer
   * @param stateBasedGame
   * @param delta
   */
  public abstract void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta);

  /**
   * Render the controller each frame
   * @param gameContainer
   * @param stateBasedGame
   * @param graphics
   */
  public abstract void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics);

  protected void changeCurrentTile(int dx, int dy) {
    movingTime = 0;
    previousTile.setEqual(currentTile);
    currentTile.add(dx, dy);
  }

  protected void smoothlyCatchUpToCurrentTile(int delta) {
    movingTime += delta;
    if(movingTime > TOTAL_MOVE_TIME) {
      movingTime = TOTAL_MOVE_TIME;
    }
    float percentMoveDone = calculatePercentMoved();
    float partialX = 0, partialY = 0;
    if (previousTile.y > currentTile.y){ partialY = percentMoveDone;}
    else if (previousTile.y < currentTile.y){ partialY = -percentMoveDone;}
    if (previousTile.x > currentTile.x){ partialX = percentMoveDone;}
    else if (previousTile.x < currentTile.x){ partialX = -percentMoveDone;}
    currentTilePlusPartial = new Coordinate(currentTile);
    currentTilePlusPartial.add(partialX, partialY);
  }

  /**
   * @return Percent moved across current tile
   */
  public float calculatePercentMoved(){
    return (TOTAL_MOVE_TIME - movingTime) / TOTAL_MOVE_TIME;
  }
}
