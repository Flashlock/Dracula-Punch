package dracula_punch.Characters.Players;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Damage_System.IAttacker;
import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public abstract class PlayerController extends CharacterController implements IAttacker {
  public PlayerController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    currentTile = new Coordinate(curLevelState.map.playerSpawnCoordinate);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    super.update(gameContainer, stateBasedGame, delta);
    if(getAnimLock()) return;
    moveByPlayerControl();
    smoothlyCatchUpToCurrentTile(delta);
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
    if (movingTime == TOTAL_MOVE_TIME && nextTileIsChosen && nextTileIsPassable) {
      changeCurrentTile(dx, dy);
      //System.out.println("Player Moved " + currentTile.x + " " + currentTile.y);
    }
  }
}
