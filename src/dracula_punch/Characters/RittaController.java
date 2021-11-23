package dracula_punch.Characters;

import dracula_punch.Actions.Input.InputMoveAction;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class RittaController extends CharacterController{
  public static final int RUN_HEIGHT = 750;
  public static final int RUN_WIDTH = 850;
  public static final int IDLE_HEIGHT = RUN_HEIGHT;
  public static final int IDLE_WIDTH = RUN_WIDTH;

  public RittaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 30;
    scaleFactor = .5f;  // changed scaling to new tiledmap!

    setScale(scaleFactor);

    // Add a movement action - for animation switching
    curLevelState.inputMoveEvent.add(new InputMoveAction(this));
  }

  @Override
  public String getRunSheet(int x, int y) {
    String sheet = null;
    if(x == 1 && y == 0){
      // right
      sheet = DraculaPunchGame.RITTA_RUN_270_DEG;
    }
    else if(x == -1 && y == 0){
      // left
      sheet = DraculaPunchGame.RITTA_RUN_90_DEG;
    }
    else if(x == 0 && y == 1){
      // up
      sheet = DraculaPunchGame.RITTA_RUN_0_DEG;
    }
    else if(x == 0 && y == -1){
      // down
      sheet = DraculaPunchGame.RITTA_RUN_180_DEG;
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
    return DraculaPunchGame.RITTA_IDLE;
  }

  @Override
  public int getIdleWidth() {
    return IDLE_WIDTH;
  }

  @Override
  public int getIdleHeight() {
    return IDLE_HEIGHT;
  }
}
