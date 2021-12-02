package dracula_punch.Damage_System.Projectiles;

import dracula_punch.Camera.Coordinate;
import dracula_punch.DraculaPunchGame;
import dracula_punch.States.LevelState;
import jig.Vector;

public class MagicBall extends Projectile{
    public MagicBall(float x, float y, Coordinate curTile, LevelState curLevelState, Vector direction) {
        super(x, y, curTile, curLevelState, direction);
        moveSpeed = .5f;

        String sheet = DraculaPunchGame.getSheetHelper(
                DraculaPunchGame.MAGIC_BALL_0_DEG,
                DraculaPunchGame.MAGIC_BALL_180_DEG,
                DraculaPunchGame.MAGIC_BALL_90_DEG,
                DraculaPunchGame.MAGIC_BALL_270_DEG,
                (int) direction.getX(),
                (int) direction.getY()
        );
        if(sheet == null) return;
        animate(sheet);
    }
}
