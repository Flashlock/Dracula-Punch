package dracula_punch.States;

import dracula_punch.Actions.*;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.*;
import dracula_punch.TestEnemy;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;

import java.util.ArrayList;

public class TestLevelState extends LevelState {
  private ArrayList<GameObject> gameObjects;
  private GameObject playerOne, playerTwo, playerThree;
  private boolean isSpaceDown;

  @Override
  public int getID() {
    return DraculaPunchGame.TEST_STATE;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
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
    playerObjects.add((CharacterController)playerOne);
    playerObjects.add((CharacterController)playerTwo);
    playerObjects.add((CharacterController)playerThree);
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Test State", 10, 25);
    graphics.scale(camera.zoomFactor, camera.zoomFactor);
    map.renderLayersBehindObjects(camera.getCamPosition());
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
  }

  /**
   * Handles game input
   * @param input
   */
  private void controls(Input input){
    boolean spaceDown = input.isKeyDown(Input.KEY_SPACE);
    if(spaceDown && !isSpaceDown){
      // attack event
      for(Action action : inputAttackEvent){
        action.Execute();
      }
    }
    isSpaceDown = spaceDown;

    boolean left, right, up, down;
    // Observe input changes
    left = input.isKeyDown(Input.KEY_A);
    right = input.isKeyDown(Input.KEY_D);
    up = input.isKeyDown(Input.KEY_W);
    down = input.isKeyDown(Input.KEY_S);
    movePlayer((CharacterController)playerOne, left, right, up, down);

    left = input.isKeyDown(Input.KEY_J);
    right = input.isKeyDown(Input.KEY_L);
    up = input.isKeyDown(Input.KEY_I);
    down = input.isKeyDown(Input.KEY_K);
    movePlayer((CharacterController)playerTwo, left, right, up, down);

    left = input.isKeyDown(Input.KEY_LEFT);
    right = input.isKeyDown(Input.KEY_RIGHT);
    up = input.isKeyDown(Input.KEY_UP);
    down = input.isKeyDown(Input.KEY_DOWN);
    movePlayer((CharacterController)playerThree, left, right, up, down);
  }

  private void movePlayer(CharacterController player, boolean left, boolean right, boolean up, boolean down) {
    boolean isMoving = player.moveLeft || player.moveRight || player.moveDown || player.moveUp;

    // Trigger event on change
    if(!player.moveLeft && left){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(-1, 0));
      }
    }
    else if(!player.moveRight && right){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(1, 0));
      }
    }
    else if(!player.moveUp && up){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(0, 1));
      }
    }
    else if(!player.moveDown && down){
      // change down
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(0, -1));
      }
    }
    else if(isMoving && !(left || right || up || down)){
      for(Action action : inputMoveEvent){
        action.Execute(new Vector(0, 0));
      }
    }

    player.moveLeft = left;
    player.moveRight = right;
    player.moveUp = up;
    player.moveDown = down;
  }
}
