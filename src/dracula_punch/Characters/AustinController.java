package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

public class AustinController extends PlayerController{
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
  }

  @Override
  public String getRunSheet(int x, int y) {
    return getSheetHelper(
            DraculaPunchGame.AUSTIN_RUN_0_DEG,
            DraculaPunchGame.AUSTIN_RUN_180_DEG,
            DraculaPunchGame.AUSTIN_RUN_90_DEG,
            DraculaPunchGame.AUSTIN_RUN_270_DEG,
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
    return getSheetHelper(
            DraculaPunchGame.AUSTIN_ATTACK_0_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_180_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_90_DEG,
            DraculaPunchGame.AUSTIN_ATTACK_270_DEG
    );
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
