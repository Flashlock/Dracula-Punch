package dracula_punch.Characters;

import dracula_punch.Actions.Input.InputAttackAction;
import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class AustinController extends CharacterController{
  public static final int RUN_HEIGHT = 628;
  public static final int RUN_WIDTH = 360;
  public static final int IDLE_HEIGHT = 620;
  public static final int IDLE_WIDTH = 544;
  public static final int ATTACK_WIDTH = 550;
  public static final int ATTACK_HEIGHT = 550;

  public AustinController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 35;
    scaleFactor = 1f;  // changed scaling to new tiledmap!

    setScale(scaleFactor);

    // Add a movement action - for animation switching
    curLevelState.inputMoveEvent.add(new InputMoveAction(this));
    curLevelState.inputAttackEvent.add(new InputAttackAction(this));
  }

  @Override
  public String getRunSheet(int x, int y) {
    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = DraculaPunchGame.AUSTIN_RUN_270_DEG;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = DraculaPunchGame.AUSTIN_RUN_90_DEG;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = DraculaPunchGame.AUSTIN_RUN_0_DEG;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = DraculaPunchGame.AUSTIN_RUN_180_DEG;
    }
    else if(x == 0 && y == 0){
      // stop - do nothing for now. No idle pose/anim
    }
    else{
      System.out.println("Invalid Direction: Unable to Animate");
    }
    return sheet;
  }

  @Override
  public int getRunWidth() {
    return RUN_WIDTH;
  }

  @Override
  public int getRunHeight() {
    return RUN_HEIGHT;
  }

  @Override
  public String getIdleSheet() {
    return DraculaPunchGame.AUSTIN_IDLE;
  }

  @Override
  public int getIdleWidth() {
    return IDLE_WIDTH;
  }

  @Override
  public int getIdleHeight() {
    return IDLE_HEIGHT;
  }

  @Override
  public String getMeleeSheet() {
    int x = (int) facingDir.getX();
    int y = (int) facingDir.getY();
    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = DraculaPunchGame.AUSTIN_ATTACK_270_DEG;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = DraculaPunchGame.AUSTIN_ATTACK_90_DEG;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = DraculaPunchGame.AUSTIN_ATTACK_0_DEG;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = DraculaPunchGame.AUSTIN_ATTACK_180_DEG;
    }
    else if(x == 0 && y == 0){
      // stop - do nothing for now. No idle pose/anim
    }
    else{
      System.out.println("Invalid Direction: Unable to Animate");
    }
    return sheet;
  }

  @Override
  public int getMeleeWidth() {
    return ATTACK_WIDTH;
  }

  @Override
  public int getMeleeHeight() {
    return ATTACK_HEIGHT;
  }

  @Override
  public String getRangedSheet() {
    return null;
  }

  @Override
  public int getRangedWidth() {
    return 0;
  }

  @Override
  public int getRangedHeight() {
    return 0;
  }
}
