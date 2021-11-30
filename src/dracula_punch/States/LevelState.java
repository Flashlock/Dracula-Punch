package dracula_punch.States;

import dracula_punch.Actions.Action;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.CharacterController;
import dracula_punch.Characters.GameObject;
import dracula_punch.DraculaPunchGame;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.tiled.TiledMap;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class LevelState extends BasicGameState {
  public DPTiledMap map;
  public Camera camera;
  public ArrayList<CharacterController> playerObjects;
  protected ArrayList<GameObject> gameObjects;
  public ArrayList<GameObject> deadObjects;

  public ArrayList<Action> move1Event = new ArrayList<>();
  public ArrayList<Action> move2Event = new ArrayList<>();
  public ArrayList<Action> move3Event = new ArrayList<>();

  public ArrayList<Action> attack1Event = new ArrayList<>();
  public ArrayList<Action> attack2Event = new ArrayList<>();
  public ArrayList<Action> attack3Event = new ArrayList<>();

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

}
