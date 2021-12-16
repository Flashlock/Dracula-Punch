package dracula_punch.States;

import dracula_punch.Actions.*;
import dracula_punch.Actions.Input.InputAttackAction;
import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.*;
import dracula_punch.Characters.Enemies.BatController;
import dracula_punch.Characters.Enemies.GargoyleController;
import dracula_punch.Characters.Enemies.SwarmManager;
import dracula_punch.Characters.Players.AmandaController;
import dracula_punch.Characters.Players.AustinController;
import dracula_punch.Characters.Players.RittaController;
import dracula_punch.TiledMap.DPTiledMap;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;
import org.newdawn.slick.state.transition.EmptyTransition;

import java.util.ArrayList;

public class TestLevelState extends LevelState {
  private GameObject playerOne, playerTwo, playerThree;
  private boolean isSpaceDown, isEDown, isUDown;
  private boolean[][] pressedButtons = {{false, false, false, false}, {false, false, false, false}, {false, false, false, false}};
  private int[] mostRecentlyPressedButton = {0, 0, 0};
  private final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
  private final int P1 = 0, P2 = 1, P3 = 2;

  private int timer = 400;

  private Boolean hasGKey;
  private Boolean hasSKey;
  // Object IDs
  private int GOLDKEY_ID = 33;
  private int SILVKEY_ID = 34;
  private int BLANK_ID = 63;

  private int WINSTATE_ID = 9;
  private int LOSESTATE_ID = 8;

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

    createCharacters();
    Coordinate enemyStart = new Coordinate(40, 15);
    SwarmManager swarm = new SwarmManager(40, 15, 5, this);
    GameObject testEnemy = new BatController(enemyStart, this, swarm);

    gameObjects.add(swarm);
    gameObjects.add(testEnemy);

    ResourceManager.getImage(DraculaPunchGame.WIN_SCREEN);
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

  private void createCharacters() {
    if (DraculaPunchGame.characterChoice[0] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      playerOne = initCharacterControllers(0);
      gameObjects.add(playerOne);
      CharacterController c1 = (CharacterController) playerOne;
      playerObjects.add(c1);
      move1Event.add(new InputMoveAction(c1));
      attack1Event.add(new InputAttackAction(c1));
    }
    if (DraculaPunchGame.characterChoice[1] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      playerTwo = initCharacterControllers(1);
      gameObjects.add(playerTwo);
      CharacterController c2 = (CharacterController) playerTwo;
      playerObjects.add(c2);
      move2Event.add(new InputMoveAction(c2));
      attack2Event.add(new InputAttackAction(c2));
    }
    if (DraculaPunchGame.characterChoice[2] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      playerThree = initCharacterControllers(2);
      gameObjects.add(playerThree);
      CharacterController c3 = (CharacterController) playerThree;
      playerObjects.add(c3);
      move3Event.add(new InputMoveAction(c3));
      attack3Event.add(new InputAttackAction(c3));
    }
  }

