package dracula_punch.States;

import dracula_punch.Actions.*;
import dracula_punch.Actions.Input.InputAttackAction;
import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.*;
import dracula_punch.TestEnemy;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestLevelState extends LevelState {
  private GameObject playerOne, playerTwo, playerThree;
  private boolean isSpaceDown, isEDown, isUDown;

  private Boolean hasGKey;
  private Boolean hasSKey;
  // Object IDs
  private int GOLDKEY_ID = 33;
  private int SILVKEY_ID = 34;
  private int BLANK_ID = 63;

  @Override
  public int getID() {
    return DraculaPunchGame.TEST_STATE;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    hasGKey = false;
    hasSKey = false;
    gameObjects = new ArrayList<>();
    playerObjects = new ArrayList<>();

    map = new DPTiledMap(DraculaPunchGame.MAP);
    camera = new Camera(map, playerObjects);
    gameObjects.add(camera);

    temporaryPlayerSelectionMethod();
    GameObject testEnemy = new TestEnemy(new Coordinate(90,90), this);
    gameObjects.add(testEnemy);
  }

  private void temporaryPlayerSelectionMethod() {
    playerOne = new AmandaController(
        DraculaPunchGame.SCREEN_WIDTH / 3f,
        DraculaPunchGame.SCREEN_HEIGHT / 3f,
        this
    );
    playerTwo = new AustinController(
        DraculaPunchGame.SCREEN_WIDTH / 3f,
        DraculaPunchGame.SCREEN_HEIGHT / 3f,
        this
    );
    playerThree = new RittaController(
        DraculaPunchGame.SCREEN_WIDTH / 3f,
        DraculaPunchGame.SCREEN_HEIGHT / 3f,
        this
    );
    gameObjects.add(playerOne);
    gameObjects.add(playerTwo);
    gameObjects.add(playerThree);

    CharacterController c1 = (CharacterController) playerOne;
    CharacterController c2 = (CharacterController) playerTwo;
    CharacterController c3 = (CharacterController) playerThree;

    playerObjects.add(c1);
    playerObjects.add(c2);
    playerObjects.add(c3);

    move1Event.add(new InputMoveAction(c1));
    move2Event.add(new InputMoveAction(c2));
    move3Event.add(new InputMoveAction(c3));

    attack1Event.add(new InputAttackAction(c1));
    attack2Event.add(new InputAttackAction(c2));
    attack3Event.add(new InputAttackAction(c3));
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Test State", 10, 25);
    graphics.scale(camera.zoomFactor, camera.zoomFactor);
    map.renderLayersBehindObjects(camera.getCamPosition());
    // this is where we render our characters
    for(GameObject gameObject : gameObjects){
      gameObject.render(gameContainer, stateBasedGame, graphics);
    }
    map.renderLayersAboveObjects(camera.getCamPosition());
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    controls(gameContainer.getInput());

    for(GameObject gameObject : gameObjects){
      gameObject.update(gameContainer, stateBasedGame, delta);
    }
    checkHasKey();
    if(hasGKey){
      openDoors();
      hasGKey = false;
    }

    // Remove dead objects
    gameObjects.removeAll(deadObjects);
    deadObjects.clear();

    // add new objects
    gameObjects.addAll(newObjects);
    newObjects.clear();
  }

  private void checkHasKey(){
    for(CharacterController p : playerObjects){
      if(map.getTileId((int)p.currentTile.x, (int)p.currentTile.y, map.getLayerIndex("Object")) == GOLDKEY_ID){
        System.out.println(p.getName() + " took the gold key");
        hasGKey = true;
        map.setTileId((int)p.currentTile.x, (int)p.currentTile.y, map.getLayerIndex("Object"), BLANK_ID);
      } else if(map.getTileId((int)p.currentTile.x, (int)p.currentTile.y, map.getLayerIndex("Object")) == SILVKEY_ID){
        System.out.println(p.getName() + " took the silver key");
        hasSKey = true;
        map.setTileId((int)p.currentTile.x, (int)p.currentTile.y, map.getLayerIndex("Object"), BLANK_ID);
      }
    }
  }

  private void openDoors(){
    // x: 55, y:16-19
    for(int i = 16; i <=19; i++){
      map.setTileId(55, i, map.getLayerIndex("SW Walls"), 63);
      map.isPassable[55][i] = true;
    }
  }

  /**
   * Handles game input
   * @param input
   */
  private void controls(Input input){
    // Check for Player 1 attack
    boolean eDown = input.isKeyDown(Input.KEY_E);
    if(eDown && !isEDown){
      for(Action action : attack1Event){
        action.Execute();
      }
    }
    isEDown = eDown;

    // Check for Player 2 attack
    boolean uDown = input.isKeyDown(Input.KEY_U);
    if(uDown && !isUDown){
      for(Action action : attack2Event){
        action.Execute();
      }
    }
    isUDown = uDown;

    // Check for Player 3 attack
    boolean spaceDown = input.isKeyDown(Input.KEY_SPACE);
    if(spaceDown && !isSpaceDown){
      for(Action action : attack3Event){
        action.Execute();
      }
    }
    isSpaceDown = spaceDown;

    // Observe movement input changes
    boolean left, right, up, down;
    left = input.isKeyDown(Input.KEY_A);
    right = input.isKeyDown(Input.KEY_D);
    up = input.isKeyDown(Input.KEY_W);
    down = input.isKeyDown(Input.KEY_S);
    movePlayer((CharacterController)playerOne, move1Event, left, right, up, down);

    left = input.isKeyDown(Input.KEY_J);
    right = input.isKeyDown(Input.KEY_L);
    up = input.isKeyDown(Input.KEY_I);
    down = input.isKeyDown(Input.KEY_K);
    movePlayer((CharacterController)playerTwo, move2Event, left, right, up, down);

    left = input.isKeyDown(Input.KEY_LEFT);
    right = input.isKeyDown(Input.KEY_RIGHT);
    up = input.isKeyDown(Input.KEY_UP);
    down = input.isKeyDown(Input.KEY_DOWN);
    movePlayer((CharacterController)playerThree, move3Event, left, right, up, down);
  }

  private void movePlayer(CharacterController player, ArrayList<Action> moveEvent, boolean left, boolean right, boolean up, boolean down) {
    boolean isMoving = player.moveLeft || player.moveRight || player.moveDown || player.moveUp;

    // Trigger event on change
    if(!player.moveLeft && left){
      for(Action action : moveEvent){
        action.Execute(new Vector(-1, 0));
      }
    }
    else if(!player.moveRight && right){
      for(Action action : moveEvent){
        action.Execute(new Vector(1, 0));
      }
    }
    else if(!player.moveUp && up){
      for(Action action : moveEvent){
        action.Execute(new Vector(0, 1));
      }
    }
    else if(!player.moveDown && down){
      // change down
      for(Action action : moveEvent){
        action.Execute(new Vector(0, -1));
      }
    }
    else if(isMoving && !(left || right || up || down)){
      for(Action action : moveEvent){
        action.Execute(new Vector(0, 0));
      }
    }

    player.moveLeft = left;
    player.moveRight = right;
    player.moveUp = up;
    player.moveDown = down;
  }
}
