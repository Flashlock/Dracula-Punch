package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

public class RittaController extends PlayerController{
  public static final int RUN_HEIGHT = 650;
  public static final int RUN_WIDTH = 550;
  public static final int IDLE_HEIGHT = 500;
  public static final int IDLE_WIDTH = 500;
  public static final int ATTACK_WIDTH = 500;
  public static final int ATTACK_HEIGHT = 500;

  public RittaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 30;
    scaleFactor = 1f;  // changed scaling to new tiledmap!

    setScale(scaleFactor);
  }

  @Override
  public String getRunSheet(int x, int y) {
    return getSheetHelper(
            DraculaPunchGame.RITTA_RUN_0_DEG,
            DraculaPunchGame.RITTA_RUN_180_DEG,
            DraculaPunchGame.RITTA_RUN_90_DEG,
            DraculaPunchGame.RITTA_RUN_270_DEG,
            x,
            y
    );
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
    return getSheetHelper(
            DraculaPunchGame.RITTA_ATTACK_0_DEG,
            DraculaPunchGame.RITTA_ATTACK_180_DEG,
            DraculaPunchGame.RITTA_ATTACK_90_DEG,
            DraculaPunchGame.RITTA_ATTACK_270_DEG
    );
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
