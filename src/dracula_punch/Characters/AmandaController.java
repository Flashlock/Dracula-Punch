package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

public class AmandaController extends PlayerController{
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
  }

  @Override
  public String getRunSheet(int x, int y) {
    return getSheetHelper(
            DraculaPunchGame.AMANDA_RUN_0_DEG,
            DraculaPunchGame.AMANDA_RUN_180_DEG,
            DraculaPunchGame.AMANDA_RUN_90_DEG,
            DraculaPunchGame.AMANDA_RUN_270_DEG,
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
    return getSheetHelper(
            DraculaPunchGame.AMANDA_ATTACK_0_DEG,
            DraculaPunchGame.AMANDA_ATTACK_180_DEG,
            DraculaPunchGame.AMANDA_ATTACK_90_DEG,
            DraculaPunchGame.AMANDA_ATTACK_270_DEG
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
