package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

public class AmandaController extends PlayerController{
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
  public String getIdleSheet() {
    return DraculaPunchGame.AMANDA_IDLE;
  }

  @Override
  public String getMeleeSheet() {
    return null;
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
}
