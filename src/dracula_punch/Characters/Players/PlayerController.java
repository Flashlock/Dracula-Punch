package dracula_punch.Characters.Players;

import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Damage_System.IAttacker;
import dracula_punch.DraculaPunchGame;
import dracula_punch.Pathfinding.DijkstraGraph;
import dracula_punch.States.LevelState;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;

public abstract class PlayerController extends CharacterController implements IAttacker {
  private boolean isDead;
  public boolean getIsDead(){ return isDead; }
  private final int respawnTime = 15000;
  private int respawnClock;
  private final DijkstraGraph respawnGraph;

  public PlayerController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    currentTile = new Coordinate(curLevelState.map.playerSpawnCoordinate);
    respawnGraph = new DijkstraGraph(curLevelState.map);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) {
    super.update(gameContainer, stateBasedGame, delta);
    if(getAnimLock()) return;
    if(isDead){
      respawnClock += delta;
      if(respawnClock > respawnTime){
        respawn();
      }
    }
    else {
      moveByPlayerControl();
      smoothlyCatchUpToCurrentTile(delta);
    }
  }

  @Override
  public void takeDamage(int damage) {
    removeImage(healthBar);
    currentHealth -= damage;
    if(currentHealth <= 0){
      curLevelState.playerObjects.remove(this);
      curLevelState.deadPlayers.add(this);
      animLock = false;
      if(curAnim != null){
        removeAnimation(curAnim);
      }
      if(idleImage != null){
        removeImage(idleImage);
      }
      currentTile = new Coordinate(-1,-1);
      isDead = true;
    }
    else{
      setHealthBar();
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
    if (movingTime == TOTAL_MOVE_TIME && nextTileIsChosen && nextTileIsPassable) {
      changeCurrentTile(dx, dy);
      //System.out.println("Player Moved " + currentTile.x + " " + currentTile.y);
    }
  }

  protected void respawn(){
    Coordinate respawn = respawnGraph.playerRespawn(curLevelState);
    currentTile = respawn;
    currentTilePlusPartial = respawn;
    respawnClock = 0;
    currentHealth = maxHealth;
    setHealthBar();
    isDead = false;
    curLevelState.deadPlayers.remove(this);
    curLevelState.playerObjects.add(this);
//    randomIdle();
  }
}
