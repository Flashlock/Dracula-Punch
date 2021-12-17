package dracula_punch.States;

import dracula_punch.Actions.*;
import dracula_punch.Actions.Input.InputAttackAction;
import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.Camera.Camera;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Characters.*;
import dracula_punch.Characters.Enemies.*;
import dracula_punch.Characters.Players.AmandaController;
import dracula_punch.Characters.Players.AustinController;
import dracula_punch.Characters.Players.PlayerController;
import dracula_punch.Characters.Players.RittaController;
import dracula_punch.TiledMap.DPTiledMap;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

import dracula_punch.DraculaPunchGame;

import java.util.ArrayList;

public class TestLevelState extends LevelState {
  private boolean isSpaceDown, isEDown, isUDown;
  private boolean[][] pressedButtons = {{false, false, false, false}, {false, false, false, false}, {false, false, false, false}};
  private int[] mostRecentlyPressedButton = {0, 0, 0};
  private final int LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3;
  private final int P1 = 0, P2 = 1, P3 = 2;

  private int timer = 400;

  private Boolean hasGKey;
  private Boolean hasSKey;
  // Object IDs
  private final int GOLDKEY_ID = 33;
  private final int SILVKEY_ID = 34;
  private final int BLANK_ID = 63;

  // temporary tile trigger win/lose states
  private final int WINSTATE_ID = 9;
  private final int LOSESTATE_ID = 8;

  @Override
  public int getID() {
    return DraculaPunchGame.TEST_STATE;
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    hasGKey = false;
    hasSKey = false;

    map = new DPTiledMap(DraculaPunchGame.MAP);
    // Doors to boss. Idk how Tiled works. No time to learn.
    map.isPassable[42][87] = true;
    map.isPassable[42][88] = true;
    camera = new Camera(map, playerObjects);
    gameObjects.add(camera);

    createCharacters();

    // Swarm 1
//    SwarmManager swarmManager = new SwarmManager(62, 91, 20, this);
//    gameObjects.add(swarmManager);
//    Coordinate coordinate = new Coordinate(69, 94);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(71, 93);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//
//    // Swarm 2
//    swarmManager = new SwarmManager(68, 58, 15, this);
//    gameObjects.add(swarmManager);
//    coordinate = new Coordinate(66, 58);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(70, 58);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(68, 61);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//
//    // Swarm 3
//    swarmManager = new SwarmManager(89, 27, 40, this);
//    gameObjects.add(swarmManager);
//    coordinate = new Coordinate(83, 27);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(98, 27);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(90, 50);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(95, 43);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(86, 47);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(89, 16);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//
//    // Swarm 4
//    swarmManager = new SwarmManager(67, 36, 10, this);
//    gameObjects.add(swarmManager);
//    coordinate = new Coordinate(61, 39);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(72, 41);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(65, 33);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(73, 32);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(67, 35);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//
//    // Swarm 5
//    swarmManager = new SwarmManager(39, 14, 20, this);
//    gameObjects.add(swarmManager);
//    coordinate = new Coordinate(41, 11);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(43, 17);
//    gameObjects.add(new BatController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(30, 14);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//
//    // Swarm 6
//    swarmManager = new SwarmManager(19, 42, 30, this);
//    gameObjects.add(swarmManager);
//    coordinate = new Coordinate(21, 49);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));
//    coordinate = new Coordinate(21, 36);
//    gameObjects.add(new GargoyleController(coordinate, this, swarmManager));

    // Boss
//    SwarmManager swarmManager = new MinionSpawner();
    SwarmManager swarmManager = new BossSpawner(17, 86, 20, this,
            null, null);
    gameObjects.add(swarmManager);
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

    playerObjects.add(playerOne);
    playerObjects.add(playerTwo);
    playerObjects.add(playerThree);

    move1Event.add(new InputMoveAction(playerOne));
    move2Event.add(new InputMoveAction(playerTwo));
    move3Event.add(new InputMoveAction(playerThree));

    attack1Event.add(new InputAttackAction(playerOne));
    attack2Event.add(new InputAttackAction(playerTwo));
    attack3Event.add(new InputAttackAction(playerThree));
  }

  private void createCharacters() {
    if (DraculaPunchGame.characterChoice[0] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      playerOne = initCharacterControllers(0);
      gameObjects.add(playerOne);
      PlayerController c1 = playerOne;
      playerObjects.add(c1);
      move1Event.add(new InputMoveAction(c1));
      attack1Event.add(new InputAttackAction(c1));
    }
    if (DraculaPunchGame.characterChoice[1] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      playerTwo = initCharacterControllers(1);
      gameObjects.add(playerTwo);
      PlayerController c2 = playerTwo;
      playerObjects.add(c2);
      move2Event.add(new InputMoveAction(c2));
      attack2Event.add(new InputAttackAction(c2));
    }
    if (DraculaPunchGame.characterChoice[2] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      playerThree = initCharacterControllers(2);
      gameObjects.add(playerThree);
      PlayerController c3 = playerThree;
      playerObjects.add(c3);
      move3Event.add(new InputMoveAction(c3));
      attack3Event.add(new InputAttackAction(c3));
    }
  }

  private PlayerController initCharacterControllers(int player) {
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
    super.update(gameContainer, stateBasedGame, delta);
    controls(gameContainer.getInput());

    for(GameObject gameObject : gameObjects){
      gameObject.update(gameContainer, stateBasedGame, delta);
    }
    checkHasKey();
    if(hasGKey){
      openDoors();
      hasGKey = false;
    }

    if(hasSKey){
      openSDoors();
      hasSKey = false;
    }

    for(GameObject gameObject : gameObjects){
      gameObject.update(gameContainer, stateBasedGame, delta);
    }
    checkHasKey();
    if(hasGKey){
      openDoors();
      hasGKey = false;
    }
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

  private void openSDoors(){
    // x: 88, y:95 - 100
    for(int i = 95; i <=100; i++){
      map.setTileId(88, i, map.getLayerIndex("NE Walls"), 63);
      map.isPassable[88][i] = true;
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