  private GameObject initCharacterControllers(int player) {
    return switch (DraculaPunchGame.characterChoice[player]) {
      case AUSTIN -> new AustinController(0,0, this);
      case AMANDA -> new AmandaController(0, 0, this);
      case RITTA, UNCHOSEN -> new RittaController(0,0, this);
    };
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

    // check win state
    for(CharacterController player : playerObjects){
      if(map.getTileId((int) player.currentTile.x, (int) player.currentTile.y, map.getLayerIndex("placement")) == WINSTATE_ID){
        // since transitions are wonky here, we create a timer to account for the transition
        timer -= delta;
        if (timer <= 0) {
          stateBasedGame.enterState(DraculaPunchGame.WIN_STATE, new EmptyTransition(), new EmptyTransition());
          timer = 400;
        }
      }
      // check lose state
      else if(map.getTileId((int) player.currentTile.x, (int)player.currentTile.y, map.getLayerIndex("placement")) == LOSESTATE_ID){
        timer -= delta;
        if (timer <= 0) {
          stateBasedGame.enterState(DraculaPunchGame.LOSE_STATE, new EmptyTransition(), new EmptyTransition());
          timer = 400;
        }
      }
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
        hasGKey = true;
        map.setTileId((int)p.currentTile.x, (int)p.currentTile.y, map.getLayerIndex("Object"), BLANK_ID);
      } else if(map.getTileId((int)p.currentTile.x, (int)p.currentTile.y, map.getLayerIndex("Object")) == SILVKEY_ID){
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
    boolean eDown = getAttackButton(input, P1);
    if(eDown && !isEDown){
      for(Action action : attack1Event){
        action.Execute();
      }
    }
    isEDown = eDown;

    // Check for Player 2 attack
    boolean uDown = getAttackButton(input, P2);
    if(uDown && !isUDown){
      for(Action action : attack2Event){
        action.Execute();
      }
    }
    isUDown = uDown;

    // Check for Player 3 attack
    boolean spaceDown = getAttackButton(input, P3);
    if(spaceDown && !isSpaceDown){
      for(Action action : attack3Event){
        action.Execute();
      }
    }
    isSpaceDown = spaceDown;

    // Observe movement input changes
    movePlayer(input, P1, (CharacterController)playerOne, move1Event);
    movePlayer(input, P2, (CharacterController)playerTwo, move2Event);
    movePlayer(input, P3, (CharacterController)playerThree, move3Event);
  }

  private boolean getAttackButton(Input input, int player) {
    return switch (DraculaPunchGame.inputSource[player]) {
      case DraculaPunchGame.KB_WASD -> input.isKeyDown(Input.KEY_E);
      case DraculaPunchGame.KB_IJKL -> input.isKeyDown(Input.KEY_U);
      case DraculaPunchGame.KB_ARROWS -> input.isKeyDown(Input.KEY_SPACE);
      default -> (anyButtonOtherThanDPadPressed(input, player));
    };
  }

  private boolean anyButtonOtherThanDPadPressed(Input input, int player) {
    for (int i = 0; i < 50; i++)
      if (i != DraculaPunchGame.PS5_CONTROLLER_LEFT_BUTTON
          && i != DraculaPunchGame.PS5_CONTROLLER_RIGHT_BUTTON
          && i != DraculaPunchGame.PS5_CONTROLLER_UP_BUTTON
          && i != DraculaPunchGame.PS5_CONTROLLER_DOWN_BUTTON
          && input.isControlPressed(i, DraculaPunchGame.inputSource[player]))
        return true;
    return false;
  }

  private void movePlayer(Input input, int player, CharacterController playerOne, ArrayList<Action> move1Event) {
    boolean[] b = getPlayerMovementInput(input, player);
    requestMovement(playerOne, move1Event, b[LEFT], b[RIGHT], b[UP], b[DOWN]);
  }

  private boolean[] getPlayerMovementInput(Input input, int player) {
    boolean left = getLeftButton(input, player);
    boolean right = getRightButton(input, player);
    boolean up = getUpButton(input, player);
    boolean down = getDownButton(input, player);
    return mostRecentlyPressedOnly(player, left, right, up, down);
  }

  private boolean getLeftButton(Input input, int player) {
    return switch (DraculaPunchGame.inputSource[player]) {
      case DraculaPunchGame.KB_WASD -> input.isKeyDown(Input.KEY_A);
      case DraculaPunchGame.KB_IJKL -> input.isKeyDown(Input.KEY_J);
      case DraculaPunchGame.KB_ARROWS -> input.isKeyDown(Input.KEY_LEFT);
      default -> input.isControllerLeft(DraculaPunchGame.inputSource[player]);
    };
  }

  private boolean getRightButton(Input input, int player) {
    return switch (DraculaPunchGame.inputSource[player]) {
      case DraculaPunchGame.KB_WASD -> input.isKeyDown(Input.KEY_D);
      case DraculaPunchGame.KB_IJKL -> input.isKeyDown(Input.KEY_L);
      case DraculaPunchGame.KB_ARROWS -> input.isKeyDown(Input.KEY_RIGHT);
      default -> input.isControllerRight(DraculaPunchGame.inputSource[player]);
    };
  }

  private boolean getUpButton(Input input, int player) {
    return switch (DraculaPunchGame.inputSource[player]) {
      case DraculaPunchGame.KB_WASD -> input.isKeyDown(Input.KEY_W);
      case DraculaPunchGame.KB_IJKL -> input.isKeyDown(Input.KEY_I);
      case DraculaPunchGame.KB_ARROWS -> input.isKeyDown(Input.KEY_UP);
      default -> input.isControllerUp(DraculaPunchGame.inputSource[player]);
    };
  }

  private boolean getDownButton(Input input, int player) {
    return switch (DraculaPunchGame.inputSource[player]) {
      case DraculaPunchGame.KB_WASD -> input.isKeyDown(Input.KEY_S);
      case DraculaPunchGame.KB_IJKL -> input.isKeyDown(Input.KEY_K);
      case DraculaPunchGame.KB_ARROWS -> input.isKeyDown(Input.KEY_DOWN);
      default -> input.isControllerDown(DraculaPunchGame.inputSource[player]);
    };
  }

  private boolean[] mostRecentlyPressedOnly(int player, boolean left, boolean right, boolean up, boolean down) {
    setIfMostRecent(player, left, LEFT);
    setIfMostRecent(player, right, RIGHT);
    setIfMostRecent(player, up, UP);
    setIfMostRecent(player, down, DOWN);
    if (!left && !right && !up && !down) mostRecentlyPressedButton[player] = -1;
    if (left && !right && !up && !down) mostRecentlyPressedButton[player] = LEFT;
    if (!left && right && !up && !down) mostRecentlyPressedButton[player] = RIGHT;
    if (!left && !right && up && !down) mostRecentlyPressedButton[player] = UP;
    if (!left && !right && !up && down) mostRecentlyPressedButton[player] = DOWN;
    return switch (mostRecentlyPressedButton[player]) {
      case LEFT -> new boolean[]{true, false, false, false};
      case RIGHT -> new boolean[]{false, true, false, false};
      case UP -> new boolean[]{false, false, true, false};
      case DOWN -> new boolean[]{false, false, false, true};
      default -> new boolean[]{false, false, false, false};
    };
  }

  private void setIfMostRecent(int player, boolean button, int buttonPosition) {
    if (button && !pressedButtons[player][buttonPosition]) {
      pressedButtons[player][buttonPosition] = true;
      mostRecentlyPressedButton[player] = buttonPosition;
    }
    if (!button) {
      pressedButtons[player][buttonPosition] = false;
    }
  }

  private void requestMovement(CharacterController player, ArrayList<Action> moveEvent, boolean left, boolean right, boolean up, boolean down) {
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
