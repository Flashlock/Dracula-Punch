package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Camera.Coordinate;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;

public class Arrow extends Projectile{

  public Arrow(float x, float y, Coordinate curTile, LevelState curLevelState, Vector direction) {
    super(x, y, curTile, curLevelState, direction);
    moveSpeed = .8f;
    TOTAL_MOVE_TIME = 100 * moveSpeed;

    String sheet = DraculaPunchGame.getSheetHelper(
        DraculaPunchGame.ARROW_0_DEG,
        DraculaPunchGame.ARROW_180_DEG,
        DraculaPunchGame.ARROW_90_DEG,
        DraculaPunchGame.ARROW_270_DEG,
        (int) direction.getX(),
        (int) direction.getY()
    );
    if(sheet == null) return;
    animate(sheet);
  }
}
