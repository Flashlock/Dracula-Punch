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
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import static dracula_punch.DraculaPunchGame.TEST_STATE;

public class CharacterSelectState extends BasicGameState {
  private CharSelectButton[] charSelectButtons;
  private enum states {P_1_CONTROLLER, P_1_CHARACTER, P_2_CONTROLLER, P_2_CHARACTER, P_3_CONTROLLER, P_3_CHARACTER}
  private states state = states.P_1_CONTROLLER;
  private DraculaPunchGame dpg;
  private Input input;
  private boolean upArrow, downArrow, leftArrow, rightArrow, space, w, a, s, d, e, i, j, k, l, u;
  private int player = 0;
  private int buttonPressDelay = 0;
  private final int DELAY_TIME = 200;

  @Override
  public int getID() {
    return DraculaPunchGame.CHARACTER_SELECT_STATE;
  }

  @Override
  public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {}

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    int xRadius = 200;
    int yRadius = 100;
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

    dpg = (DraculaPunchGame) game;
    input = container.getInput();
    /* Assign actions to each button's events
    for (CharSelectButton button : charSelectButtons) {
      button.clickEvent.add(new CharSelectClickAction(dpg, button));
      button.hoverEvent.add(new CharSelectHoverAction(dpg, button));
      button.unHoverEvent.add(new CharSelectUnHoverAction(dpg, button));
    }
    */
  }

  @Override
  public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
    graphics.drawString("Character Select", 10, 25);
    for(CharSelectButton button : charSelectButtons){
      button.render(gameContainer, stateBasedGame, graphics);
    }
    switch (state) {
      case P_1_CONTROLLER, P_2_CONTROLLER, P_3_CONTROLLER:
        drawControllerSelectPrompt(graphics);
        break;
      case P_1_CHARACTER, P_2_CHARACTER, P_3_CHARACTER:
        drawCharacterSelectPrompt(graphics);
        drawSelectionHighlight(graphics);
        break;
    }
  }

  private void drawCharacterSelectPrompt(Graphics graphics) {
    int yOffset = 500;
    graphics.drawString("Player " + (player+1) + " Select Character", 10, yOffset);
    graphics.drawString("_______________________________________", 10, yOffset);
    yOffset += 20;
    switch (dpg.inputSource[player]) {
      case DraculaPunchGame.KB_WASD:
        graphics.drawString("Press W, A, S, D to change selection", 10, yOffset);
        yOffset += 20;
        graphics.drawString("Press E to accept selection", 10, yOffset);
        break;
      case DraculaPunchGame.KB_IJKL:
        graphics.drawString("Press I, J, K, L to change selection", 10, yOffset);
        yOffset += 20;
        graphics.drawString("Press U to accept selection", 10, yOffset);
        break;
      case DraculaPunchGame.KB_ARROWS:
        graphics.drawString("Press ARROW KEYS to change selection", 10, yOffset);
        yOffset += 20;
        graphics.drawString("Press SPACE to accept selection", 10, yOffset);
        break;
      default:
        graphics.drawString("Use D-Pad to change selection", 10, yOffset);
        yOffset += 20;
        graphics.drawString("Press X to accept selection", 10, yOffset);
    }
    yOffset += 20;
    graphics.drawString("Current selection = " + getCharacterName(), 10, yOffset);
  }

  private void drawControllerSelectPrompt(Graphics graphics) {
    int yOffset = 500;
    graphics.drawString("Player " + (player+1) + " Select Controller", 10, yOffset);
    graphics.drawString("_______________________________________", 10, yOffset);
    yOffset += 20;
    if (controllerIsAvailable(DraculaPunchGame.KB_WASD)){
      graphics.drawString("Press E to select WASD", 10, yOffset);
      yOffset += 20;
    }
    if (controllerIsAvailable(DraculaPunchGame.KB_IJKL)) {
      graphics.drawString("Press U to select IJKL", 10, yOffset);
      yOffset += 20;
    }
    if (controllerIsAvailable(DraculaPunchGame.KB_ARROWS)) {
      graphics.drawString("Press SPACE to select Arrow Keys", 10, yOffset);
      yOffset += 20;
    }
    graphics.drawString("Press START to select Playstation 5 Controller", 10, yOffset);
  }

  private String getCharacterName() {
    if (dpg.characterChoice[player] == DraculaPunchGame.charIdEnum.AMANDA)
      return "Magic";
    if (dpg.characterChoice[player] == DraculaPunchGame.charIdEnum.AUSTIN)
      return "Sword";
    if (dpg.characterChoice[player] == DraculaPunchGame.charIdEnum.RITTA)
      return "Crossbow";
    return "Unselected";
  }

  private void drawSelectionHighlight(Graphics graphics) {
    if (dpg.characterChoice[player] == DraculaPunchGame.charIdEnum.AUSTIN)
      graphics.drawRect(charSelectButtons[0].getX() - 144, charSelectButtons[0].getY() - 81, 288, 162);
    if (dpg.characterChoice[player] == DraculaPunchGame.charIdEnum.AMANDA)
      graphics.drawRect(charSelectButtons[1].getX() - 144, charSelectButtons[1].getY() - 81, 288, 162);
    if (dpg.characterChoice[player] == DraculaPunchGame.charIdEnum.RITTA)
      graphics.drawRect(charSelectButtons[2].getX() - 144, charSelectButtons[2].getY() - 81, 288, 162);
  }

  @Override
  public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
    for(CharSelectButton button : charSelectButtons){
      button.update(gameContainer, stateBasedGame, delta);
    }
    watchKeys();
    characterSelectFiniteStateMachine();
    buttonPressDelay += delta;
    if (buttonPressDelay >= DELAY_TIME) { buttonPressDelay = DELAY_TIME; }
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

  public void characterSelectFiniteStateMachine() {
    switch (state) {
      case P_1_CONTROLLER, P_2_CONTROLLER, P_3_CONTROLLER:
        determineController();
        break;
      case P_1_CHARACTER, P_2_CHARACTER, P_3_CHARACTER:
        if (buttonPressDelay == DELAY_TIME)
          selectCharacter();
        lockInSelection();
    }
    printButtonPresses(input);
  }

  private void determineController() {
    if (e) selectIfAvailable(DraculaPunchGame.KB_WASD);
    if (u) selectIfAvailable(DraculaPunchGame.KB_IJKL);
    if (space) selectIfAvailable(DraculaPunchGame.KB_ARROWS);
    for (int j = 0; j <= input.getControllerCount(); j++)
      if (input.isControlPressed(DraculaPunchGame.PS5_CONTROLLER_START_BUTTON, j))
        selectIfAvailable(j);
    if (dpg.inputSource[player] != -1) { nextState(); }
  }

  private void selectIfAvailable(int controller) {
    if (controllerIsAvailable(controller))
      dpg.inputSource[player] = controller;
  }

  private boolean controllerIsAvailable(int controller) {
    for (int i = 0; i <= 2; i++)
      if (dpg.inputSource[i] == controller)
        return false;
    return true;
  }

  private void selectCharacter() {
    int controller = dpg.inputSource[player];
    switch (controller) {
      case DraculaPunchGame.KB_WASD:
        if (w || a) decrementSelection();
        if (s || d) incrementSelection();
        break;
      case DraculaPunchGame.KB_IJKL:
        if (i || j) decrementSelection();
        if (k || l) incrementSelection();
        break;
      case DraculaPunchGame.KB_ARROWS:
        if (upArrow || leftArrow) decrementSelection();
        if (downArrow || rightArrow) incrementSelection();
        break;
      default:
        if (input.isControllerUp(controller) || input.isControllerLeft(controller)) { decrementSelection(); }
        if (input.isControllerDown(controller) || input.isControllerRight(controller)) { incrementSelection(); }
    }
  }

  private void incrementSelection() {
    switch (dpg.characterChoice[player]) {
      case UNCHOSEN, RITTA -> dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AMANDA;
      case AMANDA -> dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AUSTIN;
      case AUSTIN -> dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.RITTA;
    }
    buttonPressDelay = 0;
  }

  private void decrementSelection() {
    switch (dpg.characterChoice[player]) {
      case UNCHOSEN, AMANDA -> dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.RITTA;
      case RITTA -> dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AUSTIN;
      case AUSTIN -> dpg.characterChoice[player] = DraculaPunchGame.charIdEnum.AMANDA;
    }
    buttonPressDelay = 0;
  }

  private void lockInSelection() {
    if (dpg.characterChoice[player] != DraculaPunchGame.charIdEnum.UNCHOSEN) {
      if (dpg.inputSource[player] < 0) {
        if ((dpg.inputSource[player] == DraculaPunchGame.KB_WASD && e)
            || (dpg.inputSource[player] == DraculaPunchGame.KB_IJKL && u)
            || (dpg.inputSource[player] == DraculaPunchGame.KB_ARROWS && space))
          nextState();
      }
      else if ((input.isControlPressed(DraculaPunchGame.PS5_CONTROLLER_START_BUTTON, dpg.inputSource[player])))
        nextState();
    }
  }

  private void nextState() {
    switch (state) {
      case P_1_CONTROLLER:
        state = states.P_1_CHARACTER;
        break;
      case P_1_CHARACTER:
        state = states.P_2_CONTROLLER;
        player++;
        break;
      case P_2_CONTROLLER:
        state = states.P_2_CHARACTER;
        break;
      case P_2_CHARACTER:
        state = states.P_3_CONTROLLER;
        player++;
        break;
      case P_3_CONTROLLER:
        state = states.P_3_CHARACTER;
        break;
      case P_3_CHARACTER:
        dpg.enterState(TEST_STATE, new FadeOutTransition(), new EmptyTransition());
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
