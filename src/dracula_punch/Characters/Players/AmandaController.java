package dracula_punch.Characters.Players;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.Projectiles.MagicBall;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;

public class AmandaController extends PlayerController {
  private final int rangedActionFrame = 13;

  public AmandaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 10;
    yRenderOffset = 30;
    scaleFactor = 1.1f;

    attackAction = new AttackAction(this, rangedActionFrame, AttackType.RANGED);

    setScale(scaleFactor);
  }

  @Override
  public String getRunSheet(int x, int y) {
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.AMANDA_RUN_0_DEG,
            DraculaPunchGame.AMANDA_RUN_180_DEG,
            DraculaPunchGame.AMANDA_RUN_90_DEG,
            DraculaPunchGame.AMANDA_RUN_270_DEG,
            x,
            y
    );
  }

  //region Character Controller
  @Override
  public String getIdleSheet() {
    return DraculaPunchGame.AMANDA_IDLE;
  }

  @Override
  public String getRangedSheet() {
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.AMANDA_ATTACK_0_DEG,
            DraculaPunchGame.AMANDA_ATTACK_180_DEG,
            DraculaPunchGame.AMANDA_ATTACK_90_DEG,
            DraculaPunchGame.AMANDA_ATTACK_270_DEG,
            (int) facingDir.getX(),
            (int) facingDir.getY()
    );
  }
  //endregion

  //region IAttacker
  @Override
  public String getMeleeSheet() {
    return null;
  }


  public void attack(AttackType attackType) {
    // spawn the ball and set it free
    Vector screen = curLevelState.camera.getScreenPositionFromTile(currentTile);
    curLevelState.newObjects.add(
            new MagicBall(screen.getX(), screen.getY(), currentTile, curLevelState, facingDir)
    );
  }
  //endregion
}
