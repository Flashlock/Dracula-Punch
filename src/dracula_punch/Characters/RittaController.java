package dracula_punch.Characters;

import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;

public class RittaController extends PlayerController{

  public RittaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 30;
    scaleFactor = .8f;  // changed scaling to new tiledmap!

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
  public String getIdleSheet() {
    return DraculaPunchGame.RITTA_IDLE;
  }

  @Override
  public String getMeleeSheet() {
    return null;
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
}
