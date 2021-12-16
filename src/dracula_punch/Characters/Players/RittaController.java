package dracula_punch.Characters.Players;

import dracula_punch.Actions.Damage_System.AttackAction;
import dracula_punch.Camera.Coordinate;
import dracula_punch.Damage_System.AttackType;
import dracula_punch.Damage_System.Projectiles.Arrow;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;

public class RittaController extends PlayerController{
  private final int rangedActionFrame;

  public RittaController(float x, float y, LevelState curLevelState) {
    super(x, y, curLevelState);
    xRenderOffset = 0;
    yRenderOffset = 30;
    scaleFactor = .95f;
    rangedActionFrame = 0;

    attackAction = new AttackAction(this, rangedActionFrame, AttackType.RANGED);

    setScale(scaleFactor);
  }

  //region Character Controller
  @Override
  public String getRunSheet(int x, int y) {
    return DraculaPunchGame.getSheetHelper(
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

  public String getMeleeSheet() {
    return null;
  }

  @Override
  public String getRangedSheet() {
    return DraculaPunchGame.getSheetHelper(
            DraculaPunchGame.RITTA_ATTACK_0_DEG,
            DraculaPunchGame.RITTA_ATTACK_180_DEG,
            DraculaPunchGame.RITTA_ATTACK_90_DEG,
            DraculaPunchGame.RITTA_ATTACK_270_DEG,
            (int) facingDir.getX(),
            (int) facingDir.getY()
    );
  }
  //endregion

  //region IAttacker
  @Override
  public void attack(AttackType attackType) {
    // get the tile in front of me
    int x = (int) (currentTile.x + facingDir.getX());
    int y = (int) (currentTile.y - facingDir.getY());
    Coordinate spawn = new Coordinate(x, y);
    Vector screen = curLevelState.camera.getScreenPositionFromTile(spawn);
    curLevelState.newObjects.add(
            new Arrow(screen.getX(), screen.getY(), spawn, curLevelState, facingDir)
    );  }
  //endregion
}
