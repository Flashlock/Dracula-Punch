package dracula_punch.States;

import dracula_punch.Actions.Action;
import dracula_punch.Camera.Camera;
import dracula_punch.Characters.CharacterController;
import dracula_punch.DraculaPunchGame;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public abstract class LevelState extends BasicGameState {
  public DPTiledMap map;
  public Camera camera;
  public ArrayList<CharacterController> playerObjects;

  public ArrayList<Action> inputMoveEvent = new ArrayList<>();

}
