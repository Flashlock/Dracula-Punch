package dracula_punch.Characters;

import dracula_punch.Actions.Input.InputAttackAction;
import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class AmandaController extends CharacterController{
  public static final int RUN_HEIGHT = 582;
  public static final int RUN_WIDTH = 390;
  public static final int IDLE_HEIGHT = 582;
  public static final int IDLE_WIDTH = 390;
  public static final int ATTACK_WIDTH = 340;
  public static final int ATTACK_HEIGHT = 380;

  public AmandaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 10;
    yRenderOffset = 30;
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
      sheet = DraculaPunchGame.AMANDA_RUN_270_DEG;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = DraculaPunchGame.AMANDA_RUN_90_DEG;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = DraculaPunchGame.AMANDA_RUN_0_DEG;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = DraculaPunchGame.AMANDA_RUN_180_DEG;
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
    return DraculaPunchGame.AMANDA_IDLE;
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
    return null;
  }

  @Override
  public int getMeleeWidth() {
    return 0;
  }

  @Override
  public int getMeleeHeight() {
    return 0;
  }

  @Override
  public String getRangedSheet() {
    int x = (int) facingDir.getX();
    int y = (int) facingDir.getY();
    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = DraculaPunchGame.AMANDA_ATTACK_270_DEG;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = DraculaPunchGame.AMANDA_ATTACK_90_DEG;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = DraculaPunchGame.AMANDA_ATTACK_0_DEG;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = DraculaPunchGame.AMANDA_ATTACK_180_DEG;
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
  public int getRangedWidth() {
    return ATTACK_WIDTH;
  }

  @Override
  public int getRangedHeight() {
    return ATTACK_HEIGHT;
  }
}
