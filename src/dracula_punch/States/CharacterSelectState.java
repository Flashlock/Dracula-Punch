package dracula_punch.States;

import dracula_punch.DraculaPunchGame;
import dracula_punch.UI.Buttons.CharSelectButton;
import dracula_punch.Actions.Character_Select.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class CharacterSelectState extends BasicGameState {
  private CharSelectButton[] charSelectButtons;
  private enum states {P_1_CONTROLLER, P_1_CHARACTER, P_2_CONTROLLER, P_2_CHARACTER, P_3_CONTROLLER, P_3_CHARACTER}
  private states state = states.P_1_CONTROLLER;
  private static final int PS5_CONTROLLER_START_BUTTON = 5;
  private static final int PS5_CONTROLLER_UP_BUTTON = 0;
  private static final int PS5_CONTROLLER_DOWN_BUTTON = 1;
  private static final int PS5_CONTROLLER_LEFT_BUTTON = 2;
  private static final int PS5_CONTROLLER_RIGHT_BUTTON = 3;
  private DraculaPunchGame dpg;
  private Input input;
  private boolean upArrow, downArrow, leftArrow, rightArrow, space, w, a, s, d, e, i, j, k, l, u;
  private final int KB_WASD = -2;
  private final int KB_IJKL = -3;
  private final int KB_ARROWS = -4;

  @Override
  public int getID() {
    return DraculaPunchGame.CHARACTER_SELECT_STATE;
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {}

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    int xRadius = 200;
    int yRadius = 125;
    float midX = DraculaPunchGame.SCREEN_WIDTH / 2f;
    float midY = DraculaPunchGame.SCREEN_HEIGHT / 2f;

    charSelectButtons = new CharSelectButton[]{
        new CharSelectButton(
            midX,
            midY + yRadius,
            DraculaPunchGame.AUSTIN_CHAR_SELECT,
            DraculaPunchGame.charIdEnum.AUSTIN
        ),
        new CharSelectButton(
            midX - xRadius,
            midY - yRadius,
            DraculaPunchGame.AMANDA_CHAR_SELECT,
            DraculaPunchGame.charIdEnum.AMANDA
        ),
        new CharSelectButton(
            midX + xRadius,
            midY - yRadius,
            DraculaPunchGame.RITTA_CHAR_SELECT,
            DraculaPunchGame.charIdEnum.RITTA
        )
    };

    // Assign actions to each button's events
    dpg = (DraculaPunchGame) game;
    input = container.getInput();
    for (CharSelectButton button : charSelectButtons) {
      button.clickEvent.add(new CharSelectClickAction(dpg, button));
      button.hoverEvent.add(new CharSelectHoverAction(dpg, button));
      button.unHoverEvent.add(new CharSelectUnHoverAction(dpg, button));
    }
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Character Select", 10, 25);
    for(CharSelectButton button : charSelectButtons){
      button.render(gameContainer, stateBasedGame, graphics);
    }
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
    for(CharSelectButton button : charSelectButtons){
      button.update(gameContainer, stateBasedGame, i);
    }
    watchKeys();
    characterSelectFiniteStateMachine(gameContainer);
  }

  private void watchKeys() {
    w = input.isKeyDown(Input.KEY_W);
    a = input.isKeyDown(Input.KEY_A);
    s = input.isKeyDown(Input.KEY_S);
    d = input.isKeyDown(Input.KEY_D);
    e = input.isKeyDown(Input.KEY_E);
    i = input.isKeyDown(Input.KEY_I);
    j = input.isKeyDown(Input.KEY_J);
    k = input.isKeyDown(Input.KEY_K);
    l = input.isKeyDown(Input.KEY_L);
    u = input.isKeyDown(Input.KEY_U);
    upArrow = input.isKeyDown(Input.KEY_UP);
    leftArrow = input.isKeyDown(Input.KEY_LEFT);
    downArrow = input.isKeyDown(Input.KEY_DOWN);
    rightArrow = input.isKeyDown(Input.KEY_RIGHT);
    space = input.isKeyDown(Input.KEY_SPACE);
  }

  public void characterSelectFiniteStateMachine(GameContainer gameContainer) {
    switch (state) {
      case P_1_CONTROLLER:
        determineController(0);
        break;
      case P_1_CHARACTER:
        selectCharacter(0);
        lockInSelection(0);
        break;
      case P_2_CONTROLLER:
        determineController(1);
        break;
      case P_2_CHARACTER:
        selectCharacter(1);
        lockInSelection(1);
        break;
      case P_3_CONTROLLER:
        determineController(2);
        break;
      case P_3_CHARACTER:
        selectCharacter(2);
        lockInSelection(2);
        break;
    }
    printButtonPresses(input);
  }

  private void determineController(int player) {
    if (e) { attemptControllerSelection(KB_WASD, player); }
    if (u) { attemptControllerSelection(KB_IJKL, player); }
    if (space) { attemptControllerSelection(KB_ARROWS, player); }
    for (int j = 0; j <= input.getControllerCount(); j++) {
      if (input.isControlPressed(j, PS5_CONTROLLER_START_BUTTON)) {
        attemptControllerSelection(j, player);
      }
    }
    if (dpg.inputSource[player] != -1) { nextState(); }
  }

  private void attemptControllerSelection(int controller, int player) {
    for (int i = 0; i <= 2; i++) {
      if (dpg.inputSource[i] == controller) {
        return;
      }
    }
    dpg.inputSource[player] = controller;
  }

  private void selectCharacter(int player) {
    switch (dpg.inputSource[player]) {
      case KB_WASD:
        if (w || a) { decrementSelection(player); }
        if (s || d) { incrementSelection(player); }
        break;
      case KB_IJKL:
        if (i || j) { decrementSelection(player); }
        if (k || l) { incrementSelection(player); }
        break;
      case KB_ARROWS:
        if (upArrow || leftArrow) { decrementSelection(player); }
        if (downArrow || rightArrow) { incrementSelection(player); }
        break;
      default:
        if (input.isControlPressed(player, PS5_CONTROLLER_UP_BUTTON)
            || input.isControlPressed(player, PS5_CONTROLLER_LEFT_BUTTON)) {
          decrementSelection(player);
        }
        if (input.isControlPressed(player, PS5_CONTROLLER_DOWN_BUTTON)
            || input.isControlPressed(player, PS5_CONTROLLER_RIGHT_BUTTON)) {
          incrementSelection(player);
        }
    }
  }

  private void incrementSelection(int player) {
    switch (dpg.characterChoice[player]) {
      case UNCHOSEN:
      case RITTA:
        dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AMANDA;
        break;
      case AMANDA:
        dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AUSTIN;
        break;
      case AUSTIN:
        dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.RITTA;
        break;
    }
  }

  private void decrementSelection(int player) {
    switch (dpg.characterChoice[player]) {
      case UNCHOSEN:
      case AMANDA:
        dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.RITTA;
        break;
      case RITTA:
        dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AUSTIN;
        break;
      case AUSTIN:
        dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AMANDA;
        break;
    }
  }

  private void lockInSelection(int player) {
    if(dpg.characterChoice[player] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      if (dpg.inputSource[player] == KB_WASD && e) { nextState(); }
      if (dpg.inputSource[player] == KB_IJKL && u) { nextState(); }
      if (dpg.inputSource[player] == KB_ARROWS && space) { nextState(); }
      if (input.isControlPressed(player, PS5_CONTROLLER_START_BUTTON)) {
        nextState();
      }
    }
  }

  private void nextState() {
    switch (state) {
      case P_1_CONTROLLER:
        state = states.P_1_CHARACTER;
        break;
      case P_1_CHARACTER:
        state = states.P_2_CONTROLLER;
        break;
      case P_2_CONTROLLER:
        state = states.P_2_CHARACTER;
        break;
      case P_2_CHARACTER:
        state = states.P_3_CONTROLLER;
        break;
      case P_3_CONTROLLER:
        state = states.P_3_CHARACTER;
        break;
    }
    System.out.println("State is now " + state);
  }

  private void printButtonPresses(Input input) {
    for (int j = 0; j <= input.getControllerCount(); j++) {
      for (int k = 0; k < 100; k++){
        if (input.isControlPressed(k,j)) {
          System.out.println("Controller " + j + " Pressed Button " + k);
        }
      }
    }
  }

  /*
  public void forReference(GameContainer gameContainer) {
    Input input = gameContainer.getInput();

    if (input.isControllerLeft(controller)) {
      hovered = DraculaPunchGame.charIdEnum.values()[j - 1];
    } else if (input.isControllerRight(controller)) {
      hovered = DraculaPunchGame.charIdEnum.values()[j + 1];
    }


    if (controller <= -1) {
      //gameContainer.getInput();
      for (int j = 0; j <= gameContainer.getInput().getControllerCount(); j++) {
        if (gameContainer.getInput().isButton1Pressed(j) gameContainer.getInput().isButton2Pressed(j)
        gameContainer.getInput().isButton3Pressed(j) || gameContainer.getInput().isControllerDown(j)){
          System.out.println(j);
          controller = j;
        }
      }


      if (gameContainer.getInput().isButton2Pressed(controller)) {
        System.out.println("found controller number=" + controller);
      } else {
        System.out.println("reading");

      }
    }
  }
   */
}
