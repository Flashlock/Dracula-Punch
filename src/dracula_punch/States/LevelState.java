package dracula_punch.States;

import dracula_punch.Actions.Action;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.Characters.Players.PlayerController;
import dracula_punch.DraculaPunchGame;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;

import java.util.ArrayList;

public abstract class LevelState extends BasicGameState {
  public DPTiledMap map;
  public Camera camera;
  public PlayerController playerOne, playerTwo, playerThree;

  protected final ArrayList<GameObject> gameObjects = new ArrayList<>();
  public final ArrayList<GameObject> deadObjects = new ArrayList<>();
  public final ArrayList<GameObject> newObjects = new ArrayList<>();

  public final ArrayList<PlayerController> playerObjects = new ArrayList<>();
  public final ArrayList<PlayerController> deadPlayers = new ArrayList<>();

  public ArrayList<Action> move1Event = new ArrayList<>();
  public ArrayList<Action> move2Event = new ArrayList<>();
  public ArrayList<Action> move3Event = new ArrayList<>();

  public ArrayList<Action> attack1Event = new ArrayList<>();
  public ArrayList<Action> attack2Event = new ArrayList<>();
  public ArrayList<Action> attack3Event = new ArrayList<>();

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
    // check lose state
    if(playerObjects.isEmpty()){
      stateBasedGame.enterState(DraculaPunchGame.LOSE_STATE);
    }

    // Remove dead objects
    gameObjects.removeAll(deadObjects);
    deadObjects.clear();

    // add new objects
    gameObjects.addAll(newObjects);
    newObjects.clear();
  }

  public ArrayList<GameObject> getObjectsFromTile(Coordinate tile){
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    for(GameObject gameObject : this.gameObjects){
      if(gameObject.currentTile.isEqual(tile)) {
        gameObjects.add(gameObject);
      }
    }
    return gameObjects;
  }

  public ArrayList<GameObject> getObjectsFromTile(int x, int y){
    ArrayList<GameObject> gameObjects = new ArrayList<>();
    for(GameObject gameObject : this.gameObjects){
      if(gameObject.currentTile.isEqual(x, y)) {
        gameObjects.add(gameObject);
      }
    }
    return gameObjects;
  }

  public Vector getAvgPlayerPos(){
    if(playerObjects.isEmpty()){
      return null;
    }
    Vector avgPos = new Vector(0, 0);
    for (GameObject player : playerObjects) {
      avgPos = avgPos.add(new Vector(player.currentTile.x, player.currentTile.y));
    }
    avgPos = avgPos.scale(1f / playerObjects.size());
    return avgPos;
  }
}
